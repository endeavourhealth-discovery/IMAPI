package org.endeavourhealth.imapi.logic.service

import org.endeavourhealth.imapi.model.casdoor.User
import org.endeavourhealth.imapi.model.responses.LoginResponse
import org.endeavourhealth.imapi.utility.HttpRequestService
import org.springframework.stereotype.Component

@Component
class EndeavourSecurityService {
  private val httpRequestService = HttpRequestService()
  private val endeavourSecurityUrl = System.getenv("ENDEAVOUR_SECURITY_HOST")
  private val endeavourSecurityApplication = System.getenv("ENDEAVOUR_SECURITY_APPLICATION")

  fun getLoginUrl(ipAddress: String, state: String, redirectUrl: String): String {
    val params = HashMap<String, String>()
    params["redirectUri"] = redirectUrl
    params["state"] = state
    return httpRequestService.get(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getLoginUrl",
      params,
      ipAddress,
      String::class.java
    )
  }

  fun getProfileUrl(ipAddress: String, sessionId: String): String {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    return httpRequestService.get(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getProfileUrl",
      params,
      ipAddress,
      String::class.java
    )
  }

  fun getUser(ipAddress: String, sessionId: String): User {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    return httpRequestService.get(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getUser",
      params,
      ipAddress,
      User::class.java
    )
  }

  fun updateUser(ipAddress: String, sessionId: String, user: User): User {
    val params = HashMap<String, Any>()
    params["sessionId"] = sessionId
    params["user"] = user
    return httpRequestService.post(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/updateUser",
      params,
      ipAddress,
      User::class.java
    )
  }

  fun introspect(ipAddress: String, sessionId: String): Boolean {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    return httpRequestService.get(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/introspect",
      params,
      ipAddress,
      Boolean::class.java
    )
  }

  fun login(ipAddress: String, code: String, state: String, redirectUrl: String): LoginResponse {
    val params = HashMap<String, String>()
    params["code"] = code
    params["state"] = state
    params["redirectUri"] = redirectUrl
    return httpRequestService.get(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/login",
      params,
      ipAddress,
      LoginResponse::class.java
    )
  }

  fun logout(ipAddress: String, sessionId: String): Void {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    return httpRequestService.get(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/logout",
      params,
      ipAddress,
      Void::class.java
    )
  }

  fun hasPermission(ipAddress: String, sessionId: String, obj: String, action: String): Boolean {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["object"] = obj
    params["action"] = action
    return httpRequestService.get(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authz/hasPermission",
      params,
      ipAddress,
      Boolean::class.java
    )
  }
}