package org.endeavourhealth.imapi.filer;

public class TTFilerException extends Exception {
  public TTFilerException(String message) {
    super(message);
  }

  public TTFilerException(String message, Throwable exception) {
    super(message, exception);
  }
}
