package org.endeavourhealth.imapi.model

class Namespace {
  var iri: String? = null
    private set
  var prefix: String? = null
    private set
  var name: String? = null
    private set

  fun setIri(iri: String?): Namespace {
    this.iri = iri
    return this
  }

  fun setPrefix(prefix: String?): Namespace {
    this.prefix = prefix
    return this
  }

  fun setName(name: String?): Namespace {
    this.name = name
    return this
  }
}
