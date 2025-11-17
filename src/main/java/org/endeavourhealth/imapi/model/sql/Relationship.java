package org.endeavourhealth.imapi.model.sql;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Relationship {
  private String fromField;
  private String toField;

  public Relationship(String fromField, String toField) {
    this.fromField = fromField;
    this.toField = toField;
  }
}
