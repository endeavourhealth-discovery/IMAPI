package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Entail {
  descendantsOrSelfOf("<<"),
  memberOf("^"),
  descendantsOf("<"),
  ancestorsOf(">"),
  childOrSelfOf("<<!"),
  childOf("<!"),
  equal("=");

  private final String value;
  @JsonValue
  public String getValue() {
    return this.value;
  }

  Entail(String value) {
    this.value = value;
  }
  public static Entail fromValue(String value) {
    return Arrays.stream(values())
      .filter(e -> e.value.equals(value))
      .findFirst()
      .orElseThrow(() -> new IllegalArgumentException("Unknown Entail: " + value));
  }
}
