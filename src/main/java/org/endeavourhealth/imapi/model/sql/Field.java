package org.endeavourhealth.imapi.model.sql;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Field {
  private String field;
  private String type;
  private boolean isFunction;

  public Field(String field, String type, boolean isFunction) {
    this.field = field;
    this.type = type;
    this.isFunction = isFunction;
  }

  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isFunction() {
    return isFunction;
  }

  public void setFunction(boolean function) {
    isFunction = function;
  }
}
