package org.endeavourhealth.imapi.model.uprn;

public class UprnException extends Exception {
  public UprnException(String message) {
    super(message);
  }

  public UprnException(String message, Throwable exception) {
    super(message, exception);
  }
}
