package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Value implements Assignable {
  private Operator operator;
  private String value;
  private String qualifier;
  private String valueLabel;
  private String valueParameter;
  private TTIriRef unit;

  public String getValueParameter() {
    return valueParameter;
  }

  public Value setValueParameter(String valueParameter) {
    this.valueParameter = valueParameter;
    return this;
  }

  public Operator getOperator() {
    return operator;
  }

  public Value setOperator(Operator operator) {
    this.operator = operator;
    return this;
  }


  public String getValue() {
    return value;
  }

  @Override
  public Value setValue(String value) {
    this.value = value;
    return this;
  }


  public String getQualifier() {
    return this.qualifier;
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

  public Value setQualifier(String qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  @Override
  public TTIriRef getUnit() {
    return this.unit;
  }

  @Override
  public Assignable setUnit(TTIriRef unit) {
    this.unit = unit;
    return this;
  }


}
