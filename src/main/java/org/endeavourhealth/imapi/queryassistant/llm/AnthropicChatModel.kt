package org.endeavourhealth.imapi.queryassistant.llm

import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.core.JsonValue
import com.anthropic.models.messages.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * [ChatModel] over the Anthropic Messages API (official SDK). Works against api.anthropic.com or any
 * endpoint that speaks the Messages API (set [ChatModelConfig.baseUrl]).
 *
 * Uses features the generic protocol lacks: prompt caching of the system prompt and conversation,
 * effort, server-side refusal fallbacks, and verbatim replay of assistant turns (thinking blocks).
 */
class AnthropicChatModel(
  private val config: ChatModelConfig,
  private val send: (MessageCreateParams) -> Message,
) : ChatModel {

  private val json = jacksonObjectMapper()
  private val model = config.resolvedModel
  @Volatile private var toolCache: Pair<List<ToolSpec>, List<Tool>>? = null

  override fun complete(request: ChatRequest): ChatResponse {
    val response = send(params(request))
    val blocks = response.content().mapNotNull { block ->
      block.text().map<ChatBlock> { Text(it.text()) }
        .or { block.toolUse().map { ToolCall(it.id(), it.name(), toolInput(it)) } }
        .orElse(null)
    }
    val stop = response.stopReason().orElse(null)
    val usage = response.usage()
    return ChatResponse(
      turn = ChatTurn(Role.ASSISTANT, blocks, native = response.toParam()),
      stop = when (stop) {
        StopReason.END_TURN, StopReason.STOP_SEQUENCE -> StopKind.END
        StopReason.TOOL_USE -> StopKind.TOOL_USE
        StopReason.MAX_TOKENS -> StopKind.MAX_TOKENS
        StopReason.REFUSAL -> StopKind.REFUSAL
        else -> StopKind.OTHER
      },
      rawStopReason = stop?.asString(),
      usage = TokenUsage(
        input = usage.inputTokens(),
        output = usage.outputTokens(),
        cacheRead = usage.cacheReadInputTokens().orElse(0L),
        cacheWrite = usage.cacheCreationInputTokens().orElse(0L),
      ),
    )
  }

  internal fun params(request: ChatRequest): MessageCreateParams {
    val builder = MessageCreateParams.builder()
      .model(model)
      .maxTokens(config.maxTokens)
      .systemOfTextBlockParams(
        listOf(TextBlockParam.builder().text(request.system).cacheControl(CacheControlEphemeral.builder().build()).build())
      )
      .messages(request.turns.map(::toParam))
      // automatic caching of the growing conversation prefix
      .cacheControl(CacheControlEphemeral.builder().build())
      .outputConfig(OutputConfig.builder().effort(OutputConfig.Effort.of(config.effort)).build())
    sdkTools(request.tools).forEach { builder.addTool(it) }
    if (config.refusalFallbacks) {
      builder.putAdditionalHeader("anthropic-beta", "server-side-fallback-2026-07-01")
      builder.putAdditionalBodyProperty("fallbacks", JsonValue.from("default"))
    }
    return builder.build()
  }

  private fun toParam(turn: ChatTurn): MessageParam {
    if (turn.role == Role.ASSISTANT) {
      (turn.native as? MessageParam)?.let { return it }
      return MessageParam.ofAssistantOfContentBlockParams(turn.blocks.mapNotNull(::assistantBlock))
    }
    return MessageParam.ofUserOfContentBlockParams(turn.blocks.mapNotNull(::userBlock))
  }

  private fun userBlock(block: ChatBlock): ContentBlockParam? = when (block) {
    is Text -> ContentBlockParam.ofText(block.text)
    is ToolResult -> ContentBlockParam.ofToolResult(
      ToolResultBlockParam.builder().toolUseId(block.callId).content(block.content).isError(block.isError).build()
    )
    is ToolCall -> null
  }

  /** Only used for assistant turns that did not come from this provider (no native form). */
  private fun assistantBlock(block: ChatBlock): ContentBlockParam? = when (block) {
    is Text -> ContentBlockParam.ofText(block.text)
    is ToolCall -> {
      val input: Map<String, Any?> = block.input?.let { json.convertValue(it) } ?: emptyMap()
      ContentBlockParam.ofToolUse(
        ToolUseBlockParam.builder().id(block.id).name(block.name)
          .input(ToolUseBlockParam.Input.builder().putAllAdditionalProperties(input.mapValues { JsonValue.from(it.value) }).build())
          .build()
      )
    }
    is ToolResult -> null
  }

  private fun toolInput(toolUse: ToolUseBlock): JsonNode? =
    try {
      toolUse._input().convert(JsonNode::class.java)
    } catch (e: Exception) {
      null
    }

  private fun sdkTools(specs: List<ToolSpec>): List<Tool> {
    toolCache?.let { (key, tools) -> if (key == specs) return tools }
    val tools = specs.map(::toSdkTool)
    toolCache = specs to tools
    return tools
  }

  private fun toSdkTool(spec: ToolSpec): Tool {
    val schema: Map<String, Any?> = json.convertValue(config.toolSchema(spec))
    val properties = Tool.InputSchema.Properties.builder()
    @Suppress("UNCHECKED_CAST")
    (schema["properties"] as? Map<String, Any?>).orEmpty().forEach { (k, v) -> properties.putAdditionalProperty(k, JsonValue.from(v)) }
    val inputSchema = Tool.InputSchema.builder().properties(properties.build())
    @Suppress("UNCHECKED_CAST")
    (schema["required"] as? List<String>)?.let { inputSchema.required(it) }
    schema.filterKeys { it != "type" && it != "properties" && it != "required" }
      .forEach { (k, v) -> inputSchema.putAdditionalProperty(k, JsonValue.from(v)) }
    return Tool.builder().name(spec.name).description(spec.description).inputSchema(inputSchema.build()).build()
  }

  companion object {
    /** The client is created on first use, so the application starts without credentials. */
    fun fromConfig(config: ChatModelConfig): AnthropicChatModel {
      val client by lazy {
        AnthropicOkHttpClient.builder()
          .fromEnv() // SDK credential resolution (ANTHROPIC_API_KEY, profiles, ...) unless overridden below
          .apply {
            config.baseUrl?.let { baseUrl(it) }
            config.apiKey?.let { apiKey(it) }
            config.extraHeaders.forEach { (k, v) -> putHeader(k, v) }
          }
          .timeout(config.timeout)
          .build()
      }
      return AnthropicChatModel(config) { client.messages().create(it) }
    }
  }
}
