package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.imapi.utility.EnumUtils
import org.endeavourhealth.interfacemanager.model.NamespaceVocab
import org.endeavourhealth.interfacemanager.model.TTIriRef
import java.util.regex.Pattern

fun TTIriRef.iri(iri: String): TTIriRef =
  TTIriRef().apply {
    this.iri = normaliseIri(iri)
  }

fun TTIriRef.iri(iri: String, name: String): TTIriRef =
  TTIriRef().apply {
    this.iri = normaliseIri(iri)
    this.name = name
  }

fun TTIriRef.iri(vocabEnum: Enum<*>): TTIriRef =
  iri(EnumUtils.asIri(vocabEnum).iri)

fun TTIriRef.setIri(ref: TTIriRef, iri: String): TTIriRef {
  ref.iri = normaliseIri(iri)
  return ref
}

fun TTIriRef.setIri(ref: TTIriRef, vocabEnum: Enum<*>): TTIriRef =
  setIri(ref, EnumUtils.asIri(vocabEnum).iri)

fun TTIriRef.setName(ref: TTIriRef, name: String): TTIriRef {
  ref.name = name
  return ref
}

fun TTIriRef.setDescription(ref: TTIriRef, description: String): TTIriRef {
  ref.description = description
  return ref
}

fun TTIriRef.hasName(): Boolean =
  name?.isNotEmpty() == true

fun TTIriRef.hasDescription(): Boolean =
  description?.isNotEmpty() == true

private
val iriPattern: Pattern = Pattern.compile("([a-z]+)?[:].*")

fun TTIriRef.normaliseIri(iri: String): String {
  var iriLocal = iri;
  if (iriLocal.isNotEmpty() && !iriPattern.matcher(iriLocal).matches()) {
    iriLocal = NamespaceVocab.IM.toString() + iriLocal
    if (!iriPattern.matcher(iriLocal).matches()) Thread.dumpStack()
  }
  return iriLocal;
}