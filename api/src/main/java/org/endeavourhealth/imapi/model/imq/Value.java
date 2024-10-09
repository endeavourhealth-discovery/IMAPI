package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class Value implements Assignable{
  private Operator operator;
  private String value;
  private String unit;
  private String qualifier;
  private String valueLabel;



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

  public String getUnit() {
    return unit;
  }

  public Value setUnit(String unit) {
    this.unit = unit;
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
    this.valueLabel=label;
    return this;
  }

  public Value setQualifier(String qualifier) {
    this.qualifier=qualifier;
  return this;
  }
}
