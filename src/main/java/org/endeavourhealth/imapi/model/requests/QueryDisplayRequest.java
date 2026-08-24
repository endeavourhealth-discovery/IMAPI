package org.endeavourhealth.imapi.model.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.endeavourhealth.imapi.model.imq.DisplayMode;
import org.endeavourhealth.imapi.model.imq.Query;

@NoArgsConstructor
@Getter
public class QueryDisplayRequest {

  private Query query;
  private DisplayMode displayMode;

  public QueryDisplayRequest(Query query, DisplayMode displayMode) {
    this.query = query;
    this.displayMode = displayMode;
  }

  public QueryDisplayRequest setQuery(Query query) {
    this.query = query;
    return this;
  }

  public QueryDisplayRequest setDisplayMode(DisplayMode displayMode) {
    this.displayMode = displayMode;
    return this;
  }
}
