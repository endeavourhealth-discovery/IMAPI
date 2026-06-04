package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.interfacemanager.model.GraphVocab;
import org.endeavourhealth.interfacemanager.model.ValidationVocab;

@Getter
public class EntityValidationRequest {
  private TTEntity entity;
  private String validationIri;
  private GraphVocab graph;

  public EntityValidationRequest() {
  }

  public EntityValidationRequest setEntity(TTEntity entity) {
    this.entity = entity;
    return this;
  }

  public EntityValidationRequest setValidationIri(String validationIri) {
    this.validationIri = validationIri;
    return this;
  }

  public EntityValidationRequest setValidationIri(ValidationVocab validationIri) {
    this.validationIri = validationIri.toString();
    return this;
  }

  public EntityValidationRequest setGraph(GraphVocab graph) {
    this.graph = graph;
    return this;
  }
}
