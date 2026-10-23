package org.endeavourhealth.imapi.errorhandling

class UserNotFoundException : Exception {
  constructor(errorMessage: String, err: Throwable) : super(errorMessage, err)

  constructor(errorMessage: String) : super(errorMessage)
}
