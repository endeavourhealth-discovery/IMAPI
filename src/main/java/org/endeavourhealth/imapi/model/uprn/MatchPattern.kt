package org.endeavourhealth.imapi.model.uprn

import com.fasterxml.jackson.annotation.JsonProperty

class MatchPattern {
  @get:JsonProperty("Building")
  var Building: String? = null
  @get:JsonProperty("Flat")
  var Flat: String? = null
  @get:JsonProperty("Number")
  var Number: String? = null
  @get:JsonProperty("Postcode")
  var Postcode: String? = null
  @get:JsonProperty("Street")
  var Street: String? = null
}