package org.endeavourhealth.imapi.model.imq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentReference {
  private String parameter;
  private TTIriRef referenceIri;
  private TTIriRef dataType;

  public ArgumentReference setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }

  public ArgumentReference setReferenceIri(TTIriRef referenceIri) {
    this.referenceIri = referenceIri;
    return this;
  }

  public ArgumentReference setDataType(TTIriRef dataType) {
    this.dataType = dataType;
    return this;
  }
}
