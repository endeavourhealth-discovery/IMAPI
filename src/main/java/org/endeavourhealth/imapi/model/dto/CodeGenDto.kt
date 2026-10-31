package org.endeavourhealth.imapi.model.dto

import com.fasterxml.jackson.annotation.JsonIgnore

class CodeGenDto {
  var name: String? = null
  var extension: String? = null
  var collectionWrapper: String? = null
  var datatypeMap: MutableMap<String, String> = mutableMapOf()
  var template: String? = null
  var complexTypes: Boolean? = false

  @JsonIgnore
  fun getDataType(datatype: String): String? {
    return datatypeMap[datatype]
  }

  @JsonIgnore
  fun hasCollectionWrapper(): Boolean {
    return !collectionWrapper.isNullOrEmpty()
  }
}
