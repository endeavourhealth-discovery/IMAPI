package org.endeavourhealth.imapi.transform.qofimq;

public class QofParseException extends RuntimeException {
  public QofParseException(String message) { super(message); }
  public QofParseException(String message, Throwable cause) { super(message, cause); }
}
