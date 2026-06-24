package org.endeavourhealth.imapi.model.extensions

import org.endeavourhealth.interfacemanager.model.EntityDocument
import org.endeavourhealth.interfacemanager.model.SearchTermCode
import org.endeavourhealth.interfacemanager.model.TTIriRef
import java.util.*
import kotlin.math.min


fun EntityDocument.addBinding(path: String, node: String): EntityDocument {
  if (this.binding == null) {
    this.binding = HashSet<String>().toMutableList()
  }
  this.binding?.add(path + " " + node)
  return this
}

fun EntityDocument.addTermCode(term: String?, code: String?, status: TTIriRef?, keyTerm: String?): EntityDocument {
  var keyTerm = keyTerm
  val tc: SearchTermCode = SearchTermCode()
  tc.term(term).code(code).status(status)
  if (term != null) tc.length(term.length)
  if (keyTerm == null) keyTerm = term
  if (keyTerm != null) {
    keyTerm = keyTerm.replace("[ '()\\-_./,]".toRegex(), "").lowercase(Locale.getDefault())
    keyTerm = keyTerm.substring(0, min(keyTerm.length, 30))
    tc.setKeyTerm(keyTerm)
  }
  this.termCode?.add(tc)
  return this
}

fun EntityDocument.setBinding(binding: Set<String>): EntityDocument {
  this.binding = binding.toMutableList()
  return this
}

fun EntityDocument.getBindingAsSet(): Set<String>? {
  return this.binding?.toSet()
}

fun EntityDocument.getTermCodeAsSet(): Set<SearchTermCode>? {
  return this.termCode?.toSet()
}

fun EntityDocument.setTermCode(termCode: Set<SearchTermCode>): EntityDocument {
  this.termCode = termCode.toMutableList()
  return this
}

fun EntityDocument.getIsAAsSet(): Set<TTIriRef>? {
  return this.isA?.toSet()
}

fun EntityDocument.setIsA(isA: Set<TTIriRef>): EntityDocument {
  this.isA = isA.toMutableList()
  return this
}

fun EntityDocument.getMemberOfAsSet(): Set<TTIriRef>? {
  return this.memberOf?.toSet()
}

fun EntityDocument.setMemberOf(memberOf: Set<TTIriRef>): EntityDocument {
  this.memberOf = memberOf.toMutableList()
  return this
}