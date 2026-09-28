package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import org.endeavourhealth.imapi.vocabulary.GRAPH

class SetDistillationRequest {
  var conceptList: MutableList<TTIriRef>? = null
    private set
  var graph: GRAPH? = null
    private set

  constructor(conceptList: MutableList<TTIriRef>, graph: GRAPH) {
    this.conceptList = conceptList
    this.graph = graph
  }

  constructor() {}

  fun setConceptList(conceptList: MutableList<TTIriRef>?): SetDistillationRequest {
    this.conceptList = conceptList
    return this
  }

  fun addToConceptList(concept: TTIriRef) {
    if (null == conceptList) {
      conceptList = arrayListOf()
    }
    this.conceptList!!.add(concept)
  }

  fun setGraph(graph: GRAPH?): SetDistillationRequest {
    this.graph = graph
    return this
  }
}
