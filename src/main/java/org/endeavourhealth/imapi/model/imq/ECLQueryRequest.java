package org.endeavourhealth.imapi.model.imq;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ECLQueryRequest {
  private String ecl;
  private Query query;
  private boolean showNames;
  private ECLStatus status;

  public ECLQueryRequest setEcl(String ecl) {
    this.ecl = ecl;
    return this;
  }

  public ECLQueryRequest setQuery(Query query) {
    this.query = query;
    return this;
  }

  public ECLQueryRequest setShowNames(boolean showNames) {
    this.showNames = showNames;
    return this;
  }

  public ECLQueryRequest setStatus(ECLStatus status) {
    this.status = status;
    return this;
  }
}
