package org.endeavourhealth.imapi.model.sql

data class Field(
  var field: String = "",
  var type: String = "",
  var isFunction: Boolean = false,
  var returnAs: Map<String, String> = emptyMap(),
  var join: FieldJoin? = null
)

/**
 * A functional property whose column lives on a related table, e.g. Patient.gmsDateOfRegistration is
 * episode_of_care.date_registered. The related table is joined via the relationship between the two data models.
 */
data class FieldJoin(
  var dataModel: String = "",
  var alias: String? = null,
  var condition: Condition? = null
)

const val TABLE_PLACEHOLDER = "{table}"

/**
 * Qualifies a mapped field with its table reference. An expression field marks where the reference goes with
 * {table}, e.g. TIMESTAMPDIFF(YEAR, {table}.date_of_birth, $searchDate); a plain column is prefixed.
 */
fun qualifyField(tableRef: String?, field: String): String = when {
  field.contains(TABLE_PLACEHOLDER) ->
    if (tableRef == null) field.replace("$TABLE_PLACEHOLDER.", "") else field.replace(TABLE_PLACEHOLDER, tableRef)
  tableRef == null || field.contains("(") -> field
  else -> "$tableRef.$field"
}
