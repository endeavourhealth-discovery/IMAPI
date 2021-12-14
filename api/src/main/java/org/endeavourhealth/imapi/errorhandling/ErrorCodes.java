package org.endeavourhealth.imapi.errorhandling;

public enum ErrorCodes {
    DATA_FORMAT_EXCEPTION ("DataFormatException"),
    UNKNOWN_FORMAT_CONVERSION_EXCEPTION("UnknownFormatConversionException"),
    UNHANDLED_EXCEPTION("UnhandledException"),
    NO_HANDLER_FOUND_EXCEPTION("NoHandlerFoundException"),
    HTTP_MESSAGE_NOT_READABLE("HttpMessageNotReadable"),
    HTTP_REQUEST_METHOD_NOT_SUPPORTED("HttpRequestMethodNotSupported"),
    MISSING_SERVLET_REQUEST_PARAMETER("MissingServletRequestParameter"),
    TYPE_MISMATCH("TypeMismatch"),
    HTTP_MEDIA_TYPE_NOT_SUPPORTED("HttpMediaTypeNotSupported");

    private String code;

    public String asString() {
        return code;
    }

    ErrorCodes (String code) {
        this.code = code;
    }
}
