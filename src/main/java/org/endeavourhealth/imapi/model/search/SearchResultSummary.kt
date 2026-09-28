package org.endeavourhealth.imapi.model.search

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import org.endeavourhealth.imapi.model.tripletree.TTIriRef
import java.util.function.Consumer

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class SearchResultSummary {
  var termCode: MutableSet<SearchTermCode> = hashSetOf()
    private set
  var unit: MutableSet<TTIriRef>? = null
    private set
  var qualifier: MutableList<TTIriRef>? = null
    private set

  @JsonProperty
  var name: String? = null
    private set

  @JsonProperty(value = "iri", required = true)
  var iri: String? = null
    private set

  @JsonProperty
  var code: String? = null
    private set

  @JsonProperty
  var description: String? = null
    private set

  @JsonProperty(required = true)
  var status: TTIriRef? = null
    private set

  @JsonProperty(required = true)
  var scheme: TTIriRef? = null
    private set

  @JsonProperty(required = true)
  var type: MutableSet<TTIriRef>? = hashSetOf()
    private set

  @JsonProperty(defaultValue = "0")
  var usageTotal: Int? = null
    private set

  @JsonProperty
  var bestMatch: String? = null
    private set
  var preferredName: String? = null
    private set
  var key: MutableSet<String>? = null
    private set
  var isA: MutableSet<TTIriRef>? = hashSetOf()
    private set

  constructor(
    name: String?,
    iri: String?,
    code: String?,
    description: String?,
    status: TTIriRef?,
    scheme: TTIriRef?,
    entityTypes: MutableSet<TTIriRef>?,
    isDescendentOf: MutableSet<TTIriRef>?,
    usageTotal: Int?,
    bestMatch: String?
  ) {
    this.name = name
    this.iri = iri
    this.code = code
    this.description = description
    this.status = status
    this.scheme = scheme
    this.type = entityTypes
    this.isA = isDescendentOf
    this.usageTotal = usageTotal
    this.bestMatch = bestMatch
  }

  constructor()

  fun addTermCode(term: String?, code: String?, status: TTIriRef?): SearchResultSummary {
    val tc = SearchTermCode()
    tc.setTerm(term).setCode(code).setStatus(status)
    this.termCode.add(tc)
    return this
  }

  fun setUnit(unit: MutableSet<TTIriRef>?): SearchResultSummary {
    this.unit = unit
    return this
  }

  fun addIntervalUnit(intervalUnit: TTIriRef): SearchResultSummary {
    if (this.unit == null) {
      this.unit = hashSetOf()
    }
    this.unit!!.add(intervalUnit)
    return this
  }

  fun intervalUnit(builder: Consumer<TTIriRef>): SearchResultSummary {
    val intervalUnit = TTIriRef()
    addIntervalUnit(intervalUnit)
    builder.accept(intervalUnit)
    return this
  }

  fun setQualifier(qualifier: MutableList<TTIriRef>?): SearchResultSummary {
    this.qualifier = qualifier
    return this
  }

  fun addQualifier(qualifier: TTIriRef): SearchResultSummary {
    if (this.qualifier == null) {
      this.qualifier = arrayListOf()
    }
    this.qualifier!!.add(qualifier)
    return this
  }

  fun setPreferredName(preferredName: String?): SearchResultSummary {
    this.preferredName = preferredName
    return this
  }

  fun setIsA(isA: MutableSet<TTIriRef>?): SearchResultSummary {
    this.isA = isA
    return this
  }

  fun setName(name: String?): SearchResultSummary {
    this.name = name
    return this
  }

  @JsonSetter("name")
  fun setNameFromJson(name: String?): SearchResultSummary {
    this.name = name
    if (this.bestMatch == null) this.bestMatch = name
    return this
  }

  fun setIri(iri: String?): SearchResultSummary {
    this.iri = iri
    return this
  }

  fun setCode(code: String?): SearchResultSummary {
    this.code = code
    return this
  }

  fun setDescription(description: String?): SearchResultSummary {
    this.description = description
    return this
  }

  @JsonSetter
  fun setStatus(status: TTIriRef?): SearchResultSummary {
    this.status = status
    return this
  }

  @JsonSetter
  fun setScheme(scheme: TTIriRef?): SearchResultSummary {
    this.scheme = scheme
    return this
  }

  fun setType(type: MutableSet<TTIriRef>?): SearchResultSummary {
    this.type = type
    return this
  }

  fun addType(entityType: TTIriRef): SearchResultSummary {
    if (this.type == null) this.type = hashSetOf()
    this.type!!.add(entityType)
    return this
  }

  fun setUsageTotal(usageTotal: Int?): SearchResultSummary {
    this.usageTotal = usageTotal
    return this
  }

  fun setBestMatch(bestMatch: String?): SearchResultSummary {
    this.bestMatch = bestMatch
    return this
  }

  fun setTermCode(searchTermCodes: MutableSet<SearchTermCode>): SearchResultSummary {
    this.termCode = searchTermCodes
    return this
  }

  fun setKey(key: MutableSet<String>?): SearchResultSummary {
    this.key = key
    return this
  }
}
