package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.imq.Query
import org.endeavourhealth.imapi.vocabulary.GRAPH

class MatchDisplayRequest {
  var query: Query? = null
    private set
  var graph: GRAPH? = null
    private set

  constructor(query: Query, graph: GRAPH?) {
    this.query = query
    this.graph = graph
  }

  constructor() {}

  fun setQuery(query: Query?): MatchDisplayRequest {
    this.query = query
    return this
  }

  fun setGraph(graph: GRAPH?): MatchDisplayRequest {
    this.graph = graph
    return this
  }
}
