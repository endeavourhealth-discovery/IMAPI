package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.VocabEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"node", "variable", "iri", "name", "function", "as", "return"})
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
  @JsonIgnore
  private Return returx;
  private TTIriRef dataType;
  @JsonProperty("case")
  private Case case_;
  private String description;
  private List<Match> match;
  private Bool boolMatch;

  public String getIri() {
    return iri;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public String getName() {
    return name;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public String getValue() {
    return value;
  }

  public String getValueRef() {
    return valueRef;
  }

  public boolean isInverse() {
    return inverse;
  }

  public FunctionClause getFunction() {
    return function;
  }

  public String getUnit() {
    return unit;
  }

  public String getAs() {
    return as;
  }

  public Case getCase_() {
    return case_;
  }

  public void setCase_(Case case_) {
    this.case_ = case_;
  }

  public String getDescription() {
    return description;
  }

  public List<Match> getMatch() {
    return match;
  }

  public Bool getBoolMatch() {
    return boolMatch;
  }

  public ReturnProperty setName(String name) {
    this.name = name;
    return this;
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

  public ReturnProperty setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
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

  @JsonSetter
  public ReturnProperty setAs(String as) {
    this.as = as;
    return this;
  }

  public ReturnProperty as(String as) {
    this.as = as;
    return this;
  }

  public ReturnProperty setUnit(String unit) {
    this.unit = unit;
    return this;
  }

  public ReturnProperty setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public ReturnProperty function(Consumer<FunctionClause> builder) {
    builder.accept(setFunction(new FunctionClause()).getFunction());
    return this;
  }

  public ReturnProperty setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }


  public ReturnProperty setValue(String value) {
    this.value = value;
    return this;
  }

  public ReturnProperty setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public ReturnProperty setIri(VocabEnum iri) {
    this.iri = iri.toString();
    return this;
  }

  public ReturnProperty setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }


  public ReturnProperty setDescription(String description) {
    this.description = description;
    return this;
  }
}
