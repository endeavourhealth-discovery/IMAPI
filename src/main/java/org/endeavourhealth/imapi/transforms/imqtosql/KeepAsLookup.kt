package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest

internal class KeepAsLookup(private val queryRequest: QueryRequest) {
  /** Every match carrying a `node`, indexed once. The main query is walked first, then each column group; first wins. */
  private val matchesByNode: Map<String, Query> by lazy {
    val index = LinkedHashMap<String, Query>()
    indexMatches(queryRequest.query, index)
    queryRequest.query?.columnGroup?.forEach { indexMatches(it, index) }
    index
  }

  fun getDataModelFromKeepAs(keepAs: String?): String? {
    val match = matchesByNode[keepAs ?: return null] ?: return null
    if (match.typeOf != null) {
      return match.typeOf.iri
    }
    if (match.path != null) {
      return match.path.first().typeOf.iri
    }
    return null
  }

  private fun indexMatches(match: Query?, index: MutableMap<String, Query>) {
    if (match == null) return
    match.node?.let { index.putIfAbsent(it, match) }
    match.and?.forEach { indexMatches(it, index) }
    match.or?.forEach { indexMatches(it, index) }
  }
}
