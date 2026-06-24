package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.Concept
import org.endeavourhealth.interfacemanager.model.TTIriRef


fun Concept.setSubClassOf(subClassOf: MutableSet<TTIriRef?>?): Concept? {
  this.subClassOf = subClassOf as List<TTIriRef?>?
  return this
}

fun Concept.getSubClassOf(): MutableSet<TTIriRef?>? {
  return this.subClassOf as MutableSet<TTIriRef?>?
}

@Override
fun Concept.iri(iri: String): Concept {
  this.iri = iri
  return this
}

@Override
fun Concept.name(name: String): Concept {
  this.name = name
  return this
}