package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class BoolQuery extends OsQuery {
  private final List<OsQuery> must = new ArrayList<>();
  private final List<OsQuery> filter = new ArrayList<>();
  private final List<OsQuery> should = new ArrayList<>();
  private final List<OsQuery> mustNot = new ArrayList<>();
  private Integer minimumShouldMatch;

  public BoolQuery must(OsQuery query) {
    must.add(query);
    return this;
  }

  public BoolQuery filter(OsQuery query) {
    filter.add(query);
    return this;
  }

  public BoolQuery should(OsQuery query) {
    should.add(query);
    return this;
  }

  public BoolQuery mustNot(OsQuery query) {
    mustNot.add(query);
    return this;
  }

  public BoolQuery minimumShouldMatch(int minimumShouldMatch) {
    this.minimumShouldMatch = minimumShouldMatch;
    return this;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    ObjectNode body = node.putObject("bool");
    put(body, "must", must);
    put(body, "filter", filter);
    put(body, "should", should);
    put(body, "must_not", mustNot);
    // written as text, as the Elasticsearch builders did; OpenSearch accepts a number or text
    if (minimumShouldMatch != null) body.put("minimum_should_match", String.valueOf(minimumShouldMatch));
    return node;
  }

  private static void put(ObjectNode body, String name, List<OsQuery> clauses) {
    if (clauses.isEmpty()) return;
    ArrayNode array = body.putArray(name);
    clauses.forEach(c -> array.add(c.toJson()));
  }
}
