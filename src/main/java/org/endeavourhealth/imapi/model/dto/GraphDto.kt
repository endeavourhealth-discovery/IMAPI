package org.endeavourhealth.imapi.model.dto

class GraphDto {
  enum class GraphType {
    NONE,
    WRAPPER,
    PROPERTIES,
    SUBTYPE,
    ISA
  }

  var key: String? = null
    private set
  var type: GraphType? = null
    private set
  var name: String? = null
    private set
  var iri: String? = null
    private set
  var propertyType: String? = null
    private set
  var valueTypeIri: String? = null
    private set
  var valueTypeName: String? = null
    private set

  var inheritedFromIri: String? = null
    private set
  var inheritedFromName: String? = null
    private set
  var min: String? = null
    private set
  var max: String? = null
    private set

  var children: MutableList<GraphDto> = ArrayList()
    private set

  var leafNodes: MutableList<GraphDto> = ArrayList()
    private set

  constructor() {}

  constructor(
    iri: String?,
    name: String?,
    valueTypeIri: String?,
    valueTypeName: String?,
    inheritedFromIri: String?,
    inheritedFromName: String?
  ) {
    this.name = name
    this.iri = iri
    this.valueTypeIri = valueTypeIri
    this.valueTypeName = valueTypeName
    this.inheritedFromIri = inheritedFromIri
    this.inheritedFromName = inheritedFromName
  }

  constructor(iri: String?, name: String?, valueTypeIri: String?, valueTypeName: String?) {
    this.name = name
    this.iri = iri
    this.valueTypeIri = valueTypeIri
    this.valueTypeName = valueTypeName
  }

  fun setType(type: GraphType?): GraphDto {
    this.type = type
    return this
  }

  fun setKey(key: String?): GraphDto {
    this.key = key
    return this
  }

  fun setMin(min: String?): GraphDto {
    this.min = min
    return this
  }

  fun setMax(max: String?): GraphDto {
    this.max = max
    return this
  }

  fun setValueTypeIri(valueTypeIri: String?): GraphDto {
    this.valueTypeIri = valueTypeIri
    return this
  }

  fun setValueTypeName(valueTypeName: String?): GraphDto {
    this.valueTypeName = valueTypeName
    return this
  }

  fun setName(name: String?): GraphDto {
    this.name = name
    return this
  }

  fun setIri(iri: String?): GraphDto {
    this.iri = iri
    return this
  }

  fun setPropertyType(propertyType: String?): GraphDto {
    this.propertyType = propertyType
    return this
  }

  fun setInheritedFromIri(inheritedFromIri: String?): GraphDto {
    this.inheritedFromIri = inheritedFromIri
    return this
  }

  fun setInheritedFromName(inheritedFromName: String?): GraphDto {
    this.inheritedFromName = inheritedFromName
    return this
  }

  fun setChildren(children: MutableList<GraphDto>): GraphDto {
    this.children = children
    return this
  }
}
