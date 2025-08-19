package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;
import java.util.function.Consumer;

public class Value implements Assignable {
  private Operator operator;
  private String value;
  private String qualifier;
  private String valueLabel;
  private String valueParameter;
  private List<Argument> argument;
  private TTIriRef units;

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
  public Value setArgument(List<Argument> arguments) {
    this.argument = arguments;
    return this;
  }

  @Override
  public List<Argument> getArgument() {
    return this.argument;
  }

  @Override
  public TTIriRef getUnit() {
    return this.units;
  }

  @Override
  public Value setUnit(TTIriRef units) {
    this.units = units;
    return this;
  }

  public Value addArgument(Argument argument) {
    if (this.argument == null)
      this.argument = new java.util.ArrayList<>();
    this.argument.add(argument);
    return this;
  }

  public Value argument(Consumer<Argument> builder) {
    Argument argument = new Argument();
    addArgument(argument);
    builder.accept(argument);
    return this;
  }


}
