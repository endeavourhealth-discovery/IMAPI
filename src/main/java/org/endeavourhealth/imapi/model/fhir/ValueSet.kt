package org.endeavourhealth.imapi.model.fhir

class ValueSet {
  var resourceType: String? = null
    private set
  var id: String? = null
    private set
  var meta: Meta? = null
    private set
  var text: Text? = null
    private set
  var extension: MutableList<MutableMap<String, Any>>? = mutableListOf()
    private set
  var url: String? = null
    private set
  var identifier: MutableList<Identifier>? = mutableListOf()
    private set
  var version: String? = null
    private set
  var name: String? = null
    private set
  var title: String? = null
    private set
  var status: String? = null
    private set
  var experimental: Boolean = false
    private set
  var publisher: String? = null
    private set
  var contact: MutableList<Contact>? = mutableListOf()
    private set
  var description: String? = null
    private set
  var jurisdiction: MutableList<Jurisdiction>? = mutableListOf()
  var compose: Compose? = null
    private set
  var date: String? = null
    private set
  var immutable: Boolean = false
    private set
  var copyright: String? = null
    private set

  fun setCopyright(copyright: String?): ValueSet {
    this.copyright = copyright
    return this
  }

  fun setImmutable(immutable: Boolean): ValueSet {
    this.immutable = immutable
    return this
  }

  fun setId(id: String?): ValueSet {
    this.id = id
    return this
  }

  fun setUrl(url: String?): ValueSet {
    this.url = url
    return this
  }

  fun setDate(date: String?): ValueSet {
    this.date = date
    return this
  }
}
