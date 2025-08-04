package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.VALIDATION;
import org.endeavourhealth.imapi.vocabulary.types.Graph;

@Getter
public class EntityValidationRequest {
  private TTEntity entity;
  private String validationIri;
  private Graph graph;

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

  public EntityValidationRequest setValidationIri(VALIDATION validationIri) {
    this.validationIri = validationIri.toString();
    return this;
  }

  public EntityValidationRequest setGraph(Graph graph) {
    this.graph = graph;
    return this;
  }
}
