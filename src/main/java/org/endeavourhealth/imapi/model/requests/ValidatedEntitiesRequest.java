package org.endeavourhealth.imapi.model.requests;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ValidatedEntitiesRequest {

  private List<String> snomedCodes;

  public ValidatedEntitiesRequest setSnomedCodes(List<String> snomedCodes) {
    this.snomedCodes = snomedCodes;
    return this;
  }

  public void addToSnomedCodes(String code) {
    if (null == snomedCodes) {
      snomedCodes = new ArrayList<>();
    }
    this.snomedCodes.add(code);
  }
}
