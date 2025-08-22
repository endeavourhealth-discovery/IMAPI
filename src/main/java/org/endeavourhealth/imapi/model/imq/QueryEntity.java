package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;

import java.util.Set;

public class QueryEntity extends Entity {

  private Query definition;


  public QueryEntity setIsContainedIn(Set<TTEntity> isContainedIn) {
    super.setIsContainedIn(isContainedIn);
    return this;
  }


  public Query getDefinition() {
    return definition;
  }

  public QueryEntity setDefinition(Query definition) {
    this.definition = definition;
    return this;
  }
}
