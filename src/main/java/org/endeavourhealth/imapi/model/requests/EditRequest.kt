package org.endeavourhealth.imapi.model.requests

import org.endeavourhealth.imapi.model.tripletree.TTEntity
import org.endeavourhealth.imapi.vocabulary.NAMESPACE

class EditRequest {
  var entity: TTEntity? = null
    private set
  var hostUrl: String? = null
    private set
  var namespace: NAMESPACE? = null
    private set
  var crud: String? = null
    private set

  constructor(entity: TTEntity?, hostUrl: String?) {
    this.entity = entity
    this.hostUrl = hostUrl
  }

  constructor()

  fun setEntity(entity: TTEntity?): EditRequest {
    this.entity = entity
    return this
  }

  fun setHostUrl(hostUrl: String?): EditRequest {
    this.hostUrl = hostUrl
    return this
  }

  fun setNamespace(namespace: NAMESPACE?): EditRequest {
    this.namespace = namespace
    return this
  }

  fun setCrud(crud: String?): EditRequest {
    this.crud = crud
    return this
  }
}
