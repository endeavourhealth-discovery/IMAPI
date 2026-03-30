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
    append("\nFROM ${withs.last { !it.isLeftJoin }}")
    if (withs.last().isLeftJoin) {
      val leftJoinCte = withs.last()
      val secondLastCte = withs[withs.size - 2]
      val (fk, pk) = if (leftJoinCte.table.table == secondLastCte.table.table)
        secondLastCte.table.primaryKey to secondLastCte.table.primaryKey
      else leftJoinCte.table.foreignKeyTo(secondLastCte.table)
      MySQLJoin(
        "LEFT JOIN",
        tableFrom = secondLastCte.table.table,
        tableTo = leftJoinCte.table.table,
        fromProperty = fk,
        toProperty = pk
      ).let { joins.add(it) }
      append("\n LEFT JOIN ${withs.last { it.isLeftJoin }}")
    }
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