package org.endeavourhealth.imapi.dataaccess.opensearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.utility.SharedObjectMapper;

/**
 * The body of a _search request: the query, paging and which source fields to return.
 */
public class SearchSource {
  private OsQuery query;
  private Integer from;
  private Integer size;
  private String[] sourceIncludes;

  public SearchSource query(OsQuery query) {
    this.query = query;
    return this;
  }

  public SearchSource from(int from) {
    this.from = from;
    return this;
  }

  public SearchSource size(int size) {
    this.size = size;
    return this;
  }

  public SearchSource fetchSource(String[] includes) {
    this.sourceIncludes = includes;
    return this;
  }

  public ObjectNode toJson() {
    ObjectNode node = SharedObjectMapper.INSTANCE.createObjectNode();
    if (from != null) node.put("from", from);
    if (size != null) node.put("size", size);
    if (query != null) node.set("query", query.toJson());
    if (sourceIncludes != null) {
      ArrayNode includes = node.putObject("_source").putArray("includes");
      for (String include : sourceIncludes) includes.add(include);
    }
    return node;
  }

  @Override
  public String toString() {
    try {
      return SharedObjectMapper.INSTANCE.writerWithDefaultPrettyPrinter().writeValueAsString(toJson());
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Unable to write search as JSON", e);
    }
  }
}
