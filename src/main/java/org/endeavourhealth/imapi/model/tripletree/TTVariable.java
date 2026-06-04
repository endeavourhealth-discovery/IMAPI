package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonPropertyOrder({"inverse", "iri", "name", "variable"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class TTVariable extends TTIriRefExtended {
  private String variable;
  private boolean isType;


  public TTVariable(TTIriRefExtended iri) {
    super.setIri(iri.getIri());
  }

  public TTVariable() {
  }

  public TTVariable(String iri) {
    super.setIri(iri);
  }

  public boolean isType() {
    return isType;
  }

  @JsonSetter
  public TTVariable setType(boolean type) {
    this.isType = type;
    return this;
  }

  @JsonSetter
  public TTVariable setType(TTIriRefExtended type) {
    setIri(type.getIri());
    if (type.getName() != null) setName(type.getName());
    isType = true;
    return this;
  }

  public TTVariable setIsType(boolean asType) {
    this.isType = asType;
    return this;
  }

  public String getVariable() {
    return variable;
  }

  public TTVariable setVariable(String variable) {
    this.variable = variable;
    return this;
  }
}
