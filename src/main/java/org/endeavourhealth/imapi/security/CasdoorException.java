package org.endeavourhealth.imapi.security;

/** A call to Casdoor failed or was rejected. */
public class CasdoorException extends RuntimeException {
  public CasdoorException(String message) {
    super(message);
  }
}
