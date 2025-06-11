package org.endeavourhealth.imapi.model.customexceptions;


public class EQDException extends Exception {
  public EQDException(String errorMessage, Throwable ex) {
    super(errorMessage, ex);
  }

  public EQDException(String errorMessage) {
    super(errorMessage);
  }
}