package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.interfacemanager.model.DisplayMode;

@NoArgsConstructor
@Getter
public class QueryDisplayRequest {
  public Query query;
  public DisplayMode displayMode;
  public TTIriRefExtended graph;

  public QueryDisplayRequest(Query query, DisplayMode displayMode, TTIriRefExtended graph) {
    this.query = query;
    this.displayMode = displayMode;
    this.graph = graph;
  }

  public QueryDisplayRequest setQuery(Query query) {
    this.query = query;
    return this;
  }

  public QueryDisplayRequest setDisplayMode(DisplayMode displayMode) {
    this.displayMode = displayMode;
    return this;
  }

  public QueryDisplayRequest setGraph(TTIriRefExtended graph) {
    this.graph = graph;
    return this;
  }
}
