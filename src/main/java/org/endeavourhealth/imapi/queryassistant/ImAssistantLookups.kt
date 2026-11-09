package org.endeavourhealth.imapi.queryassistant

import org.endeavourhealth.imapi.dataaccess.OSQuery
import org.endeavourhealth.imapi.logic.service.DataModelService
import org.endeavourhealth.imapi.logic.service.EntityService
import org.endeavourhealth.imapi.model.imq.Node
import org.endeavourhealth.imapi.model.iml.Page
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.Where
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.search.SearchResultSummary
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.RDF
import org.springframework.stereotype.Component

/** [AssistantLookups] backed by OpenSearch (term search) and the triple store (definitions, names, properties). */
@Component
class ImAssistantLookups(
  private val entityService: EntityService = EntityService(),
  private val dataModelService: DataModelService = DataModelService(),
) : AssistantLookups {

  override fun searchConcepts(term: String, kind: ConceptSearchKind, limit: Int): List<LookupHit> {
    val types = when (kind) {
      ConceptSearchKind.VALUE_SET -> SET_TYPES
      ConceptSearchKind.CONCEPT -> listOf(IM.CONCEPT.toString())
      ConceptSearchKind.ANY -> emptyList()
    }
    return search(term, types, limit)
  }

  override fun searchCohorts(term: String, limit: Int): List<LookupHit> = search(term, COHORT_TYPES, limit)

  override fun properties(typeIri: String): List<PropertyInfo> =
    dataModelService.getDataModelProperties(typeIri).mapNotNull { p ->
      val property = p.property ?: return@mapNotNull null
      PropertyInfo(property.iri, property.name, p.type?.iri)
    }

  override fun definition(queryIri: String): Query? {
    val entity = entityService.getBundle(queryIri, mutableSetOf(IM.DEFINITION.toString())).entity ?: return null
    val literal = entity.get(TTIriRef.iri(IM.DEFINITION)) ?: return null
    return literal.asLiteral().objectValue(Query::class.java)
  }

  override fun names(iris: Set<String>): Map<String, String> =
    if (iris.isEmpty()) emptyMap()
    else entityService.getNames(iris.toMutableSet())
      .filter { !it.name.isNullOrBlank() }
      .associate { it.iri to it.name }

  override fun existing(iris: Set<String>): Set<String> = iris.filterTo(mutableSetOf()) { entityService.iriExists(it) }

  private fun search(term: String, types: List<String>, limit: Int): List<LookupHit> {
    val request = QueryRequest()
      .setTextSearch(term)
      .setPage(Page().setPageNumber(1).setPageSize(limit))
    if (types.isNotEmpty()) {
      request.setQuery(Query().setWhere(Where().setIri(RDF.TYPE).setIs(types.map { Node.iri(it) })))
    }
    val response = OSQuery().OSQueryAsSearchResponse(request) ?: return emptyList()
    return response.entities.mapNotNull { it.toHit() }
  }

  private fun SearchResultSummary.toHit(): LookupHit? {
    val iri = iri ?: return null
    return LookupHit(
      iri = iri,
      name = name,
      code = code,
      types = type.orEmpty().mapNotNull { it.iri },
      usage = usageTotal,
    )
  }

  companion object {
    val SET_TYPES = listOf(IM.CONCEPT_SET.toString(), IM.VALUESET.toString())
    val COHORT_TYPES = listOf(IM.QUERY.toString(), IM.COHORT.toString())
  }
}
