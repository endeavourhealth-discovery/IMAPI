package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.endeavourhealth.imapi.model.imq.QueryException
import org.endeavourhealth.imapi.queryengine.QueryValidator
import org.endeavourhealth.imapi.queryassistant.llm.ToolSpec
import org.slf4j.LoggerFactory

/** Result of running a tool. [isError] marks failures the model should react to (bad input, invalid draft). */
data class ToolOutcome(val content: String, val isError: Boolean = false)

/**
 * The assistant's tools: IM lookups that ground every IRI, plus set_draft, which validates and
 * stores the model's CohortSpec. Independent of any model provider so it can be tested directly.
 */
class QueryAssistantTools(
  private val lookups: AssistantLookups,
  private val compiler: QuerySpecCompiler = QuerySpecCompiler(),
  private val decompiler: QuerySpecDecompiler = QuerySpecDecompiler(compiler),
) {
  private val log = LoggerFactory.getLogger(javaClass)
  private val json = jacksonObjectMapper()

  val specs: List<ToolSpec> = listOf(
    ToolSpec(
      SEARCH_CONCEPTS,
      "Search the Information Model for value sets / concept sets (default) or individual concepts by term. " +
        "Use this to find the IRI of every clinical code list used in a filter.",
      schema(
        """{"type":"object","properties":{
          "term":{"type":"string","description":"Search text, e.g. 'asthma' or 'HbA1c'."},
          "kind":{"type":"string","enum":["VALUE_SET","CONCEPT","ANY"],"description":"Default VALUE_SET."},
          "limit":{"type":"integer","minimum":1,"maximum":$MAX_LIMIT,"description":"Default $DEFAULT_LIMIT."}
        },"required":["term"],"additionalProperties":false}"""
      ),
    ),
    ToolSpec(
      SEARCH_COHORTS,
      "Search existing cohort queries (e.g. disease registers, 'registered for GMS') by term. " +
        "Reuse a matching cohort with kind 'cohort' instead of rebuilding its logic.",
      schema(
        """{"type":"object","properties":{
          "term":{"type":"string"},
          "limit":{"type":"integer","minimum":1,"maximum":$MAX_LIMIT}
        },"required":["term"],"additionalProperties":false}"""
      ),
    ),
    ToolSpec(
      GET_PROPERTIES,
      "List the properties of a record type (e.g. im:ClinicalEntry, im:Patient, im:MedicationRequest) " +
        "with their value types. Use it to find a property IRI not listed in the core vocabulary.",
      schema(
        """{"type":"object","properties":{
          "type":{"type":"string","description":"Record type IRI, e.g. im:ClinicalEntry."}
        },"required":["type"],"additionalProperties":false}"""
      ),
    ),
    ToolSpec(
      GET_COHORT_DEFINITION,
      "Show how an existing cohort query is built, as a CohortSpec with a plain-English read-back. " +
        "Useful as a template for a similar request.",
      schema(
        """{"type":"object","properties":{
          "iri":{"type":"string"}
        },"required":["iri"],"additionalProperties":false}"""
      ),
    ),
    ToolSpec(
      SET_DRAFT,
      "Save the complete query as a CohortSpec, replacing any previous draft. The server checks it compiles, " +
        "that every IRI was found by a lookup and exists, and returns a plain-English read-back or a list of errors to fix.",
      draftSchema(),
    ),
  )

  fun execute(name: String, input: JsonNode, workspace: AssistantWorkspace): ToolOutcome =
    try {
      when (name) {
        SEARCH_CONCEPTS -> searchConcepts(input, workspace)
        SEARCH_COHORTS -> searchCohorts(input, workspace)
        GET_PROPERTIES -> properties(input, workspace)
        GET_COHORT_DEFINITION -> definition(input, workspace)
        SET_DRAFT -> setDraft(input, workspace)
        else -> ToolOutcome("Unknown tool '$name'", isError = true)
      }
    } catch (e: ToolInputException) {
      ToolOutcome(e.message ?: "invalid input", isError = true)
    } catch (e: Exception) {
      log.warn("Query assistant tool {} failed", name, e)
      ToolOutcome("Tool '$name' failed: ${e.message ?: e.javaClass.simpleName}", isError = true)
    }

  // ------------------------------------------------------------------ lookups

  private fun searchConcepts(input: JsonNode, ws: AssistantWorkspace): ToolOutcome {
    val kind = input.path("kind").textValue()?.let { k ->
      ConceptSearchKind.entries.firstOrNull { it.name == k } ?: throw ToolInputException("kind must be one of ${ConceptSearchKind.entries}")
    } ?: ConceptSearchKind.VALUE_SET
    return hits(lookups.searchConcepts(requiredText(input, "term"), kind, limit(input)), ws)
  }

  private fun searchCohorts(input: JsonNode, ws: AssistantWorkspace) =
    hits(lookups.searchCohorts(requiredText(input, "term"), limit(input)), ws)

  private fun hits(hits: List<LookupHit>, ws: AssistantWorkspace): ToolOutcome {
    hits.forEach { ws.seenIris += it.iri }
    if (hits.isEmpty()) return ToolOutcome("No matches. Try a broader or different term.")
    val compact = hits.map { h -> h.copy(iri = IriPrefixes.compact(h.iri), types = h.types.map(IriPrefixes::compact)) }
    return ToolOutcome(json.writeValueAsString(compact))
  }

  private fun properties(input: JsonNode, ws: AssistantWorkspace): ToolOutcome {
    val type = IriPrefixes.expand(requiredText(input, "type"))
    val properties = lookups.properties(type)
    if (properties.isEmpty()) return ToolOutcome("No properties found for ${IriPrefixes.compact(type)}.", isError = true)
    ws.seenIris += type
    properties.forEach { ws.seenIris += it.iri }
    val compact = properties.map { p -> p.copy(iri = IriPrefixes.compact(p.iri), valueType = p.valueType?.let(IriPrefixes::compact)) }
    return ToolOutcome(json.writeValueAsString(compact))
  }

  private fun definition(input: JsonNode, ws: AssistantWorkspace): ToolOutcome {
    val iri = IriPrefixes.expand(requiredText(input, "iri"))
    val query = lookups.definition(iri)
      ?: return ToolOutcome("${IriPrefixes.compact(iri)} has no query definition.", isError = true)
    ws.seenIris += iri
    val spec = try {
      decompiler.decompile(query)
    } catch (e: UnsupportedQueryException) {
      return ToolOutcome(
        "This query uses features the assistant cannot express (${e.reason} at ${e.path}). " +
          "It can still be reused as a whole with {\"kind\":\"cohort\",\"iri\":\"${IriPrefixes.compact(iri)}\"}."
      )
    }
    spec.referencedIris().forEach { ws.seenIris += it.iri }
    val result = json.createObjectNode()
    result.set<JsonNode>("spec", json.valueToTree(spec))
    result.put("readback", SpecReadback.render(spec))
    return ToolOutcome(json.writeValueAsString(result))
  }

  // ------------------------------------------------------------------ set_draft

  private fun setDraft(input: JsonNode, ws: AssistantWorkspace): ToolOutcome {
    val specNode = input.get("spec") ?: throw ToolInputException("'spec' is required")
    val spec = try {
      json.treeToValue(specNode, CohortSpec::class.java)
    } catch (e: JsonMappingException) {
      return invalid(listOf("${jsonPath(e)}: ${e.originalMessage}"))
    } catch (e: JsonProcessingException) {
      return invalid(listOf(e.originalMessage))
    }

    val errors = mutableListOf<String>()
    try {
      compiler.compile(spec)
    } catch (e: QuerySpecException) {
      errors += e.errors
    }

    val uses = spec.referencedIris()
    uses.filter { it.iri !in ws.seenIris }
      .distinctBy { it.iri }
      .forEach {
        errors += "${it.path}: ${IriPrefixes.compact(it.iri)} was not returned by any lookup in this conversation; " +
          "find the right IRI with search_concepts, search_cohorts or get_properties"
      }
    val distinct = uses.map { it.iri }.toSet()
    val existing = lookups.existing(distinct)
    uses.filter { it.iri !in existing }
      .distinctBy { it.iri }
      .forEach { errors += "${it.path}: ${IriPrefixes.compact(it.iri)} does not exist in the Information Model" }
    if (errors.isNotEmpty()) return invalid(errors)

    val named = spec.withNames(lookups.names(distinct))
    val query = compiler.compile(named)
    try {
      QueryValidator().validateQuery(query)
    } catch (e: QueryException) {
      return invalid(listOf("query validation: ${e.message}"))
    }

    val readback = SpecReadback.render(named)
    ws.draft = Draft(named, query, readback)
    val result = json.createObjectNode().put("status", "valid").put("readback", readback)
    return ToolOutcome(json.writeValueAsString(result))
  }

  private fun invalid(errors: List<String>): ToolOutcome {
    val result = json.createObjectNode().put("status", "invalid")
    result.putArray("errors").apply { errors.forEach { add(it) } }
    result.put("note", "The previous draft (if any) is unchanged. Fix all errors and call set_draft again.")
    return ToolOutcome(json.writeValueAsString(result), isError = true)
  }

  private fun jsonPath(e: JsonMappingException): String =
    "$" + e.path.joinToString("") { ref ->
      when {
        ref.fieldName != null -> ".${ref.fieldName}"
        ref.index >= 0 -> "[${ref.index}]"
        else -> ""
      }
    }

  // ------------------------------------------------------------------ helpers

  private fun requiredText(input: JsonNode, field: String): String =
    input.path(field).textValue()?.takeIf { it.isNotBlank() } ?: throw ToolInputException("'$field' is required")

  private fun limit(input: JsonNode): Int =
    input.path("limit").asInt(DEFAULT_LIMIT).coerceIn(1, MAX_LIMIT)

  private fun schema(text: String): JsonNode = json.readTree(text)

  private fun draftSchema(): JsonNode {
    val spec = javaClass.getResourceAsStream("/queryassistant/cohort-spec.schema.json")
      ?.use { json.readTree(it) } as? ObjectNode
      ?: error("cohort-spec.schema.json missing from classpath")
    val defs = spec.remove("\$defs")
    val root = json.createObjectNode().put("type", "object")
    root.putObject("properties").set<JsonNode>("spec", spec)
    root.putArray("required").add("spec")
    root.put("additionalProperties", false)
    root.set<JsonNode>("\$defs", defs)
    return root
  }

  private class ToolInputException(message: String) : RuntimeException(message)

  companion object {
    const val SEARCH_CONCEPTS = "search_concepts"
    const val SEARCH_COHORTS = "search_cohorts"
    const val GET_PROPERTIES = "get_properties"
    const val GET_COHORT_DEFINITION = "get_cohort_definition"
    const val SET_DRAFT = "set_draft"
    const val DEFAULT_LIMIT = 10
    const val MAX_LIMIT = 25
  }
}
