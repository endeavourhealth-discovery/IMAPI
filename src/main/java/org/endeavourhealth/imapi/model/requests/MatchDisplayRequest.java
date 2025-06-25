package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.Match;

@NoArgsConstructor
@Getter
public class MatchDisplayRequest {
  private Match match;
  private String graph;

  public MatchDisplayRequest(Match match, String graph) {
    this.match = match;
    this.graph = graph;
  }

  public MatchDisplayRequest setMatch(Match match) {
    this.match = match;
    return this;
  }

  public MatchDisplayRequest setGraph(String graph) {
    this.graph = graph;
    return this;
  }
}
