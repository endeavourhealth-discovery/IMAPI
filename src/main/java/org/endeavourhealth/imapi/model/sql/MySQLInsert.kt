package org.endeavourhealth.imapi.model.sql

class MySQLInsert(val table: String) {
  fun toSql(): String {
    return if (table == "dataset")
      "INSERT INTO $table (`hash`, cohort_id, `group`, results)\n"
    else "INSERT INTO $table (`hash`, cohort_id)\n"
  }
}