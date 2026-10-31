package org.endeavourhealth.imapi.model

import org.endeavourhealth.imapi.model.tripletree.TTEntity

class ValidatedEntity : TTEntity() {
  var validationCode: String? = null
    private set
  var validationLabel: String? = null
    private set

  fun setValidationCode(validationCode: String?): ValidatedEntity {
    this.validationCode = validationCode
    return this
  }

  fun setValidationLabel(validationLabel: String?): ValidatedEntity {
    this.validationLabel = validationLabel
    return this
  }
}
