package org.endeavourhealth.imapi.model.sql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relationship {
  private String fromField;
  private String toField;
}
