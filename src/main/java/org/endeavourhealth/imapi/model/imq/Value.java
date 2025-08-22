package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.function.Consumer;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Value implements Assignable {
  private Operator operator;
  private String value;
  private String qualifier;
  private String valueLabel;
  private String valueParameter;
  private FunctionClause function;


  @Override
  public FunctionClause getFunction() {
    return this.function;
  }

  @Override
  public Value setFunction(FunctionClause function) {
    this.function = function;
    return this;
  }
  public Value function(Consumer<FunctionClause> builder) {
    this.function = new FunctionClause();
    builder.accept(this.function);
    return this;
  }


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







}
