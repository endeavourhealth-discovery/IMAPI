package org.endeavourhealth.imapi.model.imq;

import com.ibm.icu.text.PluralRules;

public class Having {
  private Aggregate aggregate;
  private Range range;
  private Operator operator;
  private String value;

  public Aggregate getAggregate() {
    return aggregate;
  }

  public Having setAggregate(Aggregate aggregate) {
    this.aggregate = aggregate;
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
