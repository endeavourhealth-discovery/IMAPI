package org.endeavourhealth.imapi.model.fhir

class FHIRConcept {
  var code: String? = null
    private set
  var display: String? = null
    private set
  var definition: String? = null
    private set
  var concept: MutableList<FHIRConcept>? = mutableListOf()
    private set

  fun setCode(code: String?): FHIRConcept {
    this.code = code
    return this
  }

  fun setDisplay(display: String?): FHIRConcept {
    this.display = display
    return this
  }

  fun setDefinition(definition: String?): FHIRConcept {
    this.definition = definition
    return this
  }

  fun setConcept(concept: MutableList<FHIRConcept>?): FHIRConcept {
    this.concept = concept
    return this
  }
}
