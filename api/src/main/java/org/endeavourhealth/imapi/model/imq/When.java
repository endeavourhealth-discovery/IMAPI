package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.function.Consumer;

public class When {
  private Where where;
  private String then;
  @JsonProperty("case")
  private Case case_;

  @JsonProperty("case")
  public Case getCase_() {
    return case_;
  }

  @JsonProperty("case")
  public When setCase_(Case case_) {
    this.case_ = case_;
    return this;
  }

  public When case_(Consumer<Case> builder) {
    Case case_ = new Case();
    this.case_ = case_;
    builder.accept(case_);
    return this;
  }

  public String getThen() {
    return then;
  }

  public When setThen(String then) {
    this.then = then;
    return this;
  }

  public Where getWhere() {
    return where;
  }

  public When setWhere(Where where) {
    this.where = where;
    return this;
  }

  public When where(Consumer<Where> builder) {
    this.where = new Where();
    builder.accept(this.where);
    return this;
  }


}
