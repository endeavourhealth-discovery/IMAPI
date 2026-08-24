package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"source", "paths", "target"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PathDocument {

  private List<Query> queries;

  public List<Query> getMatch() {
    return queries;
  }

  public PathDocument setMatch(List<Query> queries) {
    this.queries = queries;
    return this;
  }

  public PathDocument addMatch(Query query) {
    if (this.queries == null) this.queries = new ArrayList<>();
    this.queries.add(query);
    return this;
  }
}
