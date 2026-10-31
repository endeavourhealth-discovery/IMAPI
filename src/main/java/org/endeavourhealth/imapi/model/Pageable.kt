package org.endeavourhealth.imapi.model

class Pageable<T> {
  var totalCount: Int? = null
    private set
  var currentPage: Int? = null
    private set
  var pageSize: Int? = null
    private set
  var result: MutableList<T>? = null
    private set

  fun setTotalCount(totalCount: Int?): Pageable<T> {
    this.totalCount = totalCount
    return this
  }

  fun setCurrentPage(currentPage: Int?): Pageable<T> {
    this.currentPage = currentPage
    return this
  }

  fun setPageSize(pageSize: Int?): Pageable<T> {
    this.pageSize = pageSize
    return this
  }

  fun setResult(result: MutableList<T>?): Pageable<T> {
    this.result = result
    return this
  }
}
