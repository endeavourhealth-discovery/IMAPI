package org.endeavourhealth.imapi.model.fhir

class CodeSystem {
  var resourceType: String? = null
  var id: String? = null
    private set
  var meta: Meta? = null
    private set
  var text: Text? = null
    private set
  var extension: MutableList<Extension>? = mutableListOf()
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
  var description: String? = null
    private set
  var jurisdiction: MutableList<Jurisdiction>? = mutableListOf()
    private set
  var caseSensitive: Boolean = false
    private set
  var valueSet: String? = null
    private set
  var hierarchyMeaning: String? = null
    private set
  var content: String? = null
    private set
  var concept: MutableList<FHIRConcept>? = mutableListOf()
    private set
  var date: String? = null
    private set
  var copyright: String? = null
    private set
  var contact: MutableList<Contact>? = mutableListOf()
    private set

  fun setCopyright(copyright: String?): CodeSystem {
    this.copyright = copyright
    return this
  }

  fun setContact(contact: MutableList<Contact>?): CodeSystem {
    this.contact = contact
    return this
  }

  fun setId(id: String?): CodeSystem {
    this.id = id
    return this
  }

  fun setUrl(url: String?): CodeSystem {
    this.url = url
    return this
  }

  fun setDate(date: String?): CodeSystem {
    this.date = date
    return this
  }
}
