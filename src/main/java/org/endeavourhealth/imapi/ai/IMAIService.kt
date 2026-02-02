package org.endeavourhealth.imapi.ai

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import org.endeavourhealth.imapi.logic.service.DataModelService
import org.endeavourhealth.imapi.logic.service.EntityService
import org.endeavourhealth.imapi.logic.service.SearchService
import org.endeavourhealth.imapi.model.DataModelProperty
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException
import org.endeavourhealth.imapi.model.iml.Page
import org.endeavourhealth.imapi.model.imq.Node
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.imq.QueryException
import org.endeavourhealth.imapi.model.imq.Where
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.vocabulary.EntityType
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.Namespace
import org.endeavourhealth.imapi.vocabulary.RDF
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import java.net.URI

@Service
class IMAIService {
  private val log = LoggerFactory.getLogger(IMAIService::class.java)
  private val om = ObjectMapper()
  private val dataModelService = DataModelService()
  private val searchService = SearchService()
  private val entityService = EntityService()

  @Tool(description = "Gets the name of a given IRI to ensure the IRI exists, and check its correctness")
  fun getNameFromiri(iri: URI): String {
    log.debug("Getting name for $iri")
    return entityService.getName(iri.toString())
  }

  @Tool(description = "Gets the list of known logical data models, their IRI, name and description")
  fun getDataModels(): List<TTIriRef> {
    log.debug("Getting model list")
    return entityService.getChildren("http://endhealth.info/im#HealthRecords",
      listOf(Namespace.IM.toString()), 1, 99, false)
  }

  @Tool(description = "Get the properties (and their types) of a data model given its IRI")
  fun getHealthDataModelProperties(iri: URI): List<DataModelProperty> {
    log.debug("Getting properties for model $iri")
    return dataModelService.getDataModelProperties(iri.toString())
  }
  @Tool(description = "Get an IRI for an entity given its name and type (DATAMODEL, PROPERTY, CONCEPT)")
  @Throws(OpenSearchException::class, JsonProcessingException::class, QueryException::class)
  fun getIriForEntity(text: String, type: EntityType): String {
    log.debug("Getting IRI for entity $text of type $type ")
    val w = Where()
    w.addAnd(Where()
      .setIri(IM.HAS_STATUS)
      .setIs(listOf(
        Node().setIri(IM.ACTIVE.toString())))
    )

    val schemes = mutableListOf(
      Node().setIri(Namespace.IM.toString())
    )

    if (EntityType.CONCEPT == type) {
      schemes.add(Node().setIri(Namespace.SNOMED.toString()))
      schemes.add(Node().setIri(Namespace.SMARTLIFE.toString()))
      }

    w.addAnd(Where()
      .setIri(IM.HAS_SCHEME)
      .setIs(schemes)
    )
    w.addAnd(Where()
      .setIri(RDF.TYPE)
      .setIs(listOf(
        Node().setIri(type.toString())
      ))
    )

    val qry = Query()
    qry.setWhere(w)

    val qr = QueryRequest()
    qr.setTextSearch(text)
    qr.setPage(Page().setPageNumber(1).setPageSize(10))
    qr.setQuery(qry)
    val searchResponse = searchService.queryIMSearch(qr)

    if (searchResponse != null) {
      val entities = searchResponse.getEntities()
      if (entities != null && !entities.isEmpty()) {
        val response = om.writerWithDefaultPrettyPrinter().writeValueAsString(entities)
        log.debug(response)
        return response
      }
    }
    return ""
  }
}