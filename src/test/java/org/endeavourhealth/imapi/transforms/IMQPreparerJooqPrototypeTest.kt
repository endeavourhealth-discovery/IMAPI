package org.endeavourhealth.imapi.transforms

import org.endeavourhealth.imapi.model.imq.Operator
import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.model.requests.QueryRequest
import org.endeavourhealth.imapi.model.sql.MappingParser
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

/**
 * Demonstrates the two-phase IMQPreparer -> IRToJooqConverter pipeline on a small, real
 * (non-Spring, no DB) example against the real IMQtoMYSQL.json mapping: patients with an
 * observation whose value is greater than 5.
 */
class IMQPreparerJooqPrototypeTest {
  private val log = LoggerFactory.getLogger(IMQPreparerJooqPrototypeTest::class.java)

  @Test
  fun convertsSimpleAndChainViaJooq() {
    val tableMap = MappingParser().parse("IMQtoMYSQL.json")

    val query = Query()
      .setIri("http://example.org/Q_Prototype")
      .setTypeOf("http://endhealth.info/im#Patient")
      .and { m -> m.setTypeOf("http://endhealth.info/im#Patient").setNode("patient") }
      .and { m ->
        m.setTypeOf("http://endhealth.info/im#Observation")
          .setNode("obs")
          .where { w ->
            w.setIri("http://endhealth.info/im#value")
              .setOperator(Operator.gt)
              .setValue("5")
          }
          .return_ { r -> r.setIri("http://endhealth.info/im#effectiveDate").setAs("obs_date") }
      }

    val queryRequest = QueryRequest().setQuery(query)
    val ir = IMQPreparer(tableMap, queryRequest).prepare(query)
    val sql = IRToJooqConverter().toSql(ir)

    log.info("Generated SQL:\n{}", sql)
    println(sql)

    assertTrue(sql.startsWith("INSERT INTO dataset.cohort_results"))
    assertTrue(sql.contains("with `patient` as (select distinct patient.id from patient)"))
    assertTrue(sql.contains(", `obs` as ("))
    assertTrue(sql.contains("where observation.result_value > 5"))
    assertTrue(sql.contains("join `patient` on observation.patient_id = patient.id"))
    assertTrue(sql.contains("obs.patient_id as `entity_id`"))
    assertTrue(sql.contains("observation.clinical_effective_date as `obs_date`"))
    assertTrue(sql.contains("join `patient` on obs.patient_id = patient.id"))
    assertTrue(sql.contains("patient.organization_id as `entity_org_id`"))
  }

  @Test
  fun convertsOrBranchToUnionViaJooq() {
    val tableMap = MappingParser().parse("IMQtoMYSQL.json")
    val query = Query()
      .setIri("http://example.org/Q_OrPrototype")
      .setTypeOf("http://endhealth.info/im#Patient")
      .and { m -> m.setTypeOf("http://endhealth.info/im#Patient").setNode("patient") }
      .and { orNode ->
        orNode
          .or { b ->
            b.setTypeOf("http://endhealth.info/im#Observation").setNode("obsA")
              .where { w -> w.setIri("http://endhealth.info/im#concept").addIs("http://endhealth.info/im#ConceptA") }
          }
          .or { b ->
            b.setTypeOf("http://endhealth.info/im#Observation").setNode("obsB")
              .where { w -> w.setIri("http://endhealth.info/im#concept").addIs("http://endhealth.info/im#ConceptB") }
          }
      }

    val queryRequest = QueryRequest().setQuery(query)
    val ir = IMQPreparer(tableMap, queryRequest).prepare(query)
    val sql = IRToJooqConverter().toSql(ir)

    log.info("Generated OR/union SQL:\n{}", sql)
    println(sql)

    assertTrue(sql.contains("union"))
    assertTrue(sql.contains("ConceptA"))
    assertTrue(sql.contains("ConceptB"))
  }
}
