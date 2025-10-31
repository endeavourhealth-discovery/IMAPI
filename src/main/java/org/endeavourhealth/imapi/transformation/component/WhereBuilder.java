package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Where;

/**
 * Fluent builder for constructing IMQ Where clauses.
 * Where clauses define filtering conditions for queries.
 */
public class WhereBuilder {

  private final Where where;

  /**
   * Creates a new WhereBuilder.
   */
  public WhereBuilder() {
    this.where = new Where();
  }

  /**
   * Creates a WhereBuilder from an existing Where.
   *
   * @param where The where clause to build upon
   */
  public WhereBuilder(Where where) {
    this.where = where;
  }

  /**
   * Sets the IRI for the where clause.
   */
  public WhereBuilder withIri(String iri) {
    where.setIri(iri);
    return this;
  }

  /**
   * Sets the type constraint for this where clause.
   */
  public WhereBuilder withTypeOf(String typeOfIri) {
    where.setTypeOf(typeOfIri);
    return this;
  }

  /**
   * Sets the type constraint for this where clause.
   */
  public WhereBuilder withTypeOf(Node typeOfNode) {
    where.setTypeOf(typeOfNode);
    return this;
  }

  /**
   * Adds an AND condition.
   */
  public WhereBuilder addAnd(Where andClause) {
    where.addAnd(andClause);
    return this;
  }

  /**
   * Adds an OR condition.
   */
  public WhereBuilder addOr(Where orClause) {
    where.addOr(orClause);
    return this;
  }

  /**
   * Sets the NOT flag.
   */
  public WhereBuilder not(boolean notFlag) {
    where.setNot(notFlag);
    return this;
  }

  /**
   * Sets the value constraint.
   */
  public WhereBuilder withValue(String value) {
    where.setValue(value);
    return this;
  }

  /**
   * Adds an IS constraint.
   */
  public WhereBuilder addIs(String isIri) {
    where.addIs(isIri);
    return this;
  }

  /**
   * Adds an IS constraint with a Node.
   */
  public WhereBuilder addIs(Node isNode) {
    where.addIs(isNode);
    return this;
  }

  /**
   * Builds and returns the Where object.
   */
  public Where build() {
    return where;
  }

  /**
   * Gets the underlying Where object without building.
   */
  public Where get() {
    return where;
  }
}