package org.endeavourhealth.imapi.model;

import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PropertyDisplay {
  private int order;
  private TTIriRefExtended group;
  private List<TTIriRefExtended> property;
  private List<TTIriRefExtended> type;
  private String cardinality;
  private boolean isOr;
  private boolean isType;
  private boolean isNode;
  private String reverseCardinality;

  public PropertyDisplay() {
    property = new ArrayList<>();
    type = new ArrayList<>();
  }

  public PropertyDisplay setOrder(int order) {
    this.order = order;
    return this;
  }

  public PropertyDisplay setGroup(TTIriRefExtended group) {
    this.group = group;
    return this;
  }

  public PropertyDisplay setProperty(List<TTIriRefExtended> property) {
    this.property = property;
    return this;
  }

  public PropertyDisplay addProperty(TTIriRefExtended property) {
    if (this.property == null) {
      this.property = new ArrayList<>();
    }
    this.property.add(property);
    return this;
  }

  public PropertyDisplay setType(List<TTIriRefExtended> type) {
    this.type = type;
    return this;
  }

  public PropertyDisplay addType(TTIriRefExtended type) {
    if (this.type == null) {
      this.type = new ArrayList<>();
    }
    this.type.add(type);
    return this;
  }

  public PropertyDisplay setCardinality(String cardinality) {
    this.cardinality = cardinality;
    return this;
  }

  public PropertyDisplay setOr(boolean or) {
    isOr = or;
    return this;
  }

  public PropertyDisplay setType(boolean type) {
    isType = type;
    return this;
  }

  public PropertyDisplay setNode(boolean node) {
    isNode = node;
    return this;
  }

  public PropertyDisplay setReverseCardinality(String cardinality) {
    reverseCardinality = cardinality;
    return this;
  }
}
