package org.endeavourhealth.imapi.model.uprn

import com.fasterxml.jackson.annotation.JsonProperty

class Address {
  @get:JsonProperty("Number")
  var Number: String? = null
  @get:JsonProperty("Postcode")
  var Postcode: String? = null
  @get:JsonProperty("Street")
  var Street: String? = null
  @get:JsonProperty("Town")
  var Town: String? = null
}