package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CteSharingTest {
  private fun cohort(alias: String, iri: String = "http://x#Registered") = MySQLWith(
    table = Table(table = "dataset.cohort_results", primaryKey = "entity_id"),
    alias = "`$alias`",
    selects = mutableListOf(MySQLSelect("dataset.cohort_results.entity_id")),
    wheres = mutableListOf(MySQLPropertyValueWhere("query_result_id", "=", iri)),
    entityKeyField = "entity_id",
  )

  /** Observations of whoever is in [cohort], restricted by [after]. */
  private fun observations(alias: String, cohort: String, after: String = "5") = MySQLWith(
    table = Table(table = "observation", primaryKey = "id"),
    alias = "`$alias`",
    selects = mutableListOf(MySQLSelect("observation.*")),
    joins = mutableListOf(
      MySQLJoin("JOIN", tableFrom = "observation", tableTo = "`$cohort`", tableToAlias = "`$cohort`", fromProperty = "patient_id", toProperty = "entity_id", reference = true)
    ),
    wheres = mutableListOf(MySQLPropertyValueWhere("clinical_effective_date", ">", after, table = "observation")),
    entityKeyField = "patient_id",
  )

  private fun patients(alias: String, readsFrom: String) = MySQLWith(
    table = Table(table = "patient", primaryKey = "id"),
    alias = "`$alias`",
    selects = mutableListOf(MySQLSelect("patient.*")),
    joins = mutableListOf(
      MySQLJoin("JOIN", tableFrom = "patient", tableTo = "`$readsFrom`", tableToAlias = "`$readsFrom`", fromProperty = "id", toProperty = "patient_id", reference = true)
    ),
    entityKeyField = "id",
  )

  private fun query(vararg withs: MySQLWith) = MySQLQuery(
    withs = withs.toMutableList(),
    selects = mutableListOf(MySQLSelect("`${withs.last().alias.trim('`')}`.id", "entity_id")),
  ).also { it.insert = COHORT_RESULTS_TABLE }

  @Test
  fun anIdenticalStepBecomesAReferenceToTheFirst() {
    val q = query(cohort("reg"), observations("obs_a", "reg"), observations("obs_b", "reg"), patients("final", "obs_b"))

    assertEquals(1, CteSharing.share(q))
    val sql = q.toSql()

    assertTrue(sql.contains("`obs_b` AS (\n  SELECT `obs_a`.*\n  FROM `obs_a`"), sql)
    assertEquals(1, Regex("FROM observation").findAll(sql).count(), sql)
    // readers of the duplicate's name keep working
    assertTrue(sql.contains("JOIN `obs_b` ON patient.id = `obs_b`.patient_id"), sql)
  }

  @Test
  fun aStepWithADifferentConditionIsNotShared() {
    val q = query(cohort("reg"), observations("obs_a", "reg", after = "5"), observations("obs_b", "reg", after = "9"), patients("final", "obs_b"))
    assertEquals(0, CteSharing.share(q))
  }

  @Test
  fun aStepOverADifferentUpstreamIsNotShared() {
    val q = query(
      cohort("reg_a", "http://x#A"), cohort("reg_b", "http://x#B"),
      observations("obs_a", "reg_a"), observations("obs_b", "reg_b"),
      patients("final", "obs_b"),
    )
    assertEquals(0, CteSharing.share(q))
  }

  @Test
  fun stepsOverDuplicatedUpstreamStepsAreAlsoShared() {
    val q = query(
      cohort("reg_a"), cohort("reg_b"),
      observations("obs_a", "reg_a"), observations("obs_b", "reg_b"),
      patients("final", "obs_b"),
    )
    // reg_b duplicates reg_a, which makes obs_b a duplicate of obs_a
    assertEquals(2, CteSharing.share(q))
    val sql = q.toSql()
    assertTrue(sql.contains("`reg_b` AS (\n  SELECT `reg_a`.*"), sql)
    assertTrue(sql.contains("`obs_b` AS (\n  SELECT `obs_a`.*"), sql)
  }

  @Test
  fun theLastStepIsNeverReplaced() {
    val q = query(cohort("reg"), observations("obs_a", "reg"), observations("obs_b", "reg"))
    assertEquals(0, CteSharing.share(q))
    assertFalse(q.toSql().contains("`obs_b` AS (\n  SELECT `obs_a`.*"))
  }

  @Test
  fun canBeSwitchedOff() {
    System.setProperty(CteSharing.ENABLE_PROPERTY, "false")
    try {
      val q = query(cohort("reg"), observations("obs_a", "reg"), observations("obs_b", "reg"), patients("final", "obs_b"))
      assertEquals(0, CteSharing.share(q))
    } finally {
      System.clearProperty(CteSharing.ENABLE_PROPERTY)
    }
  }

  @Test
  fun columnPruningStillWorksThroughAShareReference() {
    val q = query(cohort("reg"), observations("obs_a", "reg"), observations("obs_b", "reg"), patients("final", "obs_b"))
    CteSharing.share(q)
    ColumnPruning.prune(q)
    val sql = q.toSql()

    assertTrue(sql.contains("SELECT DISTINCT observation.patient_id"), sql)
    assertFalse(sql.contains("observation.*"), sql)
  }
}
