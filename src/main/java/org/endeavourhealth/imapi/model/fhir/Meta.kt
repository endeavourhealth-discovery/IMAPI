package org.endeavourhealth.imapi.model.fhir

import com.fasterxml.jackson.annotation.JsonProperty

class Meta {
    var lastUpdated: String? = null
  private set

    var profile: Array<String>? = arrayOf()
  private set

  fun setLastUpdated(lastUpdated: String?):Meta {
    this.lastUpdated = lastUpdated
    return this
  }

  fun setProfile(profile: Array<String>?):Meta {
    this.profile = profile
    return this
  }
}
