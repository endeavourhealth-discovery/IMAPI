package org.endeavourhealth.imapi.model.dto

class SimpleMap {
    var name: String? = null
  private set
    var code: String? = null
  private set
    var scheme: String? = null
  private set
    var iri: String? = null
  private set
    var alternativeCode: String? = null
  private set
    var codeId: String? = null
  private set

    constructor()

    constructor(iri: String?, name: String?, code: String?, scheme: String?, alternativeCode: String?, codeId: String?) {
        this.name = name
        this.code = code
        this.scheme = scheme
        this.iri = iri
        this.alternativeCode = alternativeCode
        this.codeId = codeId
    }

    fun setName(name: String?): SimpleMap {
        this.name = name
        return this
    }

    fun setCode(code: String?): SimpleMap {
        this.code = code
        return this
    }

    fun setScheme(scheme: String?): SimpleMap {
        this.scheme = scheme
        return this
    }

    fun setIri(iri: String?): SimpleMap {
        this.iri = iri
        return this
    }

  fun setAlternativeCode(alternativeCode: String?): SimpleMap {
    this.alternativeCode = alternativeCode
    return this
  }

  fun setCodeId(codeId: String?): SimpleMap {
    this.codeId = codeId
    return this
  }
}
