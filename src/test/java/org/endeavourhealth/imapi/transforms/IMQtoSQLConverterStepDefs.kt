package org.endeavourhealth.imapi.transforms

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.endeavourhealth.imapi.dataaccess.EntityRepository
import org.endeavourhealth.imapi.logic.exporters.IMQFeatureTableExporter
import org.endeavourhealth.imapi.logic.service.QueryService
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.springframework.beans.factory.annotation.Autowired

class IMQtoSQLConverterStepDefs() {
  @Autowired
  lateinit var queryService: QueryService
  private var sql: String? = null

  private val entityRepository = EntityRepository()
  private val featureTableExporter = IMQFeatureTableExporter()
  private var failures: List<ConversionFailure> = emptyList()

  private data class ConversionFailure(val iri: String, val label: String?, val message: String?)

  @When("IMQ to SQL conversion is executed for {string}")
  fun imqToSqlConversionIsExecutedFor(iri: String) {
    assertDoesNotThrow {
      val queryRequest = QueryRequest().setQuery(Query().setIri(iri))
      sql = queryService.getSQLFromIMQ(queryRequest)
    }
  }

  @Then("SQL should be generated successfully")
  fun sqlShouldBeGeneratedSuccessfully() {
    assertTrue(!sql.isNullOrBlank())
  }

  @When("IMQ to SQL conversion is executed for all register queries")
  fun imqToSqlConversionIsExecutedForAllRegisterQueries() {
    val entities = entityRepository.getRegisterQueryEntities()
//    featureTableExporter.exportRegisterQueries(entities)
    runConversionForAll(entities)
  }

  @When("IMQ to SQL conversion is executed for all QOF queries")
  fun imqToSqlConversionIsExecutedForAllQOFQueries() {
    val entities = entityRepository.getQOFQueryEntities()
//    featureTableExporter.exportQOFQueries(entities)
    runConversionForAll(entities)
  }

  @When("IMQ to SQL conversion is executed for all SMH queries")
  fun imqToSqlConversionIsExecutedForAllSMHQueries() {
    val entities = entityRepository.getSMHQueryEntities()
//    featureTableExporter.exportSMHQueries(entities)
    runConversionForAll(entities)
  }

  @Then("SQL should be generated successfully for all of them")
  fun sqlShouldBeGeneratedSuccessfullyForAllOfThem() {
    if (failures.isNotEmpty()) {
      val byMessage = failures.groupBy { it.message ?: "unknown" }
      println("--- Failures by unique error (${byMessage.size} unique) ---")
      byMessage.entries
        .sortedByDescending { it.value.size }
        .forEach { (message, group) -> println("${group.size}x: $message") }
      println("-----------------------------------")

      val details = failures.joinToString(System.lineSeparator()) { "${it.iri} (${it.label}): ${it.message}" }
      fail<Unit>("Failed to generate SQL for ${failures.size} quer${if (failures.size == 1) "y" else "ies"}:${System.lineSeparator()}$details")
    }
  }

  private fun runConversionForAll(entities: List<TTIriRef>) {
    assertTrue(entities.isNotEmpty(), "Expected at least one query iri to be returned")
    failures = entities.mapNotNull { entity ->
      try {
        val queryRequest = QueryRequest().setQuery(Query().setIri(entity.iri))
        queryService.getSQLFromIMQ(queryRequest)
        null
      } catch (e: Exception) {
        ConversionFailure(entity.iri, entity.name, e.message ?: e.javaClass.name)
      }
    }
  }
}
