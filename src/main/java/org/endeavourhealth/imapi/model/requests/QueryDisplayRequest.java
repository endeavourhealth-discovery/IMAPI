package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.DisplayMode;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@NoArgsConstructor
@Getter
public class QueryDisplayRequest {
  public Query query;
  public DisplayMode displayMode;
  public TTIriRef graph;

  public QueryDisplayRequest(Query query, DisplayMode displayMode, TTIriRef graph) {
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

  public QueryDisplayRequest setGraph(TTIriRef graph) {
    this.graph = graph;
    return this;
  }
}
