package org.endeavourhealth.imapi.model.fhir

class Extension {
  var url: String? = null
    private set
  var valueCode: String? = null
    private set
  var valueInteger: Int? = null
    private set

  fun setValueInteger(valueInteger: Int?): Extension {
    this.valueInteger = valueInteger
    return this
  }

  fun setValueCode(valueCode: String?): Extension {
    this.valueCode = valueCode
    return this
  }

  fun setUrl(url: String?): Extension {
    this.url = url
    return this
  }
}
