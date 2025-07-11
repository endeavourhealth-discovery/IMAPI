package org.endeavourhealth.imapi.model.responses;

import lombok.Getter;

@Getter
public class EntityValidationResponse {
  private boolean valid;
  private String message;

  public EntityValidationResponse(boolean valid, String message) {
    this.valid = valid;
    this.message = message;
  }

  public EntityValidationResponse() {
  }

  public EntityValidationResponse setValid(boolean valid) {
    this.valid = valid;
    return this;
  }

  public EntityValidationResponse setMessage(String message) {
    this.message = message;
    return this;
  }
}
