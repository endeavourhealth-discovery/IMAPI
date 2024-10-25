package org.endeavourhealth.imapi.model.customexceptions;

import org.springframework.http.HttpStatus;

public class ConfigException extends Exception {

  private HttpStatus status;

  public ConfigException(String message, HttpStatus status) {
    super(message);
    setStatus(status);
  }

  public ConfigException(String message, Throwable exception, HttpStatus status) {
    super(message, exception);
    setStatus(status);
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public HttpStatus getStatus() {
    return this.status;
  }
}
