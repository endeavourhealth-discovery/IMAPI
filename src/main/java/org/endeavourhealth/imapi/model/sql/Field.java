package org.endeavourhealth.imapi.model.sql;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {
  private String field;
  private String type;
  private boolean isFunction;

  public Field() {
  }

  public Field(String field, String type, boolean isFunction) {
    this.field = field;
    this.type = type;
    this.isFunction = isFunction;
  }
}
