package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

public class NestedQuery extends OsQuery {
  private final String path;
  private final OsQuery query;
  private final ScoreMode scoreMode;
  private String[] innerHitsSourceIncludes;

  public NestedQuery(String path, OsQuery query, ScoreMode scoreMode) {
    this.path = path;
    this.query = query;
    this.scoreMode = scoreMode;
  }

  /**
   * Return the matching nested documents ("inner hits"), limited to the given source fields.
   */
  public NestedQuery innerHitsSource(String... includes) {
    this.innerHitsSourceIncludes = includes;
    return this;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    ObjectNode body = node.putObject("nested");
    body.put("path", path);
    body.set("query", query.toJson());
    body.put("score_mode", scoreMode.value());
    if (innerHitsSourceIncludes != null) {
      ArrayNode includes = body.putObject("inner_hits").putObject("_source").putArray("includes");
      for (String include : innerHitsSourceIncludes) includes.add(include);
    }
    return node;
  }
}
