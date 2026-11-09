package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.queryassistant.llm.ChatModelConfig
import org.endeavourhealth.imapi.queryassistant.llm.Protocol
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.random.Random

/**
 * End-to-end evaluation of the query assistant against real cohort queries.
 *
 * NOT part of `./gradlew test`: it calls the configured model (usually billed) and needs a configured IM environment
 * (graph store + OpenSearch env vars, QUERY_ASSISTANT_* model settings, or ANTHROPIC_API_KEY for the default). Run with `./gradlew queryAssistantEval`.
 *
 * For a deterministic sample of corpus cohorts that QuerySpec can express, the assistant is asked to build
 * the query from a description, and the draft is scored against the original:
 *  - drafted:       a valid draft was saved
 *  - IRI precision / recall over the non-core IRIs (value sets, concepts, cohorts)
 *  - rule shape:    same number of rules with the same ifTrue/ifFalse actions
 *
 * Two prompt styles: "title" (just the query's name, as a user might type it) and "readback"
 * (the original's plain-English read-back, an explicit description). System properties:
 * -DqueryAssistant.eval.size (default 10), .seed (default 42), .style (title|readback|both, default both),
 * -DquerySpec.corpus (default query-result.csv).
 */
@Tag("QueryAssistantEval")
class QueryAssistantEval {

  private val imqMapper = QuerySpecDecompiler.defaultImqMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  private val json = jacksonObjectMapper()

  data class Case(val iri: String, val title: String, val spec: CohortSpec)

  data class Score(
    val iri: String,
    val style: String,
    val drafted: Boolean,
    val iriPrecision: Double,
    val iriRecall: Double,
    val ruleShapeMatches: Boolean,
    val modelCalls: Int,
    val inputTokens: Long,
    val outputTokens: Long,
    val cacheReadTokens: Long,
    val reply: String,
    val draft: CohortSpec?,
  )

  @Test
  fun evaluate() {
    val config = ChatModelConfig.fromEnv()
    assumeTrue(
      config.protocol != Protocol.ANTHROPIC || config.apiKey != null || System.getenv("ANTHROPIC_API_KEY") != null,
      "no model configured: set QUERY_ASSISTANT_* (or ANTHROPIC_API_KEY)",
    )
    val corpus = File(System.getProperty("querySpec.corpus") ?: "query-result.csv")
    assumeTrue(corpus.exists(), "corpus ${corpus.absolutePath} not found")

    val size = System.getProperty("queryAssistant.eval.size")?.toInt() ?: 10
    val seed = System.getProperty("queryAssistant.eval.seed")?.toInt() ?: 42
    val styles = when (val s = System.getProperty("queryAssistant.eval.style") ?: "both") {
      "both" -> listOf("title", "readback")
      else -> listOf(s)
    }

    val cases = loadCases(corpus).shuffled(Random(seed)).take(size)
    val service = QueryAssistantService(ImAssistantLookups(), config.create())
    val out = File("build/query-assistant-eval").apply { mkdirs() }.resolve("results.jsonl")
    val scores = mutableListOf<Score>()

    out.bufferedWriter().use { writer ->
      for (case in cases) for (style in styles) {
        val score = runCase(service, case, style)
        scores += score
        writer.appendLine(json.writeValueAsString(score))
        println("${score.style.padEnd(8)} drafted=${score.drafted} P=${"%.2f".format(score.iriPrecision)} R=${"%.2f".format(score.iriRecall)} shape=${score.ruleShapeMatches}  ${case.title}")
      }
    }

    for (style in styles) {
      val s = scores.filter { it.style == style }
      println(
        "[$style] n=${s.size} drafted=${s.count { it.drafted }} " +
          "meanP=${"%.2f".format(s.map { it.iriPrecision }.average())} meanR=${"%.2f".format(s.map { it.iriRecall }.average())} " +
          "shape=${s.count { it.ruleShapeMatches }} calls=${s.sumOf { it.modelCalls }} " +
          "in=${s.sumOf { it.inputTokens }} out=${s.sumOf { it.outputTokens }} cacheRead=${s.sumOf { it.cacheReadTokens }}"
      )
    }
    println("Per-case results: ${out.absolutePath}")
  }

  private fun runCase(service: QueryAssistantService, case: Case, style: String): Score {
    val prompt = when (style) {
      "title" -> "Create a cohort query: ${case.title}"
      else -> "Create a cohort query that does the following:\n${SpecReadback.render(case.spec)}"
    }
    val owner = "eval"
    var reply = service.chat(null, owner, prompt)
    var calls = reply.usage.modelCalls
    var input = reply.usage.inputTokens
    var output = reply.usage.outputTokens
    var cacheRead = reply.usage.cacheReadInputTokens
    if (reply.draft == null && reply.stopReason == "end_turn") {
      // The assistant asked a clarifying question; answer once, as a non-interactive user would.
      reply = service.chat(reply.sessionId, owner, "Use your best judgement, state your assumptions, and save a draft.")
      calls += reply.usage.modelCalls; input += reply.usage.inputTokens
      output += reply.usage.outputTokens; cacheRead += reply.usage.cacheReadInputTokens
    }
    service.endSession(reply.sessionId, owner)

    val draft = reply.draft?.spec
    val expected = nonCoreIris(case.spec)
    val actual = draft?.let(::nonCoreIris).orEmpty()
    val hits = expected.intersect(actual).size.toDouble()
    return Score(
      iri = case.iri,
      style = style,
      drafted = draft != null,
      iriPrecision = if (actual.isEmpty()) 0.0 else hits / actual.size,
      iriRecall = if (expected.isEmpty()) 1.0 else hits / expected.size,
      ruleShapeMatches = draft != null && ruleShape(draft) == ruleShape(case.spec),
      modelCalls = calls,
      inputTokens = input,
      outputTokens = output,
      cacheReadTokens = cacheRead,
      reply = reply.reply,
      draft = draft,
    )
  }

  private fun nonCoreIris(spec: CohortSpec) =
    spec.referencedIris().map { it.iri }.filter { it !in QueryAssistantPrompt.coreIris }.toSet()

  private fun ruleShape(spec: CohortSpec) = spec.rules?.map { it.ifTrue to it.ifFalse } ?: listOf(null to null)

  private fun loadCases(corpus: File): List<Case> {
    val decompiler = QuerySpecDecompiler(mapper = imqMapper)
    return CorpusCsv.read(corpus).drop(1).mapNotNull { (iri, raw) ->
      val query = imqMapper.readValue(raw, Query::class.java)
      if (query.columnGroup != null || query.name.isNullOrBlank()) return@mapNotNull null
      try {
        Case(iri, query.name, decompiler.decompile(query))
      } catch (e: UnsupportedQueryException) {
        null
      }
    }
  }
}
