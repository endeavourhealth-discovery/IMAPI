package org.endeavourhealth.imapi.model.imq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;


@JsonPropertyOrder({"source", "paths", "target"})
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PathDocument {
  private List<Match> match;

  public List<Match> getMatch() {
    return match;
  }

  public PathDocument setMatch(List<Match> match) {
    this.match = match;
    return this;
  }

  public PathDocument addMatch(Match match) {
    if (this.match == null)
      this.match = new ArrayList<>();
    this.match.add(match);
    return this;
  }
}
