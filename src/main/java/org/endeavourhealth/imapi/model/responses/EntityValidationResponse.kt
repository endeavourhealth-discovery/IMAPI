package org.endeavourhealth.imapi.model.responses

class EntityValidationResponse {
  var valid = false
    private set
  var message: String? = null
    private set

  constructor(valid: Boolean, message: String?) {
    this.valid = valid
    this.message = message
  }

  constructor() {}

  fun setValid(valid: Boolean): EntityValidationResponse {
    this.valid = valid
    return this
  }

  fun setMessage(message: String?): EntityValidationResponse {
    this.message = message
    return this
  }
}
