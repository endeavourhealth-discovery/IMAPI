package org.endeavourhealth.imapi.model.fhir

class Compose {
  var include: MutableList<Include>? = mutableListOf()
    private set

  fun setInclude(include: MutableList<Include>): Compose {
    this.include = include
    return this
  }
}
