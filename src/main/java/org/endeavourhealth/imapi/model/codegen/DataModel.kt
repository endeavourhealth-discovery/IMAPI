package org.endeavourhealth.imapi.model.codegen


class DataModel {
  var iri: String? = null
    private set
  var name: String? = null
    private set
  var comment: String? = null
    private set

  var properties: MutableList<DataModelProperty> = ArrayList<DataModelProperty>()
    private set

  fun setIri(iri: String?): DataModel {
    this.iri = iri
    return this
  }

  fun setName(name: String?): DataModel {
    this.name = name
    return this
  }

  fun setComment(comment: String?): DataModel {
    this.comment = comment
    return this
  }

  fun setProperties(properties: MutableList<DataModelProperty>): DataModel {
    this.properties = properties
    return this
  }

  val propertyNames: MutableSet<String?>
    get() {
      val propertyNames: MutableSet<String?> = HashSet<String?>()
      for (p in properties) propertyNames.add(p.name)
      return propertyNames
    }

  fun addProperty(property: DataModelProperty): DataModel {
    if (!this.propertyNames.contains(property.name)) this.properties.add(property)
    return this
  }
}
