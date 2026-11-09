package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.annotation.JsonInclude
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.queryassistant.llm.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DraftView(val spec: CohortSpec, val query: Query, val readback: String)

data class UsageSummary(
  val modelCalls: Int,
  val inputTokens: Long,
  val outputTokens: Long,
  val cacheReadInputTokens: Long,
  val cacheCreationInputTokens: Long,
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AssistantReply(
  val sessionId: String,
  /** The assistant's text for this turn. */
  val reply: String,
  /** The current valid draft, if one has been saved in this conversation. */
  val draft: DraftView?,
  /** How the turn ended: end_turn, refusal, max_tokens, tool_limit, ... */
  val stopReason: String,
  val usage: UsageSummary,
)

/**
 * Chat assistant that turns a description of a cohort into a [CohortSpec] / IMQ query.
 * Runs a tool-use loop per user message over [QueryAssistantTools], against any [ChatModel].
 * A singleton (see [QueryAssistantConfiguration]) because it owns the in-memory sessions.
 */
class QueryAssistantService(
  lookups: AssistantLookups,
  private val model: ChatModel,
  /** Model calls allowed per user message before the turn is cut off. */
  private val maxModelCalls: Int = 15,
  private val sessions: AssistantSessionStore = AssistantSessionStore(),
) {
  private val log = LoggerFactory.getLogger(javaClass)
  private val tools = QueryAssistantTools(lookups)

  fun chat(sessionId: String?, owner: String, userMessage: String): AssistantReply {
    require(userMessage.isNotBlank()) { "message must not be blank" }
    val session = sessionId?.let { sessions.get(it, owner) } ?: sessions.create(owner, QueryAssistantPrompt.coreIris)
    synchronized(session) {
      return runTurn(session, userMessage)
    }
  }

  fun currentDraft(sessionId: String, owner: String): DraftView? =
    sessions.get(sessionId, owner).workspace.draft?.toView()

  fun endSession(sessionId: String, owner: String) = sessions.delete(sessionId, owner)

  private fun runTurn(session: AssistantSession, userMessage: String): AssistantReply {
    val userBlocks = session.pendingToolResults.toList() + Text(userMessage)
    session.pendingToolResults.clear()
    session.turns += ChatTurn(Role.USER, userBlocks)

    val usage = UsageCounter()
    val text = StringBuilder()
    repeat(maxModelCalls) {
      val response = model.complete(ChatRequest(QueryAssistantPrompt.system, tools.specs, session.turns.toList()))
      usage.add(response.usage)
      session.turns += response.turn
      response.turn.blocks.filterIsInstance<Text>().forEach { text.appendParagraph(it.text) }
      val calls = response.turn.blocks.filterIsInstance<ToolCall>()

      when (response.stop) {
        StopKind.TOOL_USE -> session.turns += ChatTurn(Role.USER, calls.map { runTool(it, session) })
        StopKind.REFUSAL -> {
          owePendingResults(session, calls, "The previous response was declined.")
          if (text.isEmpty()) text.append("Sorry, I can't help with that request.")
          return reply(session, text, "refusal", usage)
        }
        StopKind.MAX_TOKENS -> {
          owePendingResults(session, calls, "The tool call was cut off by the output limit; try again more concisely.")
          text.appendParagraph("(My reply was cut off. Ask me to continue.)")
          return reply(session, text, "max_tokens", usage)
        }
        StopKind.END, StopKind.OTHER -> {
          // a provider may stop with calls still pending; they must be answered before the next user text
          owePendingResults(session, calls, "This tool call was not run.")
          return reply(session, text, if (response.stop == StopKind.END) "end_turn" else response.rawStopReason ?: "unknown", usage)
        }
      }
    }
    text.appendParagraph("(I stopped after $maxModelCalls steps without finishing. Ask me to continue.)")
    // The last response asked for tools whose results were already appended, so history stays valid.
    return reply(session, text, "tool_limit", usage)
  }

  private fun runTool(call: ToolCall, session: AssistantSession): ToolResult {
    val input = call.input
      ?: return ToolResult(call.id, "Could not read the tool input: ${call.inputError ?: "invalid JSON"}. Send valid JSON arguments.", isError = true)
    val outcome = tools.execute(call.name, input, session.workspace)
    log.debug("assistant tool {} -> error={}", call.name, outcome.isError)
    return ToolResult(call.id, outcome.content, outcome.isError)
  }

  private fun owePendingResults(session: AssistantSession, calls: List<ToolCall>, message: String) {
    calls.forEach { session.pendingToolResults += ToolResult(it.id, message, isError = true) }
  }

  private fun reply(session: AssistantSession, text: StringBuilder, stopReason: String, usage: UsageCounter) =
    AssistantReply(session.id, text.toString().trim(), session.workspace.draft?.toView(), stopReason, usage.summary())

  private fun Draft.toView() = DraftView(spec, query, readback)

  private fun StringBuilder.appendParagraph(s: String) {
    if (s.isBlank()) return
    if (isNotEmpty()) append("\n\n")
    append(s.trim())
  }

  private class UsageCounter {
    var calls = 0; var input = 0L; var output = 0L; var cacheRead = 0L; var cacheWrite = 0L
    fun add(u: TokenUsage) {
      calls++; input += u.input; output += u.output; cacheRead += u.cacheRead; cacheWrite += u.cacheWrite
    }
    fun summary() = UsageSummary(calls, input, output, cacheRead, cacheWrite)
  }
}

@Configuration
class QueryAssistantConfiguration {
  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun queryAssistantService(lookups: ImAssistantLookups): QueryAssistantService {
    val config = ChatModelConfig.fromEnv()
    log.info("Query assistant model: {}", config)
    val maxCalls = System.getenv("QUERY_ASSISTANT_MAX_MODEL_CALLS")?.toIntOrNull() ?: 15
    return QueryAssistantService(lookups, LazyChatModel { config.create() }, maxCalls)
  }
}

/** Defers building the model (and failing on missing settings) until the assistant is first used. */
class LazyChatModel(factory: () -> ChatModel) : ChatModel {
  private val delegate by lazy(factory)
  override fun complete(request: ChatRequest) = delegate.complete(request)
}
