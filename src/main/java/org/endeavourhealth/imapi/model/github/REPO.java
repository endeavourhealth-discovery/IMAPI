package org.endeavourhealth.imapi.model.github;

import com.fasterxml.jackson.annotation.JsonValue;

public enum REPO {
  IM_DIRECTORY("IMDirectory"),
  IM_QUERY_RUNNER("IMQueryRunner");

  private final String value;

  REPO(String value) {
    this.value = value;
  }

  public static REPO fromValue(String value) {
    for (REPO e : REPO.values()) {
      if (e.value.equals(value)) {
        return e;
      }
    }
    throw new IllegalArgumentException("Invalid value " + value);
  }

  public String getValue() {
    return value;
  }

  @JsonValue
  @Override
  public String toString() {
    return value;
  }
}
