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
