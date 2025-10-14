package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.function.Consumer;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Value implements Assignable {
  @Getter
  private Operator operator;
  @Getter
  private String value;
  @Getter
  private TTIriRef qualifier;
  private String valueLabel;
  @Getter
  private String valueParameter;
  private FunctionClause function;
  private String description;
  @Getter
  private TTIriRef units;

  public Value setUnits(TTIriRef units) {
    this.units = units;
    return this;
  }


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
  public Value setDescription(String description) {
    this.description= description;
    return this;
  }

  @Override
  public String getDescription() {
    return description;
  }


  public Value setQualifier(TTIriRef qualifier) {
    this.qualifier = qualifier;
    return this;
  }







}
