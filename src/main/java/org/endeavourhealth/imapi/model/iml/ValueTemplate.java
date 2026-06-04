package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;

public class ValueTemplate extends EntityExtended {
  private String parameter;
  private Integer order;
  private String label;
  private TTIriRefExtended valueType;
  private Object defaultValue;
  private List<Object> valueOption;

  public TTIriRefExtended getValueType() {
    return valueType;
  }

  public ValueTemplate setValueType(TTIriRefExtended valueType) {
    this.valueType = valueType;
    return this;
  }

  public List<Object> getValueOption() {
    return valueOption;
  }

  public ValueTemplate setValueOption(List<Object> valueOption) {
    this.valueOption = valueOption;
    return this;
  }

  public String getParameter() {
    return parameter;
  }

  public ValueTemplate setParameter(String parameter) {
    this.parameter = parameter;
    return this;
  }

  public ValueTemplate addValueOption(Object valueOption) {
    if (this.valueOption == null) {
      this.valueOption = new ArrayList<>();
    }
    this.valueOption.add(valueOption);
    return this;
  }


  public Integer getOrder() {
    return order;
  }

  public ValueTemplate setOrder(Integer order) {
    this.order = order;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public ValueTemplate setLabel(String label) {
    this.label = label;
    return this;
  }

  public Object getDefaultValue() {
    return defaultValue;
  }

  public ValueTemplate setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }
}
