package org.endeavourhealth.imapi.queryassistant.llm

import com.fasterxml.jackson.databind.JsonNode
import java.time.Duration

/*
 * Provider-neutral chat + tool-calling model used by the query assistant.
 * Adapters translate to a wire protocol: AnthropicChatModel (Messages API) and
 * OpenAiCompatibleChatModel (/chat/completions, served by OpenAI, Azure, Ollama, vLLM, LiteLLM, ...).
 */

/** A tool offered to the model: name, description and JSON Schema for its input. */
data class ToolSpec(val name: String, val description: String, val inputSchema: JsonNode)

enum class Role { USER, ASSISTANT }

sealed interface ChatBlock
data class Text(val text: String) : ChatBlock

/** A tool call made by the model. [input] is null when the model sent arguments that are not valid JSON ([inputError]). */
data class ToolCall(val id: String, val name: String, val input: JsonNode?, val inputError: String? = null) : ChatBlock
data class ToolResult(val callId: String, val content: String, val isError: Boolean = false) : ChatBlock

/**
 * One message in the conversation. [native] holds the provider's own form of an assistant turn so the
 * same provider can replay it verbatim (Claude requires thinking blocks to be passed back unchanged).
 */
data class ChatTurn(val role: Role, val blocks: List<ChatBlock>, val native: Any? = null)

data class ChatRequest(val system: String, val tools: List<ToolSpec>, val turns: List<ChatTurn>)

enum class StopKind { END, TOOL_USE, MAX_TOKENS, REFUSAL, OTHER }

data class TokenUsage(
  val input: Long = 0,
  val output: Long = 0,
  val cacheRead: Long = 0,
  val cacheWrite: Long = 0,
)

/** The assistant turn produced by one model call. [rawStopReason] is the provider's own value, for logs. */
data class ChatResponse(val turn: ChatTurn, val stop: StopKind, val rawStopReason: String?, val usage: TokenUsage)

fun interface ChatModel {
  fun complete(request: ChatRequest): ChatResponse
}

class ChatModelException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

// ---------------------------------------------------------------------- configuration

enum class Protocol(val id: String) {
  ANTHROPIC("anthropic"),
  OPENAI_COMPATIBLE("openai-compatible");

  companion object {
    fun of(id: String) = entries.firstOrNull { it.id.equals(id, ignoreCase = true) }
      ?: throw IllegalArgumentException("Unknown protocol '$id', expected one of ${entries.map { it.id }}")
  }
}

/**
 * Where and how to reach the model. Configured from environment variables (see [fromEnv]):
 *
 * | variable                          | meaning                                                                 |
 * |-----------------------------------|-------------------------------------------------------------------------|
 * | QUERY_ASSISTANT_PROTOCOL          | `anthropic` (default) or `openai-compatible`                             |
 * | QUERY_ASSISTANT_BASE_URL          | endpoint, e.g. `http://localhost:11434/v1`; default is the provider's    |
 * | QUERY_ASSISTANT_API_KEY           | optional; anthropic falls back to the SDK's own env/profile resolution   |
 * | QUERY_ASSISTANT_MODEL             | model id; required for openai-compatible, default `claude-opus-5-5`      |
 * | QUERY_ASSISTANT_MAX_TOKENS        | output limit per call, default 16000                                     |
 * | QUERY_ASSISTANT_TIMEOUT_SECONDS   | per-call timeout, default 300                                            |
 * | QUERY_ASSISTANT_EXTRA_HEADERS     | `Name=value;Name2=value2`, e.g. Azure's `api-key=...`                    |
 * | QUERY_ASSISTANT_EFFORT            | anthropic only: low / medium / high (default) / xhigh / max              |
 * | QUERY_ASSISTANT_REFUSAL_FALLBACKS | anthropic only: server-side refusal fallbacks, default true             |
 * | QUERY_ASSISTANT_MAX_TOKENS_FIELD  | openai-compatible only: `max_tokens` (default) or `max_completion_tokens`|
 * | QUERY_ASSISTANT_FLATTEN_TOOL_SCHEMAS | self-contained tool schemas (no $refs) for small models; default true for openai-compatible |
 */
