package org.endeavourhealth.imapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.logic.service.SecurityService
import org.endeavourhealth.imapi.model.dto.BooleanBody
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.model.primevue.FontSize
import org.endeavourhealth.imapi.model.primevue.PrimeVueColors
import org.endeavourhealth.imapi.model.primevue.PrimeVuePresetThemes
import org.endeavourhealth.imapi.model.security.Action
import org.endeavourhealth.imapi.model.security.NamespacePermission
import org.endeavourhealth.imapi.model.security.Resource
import org.endeavourhealth.imapi.model.security.User
import org.endeavourhealth.imapi.utility.MetricsHelper
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
class UserController(
  private val securityService: SecurityService
) {
  private val log = LoggerFactory.getLogger(UserController::class.java)

  @Operation(summary = "Update user preset", description = "Updates the user preset configuration.")
  @PostMapping(value = ["/preset"], consumes = ["text/plain"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UserNotFoundException::class)
  fun updateUserPreset(
    @RequestBody preset: String
  ): User {
    MetricsHelper.recordTime("API.User.Preset.POST").use {
      log.debug("updateUserPreset")
      val theme = PrimeVuePresetThemes.fromValue(preset)
      requireNotNull(theme)
      val user = securityService.user
      user.theme = theme
      return securityService.updateUser(user)
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
  fun updateUserPrimaryColor(
    @RequestBody color: String
  ): User {
    MetricsHelper.recordTime("API.User.PrimaryColor.POST").use {
      log.debug("updateUserPrimaryColor")
      val colorEnum = PrimeVueColors.fromValue(color)
      requireNotNull(colorEnum)
      val user = securityService.user
      user.primaryColor = colorEnum
      return securityService.updateUser(user)
    }
  }

  @Operation(
    summary = "Update user surface color",
    description = "Updates the surface color configuration for the user."
  )
  @PostMapping(value = ["/surfaceColor"], consumes = ["text/plain"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  fun updateUserSurfaceColor(
    @RequestBody color: String
  ): User {
    MetricsHelper.recordTime("API.User.Surface.POST").use {
      log.debug("updateUserSurfaceColor")
      val colorEnum = PrimeVueColors.fromValue(color)
      requireNotNull(colorEnum)
      val user = securityService.user
      user.surfaceColor = colorEnum
      return securityService.updateUser(user);
    }
  }

  @Operation(
    summary = "Update user dark mode",
    description = "Updates the dark mode configuration for the user."
  )
  @PostMapping(value = ["/darkMode"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  fun updateUserDarkMode(
    @RequestBody darkMode: BooleanBody
  ): User {
    MetricsHelper.recordTime("API.User.DarkMode.POST").use {
      log.debug("updateUserDarkMode")
      val user = securityService.user
      user.darkMode = darkMode.bool
      return securityService.updateUser(user)
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
  fun updateUserFontSize(
    @RequestBody fontSize: String
  ): User {
    MetricsHelper.recordTime("API.User.FontSize.POST").use {
      log.debug("updateUserFontSize")
      val fontSizeEnum = FontSize.fromValue(fontSize)
      requireNotNull(fontSizeEnum)
      val user = securityService.user
      user.fontSize = fontSizeEnum
      return securityService.updateUser(user)
    }
  }

  @Operation(
    summary = "Update user most recent activity",
    description = "Updates the most recent activity configuration for the user."
  )
  @PostMapping(value = ["/recentActivity"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  fun updateUserRecentActivity(
    @RequestBody recentActivity: List<RecentActivityItemDto>
  ): User {
    MetricsHelper.recordTime("API.User.RecentActivity.POST").use {
      log.debug("updateUserRecentActivity")
      val user = securityService.user
      user.recentActivity = recentActivity
      return securityService.updateUser(user)
    }
  }

  @Operation(
    summary = "Update user favourites",
    description = "Updates the favourites configuration for the user."
  )
  @PostMapping(value = ["/favourites"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  fun updateUserFavourites(
    @RequestBody favourites: List<String>
  ): User {
    MetricsHelper.recordTime("API.User.Favourites.POST").use {
      log.debug("updateUserFavourites")
      val user = securityService.user
      user.favourites = favourites
      return securityService.updateUser(user)
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
  fun updateUserNamespaces(
    @RequestParam("UserId") userId: String,
    @RequestBody namespaces: MutableList<NamespacePermission>
  ) {
    MetricsHelper.recordTime("API.User.Namespaces.POST").use {
      log.debug("updateUserNamespaces")
      securityService.requiresPermission(Resource.USER, Action.UPDATE)
      if (!securityService.userExists(userId)) throw GeneralCustomException(
        "user not found",
        HttpStatus.BAD_REQUEST
      )
      securityService.updateUserNamespaces(userId, namespaces)
    }
  }

  @Operation(
    summary = "User is valid",
    description = "Checks if the user has valid authentication credentials."
  )
  @PostMapping(value = ["/valid"])
  fun isValidUser() {
    MetricsHelper.recordTime("API.User.Valid.GET").use {
      log.debug("isValidUser")
    }
  }
}