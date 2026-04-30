package org.endeavourhealth.imapi.model.postRequestPrimatives;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.endeavourhealth.imapi.model.github.REPO;

@NoArgsConstructor
@Getter
@Setter
public class REPOBody {
  private REPO repo;

  public REPOBody(REPO repo) {
    this.repo = repo;
  }
}
