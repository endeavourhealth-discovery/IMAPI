package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Instance extends IriLD{
  private TTIriRef entailment;

  public TTIriRef getEntailment() {
    return entailment;
  }

  public Instance setEntailment(TTIriRef entailment) {
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
