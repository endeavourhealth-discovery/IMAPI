package org.endeavourhealth.imapi.model.responses

import org.endeavourhealth.imapi.model.search.SearchResultSummary

class SearchResponse {
  var page: Int? = null
    private set
  var size: Int? = null
    private set
  var totalCount: Int? = null
    private set
  var highestUsage: Int? = null
    private set
  var term: String? = null
    private set
  var entities: MutableList<SearchResultSummary> = arrayListOf()
    private set
  var isExactMatch: Boolean = false
    private set

  fun setExactMatch(exactMatch: Boolean): SearchResponse {
    isExactMatch = exactMatch
    return this
  }

  fun setTerm(term: String?): SearchResponse {
    this.term = term
    return this
  }

  fun setPage(page: Int?): SearchResponse {
    this.page = page
    return this
  }

  fun setSize(size: Int?): SearchResponse {
    this.size = size
    return this
  }

  fun setHighestUsage(maxUsage: Int?): SearchResponse {
    this.highestUsage = maxUsage
    return this
  }

  fun setEntities(entities: MutableList<SearchResultSummary>): SearchResponse {
    this.entities = entities
    return this
  }

  fun addEntity(entity: SearchResultSummary): SearchResponse {
    this.entities.add(entity)
    return this
  }

  fun addEntities(entities: MutableList<SearchResultSummary>): SearchResponse {
    this.entities.addAll(entities)
    return this
  }

  fun setTotalCount(totalCount: Int?): SearchResponse {
    this.totalCount = totalCount
    return this
  }
}
