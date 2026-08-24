package org.endeavourhealth.imapi.model.dto

import org.endeavourhealth.imapi.model.tripletree.TTIriRef

class FilterOptionsDto {
  var status: MutableList<TTIriRef>? = null
    private set
  var schemes: MutableList<TTIriRef>? = null
    private set
  var types: MutableList<TTIriRef>? = null
    private set
  var sortFields: MutableList<TTIriRef>? = null
    private set
  var sortDirections: MutableList<TTIriRef>? = null
    private set
  var typeSchemes: MutableMap<String, MutableList<TTIriRef>>? = null
    private set

  fun setStatus(status: MutableList<TTIriRef>?): FilterOptionsDto {
    this.status = status
    return this
  }

  fun setSchemes(schemes: MutableList<TTIriRef>?): FilterOptionsDto {
    this.schemes = schemes
    return this
  }

  fun setTypes(types: MutableList<TTIriRef>?): FilterOptionsDto {
    this.types = types
    return this
  }

  fun setSortFields(sortFields: MutableList<TTIriRef>?): FilterOptionsDto {
    this.sortFields = sortFields
    return this
  }

  fun setSortDirections(sortDirections: MutableList<TTIriRef>?): FilterOptionsDto {
    this.sortDirections = sortDirections
    return this
  }

  fun setTypeSchemes(schemes: MutableMap<String, MutableList<TTIriRef>>?): FilterOptionsDto {
    this.typeSchemes = schemes
    return this
  }
}
