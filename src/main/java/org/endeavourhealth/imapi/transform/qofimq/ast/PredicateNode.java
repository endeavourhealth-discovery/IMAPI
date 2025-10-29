package org.endeavourhealth.imapi.transform.qofimq.ast;

import java.util.List;

public class PredicateNode implements AstNode {
  private final String field;
  private final PredicateOp op;
  private final Object value; // single value or range object
  private final List<Object> values; // for IN/NOT_IN
  private final boolean negated;

  public PredicateNode(String field, PredicateOp op, Object value, List<Object> values, boolean negated) {
    this.field = field;
    this.op = op;
    this.value = value;
    this.values = values;
    this.negated = negated;
  }

  public String getField() { return field; }
  public PredicateOp getOp() { return op; }
  public Object getValue() { return value; }
  public List<Object> getValues() { return values; }
  public boolean isNegated() { return negated; }
}
