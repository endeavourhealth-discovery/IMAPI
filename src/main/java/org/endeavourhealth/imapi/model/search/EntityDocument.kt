package org.endeavourhealth.imapi.model.search

import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.util.*
import kotlin.math.min

class EntityDocument {
  var id: Int? = null
    private set
  var iri: String? = null
    private set
  var name: String? = null
    private set
  var length: Int? = null
    private set
  var preferredName: String? = null
    private set
  var code: String? = null
    private set
  var alternativeCode: String? = null
    private set
  var scheme: TTIriRef? = null
    private set
  var type: MutableSet<TTIriRef> = hashSetOf()
    private set
  var status: TTIriRef? = null
    private set
  var termCode: MutableSet<SearchTermCode> = hashSetOf()
    private set
  var usageTotal: Int? = null
    private set
  var match: String? = null
    private set
  var isA: MutableSet<TTIriRef> = hashSetOf()
    private set
  var memberOf: MutableSet<TTIriRef> = hashSetOf()
    private set
  var subsumptionCount: Int? = null
    private set
  var binding: MutableSet<String>? = null
    private set
  var isDescendentOf: MutableList<TTIriRef> = arrayListOf()
    private set

  fun setBinding(binding: MutableSet<String>?): EntityDocument {
    this.binding = binding
    return this
  }

  fun addBinding(path: String, node: String): EntityDocument {
    if (this.binding == null) {
      this.binding = hashSetOf()
    }
    this.binding!!.add("$path $node")
    return this
  }

  fun setAlternativeCode(alternativeCode: String?): EntityDocument {
    this.alternativeCode = alternativeCode
    return this
  }

  fun setSubsumptionCount(subsumptionCount: Int?): EntityDocument {
    this.subsumptionCount = subsumptionCount
    return this
  }

  fun setLength(length: Int?): EntityDocument {
    this.length = length
    return this
  }

  fun setPreferredName(preferredName: String?): EntityDocument {
    this.preferredName = preferredName
    return this
  }

  fun setIsA(isA: MutableSet<TTIriRef>): EntityDocument {
    this.isA = isA
    return this
  }

  fun setId(id: Int?): EntityDocument {
    this.id = id
    return this
  }

  fun setIri(iri: String?): EntityDocument {
    this.iri = iri
    return this
  }

  fun setName(name: String?): EntityDocument {
    this.name = name
    return this
  }

  fun setCode(code: String?): EntityDocument {
    this.code = code
    return this
  }

  fun setScheme(scheme: TTIriRef?): EntityDocument {
    this.scheme = scheme
    return this
  }

  fun setStatus(status: TTIriRef?): EntityDocument {
    this.status = status
    return this
  }

  fun addType(type: TTIriRef): EntityDocument {
    this.type.add(type)
    return this
  }

  fun setUsageTotal(usageTotal: Int?): EntityDocument {
    this.usageTotal = usageTotal
    return this
  }

  fun setIsDescendentOf(isDescendentOf: MutableList<TTIriRef>): EntityDocument {
    this.isDescendentOf = isDescendentOf
    return this
  }

  fun setType(type: MutableSet<TTIriRef>): EntityDocument {
    this.type = type
    return this
  }

  fun setTermCode(searchTermCode: MutableSet<SearchTermCode>): EntityDocument {
    this.termCode = searchTermCode
    return this
  }

  fun addTermCode(term: String?, code: String?, status: TTIriRef?, keyTerm: String?): EntityDocument {
    var keyTerm = keyTerm
    val tc = SearchTermCode()
    tc.setTerm(term).setCode(code).setStatus(status)
    if (term != null) tc.setLength(term.length)
    if (keyTerm == null) keyTerm = term
    if (keyTerm != null) {
      keyTerm = keyTerm.replace("[ '()\\-_./,]".toRegex(), "").lowercase(Locale.getDefault())
      keyTerm = keyTerm.substring(0, min(keyTerm.length, 30))
      tc.setKeyTerm(keyTerm)
    }
    this.termCode.add(tc)
    return this
  }

  fun setMemberOf(memberOf: MutableSet<TTIriRef>): EntityDocument {
    this.memberOf = memberOf
    return this
  }

  fun setMatch(match: String?): EntityDocument {
    this.match = match
    return this
  }
}
