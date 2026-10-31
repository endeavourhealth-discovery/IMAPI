package org.endeavourhealth.imapi.model.requests

class ValidatedEntitiesRequest {
  var snomedCodes: MutableList<String>? = null

  constructor() {}

  fun setSnomedCodes(snomedCodes: MutableList<String>?): ValidatedEntitiesRequest {
    this.snomedCodes = snomedCodes
    return this
  }

  fun addToSnomedCodes(code: String) {
    if (null == snomedCodes) {
      snomedCodes = arrayListOf()
    }
    this.snomedCodes!!.add(code)
  }
}
