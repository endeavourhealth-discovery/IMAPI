package org.endeavourhealth.imapi.model.tripletree;

import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.HashMap;
import java.util.Map;

public class TTBundle {
  private TTEntityJava entity;
  private Map<String, String> predicates = new HashMap<>();

  public TTEntityJava getEntity() {
    return entity;
  }

  public TTBundle setEntity(TTEntityJava entity) {
    this.entity = entity;
    return this;
  }

  public Map<String, String> getPredicates() {
    return predicates;
  }

  public TTBundle setPredicates(Map<String, String> predicates) {
    this.predicates = predicates;
    return this;
  }

  public TTBundle addPredicate(TTIriRef predicate) {
    if (null == this.predicates)
      this.predicates = new HashMap<>();

    if (predicate != null)
      predicates.put(predicate.getIri(), predicate.getName());

    return this;
  }
}
