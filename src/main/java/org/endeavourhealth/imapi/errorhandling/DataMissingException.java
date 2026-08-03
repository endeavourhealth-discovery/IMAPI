package org.endeavourhealth.imapi.errorhandling;

public class DataMissingException extends Exception {

  public DataMissingException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public DataMissingException(String message) {
    super(message);
  }
}
