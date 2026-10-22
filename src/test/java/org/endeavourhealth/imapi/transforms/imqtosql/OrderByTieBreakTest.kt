package org.endeavourhealth.imapi.transforms.imqtosql

import org.endeavourhealth.imapi.model.imq.Order
import org.endeavourhealth.imapi.model.imq.OrderDirection
import org.endeavourhealth.imapi.model.imq.OrderLimit
import org.endeavourhealth.imapi.model.sql.Field
import org.endeavourhealth.imapi.model.sql.FieldJoin
import org.endeavourhealth.imapi.model.sql.Table
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class OrderByTieBreakTest {
  private fun observation(mapPrimaryKey: Boolean = true): Table {
    val fields = hashMapOf("http://x#effectiveDate" to Field(field = "clinical_effective_date"))
    if (mapPrimaryKey) fields["http://x#id"] = Field(field = "id")
    return Table(table = "observation", primaryKey = "id", fields = fields)
  }

  private fun orderBy(iri: String, direction: Order) =
    OrderLimit().setLimit(1).addProperty(OrderDirection().setDirection(direction).setIri(iri))

  @Test
  fun latestTiesAreBrokenByPrimaryKeyDescending() {
    val sql = getMySQLOrderBy(observation(), orderBy("http://x#effectiveDate", Order.descending), HashMap(), tieBreak = true).toSql()
    assertEquals("ORDER BY observation.clinical_effective_date DESC, observation.id DESC", sql)
  }

  @Test
  fun earliestTiesAreBrokenByPrimaryKeyAscending() {
    val sql = getMySQLOrderBy(observation(), orderBy("http://x#effectiveDate", Order.ascending), HashMap(), tieBreak = true).toSql()
    assertEquals("ORDER BY observation.clinical_effective_date ASC, observation.id ASC", sql)
  }

  @Test
  fun noTieBreakerUnlessAsked() {
    val sql = getMySQLOrderBy(observation(), orderBy("http://x#effectiveDate", Order.descending), HashMap()).toSql()
    assertEquals("ORDER BY observation.clinical_effective_date DESC", sql)
  }

  @Test
  fun primaryKeyIsNotRepeatedWhenAlreadySortedOn() {
    val sql = getMySQLOrderBy(observation(), orderBy("http://x#id", Order.descending), HashMap(), tieBreak = true).toSql()
    assertEquals("ORDER BY observation.id DESC", sql)
  }

  @Test
  fun noTieBreakerWhenSortingOnAJoinedTablesProperty() {
    val patient = Table(
      table = "patient",
      primaryKey = "id",
      fields = hashMapOf(
        "http://x#id" to Field(field = "id"),
        "http://x#registered" to Field(field = "date_registered", join = FieldJoin(dataModel = "http://x#EpisodeOfCare", alias = "gms_registration")),
      ),
    )
    val sql = getMySQLOrderBy(patient, orderBy("http://x#registered", Order.descending), HashMap(), tieBreak = true).toSql()
    assertEquals("ORDER BY gms_registration.date_registered DESC", sql)
  }

  @Test
  fun noTieBreakerWhenThePrimaryKeyIsNotAMappedColumn() {
    val sql = getMySQLOrderBy(observation(mapPrimaryKey = false), orderBy("http://x#effectiveDate", Order.descending), HashMap(), tieBreak = true).toSql()
    assertEquals("ORDER BY observation.clinical_effective_date DESC", sql)
  }
}
