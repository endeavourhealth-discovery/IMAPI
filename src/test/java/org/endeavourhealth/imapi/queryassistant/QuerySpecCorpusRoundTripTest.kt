package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.endeavourhealth.imapi.model.imq.Query
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Round-trips every cohort query in an exported corpus through QuerySpec:
 *   IMQ -> decompile -> CohortSpec -> JSON -> CohortSpec -> compile -> IMQ  (must be identical).
 *
 * The corpus is a CSV with columns s (query IRI) and o (IMQ JSON), e.g. a SPARQL export.
 * It is not checked in; point -DquerySpec.corpus at it or place query-result.csv in the project root.
 * The test is skipped when the file is absent.
 */
class QuerySpecCorpusRoundTripTest {

  private val imqMapper = QuerySpecDecompiler.defaultImqMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  private val specMapper = jacksonObjectMapper()
  private val compiler = QuerySpecCompiler()
  private val decompiler = QuerySpecDecompiler(compiler, imqMapper)

  @Test
  fun cohortCorpusRoundTripsLosslessly() {
    val file = File(System.getProperty("querySpec.corpus") ?: "query-result.csv")
    assumeTrue(file.exists(), "corpus ${file.absolutePath} not found")

    val rows = readCsv(file).drop(1)
    val unsupported = mutableMapOf<String, MutableList<String>>()
    val lossy = mutableListOf<String>()
    var datasets = 0
    var converted = 0
    var imqChars = 0L
    var specChars = 0L

    for ((iri, json) in rows) {
      val query = imqMapper.readValue(json, Query::class.java)
      if (query.columnGroup != null) {
        datasets++; continue
      }
      try {
        val spec = decompiler.decompile(query)

        // the spec must also survive its own JSON form (polymorphic kinds, enums, defaults)
        val specJson = specMapper.writeValueAsString(spec)
        val reparsed = specMapper.readValue<CohortSpec>(specJson)
        assertEquals(spec, reparsed, "spec JSON round trip changed $iri")

        val original = imqMapper.valueToTree<com.fasterxml.jackson.databind.JsonNode>(query)
        val recompiled = imqMapper.valueToTree<com.fasterxml.jackson.databind.JsonNode>(compiler.compile(reparsed))
        firstDifference(original, recompiled)?.let { error("$iri: ${it.path} ${it.description}") }

        converted++
        imqChars += imqMapper.writeValueAsString(query).length
        specChars += specJson.length
      } catch (e: UnsupportedQueryException) {
        if (e.reason.startsWith("lossy") || e.reason.startsWith("decompiled spec")) lossy += "$iri ${e.message}"
        else unsupported.getOrPut("${genericPath(e.path)}: ${e.reason}") { mutableListOf() } += iri
      }
    }

    val cohorts = rows.size - datasets
    println("QuerySpec corpus: ${rows.size} queries, $datasets datasets skipped, $cohorts cohorts")
    println("  converted losslessly: $converted (${percent(converted, cohorts)})")
    println("  unsupported:          ${unsupported.values.sumOf { it.size }}")
    unsupported.entries.sortedByDescending { it.value.size }.forEach { (reason, iris) ->
      println("    ${iris.size.toString().padStart(5)}  $reason   e.g. ${iris.first()}")
    }
    if (converted > 0) println("  spec size vs IMQ:     ${percent(specChars.toInt(), imqChars.toInt())} of the characters")
    lossy.take(20).forEach { println("  LOSSY $it") }

    assertTrue(lossy.isEmpty(), "${lossy.size} queries decompiled lossily - compiler/decompiler mismatch")
    assertTrue(converted >= cohorts * 0.95, "coverage dropped below 95%: $converted / $cohorts")
  }

  private fun genericPath(path: String) = path.replace(Regex("\\[\\d+]"), "[]")

  private fun percent(a: Int, b: Int) = if (b == 0) "n/a" else "%.1f%%".format(100.0 * a / b)

  /** Minimal RFC 4180 reader: quoted fields, doubled quotes, newlines inside quotes. Two columns. */
  private fun readCsv(file: File): List<Pair<String, String>> {
    val text = file.readText()
    val rows = mutableListOf<Pair<String, String>>()
    val fields = mutableListOf<String>()
    val field = StringBuilder()
    var i = 0
    var quoted = false
    fun endField() { fields += field.toString(); field.clear() }
    fun endRow() {
      endField()
      if (fields.size >= 2) rows += fields[0] to fields[1]
      fields.clear()
    }
    while (i < text.length) {
      val c = text[i]
      when {
        quoted && c == '"' && i + 1 < text.length && text[i + 1] == '"' -> { field.append('"'); i++ }
        c == '"' -> quoted = !quoted
        quoted -> field.append(c)
        c == ',' -> endField()
        c == '\n' -> endRow()
        c == '\r' -> {}
        else -> field.append(c)
      }
      i++
    }
    if (field.isNotEmpty() || fields.isNotEmpty()) endRow()
    return rows
  }
}
