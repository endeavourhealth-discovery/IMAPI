package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.vocabulary.types.Graph;

@NoArgsConstructor
@Getter
public class MatchDisplayRequest {
  private Match match;
  private Graph graph;

  public MatchDisplayRequest(Match match, Graph graph) {
    this.match = match;
    this.graph = graph;
  }

  public MatchDisplayRequest setMatch(Match match) {
    this.match = match;
    return this;
  }

  public MatchDisplayRequest setGraph(Graph graph) {
    this.graph = graph;
    return this;
  }
}
