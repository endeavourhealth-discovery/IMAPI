package org.endeavourhealth.imapi.model.eclBuilder;

import lombok.Getter;

@Getter
public class BuilderComponent implements BuilderValue {
  private String type;

  public BuilderComponent(String type) {
    this.type = type;
  }

  public BuilderComponent() {
  }

  public BuilderComponent setType(String type) {
    this.type = type;
    return this;
  }
}
