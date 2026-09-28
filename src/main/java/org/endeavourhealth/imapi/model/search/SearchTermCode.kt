package org.endeavourhealth.imapi.model.search

import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.util.*
import java.util.function.Function

class SearchTermCode : Comparable<SearchTermCode> {
  var term: String? = null
    private set
  var code: String? = null
    private set
  var status: TTIriRef? = null
    private set
  var length: Int? = null
    private set
  var keyTerm: String? = null
    private set

  fun setLength(length: Int?): SearchTermCode {
    this.length = length
    return this
  }

  fun setKeyTerm(keyTerm: String?): SearchTermCode {
    this.keyTerm = keyTerm
    return this
  }

  fun setTerm(term: String?): SearchTermCode {
    this.term = term
    return this
  }

  fun setCode(code: String?): SearchTermCode {
    this.code = code
    return this
  }

  @JsonSetter
  fun setStatus(status: TTIriRef?): SearchTermCode {
    this.status = status
    return this
  }

  override fun equals(o: Any?): Boolean {
    if (o !is SearchTermCode) return false
    return this.term == o.term && this.code == o.code && this.status == o.status
  }

  override fun hashCode(): Int {
    return Objects.hash(this.term, this.code, this.status)
  }

  override fun compareTo(o: SearchTermCode): Int {
    return Comparator.comparing<SearchTermCode?, String?>(
      Function { ts: SearchTermCode? -> if (ts!!.status == null) null else ts.status!!.getIri() },
      Comparator.nullsLast<String?>(Comparator.naturalOrder<String?>())
    )
      .thenComparing<String?>(
        Function { ts: SearchTermCode? -> if (ts!!.term == null || ts.term!!.isEmpty()) null else ts.term },
        Comparator.nullsLast<String?>(Comparator.naturalOrder<String?>())
      )
      .compare(this, o)
  }
}
