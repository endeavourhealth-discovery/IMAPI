package org.endeavourhealth.imapi.model.postRequestPrimatives;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringBody {
  private String value;
  public StringBody(String value) {
    this.value = value;
  }
}
