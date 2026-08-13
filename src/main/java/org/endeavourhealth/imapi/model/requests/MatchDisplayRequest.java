package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.vocabulary.GRAPH;

@NoArgsConstructor
@Getter
public class MatchDisplayRequest {

  private Query query;
  private GRAPH graph;

  public MatchDisplayRequest(Query query, GRAPH graph) {
    this.query = query;
    this.graph = graph;
  }

  public MatchDisplayRequest setQuery(Query query) {
    this.query = query;
    return this;
  }

  public MatchDisplayRequest setGraph(GRAPH graph) {
    this.graph = graph;
    return this;
  }
}
