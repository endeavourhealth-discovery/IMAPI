package org.endeavourhealth.imapi.errorhandling

class UserAuthorisationException : Exception {
  constructor(message: String) : super(message)

  constructor(message: String, cause: Throwable) : super(message, cause)
}
