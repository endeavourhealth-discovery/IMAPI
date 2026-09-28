package org.endeavourhealth.imapi.model

class ConceptContextMap(
  var id: String? = null,
  var node: String? = null,
  var value: String? = null,
  var regex: String? = null,
  var property: String? = null,
  var context: MutableList<Context>? = null
)
