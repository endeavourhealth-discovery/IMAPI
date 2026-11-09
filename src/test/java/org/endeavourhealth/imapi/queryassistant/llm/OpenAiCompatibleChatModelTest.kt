package org.endeavourhealth.imapi.queryassistant.llm

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sun.net.httpserver.HttpServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.InetSocketAddress
import java.time.Duration

class OpenAiCompatibleChatModelTest {

  private val json = jacksonObjectMapper()
  private lateinit var server: HttpServer
  private val responses = ArrayDeque<Pair<Int, String>>()
  private val bodies = mutableListOf<JsonNode>()
  private val headers = mutableListOf<Map<String, String>>()
  private val paths = mutableListOf<String>()

  @BeforeEach
  fun start() {
    server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)
    server.createContext("/") { exchange ->
      paths += exchange.requestURI.path
      headers += exchange.requestHeaders.mapKeys { it.key.lowercase() }.mapValues { it.value.first() }
      bodies.add(json.readTree(exchange.requestBody.readBytes()))
      val (status, body) = responses.removeFirstOrNull() ?: (500 to """{"error": "unexpected call"}""")
      val bytes = body.toByteArray()
      exchange.sendResponseHeaders(status, bytes.size.toLong())
      exchange.responseBody.use { it.write(bytes) }
    }
    server.start()
  }

  @AfterEach
  fun stop() = server.stop(0)

  private fun model(config: ChatModelConfig = ChatModelConfig()) = OpenAiCompatibleChatModel(
    config.copy(
      protocol = Protocol.OPENAI_COMPATIBLE,
      baseUrl = "http://127.0.0.1:${server.address.port}/v1/",
      model = config.model ?: "llama3.1:70b",
    ),
    sleep = {},
  )

  private val tool = ToolSpec(
    "search", "Search things",
    json.readTree("""{"type":"object","properties":{"term":{"type":"string"}},"required":["term"]}"""),
  )

  private fun completion(message: String, finish: String = "stop") = 200 to """
    {"id": "c1", "object": "chat.completion", "choices": [{"index": 0, "message": $message, "finish_reason": "$finish"}],
     "usage": {"prompt_tokens": 50, "completion_tokens": 10, "prompt_tokens_details": {"cached_tokens": 30}}}
  """.trimIndent()

  private val user = ChatTurn(Role.USER, listOf(Text("Patients with asthma")))

  // ------------------------------------------------------------------ request

  @Test
  fun sendsSystemToolsModelAndAuth() {
    responses += completion("""{"role": "assistant", "content": "Which time window?"}""")
    model(ChatModelConfig(apiKey = "secret", extraHeaders = mapOf("X-Team" to "imapi")))
      .complete(ChatRequest("system prompt", listOf(tool), listOf(user)))

    assertEquals("/v1/chat/completions", paths.single())
    assertEquals("Bearer secret", headers.single()["authorization"])
    assertEquals("imapi", headers.single()["x-team"])
    val body = bodies.single()
    assertEquals("llama3.1:70b", body["model"].asText())
    assertEquals(16000, body["max_tokens"].asInt())
    assertEquals(listOf("system", "user"), body["messages"].map { it["role"].asText() })
    assertEquals("system prompt", body["messages"][0]["content"].asText())
    assertEquals("function", body["tools"][0]["type"].asText())
    assertEquals("search", body["tools"][0]["function"]["name"].asText())
    assertEquals("object", body["tools"][0]["function"]["parameters"]["type"].asText())
    assertEquals("auto", body["tool_choice"].asText())
  }

  @Test
  fun noAuthHeaderWithoutKeyAndConfigurableMaxTokensField() {
    responses += completion("""{"role": "assistant", "content": "hi"}""")
    model(ChatModelConfig(maxTokensField = "max_completion_tokens")).complete(ChatRequest("s", emptyList(), listOf(user)))

    assertNull(headers.single()["authorization"])
    assertTrue(bodies.single().has("max_completion_tokens"))
    assertFalse(bodies.single().has("tools"))
  }

  @Test
  fun replaysToolCallsAndResultsInProtocolOrder() {
    responses += completion(
      """{"role": "assistant", "content": null, "tool_calls": [
          {"id": "call_a", "type": "function", "function": {"name": "search", "arguments": "{\"term\": \"asthma\"}"}}]}""",
      "tool_calls",
    )
    responses += completion("""{"role": "assistant", "content": "Done."}""")
    val model = model()
    val assistant = model.complete(ChatRequest("s", listOf(tool), listOf(user))).turn
    val results = ChatTurn(Role.USER, listOf(ToolResult("call_a", "no matches", isError = true), Text("try 'wheeze'")))
    model.complete(ChatRequest("s", listOf(tool), listOf(user, assistant, results)))

    val messages = bodies[1]["messages"]
    assertEquals(listOf("system", "user", "assistant", "tool", "user"), messages.map { it["role"].asText() })
    val call = messages[2]["tool_calls"][0]
    assertEquals("call_a", call["id"].asText())
    assertEquals("{\"term\": \"asthma\"}", call["function"]["arguments"].asText())
    assertFalse(messages[2].has("content"))
    assertEquals("call_a", messages[3]["tool_call_id"].asText())
    assertEquals("ERROR: no matches", messages[3]["content"].asText())
    assertEquals("try 'wheeze'", messages[4]["content"].asText())
  }

  @Test
  fun serialisesAssistantTurnsFromOtherProviders() {
    responses += completion("""{"role": "assistant", "content": "ok"}""")
    val foreign = ChatTurn(Role.ASSISTANT, listOf(Text("Looking."), ToolCall("t1", "search", json.readTree("""{"term":"x"}"""))))
    model().complete(ChatRequest("s", listOf(tool), listOf(user, foreign, ChatTurn(Role.USER, listOf(ToolResult("t1", "[]"))))))

    val assistant = bodies.single()["messages"][2]
    assertEquals("Looking.", assistant["content"].asText())
    assertEquals("{\"term\":\"x\"}", assistant["tool_calls"][0]["function"]["arguments"].asText())
  }

  @Test
  fun sendsSelfContainedToolSchemasByDefault() {
    val recursive = ToolSpec(
      "tree", "t",
      json.readTree("""{"type":"object","properties":{"root":{"${'$'}ref":"#/${'$'}defs/node"}},
        "${'$'}defs":{"node":{"oneOf":[{"type":"string"},{"type":"object","properties":{"kids":{"type":"array","items":{"${'$'}ref":"#/${'$'}defs/node"}}}}]}}}"""),
    )
    responses += completion("""{"role": "assistant", "content": "a"}""")
    responses += completion("""{"role": "assistant", "content": "b"}""")
    model().complete(ChatRequest("s", listOf(recursive), listOf(user)))
    model(ChatModelConfig(flattenToolSchemas = false)).complete(ChatRequest("s", listOf(recursive), listOf(user)))

    val flat = bodies[0]["tools"][0]["function"]["parameters"]
    assertFalse(flat.has("\$defs"))
    assertEquals("node", flat.at("/properties/root/title").asText())
    assertTrue(bodies[1]["tools"][0]["function"]["parameters"].has("\$defs"))
  }

  // ------------------------------------------------------------------ response

  @Test
  fun parsesToolCallsTextAndUsage() {
    responses += completion(
      """{"role": "assistant", "content": "Searching.", "tool_calls": [
          {"id": "call_a", "type": "function", "function": {"name": "search", "arguments": "{\"term\": \"asthma\"}"}}]}""",
      "tool_calls",
    )
    val response = model().complete(ChatRequest("s", listOf(tool), listOf(user)))

    assertEquals(StopKind.TOOL_USE, response.stop)
    assertEquals(Text("Searching."), response.turn.blocks[0])
    assertEquals("asthma", (response.turn.blocks[1] as ToolCall).input!!["term"].asText())
    assertEquals(TokenUsage(input = 50, output = 10, cacheRead = 30), response.usage)
  }

  @Test
  fun toleratesServerVariations() {
    // object arguments, missing id, "stop" alongside tool calls, content as parts
    responses += completion(
      """{"role": "assistant", "content": [{"type": "text", "text": "Hm."}], "tool_calls": [
          {"type": "function", "function": {"name": "search", "arguments": {"term": "copd"}}}]}""",
    )
    val response = model().complete(ChatRequest("s", listOf(tool), listOf(user)))

    assertEquals(StopKind.TOOL_USE, response.stop)
    assertEquals(Text("Hm."), response.turn.blocks[0])
    val call = response.turn.blocks[1] as ToolCall
    assertEquals("call_0", call.id)
    assertEquals("copd", call.input!!["term"].asText())
  }

  @Test
  fun invalidArgumentsBecomeAnInputError() {
    responses += completion(
      """{"role": "assistant", "tool_calls": [{"id": "c", "function": {"name": "search", "arguments": "{term: asthma"}}]}""",
      "tool_calls",
    )
    val call = model().complete(ChatRequest("s", listOf(tool), listOf(user))).turn.blocks.single() as ToolCall
    assertNull(call.input)
    assertTrue(call.inputError!!.startsWith("arguments are not valid JSON"))
  }

  @Test
  fun mapsFinishReasons() {
    responses += completion("""{"role": "assistant", "content": "partial"}""", "length")
    responses += completion("""{"role": "assistant", "content": null}""", "content_filter")
    responses += completion("""{"role": "assistant", "content": null, "refusal": "I can't help with that."}""")
    val model = model()
    val request = ChatRequest("s", emptyList(), listOf(user))

    assertEquals(StopKind.MAX_TOKENS, model.complete(request).stop)
    assertEquals(StopKind.REFUSAL, model.complete(request).stop)
    val refusal = model.complete(request)
    assertEquals(StopKind.REFUSAL, refusal.stop)
    assertEquals(Text("I can't help with that."), refusal.turn.blocks.single())
  }

  // ------------------------------------------------------------------ transport

  @Test
  fun retriesTransientErrors() {
    responses += 503 to """{"error": "busy"}"""
    responses += completion("""{"role": "assistant", "content": "ok"}""")
    val response = model().complete(ChatRequest("s", emptyList(), listOf(user)))
    assertEquals(Text("ok"), response.turn.blocks.single())
    assertEquals(2, bodies.size)
  }

  @Test
  fun clientErrorsFailWithoutLeakingTheKey() {
    responses += 400 to """{"error": {"message": "model not found"}}"""
    val e = assertThrows<ChatModelException> {
      model(ChatModelConfig(apiKey = "secret")).complete(ChatRequest("s", emptyList(), listOf(user)))
    }
    assertTrue(e.message!!.contains("HTTP 400"))
    assertTrue(e.message!!.contains("model not found"))
    assertFalse(e.message!!.contains("secret"))
    assertEquals(1, bodies.size)
  }

  @Test
  fun unreachableEndpointIsReported() {
    val dead = OpenAiCompatibleChatModel(
      ChatModelConfig(Protocol.OPENAI_COMPATIBLE, baseUrl = "http://127.0.0.1:1", model = "m", timeout = Duration.ofSeconds(2)),
      sleep = {},
    )
    val e = assertThrows<ChatModelException> { dead.complete(ChatRequest("s", emptyList(), listOf(user))) }
    assertTrue(e.message!!.startsWith("Could not reach the model"))
  }
}
