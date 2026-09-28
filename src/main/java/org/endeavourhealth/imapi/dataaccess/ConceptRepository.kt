package org.endeavourhealth.imapi.dataaccess

import org.eclipse.rdf4j.model.util.Values
import org.endeavourhealth.imapi.dataaccess.databases.IMDB
import org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper
import org.endeavourhealth.imapi.model.ConceptContextMap
import org.endeavourhealth.imapi.model.Context
import org.endeavourhealth.imapi.model.dto.SimpleMap
import org.endeavourhealth.imapi.vocabulary.IM
import org.endeavourhealth.imapi.vocabulary.RDFS
import java.util.*

class ConceptRepository {
  fun getMatchedFrom(iri: String, schemeIris: MutableList<String>?): MutableList<SimpleMap> {
    val simpleMaps: MutableList<SimpleMap> = mutableListOf()
    val sql = """
      SELECT ?s ?code ?scheme ?name ?alternativeCode ?codeId
      WHERE {
        ?s im:matchedTo ?o .
        ?s im:code ?code .
        ${SparqlHelper.valueList("scheme", schemeIris)}
        ?s im:scheme ?scheme ;
        rdfs:label ?name .
        optional {?s im:alternativeCode ?alternativeCode .}
        optional {?s im:codeId ?codeId .}
      }
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("o", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          simpleMaps.add(
            SimpleMap(
              SparqlHelper.getString(bs, "s"),
              SparqlHelper.getString(bs, "name"),
              SparqlHelper.getString(bs, "code"),
              SparqlHelper.getString(bs, "scheme"),
              SparqlHelper.getString(bs, "alternativeCode"),
              SparqlHelper.getString(bs, "codeId")
            )
          )
        }
      }
    }
    return simpleMaps
  }

  fun getMatchedTo(iri: String, schemeIris: MutableList<String>?): MutableList<SimpleMap> {
    val simpleMaps: MutableList<SimpleMap> = mutableListOf()
    val sql: String = """
      SELECT ?o ?code ?scheme ?name
      WHERE {
        ?s im:matchedTo ?o .
        ?o im:code ?code .
        ${SparqlHelper.valueList("scheme", schemeIris)}
        ?o im:scheme ?scheme .
        ?o rdfs:label ?name .
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("s", Values.iri(iri))
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          simpleMaps.add(
            SimpleMap(
              SparqlHelper.getString(bs, "o"),
              SparqlHelper.getString(bs, "name"),
              SparqlHelper.getString(bs, "code"),
              SparqlHelper.getString(bs, "scheme"),
              null,
              null
            )
          )
        }
      }
    }
    return simpleMaps
  }

  fun getPropertiesForDomains(iris: MutableSet<String>): MutableSet<String> {
    val properties: MutableSet<String> = mutableSetOf()
    val sql: String = """
      SELECT distinct ?property
            WHERE {
              Values ?parentConcept {${iris.joinToString(" ") { "<$it>" }}
               ?concept im:isA ?parentConcept.
               ?concept im:roleGroup ?group.
               ?group ?property ?value.
               filter (?property!=im:groupNumber)
               ?property rdf:type rdf:Property.}
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          properties.add(bs.getValue("property").stringValue())
        }
      }
    }
    return properties
  }

  fun getRangesForProperty(conceptIri: String?): MutableSet<String> {
    val ranges: MutableSet<String> = mutableSetOf()
    val sql: String = """
      Select ?range
      where {
        VALUES ?superProperty {<${conceptIri}>}
        ?property im:isA ?superProperty.
        ?property rdfs:range ?range.
      }
      
      """.trimIndent()

    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          ranges.add(bs.getValue("range").stringValue())
        }
      }
    }
    return ranges
  }


  fun getConceptContextMaps(iri: String): MutableList<ConceptContextMap> {
    val result: MutableList<ConceptContextMap> = mutableListOf()
    IMDB.getConnection().use { conn ->
      val sparql = """
        SELECT ?nodeName ?sourceVal ?sourceRegex ?propertyName ?publisherName ?systemName ?schema ?table ?field
        WHERE {
          ?map ?imConcept ?concept .
          ?node ?imHasMap ?map ;
          ?imTargetProperty ?property ;
          ?rdfsLabel ?nodeName .
          ?property ?rdfsLabel ?propertyName .
          ?context ?imContextNode ?node ;
          ?imSourcePublisher ?publisher .
          ?publisher rdfs:label ?publisherName .
          ?map ?imSourceValue ?sourceVal .
          OPTIONAL {
            ?context ?imSourceSystem ?system .
            ?system ?rdfsLabel ?systemName
          }
          OPTIONAL { ?context ?imSourceSchema ?schema }
          OPTIONAL { ?context ?imSourceTable ?table }
          OPTIONAL { ?context ?imSourceField ?field }
          OPTIONAL { ?context ?imSourceConcept ?concept }
        }
        ORDER BY ?nodeName ?sourceVal ?publisherName
        
        """.trimIndent()
      val qry = conn.prepareTupleSparql(sparql)
      qry.setBinding("concept", Values.iri(iri))
      qry.setBinding("imConcept", IM.CONCEPT.asDbIri())
      qry.setBinding("imHasMap", IM.HAS_MAP.asDbIri())
      qry.setBinding("rdfsLabel", RDFS.LABEL.asDbIri())
      qry.setBinding("imContextNode", IM.CONTEXT_NODE.asDbIri())
      qry.setBinding("imTargetProperty", IM.TARGET_PROPERTY.asDbIri())
      qry.setBinding("imSourcePublisher", IM.SOURCE_PUBLISHER.asDbIri())
      qry.setBinding("imSourceSystem", IM.SOURCE_SYSTEM.asDbIri())
      qry.setBinding("imSourceSchema", IM.SOURCE_SCHEMA.asDbIri())
      qry.setBinding("imSourceTable", IM.SOURCE_TABLE.asDbIri())
      qry.setBinding("imSourceField", IM.SOURCE_FIELD.asDbIri())
      qry.setBinding("imSourceValue", IM.SOURCE_VALUE.asDbIri())
      qry.setBinding("imSourceRegex", IM.SOURCE_REGEX.asDbIri())
      qry.evaluate().use { rs ->
        while (rs.hasNext()) {
          val bs = rs.next()
          if (result.stream().noneMatch { r: ConceptContextMap? ->
              r!!.node == bs.getValue("nodeName").stringValue() || r.value == bs.getValue("sourceVal")
                .stringValue() || (bs.getValue("sourceRegex") != null && r.regex == bs.getValue("sourceRegex")
                .stringValue())
            }) {
            val conceptContextMap = ConceptContextMap()
            conceptContextMap.id = UUID.randomUUID().toString()
            conceptContextMap.node = bs.getValue("nodeName").stringValue()
            conceptContextMap.value = bs.getValue("sourceVal").stringValue()
            conceptContextMap.property = bs.getValue("propertyName").stringValue()
            if (bs.getValue("sourceRegex") != null) conceptContextMap.regex = bs.getValue("sourceRegex").stringValue()
            val context = Context()
            context.publisher = bs.getValue("publisherName").stringValue()
            context.system = bs.getValue("systemName").stringValue()
            context.schema = bs.getValue("schema").stringValue()
            context.table = bs.getValue("table").stringValue()
            context.field = bs.getValue("field").stringValue()
            val contexts: MutableList<Context> = mutableListOf()
            contexts.add(context)
            conceptContextMap.context = contexts
            result.add(conceptContextMap)
          }
        }
      }
    }
    return result
  }

  fun getShortestTerm(iri: String): String? {
    val sql: String = """
      Select ?term
      where {
        values ?entity {<${iri}>}
         {
      ?entity im:hasTermCode ?termCode.
      ?termCode rdfs:label ?term.
      }
      
      }
      order by strlen(?term)
      limit 1
      
      """.trimIndent()
    IMDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return bs.getValue("term").stringValue()
        }
      }
    }
    return null
  }
}
