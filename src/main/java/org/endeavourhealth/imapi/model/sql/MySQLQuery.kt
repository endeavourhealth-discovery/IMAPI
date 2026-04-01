package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException

data class MySQLQuery(
  var withs: MutableList<MySQLWith> = ArrayList(),
  var selects: MutableList<MySQLSelect> = ArrayList(),
  var joins: MutableList<MySQLJoin> = ArrayList(),
  var create: MySQLCreate? = null,
  var savingAs: String? = null,
  var update: String? = null,
  var insert: String? = null,
) {
  val nodeToTableMap: HashMap<String, Table> = hashMapOf()

  fun toSql(): String = buildString {
    create?.let { append(it.toSql()) }
    insert?.let { append("INSERT INTO `$it`\n") }
    append("WITH ")
    append(withs.joinToString(",\n") { it.toSql() })
    append("\nSELECT ")
    append(selects.joinToString(",\n") { it.toSql() })
    append("\nFROM ${withs.last().alias}")
    append(joins.joinToString("\n") { it.toSql() })
    savingAs?.let {
      append("\n")
      append(it)
    }
    update?.let {
      append("\n")
      append(it)
    }
    append(";")
  }
}