package org.endeavourhealth.imapi.queryassistant

import com.fasterxml.jackson.annotation.JsonInclude
import org.endeavourhealth.imapi.model.imq.Query

/** One search hit, as shown to the model. IRIs are compacted (im:, sct:, ...). */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class LookupHit(
  val iri: String,
  val name: String?,
  val code: String? = null,
  val types: List<String> = emptyList(),
  val usage: Int? = null,
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class PropertyInfo(
  val iri: String,
  val name: String?,
  val valueType: String?,
)

enum class ConceptSearchKind { VALUE_SET, CONCEPT, ANY }

/**
 * Read-only access to the Information Model used by the assistant's tools.
 * Kept as an interface so the tool layer and agent loop can be tested without a triple store or OpenSearch.
 * All IRIs going in and out are full IRIs; the tool layer handles prefixing.
 */
interface AssistantLookups {
  fun searchConcepts(term: String, kind: ConceptSearchKind, limit: Int): List<LookupHit>
  fun searchCohorts(term: String, limit: Int): List<LookupHit>
  fun properties(typeIri: String): List<PropertyInfo>
  /** The stored IMQ definition of a query/cohort, or null when there is none. */
  fun definition(queryIri: String): Query?
  /** Names for the given IRIs; IRIs without a name are absent from the result. */
  fun names(iris: Set<String>): Map<String, String>
  /** The subset of [iris] that exist in the store. */
  fun existing(iris: Set<String>): Set<String>
}
