package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum Operator {
  // This will call enum constructor with one
  // String argument
  eq("=","="),
  gte(">=","greater or equal to"),
  gt(">","greater than"),
  lte("<=","less than or equal to"),
  lt("<","less than"),
  start("startsWith","starts with"),
  isTrue("isTrue","is true"),
  contains("contains","contains"),;

  // declaring private variable for getting values
  private final String value;
  @Getter
  private final String description;


  @JsonValue
  public String getValue() {
    return this.value;
  }

  public static Optional<Operator> get(String val) {
    return Arrays.stream(Operator.values())
      .filter(op -> op.value.equals(val))
      .findFirst();
  }

  // enum constructor - cannot be public or protected
  Operator(String value,String description ) {
    this.value = value;
    this.description= description;
  }
}
