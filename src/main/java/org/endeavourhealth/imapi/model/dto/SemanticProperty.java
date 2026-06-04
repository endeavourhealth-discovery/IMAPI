package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.io.Serializable;

public class SemanticProperty implements Serializable {

  private TTIriRefExtended property;
  private TTIriRefExtended type;

  public TTIriRefExtended getProperty() {
    return property;
  }

  public SemanticProperty setProperty(TTIriRefExtended property) {
    this.property = property;
    return this;
  }

  public TTIriRefExtended getType() {
    return type;
  }

  public SemanticProperty setType(TTIriRefExtended type) {
    this.type = type;
    return this;
  }
}
