package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.Namespace;

@Getter
public class EditRequest {
  private TTEntity entity;
  private String hostUrl;
  private String namespace;
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

  public EditRequest setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  public EditRequest setCrud(String crud) {
    this.crud = crud;
    return this;
  }
}