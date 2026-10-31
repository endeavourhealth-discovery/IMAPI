package org.endeavourhealth.imapi.errorhandling

import com.fasterxml.jackson.annotation.JsonFormat
import org.endeavourhealth.imapi.model.customexceptions.ErrorCodes
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.time.LocalDateTime

class ApiError private constructor(var status: HttpStatus) {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  var timestamp: LocalDateTime = LocalDateTime.now()
  var message: String? = null
  var debugMessage: String? = null
  var subErrors: MutableList<ApiSubError>? = null
  var code: String? = null
    private set

  constructor(status: HttpStatus, ex: Throwable) : this(status) {
    this.message = "Unexpected error"
    this.debugMessage = ex.localizedMessage
  }

  constructor(status: HttpStatus, message: String, ex: Throwable) : this(status) {
    this.message = message
    this.debugMessage = ex.localizedMessage
  }

  constructor(status: HttpStatus, message: String, ex: Throwable, code: ErrorCodes) : this(status) {
    this.message = message
    this.debugMessage = ex.localizedMessage
    this.setCode(code)
  }

  private fun addSubError(subError: ApiSubError) {
    if (subErrors == null) {
      subErrors = mutableListOf()
    }
    subErrors?.add(subError)
  }

  private fun addValidationError(`object`: String, field: String, rejectedValue: Any?, message: String?) {
    addSubError(ApiValidationError(`object`, message, field, rejectedValue))
  }

  private fun addValidationError(`object`: String, message: String?) {
    addSubError(ApiValidationError(`object`, message))
  }

  private fun addValidationError(fieldError: FieldError) {
    this.addValidationError(
      fieldError.objectName,
      fieldError.field,
      fieldError.rejectedValue,
      fieldError.defaultMessage
    )
  }

  fun addValidationErrors(fieldErrors: MutableList<FieldError>) {
    fieldErrors.forEach { fieldError -> addValidationError(fieldError) }
  }

  private fun addValidationError(objectError: ObjectError) {
    this.addValidationError(objectError.objectName, objectError.defaultMessage)
  }

  fun addValidationError(globalErrors: MutableList<ObjectError>) {
    globalErrors.forEach { objectError -> this.addValidationError(objectError) }
  }

  fun setCode(code: ErrorCodes): ApiError {
    this.code = code.asString()
    return this
  }
}
