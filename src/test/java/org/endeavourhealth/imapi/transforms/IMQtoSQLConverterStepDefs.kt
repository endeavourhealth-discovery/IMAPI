package org.endeavourhealth.imapi.transforms

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.endeavourhealth.imapi.logic.service.QueryService
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired

class IMQtoSQLConverterStepDefs() {
  @Autowired
  lateinit var queryService: QueryService
  private var sql: String? = null

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
}