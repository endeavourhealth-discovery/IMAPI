package org.endeavourhealth.imapi.controllers

import com.fasterxml.jackson.core.JsonProcessingException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.http.HttpException
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.model.responses.LoginResponse
import org.endeavourhealth.imapi.model.security.Permission
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.security.User
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope

@RestController
@RequestMapping("api/security")
@CrossOrigin(origins = ["*"])
@RequestScope
class SecurityController {
  private val securityService = SecurityService()
  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/public/registerUrl")
  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getRegisterUrl(request: HttpServletRequest, @RequestParam(name = "redirectUrl") redirectUrl: String): String {
    MetricsHelper.recordTime("API.SECURITY.PUBLIC.GETREGISTERURL.GET").use { t ->
      log.debug("getRegisterUrl")
      return securityService.getRegisterUrl(request, redirectUrl)
    }
  }

  @GetMapping("/public/loginUrl")
  @Throws(HttpException::class)
  fun getLoginUrl(@RequestParam(name = "redirectUrl") redirectUrl: String, request: HttpServletRequest): String {
    MetricsHelper.recordTime("API.SECURITY.PUBLIC.LOGINURL.GET").use { t ->
      log.debug("loginUrl")
      return securityService.getLoginUrl(redirectUrl, request)
    }
  }

  @GetMapping("/public/login")
  @Throws(UserAuthorisationException::class)
  fun login(
    @RequestParam(name = "code") code: String,
    @RequestParam(name = "state") state: String,
    request: HttpServletRequest,
    response: HttpServletResponse
  ): LoginResponse {
    MetricsHelper.recordTime("API.SECURITY.PUBLIC.LOGIN.GET").use { t ->
      log.debug("login")
      return securityService.loginUser(code, state, request, response)
    }
  }

  @GetMapping("/private/logout")
  @Throws(HttpException::class)
  fun logout(request: HttpServletRequest, response: HttpServletResponse) {
    MetricsHelper.recordTime("API.SECURITY.PUBLIC.LOGOUT.GET").use { t ->
      log.debug("logout")
      securityService.logout(request, response)
    }
  }

  @GetMapping("/private/getUsersInGroup")
  @Throws(UserNotFoundException::class, UserAuthorisationException::class)
  fun getUsersInGroup(request: HttpServletRequest, @RequestParam(name = "group") group: UserRole): List<User> {
    MetricsHelper.recordTime("API.SECURITY.GETUSERSINGROUP.GET").use { t ->
      log.debug("getUsersInGroup")
      securityService.requiresPermission(
        Permission(
          Resource.USER,
          mutableListOf(UserRole.TASK_MANAGER, UserRole.DEVELOPER),
          mutableListOf()
        ), request
      )
      return securityService.adminGetUsersInGroup(group, request)
    }
  }

  @GetMapping("/private/getGroups")
  @Throws(UserNotFoundException::class, UserAuthorisationException::class)
  fun getGroups(request: HttpServletRequest): List<UserRole> {
    MetricsHelper.recordTime("API.SECURITY.GETGROUPS.GET").use { t ->
      log.debug("getGroups")
      securityService.requiresPermission(
        Permission(
          Resource.USER,
          mutableListOf(UserRole.TASK_MANAGER, UserRole.DEVELOPER),
          mutableListOf()
        ), request
      )
      return securityService.adminGetGroups()
    }
  }

  @GetMapping("/private/user")
  @Throws(UserNotFoundException::class, JsonProcessingException::class)
  fun getUser(request: HttpServletRequest): User {
    MetricsHelper.recordTime("API.SECURITY.USER.GET").use { t ->
      log.debug("getUser")
      return securityService.getUser(request)
    }
  }

  @GetMapping("/private/user/profileUrl")
  @Throws(UserNotFoundException::class)
  fun getUserProfileUrl(request: HttpServletRequest): String {
    MetricsHelper.recordTime("API.SECURITY.USER.PROFILEURL.GET").use { t ->
      log.debug("getUserProfileUrl")
      return securityService.getUserUrl(request)
    }
  }
}
