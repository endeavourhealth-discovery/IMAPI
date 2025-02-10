package org.endeavourhealth.imapi.model.validation;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;

@Getter
public class EntityValidationRequest {
  private TTEntity entity;
  private String validationIri;

  public EntityValidationRequest() {
  }

  public EntityValidationRequest setEntity(TTEntity entity) {
    this.entity = entity;
    return this;
  }

  public EntityValidationRequest setValidationIri(String validationIri) {
    this.validationIri = validationIri;
    return this;
  }
}
