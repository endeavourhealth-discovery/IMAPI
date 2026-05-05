package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.dataaccess.EntityRepository
import org.endeavourhealth.imapi.logic.service.QueryService
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.vocabulary.IM
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.slf4j.LoggerFactory

@Tag("IMQFullTest")
@SpringBootTest
class IMQtoSQLFullTest {
  private val log = LoggerFactory.getLogger(IMQtoSQLFullTest::class.java)

  var entityRepository: EntityRepository = EntityRepository()
  var queryService: QueryService = QueryService()

  data class ConversionError(val iri: String, val name: String?, val message: String?)

  @Test
  fun testAllQueryEntitiesSQLConversion() {
    val entities = entityRepository.getQueryEntitiesWithDefinition()
    log.info("Found ${entities.size} entities with definitions")

    val uniqueExceptions = mutableMapOf<String, ConversionError>()

    for (entity in entities) {
      val iri = entity.iri
      val name = entity.name
      val definition = entity.get(IM.DEFINITION.asIri())

      if (definition != null) {
//                log.info("Converting entity: $iri")
        try {
          val query = definition.asLiteral().objectValue(Query::class.java)
          val queryRequest = QueryRequest().setQuery(query)
          val sql = queryService.getSQLFromIMQ(queryRequest)

          assertNotNull(sql, "SQL should not be null for entity: $iri")
//                    log.debug("SQL generated for $iri: ${sql.take(100)}...")
        } catch (e: Exception) {
          val message = e.message ?: e.javaClass.name
          if (!uniqueExceptions.containsKey(message)) {
            uniqueExceptions[message] = ConversionError(iri, name, message)
          }
//                    log.error("Failed to convert entity $iri: $message")
        }
      }
    }

    if (uniqueExceptions.isNotEmpty()) {
      println("--- Unique Conversion Exceptions ---")
      uniqueExceptions.values.forEach { error ->
        println("IRI: ${error.iri} Name: ${error.name}")
        println("Exception: ${error.message}")
        println("-----------------------------------")
      }
    }
    println("Total entities: ${entities.size}")
    println("Errors: ${uniqueExceptions.size}")
  }
}
