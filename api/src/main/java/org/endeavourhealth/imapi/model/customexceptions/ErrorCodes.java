package org.endeavourhealth.imapi.model.customexceptions;

public enum ErrorCodes {
  DATA_FORMAT_EXCEPTION("DataFormatException"),
  UNKNOWN_FORMAT_CONVERSION_EXCEPTION("UnknownFormatConversionException"),
  UNHANDLED_EXCEPTION("UnhandledException"),
  NO_HANDLER_FOUND_EXCEPTION("NoHandlerFoundException"),
  HTTP_MESSAGE_NOT_READABLE("HttpMessageNotReadable"),
  HTTP_REQUEST_METHOD_NOT_SUPPORTED("HttpRequestMethodNotSupported"),
  MISSING_SERVLET_REQUEST_PARAMETER("MissingServletRequestParameter"),
  TYPE_MISMATCH("TypeMismatch"),
  HTTP_MEDIA_TYPE_NOT_SUPPORTED("HttpMediaTypeNotSupported"),
  ACCESS_DENIED_EXCEPTION("AccessDeniedException"),
  AUTHENTICATION_EXCEPTION("AuthenticationException"),
  ECL_FORMAT_EXCEPTION("EclFormatException"),
  OPEN_SEARCH_EXCEPTION("OpenSearchException"),
  TT_FILER_EXCEPTION("TTFilerException"),
  QUERY_EXCEPTION("QueryException"),
  GENERAL_CUSTOM_EXCEPTION("GeneralCustomException"),
  CONFIG_EXCEPTION("ConfigException"),
  DOWNLOAD_EXCEPTION("DownloadException"),

  ECL_BUILDER_EXCEPTION("EclBuilderException");

  private String code;

  public String asString() {
    return code;
  }

  ErrorCodes(String code) {
    this.code = code;
  }
}
