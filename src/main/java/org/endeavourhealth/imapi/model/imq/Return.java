package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@JsonPropertyOrder({"node", "variable", "iri", "name", "function", "as"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Return implements Returnable {
  private String iri;
  private String nodeRef;
  private String name;
  private String propertyRef;
  private String pathRef;
  private boolean inverse;
  private FunctionClause function;
  private TTIriRef units;
  private String as;
  private List<Return> returx;
  private TTIriRef dataType;
  @JsonProperty("case")
  private Case case_;
  private String description;

  public String getPathRef() {
    return pathRef;
  }

  public Return setPathRef(String pathRef) {
    this.pathRef = pathRef;
    return this;
  }

  @JsonProperty("case")
  public Case getCase() {
    return case_;
  }

  @JsonProperty("case")
  public Return setCase(Case case_) {
    this.case_ = case_;
    return this;
  }

  public Return case_(Consumer<Case> builder) {
    builder.accept(this.setCase(new Case()).getCase());
    return this;
  }

  public TTIriRef getDataType() {
    return dataType;
  }

  public Return setDataType(TTIriRef dataType) {
    this.dataType = dataType;
    return this;
  }

  @JsonProperty("return")
  public List<Return> getReturn() {
    return returx;
  }

  public Return setReturn(List<Return> returns) {
    this.returx = returns;
    return this;
  }

  public Return addReturn(Return return_) {
    if (this.returx == null)
      this.returx = new ArrayList<>();
    this.returx.add(return_);
    return this;
  }

  public Return return_(Consumer<Return> builder) {
    Return return_ = new Return();
    addReturn(return_);
    builder.accept(return_);
    return this;
  }

  public Return as(String as) {
    this.as = as;
    return this;
  }

  public Return function(Consumer<FunctionClause> builder) {
    builder.accept(setFunction(new FunctionClause()).getFunction());
    return this;
  }

  public Return setValue(String value) {
    return this;
  }

  public String getIri() {
    return iri;
  }

  public Return setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public Return setIri(Enum<?> iri) {
    this.iri = EnumUtils.asIri(iri).getIri();
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public Return setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }

  public String getName() {
    return name;
  }

  public Return setName(String name) {
    this.name = name;
    return this;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public Return setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  public boolean isInverse() {
    return inverse;
  }

  public Return setInverse(boolean inverse) {
    this.inverse = inverse;
    return this;
  }

  public FunctionClause getFunction() {
    return function;
  }

  public Return setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }

  public TTIriRef getUnits() {
    return this.units;
  }

  public Return setUnits(TTIriRef units) {
    this.units = units;
    return this;
  }

  public String getAs() {
    return as;
  }

  @JsonSetter
  public Return setAs(String as) {
    this.as = as;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Return setDescription(String description) {
    this.description = description;
    return this;
  }
}
