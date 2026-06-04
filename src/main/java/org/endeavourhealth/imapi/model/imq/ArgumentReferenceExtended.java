package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.ArgumentReference;

public class ArgumentReferenceExtended extends ArgumentReference {

  @Override
  public ArgumentReferenceExtended parameter(String parameter) {
    this.setParameter(parameter);
    return this;
  }

  public ArgumentReferenceExtended referenceIri(TTIriRefExtended referenceIri) {
    this.setReferenceIri(referenceIri);
    return this;
  }

  public ArgumentReferenceExtended dataType(TTIriRefExtended dataType) {
    this.setDataType(dataType);
    return this;
  }
}
