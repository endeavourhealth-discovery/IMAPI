package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.model.imq.Bool

data class MySQLWith(
  var table: Table = Table(),
  var alias: String = "",
  val wheres: MutableList<MySQLWhere>? = mutableListOf(),
  val selects: MutableList<MySQLSelect> = mutableListOf(),
  var joins: MutableList<MySQLJoin> = mutableListOf(),
  val whereBool: Bool = Bool.and,
  val exclude: Boolean = false,
  var orderBy: MySQLOrderBy? = null,
  val fromAlias: String? = null,
  val unionWiths: MutableList<MySQLWith> = mutableListOf(),
  val unionAll: Boolean = false,
  var subQuery: MySQLWith? = null,
  var isStep: Boolean = false
) {
  private fun toSqlBody(): String {
    val selectSql = selects.joinToString(", ") { sel ->
      sel.alias?.let { "${sel.name} AS $it" } ?: sel.name
    }

    val whereSql = wheres
      ?.takeIf { it.isNotEmpty() }
      ?.joinToString(" ${whereBool.name} ") { it.toSql() }

    val joinSql = joins
      ?.takeIf { it.isNotEmpty() }
      ?.joinToString("\n") { it.toSql() }

    val orderBySql = orderBy?.toSql()

    return buildString {
      appendLine(
        "SELECT $selectSql"
      )
      if (subQuery != null) {
        appendLine("FROM (")
        appendLine(subQuery!!.toSqlBody().prependIndent("  "))
        appendLine(") ${fromAlias ?: "sq"}")
      } else {
        val fromBase = fromAlias ?: table.table
        val aliasSql = if (fromAlias == null) (table.alias?.let { " $it" } ?: "") else ""
        appendLine("FROM $fromBase$aliasSql")
      }

      if (joinSql != null) {
        appendLine(joinSql)
      }

      if (whereSql != null) {
        appendLine("  WHERE $whereSql")
      }

      if (orderBySql != null) {
        append(orderBySql)
      }
    }
  }

  private fun toUnionSqlBody(): String {
    val unions = unionWiths ?: return toSqlBody()
    val unionKeyword = if (unionAll) "UNION ALL" else "UNION"
    val unionSql = unions.joinToString("\n$unionKeyword\n") { unionWith ->
      "(${unionWith.toSqlBody()})"
    }
    val orderBySql = orderBy?.toSql()
    return if (orderBySql != null) {
      "$unionSql\n$orderBySql"
    } else {
      unionSql
    }
  }

  fun toSql(): String {
    val body = if (unionWiths != null) toUnionSqlBody() else toSqlBody()
    return buildString {
      appendLine("$alias AS (")
      appendLine(body.prependIndent("  "))
      append(")")
    }
  }
}

