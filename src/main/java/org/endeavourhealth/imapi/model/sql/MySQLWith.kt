package org.endeavourhealth.imapi.model.sql

data class MySQLWith(
  val table: Table,
  val alias: String,
  val wheres: MutableList<MySQLWhere>?,
  val selects: MutableList<MySQLSelect>,
  var joins: MutableList<MySQLJoin>?
) {
  fun toSql(): String {
    val selectSql = selects.joinToString(", ") { sel ->
      sel.alias?.let { "${sel.name} AS $it" } ?: sel.name
    }

    val whereSql = wheres
      ?.takeIf { it.isNotEmpty() }
      ?.joinToString(" AND ") { it.toSql() }

    val joinSql = joins
      ?.takeIf { it.isNotEmpty() }
      ?.joinToString(" ") { "JOIN ${it.table} ON ${it.table}.${it.innerProperty} = ${table.table}.${it.outerProperty}" }

    return buildString {
      appendLine("$alias AS (")
      append("  SELECT $selectSql")
      appendLine()
      appendLine("  FROM ${table.table}")

      if (joinSql != null) {
        appendLine("  $joinSql")
      }

      if (whereSql != null) {
        appendLine("  WHERE $whereSql")
      }

      append(")")
    }
  }
}

