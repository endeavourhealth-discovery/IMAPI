package org.endeavourhealth.imapi.model.sql

data class CTE(val table: String, val alias: String, val selects: MutableList<Select>) {
  fun toSql(): String {
    val selectSql = selects.joinToString(", ") { sel ->
      sel.alias?.let { "${sel.name} AS $it" } ?: sel.name
    }
    return """
      $alias AS (
        SELECT $selectSql
        FROM $table
      )
      """
  }
}

