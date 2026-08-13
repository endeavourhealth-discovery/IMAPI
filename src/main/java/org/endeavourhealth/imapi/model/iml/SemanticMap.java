package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.util.List;

public class SemanticMap extends TTIriRef {
  private String defaultText;
  private Double defaultValue;
  private TTIriRef sourceType;
  private List<SemanticMapEntry> entries;
  private TTIriRef sourceEntityProperty;
  private TTIriRef sourceValueProperty;
  private TTIriRef function;

  public TTIriRef getFunction() {
    return function;
  }
  public SemanticMap setFunction(TTIriRef function) {
    this.function = function;
    return this;
  }

  public TTIriRef getSourceEntityProperty() {
    return sourceEntityProperty;
  }

  public SemanticMap setSourceEntityProperty(TTIriRef sourceEntityProperty) {
    this.sourceEntityProperty = sourceEntityProperty;
    return this;
  }

  public TTIriRef getSourceValueProperty() {
    return sourceValueProperty;
  }

  public SemanticMap setSourceValueProperty(TTIriRef sourceValueProperty) {
    this.sourceValueProperty = sourceValueProperty;
    return this;
  }

  public TTIriRef getSourceType() {
    return sourceType;
  }
  public SemanticMap setSourceType(TTIriRef sourceType) {
    this.sourceType = sourceType;
    return this;
  }
  public String getDefaultText() {
    return defaultText;
  }
  public SemanticMap setDefaultText(String defaultText) {
    this.defaultText = defaultText;
    return this;
  }
  public Double getDefaultValue() {
    return defaultValue;
  }
  public SemanticMap setDefaultValue(Double defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }
  public List<SemanticMapEntry> getEntries() {
    return entries;
  }
  public SemanticMap setEntries(List<SemanticMapEntry> entries) {
    this.entries = entries;
    return this;
  }
  public SemanticMap addEntry(SemanticMapEntry entry) {
    if (this.entries == null) this.entries = new java.util.ArrayList<>();
    this.entries.add(entry);
    return this;
  }
}
