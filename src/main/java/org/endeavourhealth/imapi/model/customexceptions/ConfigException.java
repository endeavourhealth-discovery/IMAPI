package org.endeavourhealth.imapi.model.customexceptions;

public class ConfigException extends Exception {

  public ConfigException(String message) {
    super(message);
  }

  public ConfigException(String message, Throwable exception) {
    super(message, exception);
  }
}
