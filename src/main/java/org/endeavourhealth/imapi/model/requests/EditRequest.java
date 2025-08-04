package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.types.Graph;

@Getter
public class EditRequest {
  private TTEntity entity;
  private String hostUrl;
  private Graph graph;
  private String crud;

  public EditRequest(TTEntity entity, String hostUrl) {
    this.entity = entity;
    this.hostUrl = hostUrl;
  }

  public EditRequest() {

  }

  public EditRequest setEntity(TTEntity entity) {
    this.entity = entity;
    return this;
  }

  public EditRequest setHostUrl(String hostUrl) {
    this.hostUrl = hostUrl;
    return this;
  }

  public EditRequest setGraph(Graph graph) {
    this.graph = graph;
    return this;
  }

  public EditRequest setCrud(String crud) {
    this.crud = crud;
    return this;
  }
}