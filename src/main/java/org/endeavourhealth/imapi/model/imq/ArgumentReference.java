package org.endeavourhealth.imapi.model.imq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentReference {
  private String parameter;
  private String referenceIri;

  public ArgumentReference setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }

  public ArgumentReference setReferenceIri(String referenceIri) {
    this.referenceIri = referenceIri;
    return this;
  }
}
