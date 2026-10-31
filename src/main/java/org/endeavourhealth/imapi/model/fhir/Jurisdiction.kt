package org.endeavourhealth.imapi.model.fhir

import com.fasterxml.jackson.annotation.JsonProperty

class Jurisdiction {
    var coding: MutableList<Coding>? = mutableListOf()
      private set

  fun setCoding(coding: MutableList<Coding>?) {
    this.coding = coding
  }
}
