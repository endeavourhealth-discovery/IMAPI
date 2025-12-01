package org.endeavourhealth.imapi.ai

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.endeavourhealth.imapi.logic.service.DataModelService
import org.endeavourhealth.imapi.logic.service.SearchService
import org.endeavourhealth.imapi.model.DataModelProperty
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException
import org.endeavourhealth.imapi.model.iml.Page
import org.endeavourhealth.imapi.model.imq.Node
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.QueryException
import org.endeavourhealth.imapi.model.imq.Where
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.responses.SearchResponse
import org.endeavourhealth.imapi.model.search.SearchResultSummary
import org.endeavourhealth.imapi.model.sql.MappingParser
import org.endeavourhealth.imapi.model.sql.TableMap
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.Namespace
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import java.io.IOException
@Service
class IMAIService {
  private val om = ObjectMapper()
  private val dataModelService = DataModelService()
  private val searchService = SearchService()

  @Tool(description = "Get an IRI for an entity (concept, data model, property, query, set or SNOMED code) given its name")
  @Throws(OpenSearchException::class, JsonProcessingException::class, QueryException::class)
  fun getIriForEntity(text: String): String {
    val w = Where()
    w.addAnd(Where()
      .setIri(IM.HAS_STATUS)
      .setIs(listOf(
        Node().setIri(IM.ACTIVE.toString())))
    )
    w.addAnd(Where()
      .setIri(IM.HAS_SCHEME)
      .setIs(listOf(
        Node().setIri(Namespace.IM.toString()),
        Node().setIri(Namespace.SNOMED.toString()),
        Node().setIri(Namespace.SMARTLIFE.toString()))
      )
    )

    val qry = Query()
    qry.setWhere(w)

    val qr = QueryRequest()
    qr.setTextSearch(text)
    qr.setPage(Page().setPageNumber(1).setPageSize(1))
    qr.setQuery(qry)
    val searchResponse = searchService.queryIMSearch(qr)

    if (searchResponse != null) {
      val entities = searchResponse.getEntities()
      if (entities != null && !entities.isEmpty())
        return om.writeValueAsString(entities.first())
    }
    return ""
  }

  @Tool(description = "Get the properties of a given data model")
  fun getHealthDataModelProperties(modelIri: String): List<DataModelProperty> {
    return dataModelService.getDataModelProperties(modelIri)
  }
}