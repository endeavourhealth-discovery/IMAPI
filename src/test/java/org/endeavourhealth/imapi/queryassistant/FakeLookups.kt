package org.endeavourhealth.imapi.queryassistant

import org.endeavourhealth.imapi.model.imq.Query

/** In-memory [AssistantLookups] for tests. Every IRI mentioned in the fake counts as existing. */
class FakeLookups(
  val valueSets: List<LookupHit> = emptyList(),
  val cohorts: List<LookupHit> = emptyList(),
  val propertiesByType: Map<String, List<PropertyInfo>> = emptyMap(),
  val definitions: Map<String, Query> = emptyMap(),
  extraExisting: Set<String> = emptySet(),
) : AssistantLookups {
  val searches = mutableListOf<String>()

  private val names: Map<String, String> =
    (valueSets + cohorts).filter { it.name != null }.associate { it.iri to it.name!! } +
      propertiesByType.values.flatten().filter { it.name != null }.associate { it.iri to it.name!! }

  private val existing: Set<String> =
    extraExisting + QueryAssistantPrompt.coreIris + names.keys + propertiesByType.keys + definitions.keys

  override fun searchConcepts(term: String, kind: ConceptSearchKind, limit: Int): List<LookupHit> {
    searches += "concepts:$term:$kind"
    return valueSets.filter { it.name.orEmpty().contains(term, ignoreCase = true) }.take(limit)
  }

  override fun searchCohorts(term: String, limit: Int): List<LookupHit> {
    searches += "cohorts:$term"
    return cohorts.filter { it.name.orEmpty().contains(term, ignoreCase = true) }.take(limit)
  }

  override fun properties(typeIri: String) = propertiesByType[typeIri].orEmpty()
  override fun definition(queryIri: String) = definitions[queryIri]
  override fun names(iris: Set<String>) = names.filterKeys { it in iris }
  override fun existing(iris: Set<String>) = iris.filterTo(mutableSetOf()) { it in existing }
}
