package org.endeavourhealth.imapi.model.search

import org.endeavourhealth.imapi.model.imq.ECLQueryRequest
import org.endeavourhealth.imapi.model.requests.QueryRequest

class DownloadByQueryOptions {
  var queryRequest: QueryRequest? = null
    private set
  var eclSearchRequest: ECLQueryRequest? = null
    private set
  var totalCount = 0
    private set
  var format: String? = null
    private set
  var includeDefinition = false
    private set
  var includeCore = false
    private set
  var includeLegacy = false
    private set
  var includeSubsets = false
    private set
  var subsetsOnOwnRow = false
    private set
  var im1id = false
    private set

  fun setQueryRequest(queryRequest: QueryRequest?): DownloadByQueryOptions {
    this.queryRequest = queryRequest
    return this
  }

  fun setEclSearchRequest(eclSearchRequest: ECLQueryRequest?): DownloadByQueryOptions {
    this.eclSearchRequest = eclSearchRequest
    return this
  }

  fun setTotalCount(totalCount: Int): DownloadByQueryOptions {
    this.totalCount = totalCount
    return this
  }

  fun setFormat(format: String?): DownloadByQueryOptions {
    this.format = format
    return this
  }

  fun setIncludeDefinition(includeDefinition: Boolean): DownloadByQueryOptions {
    this.includeDefinition = includeDefinition
    return this
  }

  fun setIncludeCore(includeCore: Boolean): DownloadByQueryOptions {
    this.includeCore = includeCore
    return this
  }

  fun setIncludeLegacy(includeLegacy: Boolean): DownloadByQueryOptions {
    this.includeLegacy = includeLegacy
    return this
  }

  fun setIncludeSubsets(includeSubsets: Boolean): DownloadByQueryOptions {
    this.includeSubsets = includeSubsets
    return this
  }

  fun setSubsetsOnOwnRow(subsetsOnOwnRow: Boolean): DownloadByQueryOptions {
    this.subsetsOnOwnRow = subsetsOnOwnRow
    return this
  }

  fun setIm1id(im1id: Boolean): DownloadByQueryOptions {
    this.im1id = im1id
    return this
  }
}
