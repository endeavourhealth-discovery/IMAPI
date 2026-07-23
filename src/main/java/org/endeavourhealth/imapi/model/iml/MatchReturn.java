package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Return;

public class MatchReturn {
  private Match match;
  private Return returx;
  public Match getMatch() {
    return match;
  }

  public MatchReturn setMatch(Match match) {
    this.match = match;
    return this;
  }

  public Return getReturn() {
    return returx;
  }
  public MatchReturn setReturn(Return returx) {
    this.returx = returx;
    return this;
  }


}
