package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Case {
  private List<When> when;
  private ReturnProperty else_x;

  public List<When> getWhen() {
    return when;
  }

  public Case setWhen(List<When> when) {
    this.when = when;
    return this;
  }

  public Case addWhen(When when) {
    if (this.when == null)
      this.when = new ArrayList<>();
    this.when.add(when);
    return this;
  }

  public Case when(Consumer<When> builder) {
    When when = new When();
    addWhen(when);
    builder.accept(when);
    return this;
  }

  @JsonProperty("else")
  public ReturnProperty getElse() {
    return else_x;
  }

  @JsonProperty("else")
  public Case setElse(ReturnProperty else_x) {
    this.else_x = else_x;
    return this;
  }

  public Case else_x(Consumer<ReturnProperty> builder) {
    this.else_x = new ReturnProperty();
    builder.accept(this.else_x);
    return this;
  }
}
