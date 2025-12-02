package org.endeavourhealth.imapi.model.sql

data class MySQLQuery(var ctes: MutableList<CTE> = ArrayList(), var selects: MutableList<Select> = ArrayList()) {
  fun toSql(): String = """
    WITH ${ctes.joinToString(",\n")}
    SELECT ${selects.joinToString(",\n")} 
    FROM ${ctes.last().alias}
  """
}