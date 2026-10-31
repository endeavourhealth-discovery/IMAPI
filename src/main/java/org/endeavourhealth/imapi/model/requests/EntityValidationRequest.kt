package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.vocabulary.GRAPH
import org.endeavourhealth.imapi.vocabulary.VALIDATION

class EntityValidationRequest {
  var entity: TTEntity? = null
    private set
  var validationIri: String? = null
    private set
  var graph: GRAPH? = null
    private set

  fun setEntity(entity: TTEntity?): EntityValidationRequest {
    this.entity = entity
    return this
  }

  fun setValidationIri(validationIri: String?): EntityValidationRequest {
    this.validationIri = validationIri
    return this
  }

  fun setValidationIri(validationIri: VALIDATION): EntityValidationRequest {
    this.validationIri = validationIri.toString()
    return this
  }

  fun setGraph(graph: GRAPH?): EntityValidationRequest {
    this.graph = graph
    return this
  }
}
