package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.interfacemanager.model.GRAPH;

@NoArgsConstructor
@Getter
public class MatchDisplayRequest {
  private Match match;
  private GRAPH graph;

  public MatchDisplayRequest(Match match, GRAPH graph) {
    this.match = match;
    this.graph = graph;
  }

  public MatchDisplayRequest setMatch(Match match) {
    this.match = match;
    return this;
  }

  public MatchDisplayRequest setGraph(GRAPH graph) {
    this.graph = graph;
    return this;
  }
}
