package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException
import org.endeavourhealth.imapi.model.sql.MySQLSelect
import org.endeavourhealth.imapi.model.sql.MySQLWith
import org.endeavourhealth.imapi.model.sql.Table
import org.endeavourhealth.imapi.vocabulary.IM
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class StepBuildingTest {
  @Test
  fun unitsAndQualifiersAreDistinguished() {
    val days = SqlLiteralUtils.getUnitNameAndType(IM.DAYS.toString())
    assertEquals("DAY", days.unitName)
    assertNull(days.qualifierName)

    val day = SqlLiteralUtils.getUnitNameAndType(IM.DAY.toString())
    assertNull(day.unitName)
    assertEquals("DAY", day.qualifierName)
  }

  @Test
  fun unknownUnitIsRejected() {
    assertThrows(SQLConversionException::class.java) { SqlLiteralUtils.getUnitNameAndType(IM.CONCEPT.toString()) }
  }

  @Test
  fun antiJoinCarriesCohortEntityIdAsPatientId() {
    val cohort = MySQLWith(table = Table(dataModel = COHORT_DATA_MODEL_IRI), alias = "`c`", entityKeyField = ENTITY_ID_FIELD)
    assertEquals("`c`.*, `c`.entity_id as patient_id", carrySelectFrom(cohort))
    assertEquals(PATIENT_ID_FIELD, carryEntityKeyFrom(cohort))
  }

  @Test
  fun antiJoinCarriesOtherStepsUnchanged() {
    val step = MySQLWith(table = Table(dataModel = "http://x#Other"), alias = "`s`", entityKeyField = "patient_id")
    assertEquals("`s`.*", carrySelectFrom(step))
    assertEquals("patient_id", carryEntityKeyFrom(step))
  }

  @Test
  fun unionBranchMissingACarriedPropertyContributesNull() {
    val branch = MySQLWith(
      table = Table(),
      alias = "`b`",
      selects = mutableListOf(MySQLSelect("b.id"), MySQLSelect("b.date", "date")),
      entityKeyField = "id",
    )
    val normalised = normaliseForUnion(branch, listOf("http://x#date", "http://x#missing"))

    assertEquals(
      listOf("t.id", "t.date", "NULL"),
      normalised.selects.map { it.name }
    )
    assertEquals(listOf(null, "date", "missing"), normalised.selects.map { it.alias })
  }
}
