package org.endeavourhealth.imapi.model;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;

@Getter
public class ValidatedEntity extends TTEntityJava {
  private String validationCode;
  private String validationLabel;

  public ValidatedEntity() {
  }

  public ValidatedEntity(String iri, String validationCode) {
    super(iri);
    this.validationCode = validationCode;
  }

  public ValidatedEntity setValidationCode(String validationCode) {
    this.validationCode = validationCode;
    return this;
  }

  public ValidatedEntity setValidationLabel(String validationLabel) {
    this.validationLabel = validationLabel;
    return this;
  }
}
