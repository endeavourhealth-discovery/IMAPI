package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TTTypedRef extends TTIriRefExtended {
  private TTIriRefExtended type;

  public TTIriRefExtended getType() {
    return type;
  }

  @JsonSetter
  public TTTypedRef setType(TTIriRefExtended type) {
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
