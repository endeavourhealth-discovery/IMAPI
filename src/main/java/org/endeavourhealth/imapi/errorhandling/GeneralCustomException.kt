package org.endeavourhealth.imapi.errorhandling

import org.springframework.http.HttpStatus

class GeneralCustomException : Exception {
  val status: HttpStatus

  constructor(message: String, status: HttpStatus) : super(message) {
    this.status = status
  }

  constructor(message: String, exception: Throwable, status: HttpStatus) : super(message, exception) {
    this.status = status
  }
}


