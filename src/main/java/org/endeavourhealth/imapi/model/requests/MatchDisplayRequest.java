package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.interfacemanager.model.GraphVocab;

@NoArgsConstructor
@Getter
public class MatchDisplayRequest {
  private Match match;
  private GraphVocab graph;

  public MatchDisplayRequest(Match match, GraphVocab graph) {
    this.match = match;
    this.graph = graph;
  }

  public MatchDisplayRequest setMatch(Match match) {
    this.match = match;
    return this;
  }

  public MatchDisplayRequest setGraph(GraphVocab graph) {
    this.graph = graph;
    return this;
  }
}
