package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.imq.DisplayMode
import org.endeavourhealth.imapi.model.imq.Query

class QueryDisplayRequest {
  var query: Query? = null
    private set
  var displayMode: DisplayMode? = null
    private set

  constructor(query: Query, displayMode: DisplayMode?) {
    this.query = query
    this.displayMode = displayMode
  }

  constructor() {}

  fun setQuery(query: Query?): QueryDisplayRequest {
    this.query = query
    return this
  }

  fun setDisplayMode(displayMode: DisplayMode?): QueryDisplayRequest {
    this.displayMode = displayMode
    return this
  }
}
