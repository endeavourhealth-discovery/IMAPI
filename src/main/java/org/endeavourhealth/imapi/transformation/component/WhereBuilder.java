package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Where;

import java.util.ArrayList;
import java.util.List;

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
   * Adds a match condition.
   */
  public WhereBuilder addMatch(Match match) {
    if (where.getRule() == null) {
      where.setRule(new ArrayList<>());
    }
    where.getRule().add(match);
    return this;
  }

  /**
   * Adds an AND condition.
   */
  public WhereBuilder addAnd(Match match) {
    if (where.getAnd() == null) {
      where.setAnd(new ArrayList<>());
    }
    where.getAnd().add(match);
    return this;
  }

  /**
   * Adds an OR condition.
   */
  public WhereBuilder addOr(Match match) {
    if (where.getOr() == null) {
      where.setOr(new ArrayList<>());
    }
    where.getOr().add(match);
    return this;
  }

  /**
   * Adds a NOT condition.
   */
  public WhereBuilder addNot(Match match) {
    if (where.getNot() == null) {
      where.setNot(new ArrayList<>());
    }
    where.getNot().add(match);
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