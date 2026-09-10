package org.endeavourhealth.imapi.model.sql

data class Relationship(
  var dataModel: String = "",
  var fromField: String = "",
  var toField: String = "",
  var viaProperty: String? = null
)
