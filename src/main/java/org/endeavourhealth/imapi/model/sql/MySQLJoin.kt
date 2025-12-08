package org.endeavourhealth.imapi.model.sql

data class MySQLJoin(
  val tableFrom: String,
  val tableTo: String,
  val fromProperty: String? = null,
  val toProperty: String? = null,
  val inner: Boolean? = false,
  val exclude: Boolean? = false
)