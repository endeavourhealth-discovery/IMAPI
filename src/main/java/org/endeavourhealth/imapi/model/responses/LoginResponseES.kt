package org.endeavourhealth.imapi.model.responses

import org.endeavourhealth.imapi.model.security.User

class LoginResponseES {
  var sessionId: String? = null
  var user: User? = null
  var state: String? = null

  constructor(sessionId: String?, user: User?, state: String?) {
    this.sessionId = sessionId
    this.user = user
    this.state = state
  }

  constructor() {}
}
