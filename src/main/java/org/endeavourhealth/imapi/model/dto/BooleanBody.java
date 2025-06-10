package org.endeavourhealth.imapi.model.dto;

import lombok.Getter;

@Getter
public class BooleanBody {
  Boolean bool;

  public BooleanBody setBool(Boolean bool) {
    this.bool = bool;
    return this;
  }
}
