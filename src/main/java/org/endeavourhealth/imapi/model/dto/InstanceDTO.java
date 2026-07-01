package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.List;

public class InstanceDTO {

  private TTEntityJava entity;
  private List<TTIriRef> predicates;

  public TTEntityJava getEntity() {
    return entity;
  }

  public InstanceDTO setEntity(TTEntityJava entity) {
    this.entity = entity;
    return this;
  }

  public List<TTIriRef> getPredicates() {
    return predicates;
  }

  public InstanceDTO setPredicates(List<TTIriRef> predicates) {
    this.predicates = predicates;
    return this;
  }
}
