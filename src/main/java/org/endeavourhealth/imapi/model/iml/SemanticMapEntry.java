package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class SemanticMapEntry extends TTIriRef {
  private TTIriRef sourceEntity;

  private Double rangeFrom;
  private Double rangeTo;
  private String targetText;
  private Double targetValue;
  private Integer order;

  public Integer getOrder() {
    return order;
  }

  public SemanticMapEntry setOrder(Integer order) {
    this.order = order;
    return this;
  }
  public TTIriRef getSourceEntity() {
    return sourceEntity;
  }

  public SemanticMapEntry setSourceEntity(TTIriRef sourceEntity) {
    this.sourceEntity = sourceEntity;
    return this;
  }

  public Double getRangeFrom() {
    return rangeFrom;
  }

  public SemanticMapEntry setRangeFrom(Double rangeFrom) {
    this.rangeFrom = rangeFrom;
    return this;
  }

  public Double getRangeTo() {
    return rangeTo;
  }

  public SemanticMapEntry setRangeTo(Double rangeTo) {
    this.rangeTo = rangeTo;
    return this;
  }

  public String getTargetText() {
    return targetText;
  }

  public SemanticMapEntry setTargetText(String targetText) {
    this.targetText = targetText;
    return this;
  }

  public Double getTargetValue() {
    return targetValue;
  }

  public SemanticMapEntry setTargetValue(Double targetValue) {
    this.targetValue = targetValue;
    return this;
  }
}
