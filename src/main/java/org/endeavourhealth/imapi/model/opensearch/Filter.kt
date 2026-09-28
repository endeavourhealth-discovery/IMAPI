package org.endeavourhealth.imapi.model.opensearch

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName(value = "bool")
class Filter {
  @get:JsonProperty("should")
  var shoulds: MutableList<MatchPhraseId> = arrayListOf()
    private set

  @get:JsonProperty("minimum_should_match")
  var minimum: Int
    private set

  constructor(minimum: Int, should: MutableList<MatchPhraseId>) {
    this.shoulds = should
    this.minimum = minimum
  }

  constructor(minimum: Int) {
    this.shoulds = arrayListOf()
    this.minimum = minimum
  }

  fun setShoulds(shoulds: MutableList<MatchPhraseId>): Filter {
    this.shoulds = shoulds
    return this
  }

  fun addShould(should: MatchPhraseId): Filter {
    this.shoulds.add(should)
    return this
  }

  fun setMinimum(minimum: Int): Filter {
    this.minimum = minimum
    return this
  }
}
