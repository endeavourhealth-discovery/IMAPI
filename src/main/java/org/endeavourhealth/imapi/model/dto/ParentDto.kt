package org.endeavourhealth.imapi.model.dto

import org.endeavourhealth.imapi.model.tripletree.TTIriRef

class ParentDto : TTIriRef {
  var parents: MutableList<ParentDto>? = null
    private set

  constructor()

  constructor(iri: String?, name: String?, parents: MutableList<ParentDto>?) : super(iri, name) {
    this.parents = parents
  }

  fun setParents(parents: MutableList<ParentDto>?): ParentDto {
    this.parents = parents
    return this
  }

  fun hasMultipleParents(): Boolean {
    return parents != null && parents!!.size > 1
  }

  fun hasSingleParent(): Boolean {
    return parents != null && parents!!.size == 1
  }
}
