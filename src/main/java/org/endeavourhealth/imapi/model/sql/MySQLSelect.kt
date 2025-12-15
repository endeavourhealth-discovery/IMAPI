package org.endeavourhealth.imapi.model.sql

data class MySQLSelect(val name: String, var alias: String? = null) {
  fun toSql(): String {
    if (alias != null) return "$name AS `$alias`"
    else return name
  }
}