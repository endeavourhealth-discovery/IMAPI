package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

public class MatchPhrasePrefixQuery extends OsQuery {
  private final String field;
  private final String text;
  private String analyzer;
  private Float boost;
  private Integer slop;

  public MatchPhrasePrefixQuery(String field, String text) {
    this.field = field;
    this.text = text;
  }

  public MatchPhrasePrefixQuery analyzer(String analyzer) {
    this.analyzer = analyzer;
    return this;
  }

  public MatchPhrasePrefixQuery boost(float boost) {
    this.boost = boost;
    return this;
  }

  public MatchPhrasePrefixQuery slop(int slop) {
    this.slop = slop;
    return this;
  }

  @Override
  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    ObjectNode body = node.putObject("match_phrase_prefix").putObject(field);
    body.put("query", text);
    if (analyzer != null) body.put("analyzer", analyzer);
    if (slop != null) body.put("slop", slop);
    if (boost != null) body.put("boost", boost);
    return node;
  }
}
