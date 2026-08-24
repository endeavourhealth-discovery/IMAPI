package org.endeavourhealth.imapi.model.iml;

import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.Return;

public class MatchMap {
  private String baseType;
  private Query query;
  private Return returx;

  public Return getReturn() {
    return returx;
  }
  public MatchMap setReturn(Return returx) {
    this.returx = returx;
    return this;
  }
  public Query getMatch() {
    return query;
  }

  public String getBaseType() {
    return baseType;
  }
  public MatchMap setBaseType(String baseType) {
    this.baseType = baseType;
    return this;
  }

  public MatchMap setMatch(Query query) {
    this.query = query;
    return this;
  }


}
