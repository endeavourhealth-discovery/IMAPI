package org.endeavourhealth.imapi.model.sql

class MySQLOrderByItem(val property: String, val direction: String, val table: Table? = null) {
  fun toSql(): String {
    if (table?.alias != null) return "${table.alias}.$property $direction"
    return "$property $direction"
  }
}

class MySQLOrderBy(val items: MutableList<MySQLOrderByItem>, val limit: Int? = null) {
  fun toSql(): String {
    val orderBy = if (items.isNotEmpty()) {
      "ORDER BY " + items.joinToString(", ") { it.toSql() }
    } else {
      ""
    }

    val limitClause = limit?.let { " LIMIT $it" } ?: ""

    return orderBy + limitClause
  }
}