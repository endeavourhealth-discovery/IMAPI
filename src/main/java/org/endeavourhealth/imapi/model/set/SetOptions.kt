package org.endeavourhealth.imapi.model.set

class SetOptions {
  var setIri: String? = null
    private set
  var includeDefinition = false
    private set
  var includeCore = false
    private set
  var includeLegacy = false
    private set
  var includeSubsets = false
    private set
  var schemes: MutableList<String>? = null
    private set
  var isIncludeIM1id: Boolean = false
    private set
  var subsumptions: MutableList<String>? = null
    private set

  constructor(
    setIri: String?,
    includeDefinition: Boolean,
    includeCore: Boolean,
    includeLegacy: Boolean,
    includeSubsets: Boolean,
    schemes: MutableList<String>?,
    subsumptions: MutableList<String>?
  ) {
    this.setIri = setIri
    this.includeDefinition = includeDefinition
    this.includeCore = includeCore
    this.includeLegacy = includeLegacy
    this.includeSubsets = includeSubsets
    this.schemes = schemes
    this.subsumptions = subsumptions
  }

  fun setSetIri(setIri: String?): SetOptions {
    this.setIri = setIri
    return this
  }

  fun setSchemes(schemes: MutableList<String>?): SetOptions {
    this.schemes = schemes
    return this
  }

  fun setSubsumptions(subsumptions: MutableList<String>?): SetOptions {
    this.subsumptions = subsumptions
    return this
  }

  fun includeDefinition(): Boolean {
    return includeDefinition
  }

  fun includeCore(): Boolean {
    return includeCore
  }

  fun includeLegacy(): Boolean {
    return includeLegacy
  }

  fun includeSubsets(): Boolean {
    return includeSubsets
  }

  fun setIncludeDefinition(includeDefinition: Boolean): SetOptions {
    this.includeDefinition = includeDefinition
    return this
  }

  fun setIncludeCore(includeCore: Boolean): SetOptions {
    this.includeCore = includeCore
    return this
  }

  fun setIncludeLegacy(includeLegacy: Boolean): SetOptions {
    this.includeLegacy = includeLegacy
    return this
  }

  fun setIncludeSubsets(includeSubsets: Boolean): SetOptions {
    this.includeSubsets = includeSubsets
    return this
  }

  fun setIncludeIM1id(includeIM1id: Boolean): SetOptions {
    this.isIncludeIM1id = includeIM1id
    return this
  }
}
