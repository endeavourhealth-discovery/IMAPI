package org.endeavourhealth.imapi.model.config;

public class ComponentLayoutItem {
  private String label;
  private String predicate;
  private String type;
  private String size;
  private int order;

  public ComponentLayoutItem() {
  }

  public ComponentLayoutItem(String label, String predicate, String type, String size, int order) {
    this.label = label;
    this.predicate = predicate;
    this.type = type;
    this.size = size;
    this.order = order;
  }

  public String getLabel() {
    return label;
  }

  public ComponentLayoutItem setLabel(String label) {
    this.label = label;
    return this;
  }

  public String getPredicate() {
    return predicate;
  }

  public ComponentLayoutItem setPredicate(String predicate) {
    this.predicate = predicate;
    return this;
  }

  public String getType() {
    return type;
  }

  public ComponentLayoutItem setType(String type) {
    this.type = type;
    return this;
  }

  public String getSize() {
    return size;
  }

  public ComponentLayoutItem setSize(String size) {
    this.size = size;
    return this;
  }

  public int getOrder() {
    return order;
  }

  public ComponentLayoutItem setOrder(int order) {
    this.order = order;
    return this;
  }
}
