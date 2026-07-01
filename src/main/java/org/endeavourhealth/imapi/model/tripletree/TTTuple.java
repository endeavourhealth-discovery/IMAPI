package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TTTuple {
  private TTIriRef predicate;
  private TTValueJava value;

  public TTTuple() {
  }

  public TTTuple(TTIriRef predicate, TTValueJava value) {
    this.predicate = predicate;
    this.value = value;
  }

  public TTIriRef getPredicate() {
    return predicate;
  }

  @JsonSetter
  public TTTuple setPredicate(TTIriRef predicate) {
    this.predicate = predicate;
    return this;
  }

  public TTValueJava getValue() {
    return value;
  }

  public TTTuple setValue(TTValueJava value) {
    this.value = value;
    return this;
  }
}
