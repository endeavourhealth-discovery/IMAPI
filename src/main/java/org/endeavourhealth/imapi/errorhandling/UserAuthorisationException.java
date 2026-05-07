package org.endeavourhealth.imapi.errorhandling;

public class UserAuthorisationException extends Exception {
  public UserAuthorisationException(String message) {
    super(message);
  }

  public UserAuthorisationException(String message, Throwable cause) {
    super(message, cause);
  }
}
