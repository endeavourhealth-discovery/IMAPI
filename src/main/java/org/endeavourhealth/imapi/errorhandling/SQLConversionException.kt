package org.endeavourhealth.imapi.errorhandling

class SQLConversionException : Exception {
  var sql: String? = null

  constructor(message: String) : super(message)

  constructor(message: String, cause: Throwable) : super(message, cause)

  constructor(message: String, sql: String) : super(message) {
    this.sql = sql
  }

  constructor(message: String, sql: String, cause: Throwable) : super(message, cause) {
    this.sql = sql
  }
}
