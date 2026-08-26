package org.endeavourhealth.imapi.model.fhir

import com.fasterxml.jackson.annotation.JsonProperty

class Text {
    var status: String? = null
  private set

    var div: String? = null
  private set

  fun setStatus(value: String):Text {
    status = value
    return this
  }

  fun setDiv(value: String):Text {
    div = value
    return this
  }
}
