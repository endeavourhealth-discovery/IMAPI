package org.endeavourhealth.imapi.model.sql

class MySQLInsert(val table: String, val iri: String) {
  fun toSql(): String {
    return if (table == "dataset")
      "INSERT INTO $table ($iri, id, results)\n"
    else "INSERT INTO $table ($iri, id)\n"
  }
}