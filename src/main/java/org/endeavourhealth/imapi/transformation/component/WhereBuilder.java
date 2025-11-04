package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.model.imq.Operator;

/**
 * Fluent builder for constructing IMQuery WHERE conditions.
 * Simplifies creation of WHERE clauses in rules.
 */
public class WhereBuilder {
  private final Where where;

  public WhereBuilder() {
    this.where = new Where();
  }

  public WhereBuilder(String iri) {
    this.where = new Where();
    this.where.setIri(iri);
  }

  public WhereBuilder iri(String iri) {
    where.setIri(iri);
    return this;
  }

  public WhereBuilder value(Object value) {
    where.setValue(value.toString());
    return this;
  }

  public WhereBuilder operator(Operator operator) {
    where.setOperator(operator);
    return this;
  }

  public WhereBuilder operatorSymbol(String symbol) {
    Operator op = mapOperatorSymbol(symbol);
    where.setOperator(op);
    return this;
  }

  public WhereBuilder isNull() {
    where.setIsNull(true);
    return this;
  }

  public WhereBuilder isNotNull() {
    where.setIsNotNull(true);
    return this;
  }

  public WhereBuilder eq(Object value) {
    where.setOperator(Operator.eq);
    where.setValue(value.toString());
    return this;
  }

  public WhereBuilder notEquals(Object value) {
    where.setOperator(Operator.eq);
    where.setNot(true);
    where.setValue(value.toString());
    return this;
  }

  public WhereBuilder lessThan(Object value) {
    where.setOperator(Operator.lt);
    where.setValue(value.toString());
    return this;
  }

  public WhereBuilder greaterThan(Object value) {
    where.setOperator(Operator.gt);
    where.setValue(value.toString());
    return this;
  }

  public WhereBuilder lessOrEqual(Object value) {
    where.setOperator(Operator.lte);
    where.setValue(value.toString());
    return this;
  }

  public WhereBuilder greaterOrEqual(Object value) {
    where.setOperator(Operator.gte);
    where.setValue(value.toString());
    return this;
  }

  public Where build() {
    return where;
  }

  private Operator mapOperatorSymbol(String symbol) {
    return switch (symbol) {
      case "=", "!=" , "≠" -> Operator.eq; // Not equals handled via setNot(true)
      case "<" -> Operator.lt;
      case ">" -> Operator.gt;
      case "<=" -> Operator.lte;
      case ">=" -> Operator.gte;
      default -> Operator.eq;
    };
  }
}