package org.endeavourhealth.imapi.model.codegen

class CodeGenTemplate {
  var header: String? = ""
    private set
  var footer: String? = ""
    private set
  var property: String? = ""
    private set
  var collectionProperty: String? = ""
    private set

  fun setHeader(header: String?): CodeGenTemplate {
    this.header = header
    return this
  }

  fun setFooter(footer: String?): CodeGenTemplate {
    this.footer = footer
    return this
  }

  fun setProperty(property: String?): CodeGenTemplate {
    this.property = property
    return this
  }

  fun setCollectionProperty(collectionProperty: String?): CodeGenTemplate {
    this.collectionProperty = collectionProperty
    return this
  }
}
