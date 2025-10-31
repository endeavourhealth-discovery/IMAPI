package org.endeavourhealth.imapi.transformation.component;

import org.endeavourhealth.imapi.model.imq.Query;

/**
 * Factory for creating Query builders and related components.
 */
public class QueryBuilderFactory {

  /**
   * Creates an empty Query object.
   *
   * @return A new Query instance
   */
  public static Query createEmptyQuery() {
    return new Query();
  }

  /**
   * Creates a QueryBuilder for a new Query.
   *
   * @return A new QueryBuilder
   */
  public static QueryBuilder createQueryBuilder() {
    return new QueryBuilder();
  }

  /**
   * Creates a QueryBuilder from an existing Query.
   *
   * @param query The query to build from
   * @return A QueryBuilder wrapping the query
   */
  public static QueryBuilder createQueryBuilder(Query query) {
    return new QueryBuilder(query);
  }

  /**
   * Creates a MatchBuilder for query conditions.
   *
   * @return A new MatchBuilder
   */
  public static MatchBuilder createMatchBuilder() {
    return new MatchBuilder();
  }

  /**
   * Creates a PathBuilder for navigation paths.
   *
   * @return A new PathBuilder
   */
  public static PathBuilder createPathBuilder() {
    return new PathBuilder();
  }

  /**
   * Creates a ReturnBuilder for return clauses.
   *
   * @return A new ReturnBuilder
   */
  public static ReturnBuilder createReturnBuilder() {
    return new ReturnBuilder();
  }

  /**
   * Creates a WhereBuilder for where clauses.
   *
   * @return A new WhereBuilder
   */
  public static WhereBuilder createWhereBuilder() {
    return new WhereBuilder();
  }

  /**
   * Creates a NodeBuilder for node objects.
   *
   * @return A new NodeBuilder
   */
  public static NodeBuilder createNodeBuilder() {
    return new NodeBuilder();
  }
}