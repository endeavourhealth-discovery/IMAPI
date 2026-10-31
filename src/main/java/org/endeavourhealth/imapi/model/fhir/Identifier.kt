package org.endeavourhealth.imapi.model.fhir

import com.fasterxml.jackson.annotation.JsonProperty

class Identifier {
    var system: String? = null
  private set

    var value: String? = null
  private set
    var use: String? = null
        private set

  fun setSystem(system: String?): Identifier {
    this.system = system
    return this
  }

  fun setValue(value: String?): Identifier {
    this.value = value
    return this
  }

    fun setUse(use: String?): Identifier {
        this.use = use
        return this
    }
}
