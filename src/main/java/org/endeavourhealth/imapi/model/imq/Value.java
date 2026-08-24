package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.function.Consumer;

import lombok.Getter;
import org.endeavourhealth.imapi.model.imq.Assignable;
import org.endeavourhealth.imapi.model.imq.Compare;
import org.endeavourhealth.imapi.model.imq.FunctionClause;
import org.endeavourhealth.imapi.model.imq.Operator;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Value implements Assignable {

  private String iri;
  private String name;
  private String nodeRef;
  private String propertyRef;

  public String getIri() {
    return iri;
  }

  public Value setIri(String iri) {
    this.iri = iri;
    return this;
  }

  public String getName() {
    return name;
  }

  public Value setName(String name) {
    this.name = name;
    return this;
  }

  public String getNodeRef() {
    return nodeRef;
  }

  public Value setNodeRef(String nodeRef) {
    this.nodeRef = nodeRef;
    return this;
  }

  public String getPropertyRef() {
    return propertyRef;
  }

  public Value setPropertyRef(String propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  @Getter
  private Operator operator;

  @Getter
  private String value;

  private String valueLabel;

  @Getter
  private String valueParameter;

  private FunctionClause function;
  private String description;

  private TTIriRef units;
  private boolean invalid;
  private String valueTerm;
  private Compare compare;

  public boolean isInvalid() {
    return invalid;
  }

  public Value setIsInvalid(boolean invalid) {
    this.invalid = invalid;
    return this;
  }

  @Override
  public String getValueTerm() {
    return this.valueTerm;
  }

  @Override
  public Assignable setValueTerm(String valueTerm) {
    this.valueTerm = valueTerm;
    return this;
  }

  public TTIriRef getUnits() {
    return this.units;
  }

  public Value setUnits(TTIriRef units) {
    this.units = units;
    return this;
  }

  public Value function(Consumer<FunctionClause> builder) {
    this.function = new FunctionClause();
    builder.accept(this.function);
    return this;
  }

  public Value setValueParameter(String valueParameter) {
    this.valueParameter = valueParameter;
    return this;
  }

  public Value setOperator(Operator operator) {
    this.operator = operator;
    return this;
  }

  @Override
  public Value setValue(String value) {
    this.value = value;
    return this;
  }

  @Override
  public String getValueLabel() {
    return this.valueLabel;
  }

  @Override
  public Assignable setValueLabel(String label) {
    this.valueLabel = label;
    return this;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Value setDescription(String description) {
    this.description = description;
    return this;
  }

  public Compare getCompare() {
    return this.compare;
  }

  @Override
  public Value setCompare(Compare compare) {
    this.compare = compare;
    return this;
  }

  public Value compare(Consumer<Compare> builder) {
    this.compare = new Compare();
    builder.accept(this.compare);
    return this;
  }
}
