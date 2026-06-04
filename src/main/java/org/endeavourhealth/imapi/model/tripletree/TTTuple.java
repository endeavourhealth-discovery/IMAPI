package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonSetter;

public class TTTuple {
  private TTIriRefExtended predicate;
  private TTValue value;

  public TTTuple() {
  }

  public TTTuple(TTIriRefExtended predicate, TTValue value) {
    this.predicate = predicate;
    this.value = value;
  }

  public TTIriRefExtended getPredicate() {
    return predicate;
  }

  @JsonSetter
  public TTTuple setPredicate(TTIriRefExtended predicate) {
    this.predicate = predicate;
    return this;
  }

  public TTValue getValue() {
    return value;
  }

  public TTTuple setValue(TTValue value) {
    this.value = value;
    return this;
  }
}
