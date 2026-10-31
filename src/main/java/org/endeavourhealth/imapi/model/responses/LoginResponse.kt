package org.endeavourhealth.imapi.model.responses

import org.endeavourhealth.imapi.model.security.User

class LoginResponse {
  var user: User? = null
  var state: String? = null

  constructor() {}

  constructor(user: User, state: String?) {
    this.user = user
    this.state = state
  }


}
