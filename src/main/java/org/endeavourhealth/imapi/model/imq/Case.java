package org.endeavourhealth.imapi.model.imq;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Case {
  private List<When> when;
  private String else_;

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
  public String getElse() {
    return this.else_;
  }

  @JsonProperty("else")
  public Case setElse(String else_) {
    this.else_ = else_;
    return this;
  }

}
