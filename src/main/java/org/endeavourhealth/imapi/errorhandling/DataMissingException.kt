package org.endeavourhealth.imapi.errorhandling

class DataMissingException : Exception {
  constructor(errorMessage: String, err: Throwable) : super(errorMessage, err)

  constructor(message: String) : super(message)
}
