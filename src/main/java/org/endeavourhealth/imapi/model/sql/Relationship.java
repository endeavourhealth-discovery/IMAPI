package org.endeavourhealth.imapi.model.sql;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Relationship {
  private String fromField;
  private String toField;

  public Relationship(String fromField, String toField) {
    this.fromField = fromField;
    this.toField = toField;
  }

  public String getFromField() {
    return fromField;
  }

  public void setFromField(String fromField) {
    this.fromField = fromField;
  }

  public String getToField() {
    return toField;
  }

  public void setToField(String toField) {
    this.toField = toField;
  }
}
