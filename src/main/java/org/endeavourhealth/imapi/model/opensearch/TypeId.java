package org.endeavourhealth.imapi.model.opensearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TypeId implements MatchPhraseId {
  private String id;

  public TypeId(String id) {
    this.id = id;
  }

  @JsonProperty("entityType.iri")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
