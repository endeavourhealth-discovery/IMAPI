package org.endeavourhealth.imapi.model.dto

import com.fasterxml.jackson.annotation.JsonIgnore

class CodeGenDto {
  var name: String? = null
    private set
  var extension: String? = null
    private set
  var collectionWrapper: String? = null
    private set
  var datatypeMap: MutableMap<String, String>? = HashMap<String, String>()
    private set
  var template: String? = null
    private set
  var complexTypes: Boolean? = false
    private set

  fun setName(name: String?): CodeGenDto {
    this.name = name
    return this
  }

  fun setExtension(extension: String?): CodeGenDto {
    this.extension = extension
    return this
  }

  fun setCollectionWrapper(collectionWrapper: String?): CodeGenDto {
    this.collectionWrapper = collectionWrapper
    return this
  }

  fun setDatatypeMap(datatypeMap: MutableMap<String, String>?): CodeGenDto {
    this.datatypeMap = datatypeMap
    return this
  }

  @JsonIgnore
  fun getDataType(datatype: String): String? {
    if (datatypeMap == null) return null

    return datatypeMap!![datatype]
  }

  fun setTemplate(template: String?): CodeGenDto {
    this.template = template
    return this
  }

  fun setComplexTypes(complexTypes: Boolean?): CodeGenDto {
    this.complexTypes = complexTypes
    return this
  }

  @JsonIgnore
  fun hasCollectionWrapper(): Boolean {
    return collectionWrapper != null && collectionWrapper!!.isNotEmpty()
  }
}
