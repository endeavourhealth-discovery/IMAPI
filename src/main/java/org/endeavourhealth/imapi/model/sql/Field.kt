package org.endeavourhealth.imapi.model.sql

data class Field(
  var field: String = "",
  var type: String = "",
  var isFunction: Boolean = false
)