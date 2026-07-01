package org.endeavourhealth.imapi.model.dto;

import java.util.List;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class UnassignedEntity {

  private String iri;
  private String name;
  private List<TTIriRef> suggestions;

  public UnassignedEntity() {
  }

  public UnassignedEntity(String iri, String name, List<TTIriRef> suggestions) {
    this.iri = iri;
    this.name = name;
    this.suggestions = suggestions;
  }

  public String getIri() {
    return iri;
  }

  public UnassignedEntity setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public UnassignedEntity setName(String name) {
    this.name = name;
    return this;
  }

  public List<TTIriRef> getSuggestions() {
    return suggestions;
  }

  public UnassignedEntity setSuggestions(List<TTIriRef> suggestions) {
    this.suggestions = suggestions;
    return this;
  }
}
