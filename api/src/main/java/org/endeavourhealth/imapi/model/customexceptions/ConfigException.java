package org.endeavourhealth.imapi.model.customexceptions;

import org.springframework.http.HttpStatus;

public class ConfigException extends Exception {

  public ConfigException(String message) {
    super(message);
  }

  public ConfigException(String message, Throwable exception) {
    super(message, exception);
  }
}
