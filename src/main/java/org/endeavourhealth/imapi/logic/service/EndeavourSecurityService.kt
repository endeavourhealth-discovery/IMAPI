package org.endeavourhealth.imapi.logic.service

import org.endeavourhealth.imapi.model.casdoor.User
import org.endeavourhealth.imapi.model.responses.LoginResponse
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.utility.HttpRequestService
import org.springframework.stereotype.Component

@Component
class EndeavourSecurityService {
  private val httpRequestService = HttpRequestService()
  private val endeavourSecurityUrl = System.getenv("ENDEAVOUR_SECURITY_HOST")
  private val endeavourSecurityApplication = System.getenv("ENDEAVOUR_SECURITY_APPLICATION")

  fun getLoginUrl(ipAddress: String, redirectUrl: String): String {
    val params = HashMap<String, String>()
    params["redirectUri"] = redirectUrl
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getLoginUrl",
      String::class.java,
      params,
      headers
    )
  }

  fun getRegisterUrl(ipAddress: String, redirectUrl: String): String {
    val params = HashMap<String, String>()
    params["redirectUri"] = redirectUrl
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getRegisterUrl",
      String::class.java,
      params,
      headers
    )
  }

  fun getProfileUrl(ipAddress: String, sessionId: String): String {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getProfileUrl",
      String::class.java,
      params,
      headers
    )
  }

  fun getUser(ipAddress: String, sessionId: String): User {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getUser",
      User::class.java,
      params,
      headers
    )
  }

  fun updateUser(ipAddress: String, sessionId: String, user: User): User {
    val params = HashMap<String, Any>()
    params["sessionId"] = sessionId
    params["user"] = user
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress, "content-type" to "application/json")
    return httpRequestService.httpPost(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/updateUser",
      User::class.java,
      params,
      headers
    )
  }

  fun introspect(ipAddress: String, sessionId: String): Boolean {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/introspect",
      Boolean::class.java,
      params,
      headers
    )
  }

  fun login(ipAddress: String, code: String, state: String, redirectUrl: String): LoginResponse {
    val params = HashMap<String, String>()
    params["code"] = code
    params["state"] = state
    params["redirectUri"] = redirectUrl
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/login",
      LoginResponse::class.java,
      params,
      headers
    )
  }

  fun logout(ipAddress: String, sessionId: String): Unit {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/logout",
      Void::class.java,
      params,
      headers
    )
  }

  fun hasPermission(ipAddress: String, sessionId: String, obj: String, action: String): Boolean {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["object"] = obj
    params["action"] = action
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authz/hasPermission",
      Boolean::class.java,
      params,
      headers
    )
  }

  fun isUser(ipAddress: String, sessionId: String, userId: String): Boolean {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["userId"] = userId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/isUser",
      Boolean::class.java,
      params,
      headers
    )
  }

  fun adminGetUsersWithRole(ipAddress: String, sessionId: String, role: UserRole): List<User> {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["role"] = role.toString()
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response: Array<User> = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/adminGetUsersWithRole",
      Array<User>::class.java,
      params,
      headers
    )
    return response.toMutableList()
  }

  fun adminGetUser(ipAddress: String, sessionId: String, userId: String): User {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["userId"] = userId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    return httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/adminGetUser",
      User::class.java,
      params,
      headers
    )
  }

  fun adminUpdateUser(ipAddress: String, sessionId: String, user: User): User {
    val params = HashMap<String, Any>()
    params["sessionId"] = sessionId
    params["user"] = user
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress, "content-type" to "application/json")
    return httpRequestService.httpPost(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/adminUpdateUser",
      User::class.java,
      params,
      headers
    )
  }
}