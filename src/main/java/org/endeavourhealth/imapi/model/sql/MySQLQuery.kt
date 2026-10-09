package org.endeavourhealth.imapi.model.sql

data class MySQLQuery(
  var withs: MutableList<MySQLWith> = ArrayList(),
  var selects: MutableList<MySQLSelect> = ArrayList(),
  var joins: MutableList<MySQLJoin> = ArrayList(),
  var savingAs: String? = null,
  var update: String? = null,
  var insert: String? = null,
) {
  val nodeToTableMap: HashMap<String, Table> = hashMapOf()

  fun toSql(): String = buildString {
    insert?.let { append("INSERT INTO $it\n") }
    append("WITH ")
    append(withs.joinToString(",\n") { it.toSql() })
    // MySQL's TempTable engine loses materialised CTEs that are referenced from several places in long CTE
    // chains ("Table '#sql...' doesn't exist"); the MEMORY engine does not have this bug.
    append("\nSELECT $TEMP_TABLE_ENGINE_HINT ")
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

  companion object {
    const val TEMP_TABLE_ENGINE_HINT = "/*+ SET_VAR(internal_tmp_mem_storage_engine=MEMORY) */"
  }
}