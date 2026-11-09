package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.endeavourhealth.imapi.queryassistant.llm.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class QueryAssistantServiceTest {

  private val json = jacksonObjectMapper()
  private val astCod = "http://snomed.info/sct#999012891000230104"
  private val lookups = FakeLookups(valueSets = listOf(LookupHit(astCod, "AST_COD")))

  /** Returns scripted responses in order and records each request it was sent. */
  private class ScriptedModel(vararg responses: ChatResponse) : ChatModel {
    private val queue = ArrayDeque(responses.toList())
    val requests = mutableListOf<ChatRequest>()
    override fun complete(request: ChatRequest): ChatResponse {
      requests.add(request)
      return queue.removeFirstOrNull() ?: error("model called more times than scripted")
    }
  }

  private fun respond(stop: StopKind, vararg blocks: ChatBlock) =
    ChatResponse(ChatTurn(Role.ASSISTANT, blocks.toList()), stop, stop.name, TokenUsage(input = 100, output = 20, cacheRead = 80))

  private fun call(id: String, name: String, input: String) = ToolCall(id, name, json.readTree(input))

  private val spec = """{"spec": {"rules": [
    {"match": {"kind": "cohort", "iri": "im:Q_RegisteredGMS"}, "ifTrue": "NEXT", "ifFalse": "REJECT"},
    {"match": {"kind": "records", "entity": "im:ClinicalEntry",
      "filter": {"kind": "concept", "concepts": [{"iri": "sct:999012891000230104", "match": "SET_MEMBERS"}]}},
     "ifTrue": "SELECT", "ifFalse": "REJECT"}]}}"""

  @Test
  fun runsToolLoopAndReturnsDraft() {
    val model = ScriptedModel(
      respond(StopKind.TOOL_USE, Text("Let me find the asthma code list."), call("t1", "search_concepts", """{"term": "AST_COD"}""")),
      respond(StopKind.TOOL_USE, call("t2", "set_draft", spec)),
      respond(StopKind.END, Text("Drafted: registered patients with an asthma code.")),
    )
    val reply = QueryAssistantService(lookups, model).chat(null, "user-1", "Patients with asthma")

    assertEquals("end_turn", reply.stopReason)
    assertEquals("Let me find the asthma code list.\n\nDrafted: registered patients with an asthma code.", reply.reply)
    assertTrue(reply.draft!!.readback.contains("AST_COD (set)"))
    assertEquals(3, reply.usage.modelCalls)
    assertEquals(240, reply.usage.cacheReadInputTokens)

    // every request carries the fixed system prompt and tools, plus the whole append-only history
    val last = model.requests[2]
    assertEquals(QueryAssistantPrompt.system, last.system)
    assertEquals(
      listOf("search_concepts", "search_cohorts", "get_properties", "get_cohort_definition", "set_draft"),
      last.tools.map { it.name },
    )
    assertEquals(listOf(Role.USER, Role.ASSISTANT, Role.USER, Role.ASSISTANT, Role.USER), last.turns.map { it.role })
    val draftResult = last.turns[4].blocks.single() as ToolResult
    assertEquals("t2", draftResult.callId)
    assertFalse(draftResult.isError, draftResult.content)
  }

  @Test
  fun invalidToolArgumentsAreReportedBackToTheModel() {
    val model = ScriptedModel(
      respond(StopKind.TOOL_USE, ToolCall("t1", "search_concepts", null, "arguments are not valid JSON")),
      respond(StopKind.END, Text("ok")),
    )
    QueryAssistantService(lookups, model).chat(null, "u", "hi")

    val result = model.requests[1].turns.last().blocks.single() as ToolResult
    assertTrue(result.isError)
    assertTrue(result.content.contains("arguments are not valid JSON"))
  }

  @Test
  fun continuesConversationInTheSameSession() {
    val model = ScriptedModel(respond(StopKind.END, Text("Which time window?")), respond(StopKind.END, Text("Thanks.")))
    val service = QueryAssistantService(lookups, model)
    val first = service.chat(null, "user-1", "Patients with asthma")
    service.chat(first.sessionId, "user-1", "Last 12 months")

    assertEquals(listOf(Role.USER, Role.ASSISTANT, Role.USER), model.requests[1].turns.map { it.role })
    assertThrows<SessionNotFoundException> { service.chat(first.sessionId, "someone-else", "hi") }
  }

  @Test
  fun truncatedToolCallIsAnsweredBeforeTheNextUserMessage() {
    val model = ScriptedModel(
      respond(StopKind.MAX_TOKENS, call("t1", "set_draft", "{}")),
      respond(StopKind.END, Text("Sorry, retrying.")),
    )
    val service = QueryAssistantService(lookups, model)
    val first = service.chat(null, "u", "Patients with asthma")
    assertEquals("max_tokens", first.stopReason)

    service.chat(first.sessionId, "u", "continue")
    val blocks = model.requests[1].turns[2].blocks
    assertEquals("t1", (blocks[0] as ToolResult).callId)
    assertTrue((blocks[0] as ToolResult).isError)
    assertEquals("continue", (blocks[1] as Text).text)
  }

  @Test
  fun stopsAfterTheModelCallLimit() {
    val looping = (1..3).map { respond(StopKind.TOOL_USE, call("t$it", "search_cohorts", """{"term": "x"}""")) }
    val reply = QueryAssistantService(lookups, ScriptedModel(*looping.toTypedArray()), maxModelCalls = 3).chat(null, "u", "hi")

    assertEquals("tool_limit", reply.stopReason)
    assertTrue(reply.reply.contains("stopped after 3 steps"))
  }

  @Test
  fun refusalEndsTheTurnPolitely() {
    val reply = QueryAssistantService(lookups, ScriptedModel(respond(StopKind.REFUSAL))).chat(null, "u", "hi")
    assertEquals("refusal", reply.stopReason)
    assertEquals("Sorry, I can't help with that request.", reply.reply)
  }
}
