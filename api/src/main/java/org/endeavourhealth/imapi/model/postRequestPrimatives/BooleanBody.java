package org.endeavourhealth.imapi.model.postRequestPrimatives;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooleanBody {
  private Boolean value;
  public BooleanBody(Boolean value) {
    this.value = value;
  }
}
