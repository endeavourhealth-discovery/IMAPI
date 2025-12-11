package org.endeavourhealth.imapi.model.casdoor

import com.fasterxml.jackson.annotation.JsonIgnore
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole

class User {
  var id: String = ""
  var username: String = ""
  var email: String = ""
  var firstName: String = ""
  var lastName: String = ""
  var password: String = ""
    set(value) {
      field = ""
    }
  var avatar: String = ""
  var roles: List<UserRole> = mutableListOf()
  var groups: List<String> = mutableListOf()
  var test: String = ""

  @JsonIgnore
  fun adminSetPassword(password: String) {
    this.password = password
  }

  // required for casbin
  fun getRoleNames(): List<String> {
    return this.roles.map { it.name }
  }
}