package org.endeavourhealth.imapi.logic.service

import org.endeavourhealth.imapi.dataaccess.SetRepository
import org.endeavourhealth.imapi.model.iml.Concept
import org.endeavourhealth.imapi.model.iml.Page
import org.endeavourhealth.imapi.model.imq.*
import org.endeavourhealth.imapi.model.responses.SearchResponse
import org.endeavourhealth.imapi.model.search.SearchResultSummary
import org.endeavourhealth.imapi.queryengine.QueryDescriptor
import org.endeavourhealth.imapi.queryengine.SparqlOptimizer
import org.endeavourhealth.imapi.transforms.ECLToIMQ
import org.endeavourhealth.imapi.transforms.IMQToECL
import org.springframework.stereotype.Component

@Component
class EclService {
  private val setRepository = SetRepository()

  fun validateModelFromQuery(eclQuery: ECLQueryRequest): ECLQueryRequest {
    eclQuery.status = ECLQueryValidator().validateQuery(eclQuery.getQuery(), ValidationLevel.ECL)
    return eclQuery
  }


  fun validateModelFromECL(eclQuery: ECLQueryRequest): ECLQueryRequest {
    try {
      getQueryFromECL(eclQuery)
      validateModelFromQuery(eclQuery)
      if (!eclQuery.status.isValid) {
        IMQToECL().getECLFromQuery(eclQuery)
      }
      return eclQuery
    } catch (e: Exception) {
      val eclStatus = ECLStatus()
      eclStatus.isValid = false
      eclStatus.setMessage(e.message)
      eclQuery.setStatus(eclStatus)
      return eclQuery
    }
  }


  @Throws(QueryException::class)
  fun getEcl(inferred: ECLQueryRequest): String? {
    val eclQueryRequest = ECLQueryRequest()
    eclQueryRequest.setShowNames(true)
    eclQueryRequest.query = inferred.query
    IMQToECL().getECLFromQuery(eclQueryRequest)
    return eclQueryRequest.ecl
  }

  @Throws(QueryException::class)
  fun getEclSearchTotalCount(request: ECLQueryRequest): Int {
    return setRepository.getSetExpansionTotalCount(request.query, request.statusFilter)
  }

  @Throws(QueryException::class)
  fun evaluateECLQuery(request: ECLQueryRequest): MutableSet<Concept> {
    return setRepository.getSetExpansionFromQuery(
      request.query,
      request.statusFilter,
      mutableListOf(),
      Page()
        .setPageNumber(request.page)
        .setPageSize(request.size)
    )
  }

  @Throws(QueryException::class)
  fun eclSearch(request: ECLQueryRequest): SearchResponse {
    SparqlOptimizer().optimizeQuery(request.query)
    var totalCount = 0
    if (request.page == 1) totalCount = getEclSearchTotalCount(request)
    val evaluated = evaluateECLQuery(request)
    val evaluatedAsSummary = evaluated
      .map { concept ->
        SearchResultSummary()
          .setIri(concept.iri)
          .setName(concept.name)
          .setCode(concept.code)
          .setScheme(concept.scheme)
          .setStatus(concept.status)
          .setType(concept.type)
      }.toMutableList()
    val result = SearchResponse()
    result.setEntities(evaluatedAsSummary)
    result.setTotalCount(totalCount)
    result.setPage(request.page)
    return result
  }

  @Throws(QueryException::class)
  fun getECLFromQuery(eclQuery: ECLQueryRequest): ECLQueryRequest {
    try {
      QueryDescriptor().describeQuery(eclQuery.query, DisplayMode.ORIGINAL)
      IMQToECL().getECLFromQuery(eclQuery)
    } catch (e: Exception) {
      throw QueryException(e.message, e)
    }
    return eclQuery
  }


  fun getQueryFromECL(eclQuery: ECLQueryRequest): ECLQueryRequest {
    val ecl = eclQuery.ecl
    if (ecl == null || ecl.isEmpty()) return eclQuery
    ECLToIMQ().getQueryFromECL(eclQuery, true)
    val query = eclQuery.query
    if (query != null && !query.isInvalid) {
      try {
        QueryDescriptor().describeQuery(query, DisplayMode.ORIGINAL)
      } catch (e: Exception) {
        eclQuery.status.isValid = false
        eclQuery.status.setMessage(e.message)
      }
    }
    return eclQuery
  }

  @Throws(QueryException::class)
  fun validateEcl(eclQuery: ECLQueryRequest): ECLQueryRequest {
    val ecl = eclQuery.ecl
    if (ecl == null || ecl.isEmpty()) return eclQuery
    try {
      ECLToIMQ().getQueryFromECL(eclQuery, true)
      QueryDescriptor().describeQuery(eclQuery.query, DisplayMode.ORIGINAL)
    } catch (e: Exception) {
      val eclStatus = ECLStatus()
      eclStatus.isValid = false
      eclStatus.setMessage(e.message)
      eclQuery.setStatus(eclStatus)
      return eclQuery
    }
    return eclQuery
  }

  fun getEclFromEcl(eclQuery: ECLQueryRequest): ECLQueryRequest {
    val ecl = eclQuery.ecl
    if (ecl == null || ecl.isEmpty()) return eclQuery
    ECLToIMQ().getQueryFromECL(eclQuery, true)
    IMQToECL().getECLFromQuery(eclQuery)
    return eclQuery
  }
}
