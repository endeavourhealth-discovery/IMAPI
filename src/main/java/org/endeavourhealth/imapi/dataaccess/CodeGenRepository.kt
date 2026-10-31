package org.endeavourhealth.imapi.dataaccess

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.node.ObjectNode
import org.eclipse.rdf4j.model.Literal
import org.eclipse.rdf4j.model.util.Values
import org.endeavourhealth.imapi.dataaccess.databases.ConfigDB
import org.endeavourhealth.imapi.logic.CachedObjectMapper
import org.endeavourhealth.imapi.model.dto.CodeGenDto
import org.endeavourhealth.imapi.vocabulary.*
import org.slf4j.LoggerFactory

class CodeGenRepository {
  private val log = LoggerFactory.getLogger(javaClass)
  fun getCodeTemplateList(): MutableList<String> {
    val result: MutableList<String> = mutableListOf()
    val sparql = """
      SELECT ?name
      WHERE {
        ?s ?type ?codeTemplate .
        ?s ?label ?name
      }
      
      """.trimIndent()
    ConfigDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("type", RDF.TYPE.asDbIri())
      qry.setBinding("codeTemplate", IM.CODE_TEMPLATE.asDbIri())
      qry.setBinding("label", RDFS.LABEL.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          result.add(bs.getValue("name").stringValue())
        }
      }
    }
    return result
  }

  fun getCodeTemplate(name: String): CodeGenDto {
    val result = CodeGenDto()
    val sparql = """
      SELECT ?p ?o
      WHERE {
        ?s ?p ?o .
      }
      
      """.trimIndent()
    ConfigDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("s", Values.iri(NAMESPACE.IM_CODE_TEMPLATE.toString() + name))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          try {
            CachedObjectMapper().use { om ->
              when (CodeTemplate.from(bs.getValue("p").stringValue())) {
                CodeTemplate.DATATYPE_MAP -> {
                  val map = om.readTree(bs.getValue("o").stringValue()) as ObjectNode
                  map.properties().forEach { (k, v) ->
                    result.datatypeMap[k] = v.textValue()
                  }
                }

                CodeTemplate.WRAPPER -> result.collectionWrapper = bs.getValue("o").stringValue()
                CodeTemplate.EXTENSION -> result.extension = bs.getValue("o").stringValue()
                CodeTemplate.LABEL -> result.name = bs.getValue("o").stringValue()
                CodeTemplate.DEFINITION -> result.template = bs.getValue("o").stringValue()
                CodeTemplate.INCLUDE_COMPLEX_TYPES -> result.complexTypes = (bs.getValue("o") as Literal).booleanValue()
                else -> {}
              }
            }
          } catch (e: JsonProcessingException) {
            log.error("Unable to parse codeTemplate", e)
          }
        }
      }
    }
    return result
  }

  fun updateCodeTemplate(
    name: String,
    extension: String,
    wrapper: String,
    dataTypeMap: MutableMap<String, String>,
    template: String,
    complexTypes: Boolean?
  ) {
    var complexTypes = complexTypes
    if (null == complexTypes) complexTypes = false

    val deleteSparql = """
      DELETE WHERE {
        ?s ?p ?o
      }
      
      """.trimIndent()
    ConfigDB.getConnection().use { conn ->
      val qry = conn.prepareDeleteSparql(deleteSparql)
      qry.setBinding("s", Values.iri(NAMESPACE.IM_CODE_TEMPLATE.toString() + name))
      qry.execute()
    }
    val insertSparql = """
      INSERT {
        ?iri ?label ?name .
        ?iri ?extensionType ?extension .
        ?iri ?type ?typeIri .
        ?iri ?definition ?template .
        ?iri ?typeMap ?datatypeMap .
        ?iri ?wrapperType ?wrapper .
        ?iri ?includeComplex ?complexTypes .
      }
      WHERE {
        SELECT ?iri ?label ?extension {}
      }
      
      """.trimIndent()
    ConfigDB.getConnection().use { conn ->
      try {
        CachedObjectMapper().use { om ->
          val qry2 = conn.prepareInsertSparql(insertSparql)
          qry2.setBinding("iri", Values.iri(NAMESPACE.IM_CODE_TEMPLATE.toString() + name))
          qry2.setBinding("label", RDFS.LABEL.asDbIri())
          qry2.setBinding("name", Values.literal(name))
          qry2.setBinding("extensionType", CodeTemplate.EXTENSION.asDbIri())
          qry2.setBinding("extension", Values.literal(extension))
          qry2.setBinding("type", RDF.TYPE.asDbIri())
          qry2.setBinding("typeIri", IM.CODE_TEMPLATE.asDbIri())
          qry2.setBinding("definition", CodeTemplate.DEFINITION.asDbIri())
          qry2.setBinding("template", Values.literal(template))
          qry2.setBinding("typeMap", CodeTemplate.DATATYPE_MAP.asDbIri())
          qry2.setBinding("datatypeMap", Values.literal(om.writeValueAsString(dataTypeMap)))
          qry2.setBinding("wrapperType", CodeTemplate.WRAPPER.asDbIri())
          qry2.setBinding("wrapper", Values.literal(wrapper))
          qry2.setBinding("includeComplex", CodeTemplate.INCLUDE_COMPLEX_TYPES.asDbIri())
          qry2.setBinding("complexTypes", Values.literal(complexTypes))
          qry2.execute()
        }
      } catch (err: JsonProcessingException) {
        log.error("Error updating codeTemplate", err)
      }
    }
  }
}
