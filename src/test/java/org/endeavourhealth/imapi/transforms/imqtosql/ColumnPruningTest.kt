package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.sql.MySQLJoin
import org.endeavourhealth.imapi.model.sql.MySQLPropertyValueWhere
import org.endeavourhealth.imapi.model.sql.MySQLQuery
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ColumnPruningTest {
  /** cohort -> observations of the cohort -> a later step that filters on one of their columns -> the patients. */
  private fun chain(finalSelects: List<MySQLSelect> = listOf(MySQLSelect("patient.*"))): MySQLQuery {
    val cohort = MySQLWith(
      table = Table(table = "dataset.cohort_results", primaryKey = "entity_id"),
      alias = "`cohort`",
      selects = mutableListOf(MySQLSelect("dataset.cohort_results.entity_id")),
      entityKeyField = "entity_id",
    )
    val observations = MySQLWith(
      table = Table(table = "observation", primaryKey = "id"),
      alias = "`obs`",
      selects = mutableListOf(MySQLSelect("observation.*")),
      joins = mutableListOf(
        MySQLJoin("JOIN", tableFrom = "observation", tableTo = "`cohort`", tableToAlias = "`cohort`", fromProperty = "patient_id", toProperty = "entity_id", reference = true)
      ),
      entityKeyField = "patient_id",
    )
    val later = MySQLWith(
      table = Table(table = "obs", primaryKey = "id"),
      alias = "`later`",
      selects = mutableListOf(MySQLSelect("obs.*")),
      wheres = mutableListOf(MySQLPropertyValueWhere("clinical_effective_date", "<", "5", table = "obs")),
      entityKeyField = "patient_id",
    )
    val patients = MySQLWith(
      table = Table(table = "patient", primaryKey = "id"),
      alias = "`final`",
      selects = finalSelects.toMutableList(),
      joins = mutableListOf(
        MySQLJoin("JOIN", tableFrom = "patient", tableTo = "`later`", tableToAlias = "`later`", fromProperty = "id", toProperty = "patient_id", reference = true)
      ),
      entityKeyField = "id",
    )
    return MySQLQuery(
      withs = mutableListOf(cohort, observations, later, patients),
      selects = mutableListOf(MySQLSelect("`final`.id", "entity_id")),
    ).also { it.insert = COHORT_RESULTS_TABLE }
  }

  @Test
  fun starsAreNarrowedToTheColumnsLaterStepsUse() {
    val query = chain()
    ColumnPruning.prune(query)
    val sql = query.toSql()

    assertTrue(sql.contains("SELECT DISTINCT observation.patient_id, observation.clinical_effective_date"), sql)
    assertTrue(sql.contains("SELECT DISTINCT obs.patient_id"), sql)
    assertTrue(sql.contains("SELECT DISTINCT patient.id"), sql)
    assertFalse(sql.contains("observation.*"), sql)
    assertFalse(sql.contains("obs.*"), sql)
    assertFalse(sql.contains("patient.*"), sql)
  }

  @Test
  fun aStarUsedInAnUnmodelledFormIsLeftAlone() {
    val query = chain(listOf(MySQLSelect("`later`.*, `later`.patient_id AS patient_id")))
    ColumnPruning.prune(query)
    val sql = query.toSql()

    assertTrue(sql.contains("observation.*"), sql)
    assertTrue(sql.contains("obs.*"), sql)
  }

  @Test
  fun queriesThatCountRowsAreLeftAlone() {
    val query = chain()
    query.withs[3].selects.add(MySQLSelect("COUNT(*)", "n", isAggregate = true))
    ColumnPruning.prune(query)

    assertTrue(query.toSql().contains("observation.*"))
  }

  @Test
  fun canBeSwitchedOff() {
    System.setProperty(ColumnPruning.ENABLE_PROPERTY, "false")
    try {
      val query = chain()
      ColumnPruning.prune(query)
      assertTrue(query.toSql().contains("observation.*"))
    } finally {
      System.clearProperty(ColumnPruning.ENABLE_PROPERTY)
    }
  }

  @Test
  fun distinctIsOmittedWhereRowsAreAlreadyUnique() {
    val step = MySQLWith(table = Table(table = "t"), alias = "`a`", selects = mutableListOf(MySQLSelect("t.id")), distinct = false)
    assertFalse(step.toSql().contains("DISTINCT"))
    assertTrue(step.toSql().contains("SELECT t.id"))
  }

  @Test
  fun unionBranchesDropDistinctBecauseTheUnionDedupes() {
    fun branch(table: String) = MySQLWith(table = Table(table = table), alias = "`$table`", selects = mutableListOf(MySQLSelect("$table.id")))
    val union = MySQLWith(table = Table(table = "x"), alias = "`u1`", unionWiths = mutableListOf(branch("a"), branch("b")))

    val sql = union.toSql()
    assertFalse(sql.contains("DISTINCT"), sql)
    assertTrue(sql.contains("UNION"), sql)
  }
}
