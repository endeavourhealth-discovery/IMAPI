package org.endeavourhealth.imapi.errorhandling;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SQLConversionException extends Exception {
  private String sql;

  public SQLConversionException(String message) {
    super(message);
  }

  public SQLConversionException(String message, Throwable cause) {
    super(message,  cause);
  }

  public SQLConversionException(String message, String sql) {
    super(message);
    this.sql = sql;
  }

  public SQLConversionException(String message, String sql, Throwable cause) {
    super(message, cause);
    this.sql = sql;
  }
}
