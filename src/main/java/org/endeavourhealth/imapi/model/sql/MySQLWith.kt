package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.model.imq.Bool

data class MySQLWith(
  val table: Table,
  val alias: String,
  val wheres: MutableList<MySQLWhere>?,
  val selects: MutableList<MySQLSelect>,
  var joins: MutableList<MySQLJoin>?,
  val whereBool: Bool,
  val exclude: Boolean = false
) {
  fun toSql(): String {
    val selectSql = selects.joinToString(", ") { sel ->
      sel.alias?.let { "${sel.name} AS $it" } ?: sel.name
    }

    val whereSql = wheres
      ?.takeIf { it.isNotEmpty() }
      ?.joinToString(" ${whereBool.name} ") { it.toSql() }

    val joinSql = joins
      ?.takeIf { it.isNotEmpty() }
      ?.joinToString("\n") { it.toSql() }

    return buildString {
      appendLine("$alias AS (")
      appendLine("  SELECT $selectSql")
      appendLine("  FROM ${table.table}")

      if (joinSql != null) {
        appendLine(joinSql)
      }

      if (whereSql != null) {
        appendLine("  WHERE $whereSql")
      }

      append(")")
    }
  }
}

