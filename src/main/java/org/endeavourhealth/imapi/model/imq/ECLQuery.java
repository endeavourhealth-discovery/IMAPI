package org.endeavourhealth.imapi.model.imq;

import lombok.Getter;
import lombok.Setter;

public class ECLQuery {
  @Getter
  @Setter
  private String ecl;
  @Getter
  @Setter
  private Query query;
  @Getter
  @Setter
  private boolean showNames;
  @Getter
  @Setter
  private ECLStatus status;

}
