package org.endeavourhealth.imapi.model.sql

class MySQLInsert(val table: String) {
  fun toSql(): String {
    return if (table == "dataset")
      "INSERT IGNORE INTO $table (hashCode, patient_id, `group`, results)\n"
    else "INSERT IGNORE INTO $table (hashCode, patient_id)\n"
  }
}