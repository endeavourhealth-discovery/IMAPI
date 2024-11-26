package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Value implements Assignable{
  private Operator operator;
  private String value;
  private String qualifier;
  private String valueLabel;
  private List<Argument> argument;
  private String valueParameter;

  public String getValueParameter() {
    return valueParameter;
  }

  public Value setValueParameter(String valueParameter) {
    this.valueParameter = valueParameter;
    return this;
  }

  @Override
  public List<Argument> getArgument() {
    return argument;
  }

  @Override
  public Value setArgument(List<Argument> argument) {
    this.argument = argument;
    return this;
  }
  public Value addArgument (Argument argument){
      if (this.argument == null) {
        this.argument = new ArrayList<>();
      }
      this.argument.add(argument);
      return this;
    }
  public Value argument (Consumer< Argument > builder) {
      Argument argument = new Argument();
      addArgument(argument);
      builder.accept(argument);
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
    this.valueLabel=label;
    return this;
  }

  public Value setQualifier(String qualifier) {
    this.qualifier=qualifier;
  return this;
  }

  @JsonIgnore
  public String getUnit(){
    if (this.argument!=null) {
      if (this.argument.get(0).getParameter().contains("unit")) {
        String timeUnit = argument.get(0).getValueIri().getIri();
        return timeUnit.substring(timeUnit.lastIndexOf("#") + 1);
      }
      else return "";
    }
    else return "";
  }

  @JsonIgnore
  public Value setUnit(String unit){
    this.addArgument(new Argument()
      .setParameter("units")
      .setValueIri(TTIriRef.iri(IM.NAMESPACE+unit).setName(unit)));
    return this;
  }



}
