package org.endeavourhealth.imapi.logic.service

import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException
import org.endeavourhealth.imapi.model.responses.LoginResponseES
import org.endeavourhealth.imapi.model.security.Permission
import org.endeavourhealth.imapi.model.security.User
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.utility.HttpRequestService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EndeavourSecurityService {
  private val log = LoggerFactory.getLogger(EndeavourSecurityService::class.java)
  private val httpRequestService = HttpRequestService()
  private val endeavourSecurityUrl = System.getenv("ENDEAVOUR_SECURITY_HOST")
  private val endeavourSecurityApplication = System.getenv("ENDEAVOUR_SECURITY_APPLICATION")

  fun getLoginUrl(ipAddress: String, redirectUrl: String): String {
    log.debug("getLoginUrl")
    val params = HashMap<String, String>()
    params["redirectUri"] = redirectUrl
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getLoginUrl",
      String::class.java,
      params,
      headers
    )
    log.debug(response)
    if (null != response) return response
    else throw UserAuthorisationException("Failed to get login url")
  }

  fun getRegisterUrl(ipAddress: String, redirectUrl: String): String {
    log.debug("getRegisterUrl")
    val params = HashMap<String, String>()
    params["redirectUri"] = redirectUrl
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getRegisterUrl",
      String::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to get register url")
  }

  fun getProfileUrl(ipAddress: String, sessionId: String): String {
    log.debug("getProfileUrl")
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getProfileUrl",
      String::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to get profile url")
  }

  fun getUser(ipAddress: String, sessionId: String): User {
    log.debug("getUser")
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/getUser",
      User::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to get user")
  }

  fun updateUser(ipAddress: String, sessionId: String, user: User): User {
    log.debug("updateUser")
    val params = HashMap<String, Any>()
    params["sessionId"] = sessionId
    params["user"] = user
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress, "content-type" to "application/json")
    val response = httpRequestService.httpPost(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/updateUser",
      User::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to update user")
  }

  fun introspect(ipAddress: String, sessionId: String): Boolean {
    log.debug("introspect")
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    try {
      val response = httpRequestService.httpGet(
        endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/introspect",
        Boolean::class.java,
        params,
        headers
      )
      if (null != response) return response
      else throw UserAuthorisationException("Failed to introspect user")

    } catch (e: Exception) {
      log.warn("Failed to introspect.", e)
      return false
    }
  }

  fun login(ipAddress: String, code: String, state: String): LoginResponseES {
    log.debug("login")
    val params = HashMap<String, String>()
    params["code"] = code
    params["state"] = state
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/login",
      LoginResponseES::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to login")
  }

  fun logout(ipAddress: String, sessionId: String): Unit {
    log.debug("logout")
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/logout",
      String::class.java,
      params,
      headers
    )
  }

  fun hasPermission(ipAddress: String, sessionId: String, permission: Permission): Boolean {
    val params = HashMap<String, Any>()
    params["sessionId"] = sessionId
    params["permission"] = permission
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpPost(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authz/hasPermission",
      Boolean::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to check hasPermission")
  }

  fun requiresPermission(ipAddress: String, sessionId: String, permission: Permission): Unit {
    val hasPermission = hasPermission(ipAddress, sessionId, permission)
    if (!hasPermission) throw UserAuthorisationException("Insufficient authorization to access resource")
  }

  fun isUser(ipAddress: String, sessionId: String, userId: String): Boolean {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["userId"] = userId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/isUser",
      Boolean::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to check isUser")
  }

  fun adminGetUsersWithRole(ipAddress: String, sessionId: String, role: UserRole): List<User> {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["role"] = role.toString()
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/adminGetUsersWithRole",
      Array<User>::class.java,
      params,
      headers
    )
    if (null != response) return response.toMutableList()
    else throw UserAuthorisationException("Failed to get users with role")
  }

  fun adminGetUser(ipAddress: String, sessionId: String, userId: String): User {
    val params = HashMap<String, String>()
    params["sessionId"] = sessionId
    params["userId"] = userId
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress)
    val response = httpRequestService.httpGet(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/adminGetUser",
      User::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to admin get user")
  }

  fun adminUpdateUser(ipAddress: String, sessionId: String, user: User): User {
    val params = HashMap<String, Any>()
    params["sessionId"] = sessionId
    params["user"] = user
    val headers = hashMapOf("X-CLIENT-IP" to ipAddress, "content-type" to "application/json")
    val response = httpRequestService.httpPost(
      endeavourSecurityUrl + "/api/" + endeavourSecurityApplication + "/authn/adminUpdateUser",
      User::class.java,
      params,
      headers
    )
    if (null != response) return response
    else throw UserAuthorisationException("Failed to admin update user")
  }
}