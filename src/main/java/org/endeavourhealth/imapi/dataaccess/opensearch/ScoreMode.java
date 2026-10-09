package org.endeavourhealth.imapi.dataaccess.opensearch;

/**
 * How the scores of matching nested documents are combined.
 */
public enum ScoreMode {
  AVG("avg"),
  MAX("max"),
  MIN("min"),
  NONE("none"),
  SUM("sum");

  private final String value;

  ScoreMode(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
