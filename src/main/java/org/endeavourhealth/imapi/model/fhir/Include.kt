package org.endeavourhealth.imapi.model.fhir

import com.fasterxml.jackson.annotation.JsonProperty

class Include {
    var system: String? = null
      private set

  fun setSystem(system: String?): Include {
    this.system = system
    return this
  }
}
