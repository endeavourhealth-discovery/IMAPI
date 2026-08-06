package org.endeavourhealth.imapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class PropertyDisplay {
  @Getter
  private int order;
  @Getter
  private TTIriRef group;
  @Getter
  private List<TTIriRef> property;
  @Getter
  private List<TTIriRef> type;
  @Getter
  private String cardinality;
  @JsonProperty("isOr")
  private boolean isOr;
  @JsonProperty("isType")
  private boolean typeFlag;
  @JsonProperty("isNode")
  private boolean isNode;
  @Getter
  private String reverseCardinality;

  public PropertyDisplay() {
    property = new ArrayList<>();
    type = new ArrayList<>();
  }

  public PropertyDisplay setOrder(int order) {
    this.order = order;
    return this;
  }

  public PropertyDisplay setGroup(TTIriRef group) {
    this.group = group;
    return this;
  }

  public PropertyDisplay setProperty(List<TTIriRef> property) {
    this.property = property;
    return this;
  }

  public PropertyDisplay addProperty(TTIriRef property) {
    if (this.property == null) {
      this.property = new ArrayList<>();
    }
    this.property.add(property);
    return this;
  }

  public PropertyDisplay addType(TTIriRef type) {
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

  @JsonProperty("isOr")
  public boolean isOr() {
    return isOr;
  }

  public PropertyDisplay setIsOr(boolean or) {
    isOr = or;
    return this;
  }

  @JsonProperty("isType")
  public boolean getTypeFlag() {
    return typeFlag;
  }

  public PropertyDisplay setTypeFlag(boolean type) {
    typeFlag = type;
    return this;
  }

  public PropertyDisplay setType(List<TTIriRef> type) {
    this.type = type;
    return this;
  }

  @JsonProperty("isNode")
  public boolean isNode() {
    return isNode;
  }

  public PropertyDisplay setIsNode(boolean node) {
    isNode = node;
    return this;
  }

  public PropertyDisplay setReverseCardinality(String cardinality) {
    reverseCardinality = cardinality;
    return this;
  }
}
