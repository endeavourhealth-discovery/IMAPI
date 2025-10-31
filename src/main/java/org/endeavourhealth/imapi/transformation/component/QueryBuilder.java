package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Fluent builder for constructing IMQ Query objects.
 * Simplifies the creation of complex Query structures with a chainable API.
 */
public class QueryBuilder {

  private final Query query;

  /**
   * Creates a new QueryBuilder.
   */
  public QueryBuilder() {
    this.query = new Query();
  }

  /**
   * Creates a QueryBuilder from an existing Query.
   *
   * @param query The query to build upon
   */
  public QueryBuilder(Query query) {
    this.query = query;
  }

  /**
   * Sets the query name.
   */
  public QueryBuilder withName(String name) {
    query.setName(name);
    return this;
  }

  /**
   * Sets the query description.
   */
  public QueryBuilder withDescription(String description) {
    query.setDescription(description);
    return this;
  }

  /**
   * Sets the query IRI.
   */
  public QueryBuilder withIri(String iri) {
    query.setIri(iri);
    return this;
  }

  /**
   * Sets active-only filtering.
   */
  public QueryBuilder withActiveOnly(boolean activeOnly) {
    query.setActiveOnly(activeOnly);
    return this;
  }

  /**
   * Adds a prefix to the query.
   */
  public QueryBuilder addPrefix(String prefix, String namespace) {
    query.addPrefix(prefix, namespace);
    return this;
  }

  /**
   * Adds a match condition using AND logic.
   */
  public QueryBuilder addAnd(Match match) {
    query.addAnd(match);
    return this;
  }

  /**
   * Adds a match condition using AND logic with builder.
   */
  public QueryBuilder and(Consumer<Match> builder) {
    Match match = new Match();
    query.addAnd(match);
    builder.accept(match);
    return this;
  }

  /**
   * Adds a match condition using OR logic.
   */
  public QueryBuilder addOr(Match match) {
    query.addOr(match);
    return this;
  }

  /**
   * Adds a match condition using OR logic with builder.
   */
  public QueryBuilder or(Consumer<Match> builder) {
    Match match = new Match();
    query.addOr(match);
    builder.accept(match);
    return this;
  }

  /**
   * Adds a match condition using NOT logic.
   */
  public QueryBuilder addNot(Match match) {
    query.addNot(match);
    return this;
  }

  /**
   * Adds a match condition using NOT logic with builder.
   */
  public QueryBuilder not(Consumer<Match> builder) {
    Match match = new Match();
    query.addNot(match);
    builder.accept(match);
    return this;
  }

  /**
   * Adds a path to the query.
   */
  public QueryBuilder addPath(Path path) {
    query.addPath(path);
    return this;
  }

  /**
   * Adds a path using builder.
   */
  public QueryBuilder path(Consumer<Path> builder) {
    Path path = new Path();
    query.addPath(path);
    builder.accept(path);
    return this;
  }

  /**
   * Adds a column group.
   */
  public QueryBuilder addColumnGroup(Match match) {
    query.addColumnGroup(match);
    return this;
  }

  /**
   * Adds a group-by clause.
   */
  public QueryBuilder addGroupBy(GroupBy groupBy) {
    query.addGroupBy(groupBy);
    return this;
  }

  /**
   * Sets the return clause.
   */
  public QueryBuilder withReturn(Consumer<Return> builder) {
    query.return_(builder);
    return this;
  }

  /**
   * Builds and returns the Query object.
   */
  public Query build() {
    return query;
  }

  /**
   * Gets the underlying Query object without building.
   */
  public Query get() {
    return query;
  }
}