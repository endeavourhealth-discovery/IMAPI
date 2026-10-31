package org.endeavourhealth.imapi.model.config

class Config {
  var dbid: Int? = null
    private set
  var name: String? = null
    private set
  var comment: String? = null
    private set
  var data: String? = null
    private set

  fun setDbid(dbid: Int?): Config {
    this.dbid = dbid
    return this
  }

  fun setName(name: String?): Config {
    this.name = name
    return this
  }

  fun setData(data: String?): Config {
    this.data = data
    return this
  }

  fun setComment(comment: String?): Config {
    this.comment = comment
    return this
  }
}
