package org.endeavourhealth.imapi.model.uprn

import com.fasterxml.jackson.annotation.JsonProperty

class Address {
  @get:JsonProperty("Flat")
  var Flat: String? = null
  @get:JsonProperty("Building")
  var Building: String?= null;
  @get:JsonProperty("Number")
  var Number: String? = null
  @get:JsonProperty("Postcode")
  var Postcode: String? = null
  @get:JsonProperty("Dependent_thoroughfare")
  var Dependent_thoroughfare: String? = null
  @get:JsonProperty("Street")
  var Street: String? = null
  @get:JsonProperty("Dependent_locality")
  var Dependent_locality: String? = null
  @get:JsonProperty("Locality")
  var Locality: String? = null
  @get:JsonProperty("Town")
  var Town: String? = null
  @get: JsonProperty("Organisation")
  var Organisation: String? = null;
}