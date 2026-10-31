package org.endeavourhealth.imapi.dataaccess.helpers

class DALException : RuntimeException {
  constructor(msg: String) : super(msg)
  constructor(msg: String, cause: Throwable) : super(msg, cause)
}
