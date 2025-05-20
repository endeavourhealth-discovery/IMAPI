package org.endeavourhealth.imapi.model.opensearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusId implements MatchPhraseId {
  private String id;

  public StatusId(String id) {
    this.id = id;
  }

  @JsonProperty("status.iri")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
