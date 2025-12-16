package org.endeavourhealth.imapi.model.sql

data class MySQLQuery(
  var withs: MutableList<MySQLWith> = ArrayList(),
  var selects: MutableList<MySQLSelect> = ArrayList(),
  var joins: MutableList<MySQLJoin> = ArrayList(),
  var insert: MySQLInsert? = null
) {
  fun toSql(): String = buildString {
    insert?.let { append(it.toSql()) }
    append("WITH ")
    append(withs.joinToString(",\n") { it.toSql() })
    append("\nSELECT ")
    append(selects.joinToString(",\n") { it.toSql() })
    append("\nFROM ${withs.last().alias}")
    append(joins.joinToString(",\n") { it.toSql() })
  }
}