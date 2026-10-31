package org.endeavourhealth.imapi.errorhandling

data class ApiValidationError(
  var `object`: String,
  var message: String? = null,
  var field: String? = null,
  var rejectedValue: Any? = null,
) : ApiSubError