package org.endeavourhealth.imapi.model.sql;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Field {
  private String field;
  private String type;
  private boolean isFunction;

  public Field(String field, String type, boolean isFunction) {
    this.field = field;
    this.type = type;
    this.isFunction = isFunction;
  }
}
