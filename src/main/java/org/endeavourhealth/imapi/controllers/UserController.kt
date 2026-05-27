package org.endeavourhealth.imapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.model.dto.BooleanBody
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.model.security.NamespacePermission
import org.endeavourhealth.imapi.model.security.Permission
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.security.User
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole
import org.endeavourhealth.imapi.utility.APIGuard
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.endeavourhealth.interfacemanager.model.FontSize
import org.endeavourhealth.interfacemanager.model.PrimeVueColors
import org.endeavourhealth.interfacemanager.model.PrimeVuePresetThemes
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope

@RestController
@RequestScope
@RequestMapping(value = ["api/user/private"])
@Tag(
  name = "UserController",
  description = "Controller for managing user preferences, accessibility features, and other user details."
)
@CrossOrigin(origins = ["*"])
open class UserController(
  private val securityService: SecurityService
) {
  private val log = LoggerFactory.getLogger(UserController::class.java)
  private val apiGuard = APIGuard()

  @Operation(summary = "Update user preset", description = "Updates the user preset configuration.")
  @PostMapping(value = ["/preset"], consumes = ["text/plain"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UserNotFoundException::class)
  open fun updateUserPreset(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestBody preset: String
  ): User {
    MetricsHelper.recordTime("API.User.Preset.POST").use {
      log.debug("updateUserPreset")
      val theme = PrimeVuePresetThemes.decode(preset)
      requireNotNull(theme)
      val user = securityService.getUser(request)
      user.theme = theme
      return securityService.updateUser(request, user)
    }
  }

  @Operation(
    summary = "Update user primary color",
    description = "Updates the primary color configuration for the user."
  )
  @PostMapping(value = ["/primaryColor"], consumes = ["text/plain"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UserNotFoundException::class)
  open fun updateUserPrimaryColor(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestBody color: String
  ): User {
    MetricsHelper.recordTime("API.User.PrimaryColor.POST").use {
      log.debug("updateUserPrimaryColor")
      val colorEnum = PrimeVueColors.decode(color)
      requireNotNull(colorEnum)
      val user = securityService.getUser(request)
      user.primaryColor = colorEnum
      return securityService.updateUser(request, user)
    }
  }

  @Operation(
    summary = "Update user surface color",
    description = "Updates the surface color configuration for the user."
  )
  @PostMapping(value = ["/surfaceColor"], consumes = ["text/plain"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserSurfaceColor(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestBody color: String
  ): User {
    MetricsHelper.recordTime("API.User.Surface.POST").use {
      log.debug("updateUserSurfaceColor")
      val colorEnum = PrimeVueColors.decode(color)
      requireNotNull(colorEnum)
      val user = securityService.getUser(request)
      user.surfaceColor = colorEnum
      return securityService.updateUser(request, user);
    }
  }

  @Operation(
    summary = "Update user dark mode",
    description = "Updates the dark mode configuration for the user."
  )
  @PostMapping(value = ["/darkMode"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserDarkMode(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestBody darkMode: BooleanBody
  ): User {
    MetricsHelper.recordTime("API.User.DarkMode.POST").use {
      log.debug("updateUserDarkMode")
      val user = securityService.getUser(request)
      user.darkMode = darkMode.bool
      return securityService.updateUser(request, user)
    }
  }

  @Operation(
    summary = "Update user font size",
    description = "Updates the font size configuration for the user."
  )
  @PostMapping(value = ["/fontSize"], consumes = ["text/plain"], produces = ["application/json"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UserNotFoundException::class)
  open fun updateUserFontSize(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestBody fontSize: String
  ): User {
    MetricsHelper.recordTime("API.User.FontSize.POST").use {
      log.debug("updateUserFontSize")
      val fontSizeEnum = FontSize.decode(fontSize)
      requireNotNull(fontSizeEnum)
      val user = securityService.getUser(request)
      user.fontSize = fontSizeEnum
      return securityService.updateUser(request, user)
    }
  }

  @Operation(
    summary = "Update user most recent activity",
    description = "Updates the most recent activity configuration for the user."
  )
  @PostMapping(value = ["/recentActivity"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserRecentActivity(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestBody recentActivity: List<RecentActivityItemDto>
  ): User {
    MetricsHelper.recordTime("API.User.RecentActivity.POST").use {
      log.debug("updateUserRecentActivity")
      val user = securityService.getUser(request)
      user.recentActivity = recentActivity
      return securityService.updateUser(request, user)
    }
  }

  @Operation(
    summary = "Update user favourites",
    description = "Updates the favourites configuration for the user."
  )
  @PostMapping(value = ["/favourites"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserFavourites(
    request: HttpServletRequest,
    response: HttpServletResponse,
    @RequestBody favourites: List<String>
  ): User {
    MetricsHelper.recordTime("API.User.Favourites.POST").use {
      log.debug("updateUserFavourites")
      val user = securityService.getUser(request)
      user.favourites = favourites
      return securityService.updateUser(request, user)
    }
  }

  @Operation(
    summary = "Update user namespaces",
    description = "Updates the list of namespaces for a user. Requires admin authority."
  )
  @PostMapping(value = ["/namespaces"], produces = ["application/json"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(Exception::class)
  open fun updateUserNamespaces(
    request: HttpServletRequest,
    @RequestParam("UserId") userId: String,
    @RequestBody namespaces: List<NamespacePermission>
  ) {
    MetricsHelper.recordTime("API.User.Namespaces.POST").use {
      log.debug("updateUserNamespaces")
      securityService.requiresPermission(Permission(Resource.USER, mutableListOf(UserRole.ADMIN), emptyList()), request)
      if (!securityService.userExists(userId, request)) throw GeneralCustomException(
        "user not found",
        HttpStatus.BAD_REQUEST
      )
      securityService.updateUserNamespaces(userId, namespaces, request)
    }
  }

  @Operation(
    summary = "User is valid",
    description = "Checks if the user has valid authentication credentials."
  )
  @PostMapping(value = ["/valid"])
  open fun isValidUser() {
    MetricsHelper.recordTime("API.User.Valid.GET").use {
      log.debug("isValidUser")
    }
  }
}