data class ChatModelConfig(
  val protocol: Protocol = Protocol.ANTHROPIC,
  val baseUrl: String? = null,
  val apiKey: String? = null,
  val model: String? = null,
  val maxTokens: Long = 16_000,
  val timeout: Duration = Duration.ofMinutes(5),
  val extraHeaders: Map<String, String> = emptyMap(),
  val effort: String = "high",
  val refusalFallbacks: Boolean = true,
  val maxTokensField: String = "max_tokens",
  /** Send self-contained tool schemas without $refs (see [ToolSchemas]); default on for openai-compatible. */
  val flattenToolSchemas: Boolean? = null,
) {
  val resolvedFlattenToolSchemas: Boolean
    get() = flattenToolSchemas ?: (protocol == Protocol.OPENAI_COMPATIBLE)

  /** The schema to send for [spec] under this configuration. */
  fun toolSchema(spec: ToolSpec): JsonNode =
    if (resolvedFlattenToolSchemas) ToolSchemas.flatten(spec.inputSchema) else spec.inputSchema

  val resolvedModel: String
    get() = model ?: when (protocol) {
      Protocol.ANTHROPIC -> DEFAULT_ANTHROPIC_MODEL
      Protocol.OPENAI_COMPATIBLE -> throw IllegalStateException("QUERY_ASSISTANT_MODEL is required for the openai-compatible protocol")
    }

  fun create(): ChatModel = when (protocol) {
    Protocol.ANTHROPIC -> AnthropicChatModel.fromConfig(this)
    Protocol.OPENAI_COMPATIBLE -> OpenAiCompatibleChatModel(this)
  }

  /** Never print the API key. */
  override fun toString() =
    "ChatModelConfig(protocol=${protocol.id}, baseUrl=${baseUrl ?: "<default>"}, model=${model ?: "<default>"}, " +
      "apiKey=${if (apiKey.isNullOrEmpty()) "<none>" else "<set>"}, flatToolSchemas=$resolvedFlattenToolSchemas)"

  companion object {
    const val DEFAULT_ANTHROPIC_MODEL = "claude-opus-5-5"

    fun fromEnv(env: Map<String, String> = System.getenv()): ChatModelConfig {
      fun v(name: String) = env["QUERY_ASSISTANT_$name"]?.trim()?.takeIf { it.isNotEmpty() }
      return ChatModelConfig(
        protocol = v("PROTOCOL")?.let(Protocol::of) ?: Protocol.ANTHROPIC,
        baseUrl = v("BASE_URL"),
        apiKey = v("API_KEY"),
        model = v("MODEL"),
        maxTokens = v("MAX_TOKENS")?.toLong() ?: 16_000,
        timeout = v("TIMEOUT_SECONDS")?.toLong()?.let(Duration::ofSeconds) ?: Duration.ofMinutes(5),
        extraHeaders = v("EXTRA_HEADERS")?.let(::parseHeaders).orEmpty(),
        effort = v("EFFORT")?.lowercase() ?: "high",
        refusalFallbacks = v("REFUSAL_FALLBACKS")?.toBooleanStrictOrNull() ?: true,
        maxTokensField = v("MAX_TOKENS_FIELD") ?: "max_tokens",
        flattenToolSchemas = v("FLATTEN_TOOL_SCHEMAS")?.toBooleanStrictOrNull(),
      )
    }

    internal fun parseHeaders(text: String): Map<String, String> =
      text.split(';').map { it.trim() }.filter { it.isNotEmpty() }.associate { entry ->
        val eq = entry.indexOf('=')
        require(eq > 0) { "QUERY_ASSISTANT_EXTRA_HEADERS entries must be Name=value, got '$entry'" }
        entry.substring(0, eq).trim() to entry.substring(eq + 1).trim()
      }
  }
}
