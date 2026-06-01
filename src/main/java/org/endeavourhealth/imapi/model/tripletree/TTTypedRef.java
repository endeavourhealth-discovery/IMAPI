package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TTTypedRef extends TTIriRef {
  private TTIriRef type;

  public TTIriRef getType() {
    return type;
  }

  @JsonSetter
  public TTTypedRef setType(TTIriRef type) {
    this.type = type;
    return this;
  }

  @Override
  public TTTypedRef iri(String iri) {
    super.setIri(iri);
    return this;
  }

  @Override
  public TTTypedRef name(String name) {
    super.setName(name);
    return this;
  }
}
