package org.endeavourhealth.imapi.errorhandling;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        ApiError error = new ApiError(HttpStatus.NOT_FOUND, message, ex, ErrorCodes.NO_HANDLER_FOUND_EXCEPTION);
        return buildResponseEntity(error);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Request required input is missing or null";
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, ex, ErrorCodes.HTTP_MESSAGE_NOT_READABLE);
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        Set<HttpMethod> methods = ex.getSupportedHttpMethods();
        if (methods != null) methods.forEach(t -> builder.append(t + " "));
        String message = "Method: " + ex.getMethod() + " is not supported for this API. Supported methods are " + builder;
        ApiError error = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, message, ex, ErrorCodes.HTTP_REQUEST_METHOD_NOT_SUPPORTED);
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Required parameter: " + ex.getParameterName() + " is missing";
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, ex, ErrorCodes.MISSING_SERVLET_REQUEST_PARAMETER);
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Type mismatch. ";
        String type = null;
        if (ex.getRequiredType() != null) type = ex.getRequiredType().getName();
        if (type != null) message += ex.getPropertyName() + " should be of type " + type;
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, message, ex, ErrorCodes.TYPE_MISMATCH);
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        String message;
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
        message = builder.substring(0, builder.length() -2);
        ApiError error = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message, ex, ErrorCodes.HTTP_MEDIA_TYPE_NOT_SUPPORTED);
        return buildResponseEntity(error);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ex.printStackTrace();
        String message = "Unhandled server error occurred";
        ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, ex, ErrorCodes.UNHANDLED_EXCEPTION);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(DataFormatException.class)
    protected ResponseEntity<Object> handleDataFormatException(DataFormatException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex, ErrorCodes.DATA_FORMAT_EXCEPTION);
        return buildResponseEntity(error);
    }

    @ExceptionHandler(UnknownFormatConversionException.class)
    protected ResponseEntity<Object> handleUnknownFormatConversionException(UnknownFormatConversionException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex, ErrorCodes.UNKNOWN_FORMAT_CONVERSION_EXCEPTION);
        return buildResponseEntity(error);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
