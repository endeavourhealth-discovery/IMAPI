package org.endeavourhealth.imapi.model.opensearch

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "bool")
class Query {
  @get:JsonProperty("must")
  var musts: MutableList<Prefix> = arrayListOf()

  @get:JsonProperty("filter")
  var filters: MutableList<Filter> = arrayListOf()

  constructor(must: MutableList<Prefix>, filter: MutableList<Filter>) {
    this.musts = must
    this.filters = filter
  }

  constructor() {
    this.musts = arrayListOf()
    this.filters = arrayListOf()
  }

  fun addMust(must: String): Query {
    this.musts.add(Prefix(must))
    return this
  }

  fun addFilter(filter: Filter): Query {
    this.filters.add(filter)
    return this
  }
}
