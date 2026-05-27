package org.endeavourhealth.imapi.model.imq;

import org.endeavourhealth.interfacemanager.model.Aggregate;
import org.endeavourhealth.interfacemanager.model.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Having {
  private Aggregate function;
  private String identifier;
  private Range range;
  private Operator operator;
  private String value;
  private List<Having> and;
  private List<Having> or;
  private boolean not;

  public List<Having> getAnd() {
    return and;
  }

  public Having setAnd(List<Having> and) {
    this.and = and;
    return this;
  }

  public List<Having> getOr() {
    return or;
  }

  public Having setOr(List<Having> or) {
    this.or = or;
    return this;
  }

  public boolean isNot() {
    return not;
  }

  public Having setNot(boolean not) {
    this.not = not;
    return this;
  }

  public Having addAnd(Having having) {
    if (this.and == null) {
      this.and = new ArrayList<>();
    }
    this.and.add(having);
    return this;
  }

  public Having addOr(Having having) {
    if (this.or == null) {
      this.or = new ArrayList<>();
    }
    this.or.add(having);
    return this;
  }

  public Having and(Consumer<Having> builder) {
    Having and = new Having();
    addAnd(and);
    builder.accept(and);
    return this;
  }

  public Having or(Consumer<Having> builder) {
    Having or = new Having();
    addOr(or);
    builder.accept(or);
    return this;
  }

  public String getIdentifier() {
    return identifier;
  }

  public Having setIdentifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  public Aggregate getFunction() {
    return function;
  }

  public Having setFunction(Aggregate function) {
    this.function = function;
    return this;
  }

  public Range getRange() {
    return range;
  }

  public Having setRange(Range range) {
    this.range = range;
    return this;
  }

  public Operator getOperator() {
    return operator;
  }

  public Having setOperator(Operator operator) {
    this.operator = operator;
    return this;
  }

  public String getValue() {
    return value;
  }

  public Having setValue(String value) {
    this.value = value;
    return this;
  }
}
