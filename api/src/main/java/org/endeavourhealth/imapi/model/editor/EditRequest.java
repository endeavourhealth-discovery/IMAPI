package org.endeavourhealth.imapi.model.editor;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;

@Getter
public class EditRequest {
  private TTEntity entity;
  private String hostUrl;

  public EditRequest(TTEntity entity, String hostUrl) {
    this.entity = entity;
    this.hostUrl = hostUrl;
  }

  public EditRequest() {

  }

  public void setEntity(TTEntity entity) {
    this.entity = entity;
  }

  public void setHostUrl(String hostUrl) {
    this.hostUrl = hostUrl;
  }
}
