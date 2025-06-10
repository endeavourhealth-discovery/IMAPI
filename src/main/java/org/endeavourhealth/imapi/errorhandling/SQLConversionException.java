package org.endeavourhealth.imapi.errorhandling;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SQLConversionException extends Throwable {
  private String message;
  private String sql;

  public SQLConversionException(String message) {
    this.message = message;
  }

  public SQLConversionException(String message, String sql) {
    this.message = message;
    this.sql = sql;
  }
}
