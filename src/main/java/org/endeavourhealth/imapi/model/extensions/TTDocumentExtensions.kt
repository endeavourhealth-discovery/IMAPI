package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.imapi.model.tripletree.TTDocumentJava

fun TTDocumentJava.addPrefix(iri: String, prefix: String): TTDocumentJava {
  this.context.add(iri, prefix)
  return this
}