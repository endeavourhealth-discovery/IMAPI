package org.endeavourhealth.imapi.queryassistant.llm

import com.anthropic.core.jsonMapper
import com.anthropic.models.messages.Message
import com.anthropic.models.messages.MessageCreateParams
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AnthropicChatModelTest {

  private val json = jacksonObjectMapper()
  private val tool = ToolSpec(
    "search", "Search things",
    json.readTree("""{"type":"object","properties":{"term":{"type":"string"}},"required":["term"],"${'$'}defs":{"x":{"type":"string"}}}"""),
  )

  private class Recorder(private val response: String) : (MessageCreateParams) -> Message {
    val bodies = mutableListOf<JsonNode>()
    val betas = mutableListOf<List<String>>()
    override fun invoke(params: MessageCreateParams): Message {
      bodies.add(jsonMapper().valueToTree<JsonNode>(params._body()))
      betas.add(params._headers().values("anthropic-beta"))
      return jsonMapper().readValue(response, Message::class.java)
    }
  }

  private fun message(stopReason: String, content: String) = """
    {"id": "msg", "type": "message", "role": "assistant", "model": "claude-opus-5-5",
     "content": [$content], "stop_reason": "$stopReason", "stop_sequence": null,
     "usage": {"input_tokens": 100, "output_tokens": 20, "cache_read_input_tokens": 80, "cache_creation_input_tokens": 5}}
  """.trimIndent()

  private val toolUse = """{"type": "thinking", "thinking": "", "signature": "sig"},
    {"type": "text", "text": "Searching."}, {"type": "tool_use", "id": "t1", "name": "search", "input": {"term": "asthma"}}"""

  @Test
  fun mapsResponseToNeutralTurn() {
    val model = AnthropicChatModel(ChatModelConfig(), Recorder(message("tool_use", toolUse)))
    val response = model.complete(ChatRequest("system", listOf(tool), listOf(ChatTurn(Role.USER, listOf(Text("hi"))))))

    assertEquals(StopKind.TOOL_USE, response.stop)
    assertEquals(Text("Searching."), response.turn.blocks[0])
    val call = response.turn.blocks[1] as ToolCall
    assertEquals("t1", call.id)
    assertEquals("asthma", call.input!!["term"].asText())
    assertEquals(TokenUsage(100, 20, 80, 5), response.usage)
    assertNotNull(response.turn.native)
  }

  @Test
  fun requestUsesCachingEffortFallbacksAndTools() {
    val recorder = Recorder(message("end_turn", """{"type": "text", "text": "ok"}"""))
    AnthropicChatModel(ChatModelConfig(), recorder)
      .complete(ChatRequest("system prompt", listOf(tool), listOf(ChatTurn(Role.USER, listOf(Text("hi"))))))

    val body = recorder.bodies.single()
    assertEquals("claude-opus-5-5", body["model"].asText())
    assertEquals("system prompt", body["system"][0]["text"].asText())
    assertEquals("ephemeral", body["system"][0]["cache_control"]["type"].asText())
    assertEquals("ephemeral", body["cache_control"]["type"].asText())
    assertEquals("high", body["output_config"]["effort"].asText())
    assertEquals("default", body["fallbacks"].asText())
    assertEquals(listOf("server-side-fallback-2026-07-01"), recorder.betas.single())
    val schema = body["tools"][0]["input_schema"]
    assertEquals(listOf("term"), schema["required"].map { it.asText() })
    assertTrue(schema.has("\$defs"))
  }

  @Test
  fun fallbacksAndModelAreConfigurable() {
    val recorder = Recorder(message("end_turn", """{"type": "text", "text": "ok"}"""))
    AnthropicChatModel(ChatModelConfig(model = "claude-sonnet-5-5", refusalFallbacks = false, effort = "medium"), recorder)
      .complete(ChatRequest("s", emptyList(), listOf(ChatTurn(Role.USER, listOf(Text("hi"))))))

    val body = recorder.bodies.single()
    assertEquals("claude-sonnet-5-5", body["model"].asText())
    assertEquals("medium", body["output_config"]["effort"].asText())
    assertFalse(body.has("fallbacks"))
    assertTrue(recorder.betas.single().isEmpty())
  }

  @Test
  fun replaysOwnAssistantTurnsVerbatimIncludingThinking() {
    val first = Recorder(message("tool_use", toolUse))
    val model = AnthropicChatModel(ChatModelConfig(), first)
    val user = ChatTurn(Role.USER, listOf(Text("hi")))
    val assistant = model.complete(ChatRequest("s", listOf(tool), listOf(user))).turn

    val second = Recorder(message("end_turn", """{"type": "text", "text": "done"}"""))
    AnthropicChatModel(ChatModelConfig(), second).complete(
      ChatRequest("s", listOf(tool), listOf(user, assistant, ChatTurn(Role.USER, listOf(ToolResult("t1", "no matches", isError = true)))))
    )

    val messages = second.bodies.single()["messages"]
    val replayed = messages[1]["content"]
    assertEquals(listOf("thinking", "text", "tool_use"), replayed.map { it["type"].asText() })
    assertEquals("sig", replayed[0]["signature"].asText())
    val result = messages[2]["content"][0]
    assertEquals("tool_result", result["type"].asText())
    assertEquals("t1", result["tool_use_id"].asText())
    assertTrue(result["is_error"].asBoolean())
  }

  @Test
  fun buildsAssistantTurnsFromOtherProviders() {
    val recorder = Recorder(message("end_turn", """{"type": "text", "text": "ok"}"""))
    val foreign = ChatTurn(Role.ASSISTANT, listOf(Text("Looking."), ToolCall("c1", "search", json.readTree("""{"term":"x"}"""))))
    AnthropicChatModel(ChatModelConfig(), recorder).complete(
      ChatRequest("s", listOf(tool), listOf(ChatTurn(Role.USER, listOf(Text("hi"))), foreign, ChatTurn(Role.USER, listOf(ToolResult("c1", "[]")))))
    )
    val content = recorder.bodies.single()["messages"][1]["content"]
    assertEquals("tool_use", content[1]["type"].asText())
    assertEquals("x", content[1]["input"]["term"].asText())
  }
}
