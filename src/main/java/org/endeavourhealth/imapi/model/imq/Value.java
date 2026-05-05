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


  public Value setUnits(TTIriRef units) {
    this.units = units;
    return this;
  }

  public TTIriRef getUnits(){
    return this.units;
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
