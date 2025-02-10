package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"node", "variable", "iri", "name", "function", "as"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ReturnProperty {
  private String iri;
  private String nodeRef;
  private String name;
  private String propertyRef;
  private String value;
  private String valueRef;
  private boolean inverse;
  private FunctionClause function;
  private String unit;
  private String as;
  private Return returx;
  private TTIriRef dataType;
  @JsonProperty("case")
  private Case case_;
  private String description;
  private List<Match> match;
  private Bool boolMatch;

  public String getName() {
    return name;
  }

  public ReturnProperty setName(String name) {
    this.name = name;
    return this;
  }

  public List<Match> getMatch() {
    return match;
  }

  public ReturnProperty setMatch(List<Match> match) {
    this.match = match;
    return this;
  }

  public ReturnProperty addMatch(Match match) {
    if (this.match == null) {
      this.match = new ArrayList<>();
    }
    this.match.add(match);
    return this;
  }

  public ReturnProperty match(Consumer<Match> builder) {
    Match match = new Match();
    addMatch(match);
    builder.accept(match);
    return this;
  }


  public Bool getBoolMatch() {
    return boolMatch;
  }

  public ReturnProperty setBoolMatch(Bool boolMatch) {
    this.boolMatch = boolMatch;
    return this;
  }

  @JsonProperty("case")
  public Case getCase() {
    return case_;
  }

  @JsonProperty("case")
  public ReturnProperty setCase(Case case_) {
    this.case_ = case_;
    return this;
  }

  public ReturnProperty case_(Consumer<Case> builder) {
    builder.accept(this.setCase(new Case()).getCase());
    return this;
  }

  public TTIriRef getDataType() {
    return dataType;
  }

  public ReturnProperty setDataType(TTIriRef dataType) {
    this.dataType = dataType;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public ReturnProperty setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }

  public String getValueRef() {
    return valueRef;
  }

  public ReturnProperty setValueRef(String valueRef) {
    this.valueRef = valueRef;
    return this;
  }

  @JsonProperty("return")
  public Return getReturn() {
    return returx;
  }

  public ReturnProperty setReturn(Return returx) {
    this.returx = returx;
    return this;
  }

  public ReturnProperty return_(Consumer<Return> builder) {
    this.returx = new Return();
    builder.accept(this.returx);
    return this;
  }

  public String getAs() {
    return as;
  }

  @JsonSetter
  public ReturnProperty setAs(String as) {
    this.as = as;
    return this;
  }

  public ReturnProperty as(String as) {
    this.as = as;
    return this;
  }

  public String getUnit() {
    return unit;
  }

  public ReturnProperty setUnit(String unit) {
    this.unit = unit;
    return this;
  }

  public FunctionClause getFunction() {
    return function;
  }

  public ReturnProperty setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public ReturnProperty function(Consumer<FunctionClause> builder) {
    builder.accept(setFunction(new FunctionClause()).getFunction());
    return this;
  }

  public boolean isInverse() {
    return inverse;
  }

  public ReturnProperty setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }


  public String getValue() {
    return value;
  }

  public ReturnProperty setValue(String value) {
    this.value = value;
    return this;
  }

  @JsonProperty("@id")
  public String getIri() {
    return iri;
  }

  public ReturnProperty setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public ReturnProperty setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
