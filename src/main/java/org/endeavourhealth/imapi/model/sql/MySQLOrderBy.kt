package org.endeavourhealth.imapi.model.sql

class MySQLOrderByItem(val property: String, val direction: String, val table: Table? = null) {
  fun toSql(): String {
    return "${table?.alias ?: table?.table}.$property $direction"
  }
}

class MySQLOrderBy(val items: MutableList<MySQLOrderByItem>, val limit: Int? = null) {
  fun toSql(): String {
    val orderBy = if (items.isNotEmpty()) {
      "ORDER BY " + items.joinToString(", ") { it.toSql() }
    } else {
      ""
    }
    return orderBy
  }
}