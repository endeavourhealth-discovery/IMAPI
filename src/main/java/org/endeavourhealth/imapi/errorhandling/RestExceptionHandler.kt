package org.endeavourhealth.imapi.errorhandling

import jakarta.servlet.http.HttpServletRequest
import org.apache.catalina.connector.ClientAbortException
import org.endeavourhealth.imapi.filer.TTFilerException
import org.endeavourhealth.imapi.model.customexceptions.*
import org.endeavourhealth.imapi.model.imq.QueryException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.zip.DataFormatException
import javax.security.sasl.AuthenticationException

//@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
  override fun handleNoHandlerFoundException(
    ex: NoHandlerFoundException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any?> {
    val message = "No handler found for ${ex.httpMethod} ${ex.requestURL}"
    val error = ApiError(HttpStatus.NOT_FOUND, message, ex, ErrorCodes.NO_HANDLER_FOUND_EXCEPTION)
    return buildResponseEntity(error)
  }

  override fun handleHttpMessageNotReadable(
    ex: HttpMessageNotReadableException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any?> {
    val message = "Request required input is missing or null"
    val error = ApiError(HttpStatus.BAD_REQUEST, message, ex, ErrorCodes.HTTP_MESSAGE_NOT_READABLE)
    return buildResponseEntity(error)
  }

  override fun handleHttpRequestMethodNotSupported(
    ex: HttpRequestMethodNotSupportedException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any?> {
    val methods = ex.getSupportedHttpMethods()
    val methodsString = methods?.joinToString(" ") ?: ""
    val message = "Method: ${ex.method} is not supported for this API. Supported methods are $methodsString"
    val error = ApiError(HttpStatus.METHOD_NOT_ALLOWED, message, ex, ErrorCodes.HTTP_REQUEST_METHOD_NOT_SUPPORTED)
    return buildResponseEntity(error)
  }

  override fun handleMissingServletRequestParameter(
    ex: MissingServletRequestParameterException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any?> {
    val message = "Required parameter: ${ex.parameterName} is missing"
    val error = ApiError(HttpStatus.BAD_REQUEST, message, ex, ErrorCodes.MISSING_SERVLET_REQUEST_PARAMETER)
    return buildResponseEntity(error)
  }

  override fun handleTypeMismatch(
    ex: TypeMismatchException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any?> {
    var message = "Type mismatch. "
    val type = ex.requiredType?.name
    if (type != null) message += "${ex.propertyName} should be of type $type"
    val error = ApiError(HttpStatus.BAD_REQUEST, message, ex, ErrorCodes.TYPE_MISMATCH)
    return buildResponseEntity(error)
  }

  override fun handleHttpMediaTypeNotSupported(
    ex: HttpMediaTypeNotSupportedException,
    headers: HttpHeaders,
    status: HttpStatusCode,
    request: WebRequest
  ): ResponseEntity<Any?> {
    val message =
      "${ex.contentType} media is not supported. Supported media types are ${ex.supportedMediaTypes.joinToString(", ")}"
    val error = ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message, ex, ErrorCodes.HTTP_MEDIA_TYPE_NOT_SUPPORTED)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(Throwable::class)
  protected fun handleAll(ex: Exception?, request: WebRequest?): ResponseEntity<Any?> {
    val message = "Unhandled server error occurred"
    val error = ApiError(
      HttpStatus.INTERNAL_SERVER_ERROR,
      message,
      Exception("Unhandled server error"),
      ErrorCodes.UNHANDLED_EXCEPTION
    )
    return buildResponseEntity(error)
  }

  @ExceptionHandler(ClientAbortException::class)
  protected fun handleClientAbortException(
    ex: ClientAbortException?,
    req: HttpServletRequest,
    request: WebRequest?
  ): ResponseEntity<Any?>? {
    return if ("/api/entity/public/search" == req.requestURI) {
      ResponseEntity<Any?>(HttpStatus.OK)
    } else {
      handleAll(ex, request)
    }
  }

  @ExceptionHandler(AccessDeniedException::class)
  protected fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<Any?>? {
    throw ex
  }

  @ExceptionHandler(DataFormatException::class)
  protected fun handleDataFormatException(ex: DataFormatException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.BAD_REQUEST, ex.message!!, ex, ErrorCodes.DATA_FORMAT_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(EclFormatException::class)
  protected fun handleEclFormatException(ex: EclFormatException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.BAD_REQUEST, ex.message!!, ex, ErrorCodes.ECL_FORMAT_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(OpenSearchException::class)
  protected fun handleOpenSearchException(ex: OpenSearchException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.message!!, ex, ErrorCodes.OPEN_SEARCH_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(TTFilerException::class)
  protected fun handleTTFilerException(ex: TTFilerException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.BAD_REQUEST, ex.message!!, ex, ErrorCodes.TT_FILER_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(QueryException::class)
  protected fun handleQueryException(ex: QueryException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.BAD_REQUEST, ex.message!!, ex, ErrorCodes.QUERY_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(GeneralCustomException::class)
  protected fun handleGeneralCustomException(ex: GeneralCustomException): ResponseEntity<Any?> {
    val error = ApiError(ex.status, ex.message!!, ex, ErrorCodes.GENERAL_CUSTOM_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(ConfigException::class)
  protected fun handleConfigException(ex: ConfigException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.message!!, ex, ErrorCodes.CONFIG_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(DownloadException::class)
  protected fun handleDownloadException(ex: DownloadException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.message!!, ex, ErrorCodes.DOWNLOAD_EXCEPTION)
    return buildResponseEntity(error)
  }


  @ExceptionHandler(IllegalArgumentException::class)
  protected fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.BAD_REQUEST, ex.message!!, ex, ErrorCodes.ILLEGAL_ARGUMENT_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(SQLConversionException::class)
  protected fun handleSQLConversionException(ex: SQLConversionException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.FAILED_DEPENDENCY, ex.message!!, ex, ErrorCodes.SQL_CONVERSION_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(UserNotFoundException::class)
  protected fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.FORBIDDEN, ex.message!!, ex, ErrorCodes.USER_NOT_FOUND_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(AuthenticationException::class)
  protected fun handleAuthenticationException(ex: AuthenticationException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.UNAUTHORIZED, ex.message!!, ex, ErrorCodes.AUTHENTICATION_EXCEPTION)
    return buildResponseEntity(error)
  }

  @ExceptionHandler(DataMissingException::class)
  protected fun handleDataMissingException(ex: DataMissingException): ResponseEntity<Any?> {
    val error = ApiError(HttpStatus.BAD_REQUEST, ex.message!!, ex, ErrorCodes.DATA_MISSING_EXCEPTION)
    return buildResponseEntity(error)
  }

  private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any?> {
    return ResponseEntity<Any?>(apiError, apiError.status)
  }
}
