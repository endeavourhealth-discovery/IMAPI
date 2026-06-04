package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.List;

public class InstanceDTO {

  private TTEntity entity;
  private List<TTIriRefExtended> predicates;

  public TTEntity getEntity() {
    return entity;
  }

  public InstanceDTO setEntity(TTEntity entity) {
    this.entity = entity;
    return this;
  }

  public List<TTIriRefExtended> getPredicates() {
    return predicates;
  }

  public InstanceDTO setPredicates(List<TTIriRefExtended> predicates) {
    this.predicates = predicates;
    return this;
  }
}
