package org.endeavourhealth.imapi.model.imq;

import lombok.Getter;
import lombok.Setter;

public class ECLStatus {
  @Getter
  @Setter
  private boolean valid;
  @Getter
  @Setter
  private Integer line;
  @Getter
  @Setter
  private Integer offset;
  @Getter
  @Setter
  private String message;
}
