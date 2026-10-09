package org.endeavourhealth.imapi.dataaccess.opensearch;

/**
 * Edit distance allowed by a match query.
 */
public enum Fuzziness {
  ZERO("0"),
  ONE("1"),
  TWO("2"),
  AUTO("AUTO");

  private final String value;

  Fuzziness(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }
}
