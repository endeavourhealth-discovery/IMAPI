package org.endeavourhealth.imapi.model.uprn

import com.fasterxml.jackson.annotation.JsonProperty

class Activity {
  @get:JsonProperty("DT")
  var DT: String? = null
  @get:JsonProperty("A")
  var A: String? = null
  @get:JsonProperty("F")
  var F: String? = null
}