package org.endeavourhealth.imapi.model.sql

import org.endeavourhealth.imapi.errorhandling.SQLConversionException

data class MySQLQuery(
  var withs: MutableList<MySQLWith> = ArrayList(),
  var selects: MutableList<MySQLSelect> = ArrayList(),
  var joins: MutableList<MySQLJoin> = ArrayList(),
  var insert: MySQLInsert? = null,
  var savingAs: String? = null,
  var update: String? = null
) {
  val nodeToTableMap: HashMap<String, Table> = hashMapOf()

  fun toSql(): String = buildString {
    insert?.let { append(it.toSql()) }
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
  }


  fun joinWiths() {
    val result = mutableListOf<MySQLWith>()
    var lastStep: MySQLWith? = null

    for (w in withs) {
      if (w.isStep) {
        lastStep = w
      } else {
        lastStep?.let { result.add(it) }
        lastStep = null
        result.add(w)
      }
    }
    lastStep?.let { result.add(it) }

    for (i in 0 until result.size - 1) {
      val current = result[i]
      val next = result[i + 1]

      val currentTable = current.table
      val nextTable = next.table


      val (fk, pk) = if (currentTable.table == nextTable.table) currentTable.primaryKey to nextTable.primaryKey else currentTable.foreignKeyTo(
        nextTable
      )

      if (fk == null || pk == null) {
        throw SQLConversionException(
          "No relationship between ${currentTable.table} and ${nextTable.table}"
        )
      }

      val join =
        if (next.exclude) {
          MySQLJoin(
            join = "LEFT JOIN",
            tableFrom = current.alias.ifBlank { currentTable.table },
            tableTo = nextTable.table,
            tableToAlias = next.alias,
            fromProperty = fk,
            toProperty = pk,
            reference = true
          ).apply {
            wheres.add(MySQLPropertyValueWhere("${next.alias}.$pk", "IS", "NULL"))
          }
        } else {
          MySQLJoin(
            join = "JOIN",
            tableFrom = current.alias.ifBlank { currentTable.table },
            tableTo = nextTable.table,
            tableToAlias = next.alias,
            fromProperty = fk,
            toProperty = pk,
            reference = true
          )
        }

      joins.add(join)
    }
  }
}