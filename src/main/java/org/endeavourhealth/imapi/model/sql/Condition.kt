package org.endeavourhealth.imapi.model.sql

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Condition @JsonCreator constructor(
  @JsonProperty("field") val field: String,
  @JsonProperty("value") val value: String
)
