package org.endeavourhealth.imapi.model.sql

data class MySQLJoin(
  val join: String,
  var tableFrom: String,
  val tableTo: String,
  val tableToAlias: String? = null,
  val fromProperty: String? = null,
  val toProperty: String? = null,
  val wheres: MutableList<MySQLWhere> = ArrayList(),
  val reference: Boolean? = false,
) {
  fun toSql(): String {
    val joinString =
      if (tableToAlias != null) {
        if (reference == true) {
          return "  $join $tableToAlias ON $tableFrom.$fromProperty = $tableToAlias.$toProperty"
        }
        "  $join $tableTo $tableToAlias ON $tableFrom.$fromProperty = $tableToAlias.$toProperty"
      } else "  $join $tableTo ON $tableFrom.$fromProperty = $tableTo.$toProperty"
    if (wheres.isEmpty())
      return joinString
    return "$joinString\n  AND ${wheres.joinToString(" AND ") { it.toSql() }}"
  }
}