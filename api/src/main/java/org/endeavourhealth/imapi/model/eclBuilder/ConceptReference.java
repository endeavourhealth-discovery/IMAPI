package org.endeavourhealth.imapi.model.eclBuilder;

import lombok.Getter;

@Getter
public class ConceptReference {
  private String iri;
  private String name;

  public ConceptReference(String iri) {
    this.iri = iri;
  }

  public ConceptReference() {
  }

  public ConceptReference setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public ConceptReference setName(String name) {
    this.name = name;
    return this;
  }
}
