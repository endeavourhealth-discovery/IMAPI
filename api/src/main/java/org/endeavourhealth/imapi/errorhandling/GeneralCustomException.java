package org.endeavourhealth.imapi.errorhandling;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GeneralCustomException extends Exception {

  private HttpStatus status;

  public GeneralCustomException(String message, HttpStatus status) {
    super(message);
    setStatus(status);
  }

  public GeneralCustomException(String message, Throwable exception, HttpStatus status) {
    super(message, exception);
    setStatus(status);
  }


  public void setStatus(HttpStatus status) {
    this.status = status;
  }
}


