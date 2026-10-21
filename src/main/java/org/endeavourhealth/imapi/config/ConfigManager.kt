package org.endeavourhealth.imapi.config

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import org.eclipse.rdf4j.model.util.Values
import org.endeavourhealth.imapi.dataaccess.databases.ConfigDB
import org.endeavourhealth.imapi.logic.CachedObjectMapper
import org.endeavourhealth.imapi.model.config.Config
import org.endeavourhealth.imapi.vocabulary.CONFIG
import org.endeavourhealth.imapi.vocabulary.GRAPH
import org.endeavourhealth.imapi.vocabulary.VocabEnum
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigManager {
  private val log = LoggerFactory.getLogger(javaClass)

  @Throws(JsonProcessingException::class)
  fun <T> getConfig(iri: CONFIG, resultType: TypeReference<T>): T? {
    log.debug("getConfig<TypeReference>")

    CachedObjectMapper().use { om ->
      val config = getConfig(iri) ?: return null
      return om.readValue<T>(config.data, resultType)
    }
  }

  fun getConfig(config: CONFIG): Config? {
    val sql = """
      SELECT ?name ?data
      WHERE {
        ?s ?label ?name ;
        ?config ?data .
      }
      
      """.trimIndent()

    ConfigDB.getConnection().use { conn ->
      val qry = conn.prepareTupleSparql(sql)
      qry.setBinding("s", config.asDbIri())
      qry.setBinding("label", CONFIG.LABEL.asDbIri())
      qry.setBinding("config", CONFIG.HAS_CONFIG.asDbIri())
      qry.evaluate().use { rs ->
        if (rs.hasNext()) {
          val bs = rs.next()
          return Config()
            .setName(bs.getValue("name").stringValue())
            .setData(bs.getValue("data").stringValue())
        } else {
          return null
        }
      }
    }
  }

  fun setConfig(iri: CONFIG, config: Config) {
    insert(iri, CONFIG.LABEL, config.name)
    insert(iri, CONFIG.COMMENT, config.comment)
    insert(iri, CONFIG.HAS_CONFIG, config.data)
  }

  private fun insert(subject: CONFIG?, predicate: VocabEnum?, `object`: String?) {
    requireNotNull(subject) { "subject must not be null" }
    requireNotNull(predicate) { "predicate must not be null" }
    CachedObjectMapper().use { om ->
      ConfigDB.getConnection().use { conn ->
        var query = """
        DELETE {
            ?s ?p ?oAny
        }
        INSERT {
            ?s ?p ?o
        }
        
        """.trimIndent()
        query += if (null == getConfig(subject))
          "WHERE {}"
        else
          "WHERE { ?s ?p ?oAny }"

        val qry = conn.prepareUpdateSparql(query, GRAPH.CONFIG)
        qry.setBinding("s", subject.asDbIri())
        qry.setBinding("p", predicate.asDbIri())
        qry.setBinding("o", Values.literal(`object`))
        qry.execute()
      }
    }
  }
}
