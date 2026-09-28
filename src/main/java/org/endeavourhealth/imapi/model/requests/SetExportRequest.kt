package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.set.SetOptions

class SetExportRequest {
  var ownRow: Boolean? = null
    private set
  var format: String? = null
    private set
  var options: SetOptions? = null
    private set

  constructor(ownRow: Boolean?, format: String?, options: SetOptions?) {
    this.ownRow = ownRow
    this.format = format
    this.options = options
  }

  constructor() {}

  fun setOwnRow(ownRow: Boolean?): SetExportRequest {
    this.ownRow = ownRow
    return this
  }

  fun setFormat(format: String?): SetExportRequest {
    this.format = format
    return this
  }

  fun setOptions(options: SetOptions?): SetExportRequest {
    this.options = options
    return this
  }
}
