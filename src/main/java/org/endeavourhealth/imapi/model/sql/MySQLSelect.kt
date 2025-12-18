package org.endeavourhealth.imapi.model.sql

data class MySQLSelect(val name: String, var alias: String? = null) {
  fun toSql(): String {
    return if (alias != null) "$name AS `$alias`"
    else name
  }
}