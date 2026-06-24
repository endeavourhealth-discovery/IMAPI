package org.endeavourhealth.imapi.model.iml;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParameterTemplate extends Entity {
  private String label;
  private Integer order;
  private List<ValueTemplate> valueTemplate;

  public Integer getOrder() {
    return order;
  }

  public ParameterTemplate setOrder(Integer order) {
    this.order = order;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public ParameterTemplate setLabel(String label) {
    this.label = label;
    return this;
  }


  public List<ValueTemplate> getValueTemplate() {
    return valueTemplate;
  }

  public ParameterTemplate setValueTemplate(List<ValueTemplate> valueTemplate) {
    this.valueTemplate = valueTemplate;
    return this;
  }

  public ParameterTemplate addValueTemplate(ValueTemplate valueTemplate) {
    if (this.valueTemplate == null) {
      this.valueTemplate = new ArrayList<>();
    }
    this.valueTemplate.add(valueTemplate);
    return this;
  }

  public ParameterTemplate valueTemplate(Consumer<ValueTemplate> builder) {
    ValueTemplate valueTemplate = new ValueTemplate();
    addValueTemplate(valueTemplate);
    builder.accept(valueTemplate);
    return this;
  }


}
