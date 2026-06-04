package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

public class Instance extends IriLD{
  private TTIriRefExtended entailment;

  public TTIriRefExtended getEntailment() {
    return entailment;
  }

  public Instance setEntailment(TTIriRefExtended entailment) {
    this.entailment = entailment;
    return this;
  }

  @Override
  public Instance setIri(String iri) {
    super.setIri(iri);
    return this;
  }

  @Override
  public Instance setName(String name) {
    super.setName(name);
    return this;
  }
}
