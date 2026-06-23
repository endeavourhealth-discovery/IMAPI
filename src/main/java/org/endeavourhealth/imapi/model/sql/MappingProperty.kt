package org.endeavourhealth.imapi.model.sql

data class MappingProperty(
  var path: List<String> = emptyList(),
  var dataModel: String = "",
  var condition: Condition? = null
)