package org.endeavourhealth.imapi.model.sql

data class PropertyMapItem(
  val dataModel: String,
  val condition: Condition? = null,
)
