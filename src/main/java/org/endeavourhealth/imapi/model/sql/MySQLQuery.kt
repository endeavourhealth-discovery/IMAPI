package org.endeavourhealth.imapi.model.sql

data class MySQLQuery(var withs: MutableList<MySQLWith> = ArrayList(), var selects: MutableList<MySQLSelect> = ArrayList(), var joins: MutableList<MySQLJoin> = ArrayList()) {
  fun toSql(): String = buildString {
    append("WITH ")
    append(withs.joinToString(",\n") { it.toSql() })
    append("\nSELECT ")
    append(selects.joinToString(",\n") { it.toSql() })
    append("\nFROM ${withs.last().alias}")
    append(joins.joinToString(",\n") { it.toSql() })
  }
}