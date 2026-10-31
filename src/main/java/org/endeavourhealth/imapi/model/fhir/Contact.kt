package org.endeavourhealth.imapi.model.fhir

class Contact {
  var telecom: MutableList<Identifier>? = mutableListOf()
    private set
  var name: String? = null
    private set

  fun setName(name: String?): Contact {
    this.name = name
    return this
  }

  fun setTelecom(telecom: MutableList<Identifier>): Contact {
    this.telecom = telecom
    return this
  }
}
