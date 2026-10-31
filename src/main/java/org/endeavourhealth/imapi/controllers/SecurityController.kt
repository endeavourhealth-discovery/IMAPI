package org.endeavourhealth.imapi.controllers

import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.model.security.Action
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.security.User
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope

/**
 * Signing in and out is handled by the frontends directly against Casdoor (OIDC); the API only validates the resulting access token.
 */
@RestController
@RequestMapping("api/security")
@CrossOrigin(origins = ["*"])
@RequestScope
class SecurityController {
  private val securityService = SecurityService()
  private val log = LoggerFactory.getLogger(javaClass)

  @GetMapping("/private/getUsersInGroup")
  @Throws(UserNotFoundException::class)
  fun getUsersInGroup(@RequestParam(name = "group") group: UserRole): MutableList<User> {
    MetricsHelper.recordTime("API.SECURITY.GETUSERSINGROUP.GET").use {
      log.debug("getUsersInGroup")
      securityService.requiresPermission(Resource.USER, Action.READ)
      return securityService.adminGetUsersInGroup(group)
    }
  }

  @GetMapping("/private/getGroups")
  @Throws(UserNotFoundException::class)
  fun getGroups(): MutableList<UserRole> {
    MetricsHelper.recordTime("API.SECURITY.GETGROUPS.GET").use {
      log.debug("getGroups")
      securityService.requiresPermission(Resource.USER, Action.READ)
      return securityService.adminGetGroups()
    }
  }

  @GetMapping("/private/user")
  @Throws(UserNotFoundException::class)
  fun getUser(): User {
    MetricsHelper.recordTime("API.SECURITY.USER.GET").use {
      log.debug("getUser")
      return securityService.user
    }
  }

  @GetMapping("/private/user/profileUrl")
  fun getUserProfileUrl(): String {
    MetricsHelper.recordTime("API.SECURITY.USER.PROFILEURL.GET").use {
      log.debug("getUserProfileUrl")
      return securityService.userUrl
    }
  }
}
