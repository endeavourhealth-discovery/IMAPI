package org.endeavourhealth.imapi.queryassistant.llm

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/**
 * [ChatModel] over the OpenAI-compatible `POST {baseUrl}/chat/completions` protocol with function calling.
 * This is served by OpenAI and Azure OpenAI, and by most self-hosted and gateway servers
 * (Ollama, vLLM, LM Studio, LiteLLM, llama.cpp server, ...), so any model behind such a URL can be used.
 *
 * Tool errors have no dedicated flag in this protocol, so they are sent as tool messages prefixed with "ERROR: ".
 */
class OpenAiCompatibleChatModel(
  private val config: ChatModelConfig,
  private val http: HttpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(30)).build(),
  private val sleep: (Duration) -> Unit = { Thread.sleep(it.toMillis()) },
) : ChatModel {

  private val log = LoggerFactory.getLogger(javaClass)
  private val json = jacksonObjectMapper()
  private val model = config.resolvedModel
  private val endpoint: URI = URI.create((config.baseUrl ?: DEFAULT_BASE_URL).trimEnd('/') + "/chat/completions")
  @Volatile private var schemaCache: Pair<List<ToolSpec>, List<JsonNode>>? = null

  override fun complete(request: ChatRequest): ChatResponse {
    val body = json.writeValueAsString(requestBody(request))
    val response = post(body)
    return parse(response)
  }

  // ------------------------------------------------------------------ request

  internal fun requestBody(request: ChatRequest): ObjectNode {
    val body = json.createObjectNode().put("model", model)
    body.put(config.maxTokensField, config.maxTokens)
    val messages = body.putArray("messages")
    messages.addObject().put("role", "system").put("content", request.system)
    request.turns.forEach { appendTurn(it, messages) }
    if (request.tools.isNotEmpty()) {
      val tools = body.putArray("tools")
      request.tools.zip(schemas(request.tools)).forEach { (spec, schema) ->
        val function = tools.addObject().put("type", "function").putObject("function")
        function.put("name", spec.name).put("description", spec.description)
        function.set<JsonNode>("parameters", schema)
      }
      body.put("tool_choice", "auto")
    }
    return body
  }

  /** Tool schemas as sent; flattening is deterministic, so it is done once per tool list. */
  private fun schemas(specs: List<ToolSpec>): List<JsonNode> {
    schemaCache?.let { (key, schemas) -> if (key == specs) return schemas }
    return specs.map(config::toolSchema).also { schemaCache = specs to it }
  }

  private fun appendTurn(turn: ChatTurn, messages: ArrayNode) {
    when (turn.role) {
      Role.USER -> {
        // tool results must directly follow the assistant message that made the calls
        turn.blocks.filterIsInstance<ToolResult>().forEach { r ->
          messages.addObject().put("role", "tool").put("tool_call_id", r.callId)
            .put("content", if (r.isError) "ERROR: ${r.content}" else r.content)
        }
        val text = turn.blocks.filterIsInstance<Text>().joinToString("\n\n") { it.text }
        if (text.isNotEmpty()) messages.addObject().put("role", "user").put("content", text)
      }

      Role.ASSISTANT -> {
        val message = messages.addObject().put("role", "assistant")
        val text = turn.blocks.filterIsInstance<Text>().joinToString("\n\n") { it.text }
        val nativeCalls = (turn.native as? JsonNode)?.get("tool_calls")?.takeIf { it.isArray && it.size() > 0 }
        val calls = turn.blocks.filterIsInstance<ToolCall>()
        if (text.isNotEmpty() || calls.isEmpty()) message.put("content", text)
        when {
          // replay the server's own tool_calls so ids and argument strings round-trip exactly
          nativeCalls != null -> message.set<JsonNode>("tool_calls", normaliseCalls(nativeCalls, calls))
          calls.isNotEmpty() -> {
            val array = message.putArray("tool_calls")
            calls.forEach { c ->
              array.addObject().put("id", c.id).put("type", "function").putObject("function")
                .put("name", c.name).put("arguments", c.input?.let(json::writeValueAsString) ?: "{}")
            }
          }
        }
      }
    }
  }

  /** Native calls with ids aligned to the ids we generated (some servers omit them) and string arguments. */
  private fun normaliseCalls(native: JsonNode, calls: List<ToolCall>): ArrayNode {
    val out = json.createArrayNode()
    native.forEachIndexed { i, call ->
      val function = call.path("function")
      val args = function.get("arguments")
      out.addObject().put("id", calls.getOrNull(i)?.id ?: call.path("id").asText()).put("type", "function")
        .putObject("function").put("name", function.path("name").asText())
        .put("arguments", if (args == null || args.isNull) "{}" else if (args.isTextual) args.asText() else json.writeValueAsString(args))
    }
    return out
  }

  // ------------------------------------------------------------------ transport

  private fun post(body: String): JsonNode {
    var attempt = 0
    while (true) {
      val builder = HttpRequest.newBuilder(endpoint)
        .timeout(config.timeout)
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body))
      config.apiKey?.let { builder.header("Authorization", "Bearer $it") }
      config.extraHeaders.forEach { (k, v) -> builder.header(k, v) }

      val response = try {
        http.send(builder.build(), HttpResponse.BodyHandlers.ofString())
      } catch (e: java.io.IOException) {
        if (attempt++ < MAX_RETRIES) { sleep(backoff(attempt, null)); continue }
        throw ChatModelException("Could not reach the model at $endpoint: ${e.message}", e)
      }

      val status = response.statusCode()
      if (status in RETRYABLE && attempt < MAX_RETRIES) {
        attempt++
        log.warn("Model endpoint returned {}; retrying ({}/{})", status, attempt, MAX_RETRIES)
        sleep(backoff(attempt, response.headers().firstValue("retry-after").orElse(null)))
        continue
      }
      if (status !in 200..299) {
        throw ChatModelException("Model endpoint $endpoint returned HTTP $status: ${response.body().take(500)}")
      }
      val parsed = try {
        json.readTree(response.body())
      } catch (e: Exception) {
        throw ChatModelException("Model endpoint returned a non-JSON response: ${response.body().take(200)}", e)
      }
      parsed.get("error")?.takeIf { !it.isNull }?.let { throw ChatModelException("Model endpoint returned an error: $it") }
      return parsed
    }
  }

  private fun backoff(attempt: Int, retryAfter: String?): Duration =
    retryAfter?.toLongOrNull()?.let { Duration.ofSeconds(it.coerceAtMost(30)) } ?: Duration.ofSeconds(1L shl (attempt - 1))

  // ------------------------------------------------------------------ response

  internal fun parse(response: JsonNode): ChatResponse {
    val choice = response.path("choices").path(0)
    val message = choice.path("message")
    if (message.isMissingNode) throw ChatModelException("Model response has no choices[0].message: ${response.toString().take(300)}")

    val blocks = mutableListOf<ChatBlock>()
    contentText(message.get("content"))?.let { blocks += Text(it) }
    val refusal = message.path("refusal").textValue()?.takeIf { it.isNotBlank() }
    refusal?.let { blocks += Text(it) }

    message.path("tool_calls").forEachIndexed { i, call ->
      val function = call.path("function")
      val id = call.path("id").textValue()?.takeIf { it.isNotBlank() } ?: "call_$i"
      val name = function.path("name").asText()
      val args = function.get("arguments")
      blocks += when {
        args == null || args.isNull -> ToolCall(id, name, json.createObjectNode())
        args.isObject -> ToolCall(id, name, args)
        else -> try {
          val text = args.asText()
          ToolCall(id, name, if (text.isBlank()) json.createObjectNode() else json.readTree(text))
        } catch (e: Exception) {
          ToolCall(id, name, null, "arguments are not valid JSON: ${e.message?.lineSequence()?.firstOrNull()}")
        }
      }
    }

    val finish = choice.path("finish_reason").textValue()
    val hasCalls = blocks.any { it is ToolCall }
    val stop = when {
      refusal != null && !hasCalls -> StopKind.REFUSAL
      finish == "length" -> StopKind.MAX_TOKENS
      finish == "content_filter" -> StopKind.REFUSAL
      hasCalls -> StopKind.TOOL_USE // some servers report "stop" alongside tool calls
      finish == null || finish == "stop" || finish == "end_turn" -> StopKind.END
      else -> StopKind.OTHER
    }

    val usage = response.path("usage")
    return ChatResponse(
      turn = ChatTurn(Role.ASSISTANT, blocks, native = message),
      stop = stop,
      rawStopReason = finish,
      usage = TokenUsage(
        input = usage.path("prompt_tokens").asLong(0),
        output = usage.path("completion_tokens").asLong(0),
        cacheRead = usage.path("prompt_tokens_details").path("cached_tokens").asLong(0),
      ),
    )
  }

  /** Content may be a string or a list of parts ({type: text, text}). */
  private fun contentText(content: JsonNode?): String? = when {
    content == null || content.isNull -> null
    content.isTextual -> content.asText()
    content.isArray -> content.filter { it.path("type").asText() == "text" }.joinToString("") { it.path("text").asText() }
    else -> null
  }?.takeIf { it.isNotBlank() }

  companion object {
    const val DEFAULT_BASE_URL = "https://api.openai.com/v1"
    private const val MAX_RETRIES = 2
    private val RETRYABLE = setOf(408, 409, 429, 500, 502, 503, 504)
  }
}
