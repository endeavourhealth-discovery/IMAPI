package org.endeavourhealth.imapi.model.sql

data class MySQLSelect(
  val name: String,
  var alias: String? = null,
  val isAggregate: Boolean = false
) {
  fun toSql(): String =
    if (alias != null) "$name AS `${alias!!.replace("`", "")}`" else name
}
