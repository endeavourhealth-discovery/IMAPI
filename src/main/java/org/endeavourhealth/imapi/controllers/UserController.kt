package org.endeavourhealth.imapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.endeavourhealth.imapi.casbin.CasbinEnforcer
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.logic.service.CasdoorService
import org.endeavourhealth.imapi.logic.service.UserService
import org.endeavourhealth.imapi.model.dto.BooleanBody
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope

@RestController
@RequestScope
@RequestMapping(value = ["api/user"])
@Tag(
  name = "UserController",
  description = "Controller for managing user preferences, accessibility features, and other user details."
)
@CrossOrigin(origins = ["*"])
open class UserController(
  private val userService: UserService,
  private val casbinEnforcer: CasbinEnforcer,
  private val casdoorService: CasdoorService
) {
  private val log = LoggerFactory.getLogger(UserController::class.java)

  @Operation(summary = "Update user preset", description = "Updates the user preset configuration.")
  @PostMapping(value = ["/preset"], consumes = ["text/plain"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UserNotFoundException::class)
  open fun updateUserPreset(request: HttpServletRequest, response: HttpServletResponse, @RequestBody preset: String) {
    MetricsHelper.recordTime("API.User.Preset.POST").use {
      log.debug("updateUserPreset")
      val user = casdoorService.getCasdoorUser(request)
      userService.updateUserTheme(user, preset)
      casdoorService.clearAccessToken(response)
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
  ) {
    MetricsHelper.recordTime("API.User.PrimaryColor.POST").use {
      log.debug("updateUserPrimaryColor")
      val user = casdoorService.getCasdoorUser(request)
      userService.updateUserPrimaryColor(user, color)
      casdoorService.clearAccessToken(response)
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
  ) {
    MetricsHelper.recordTime("API.User.Surface.POST").use {
      log.debug("updateUserSurfaceColor")
      val user = casdoorService.getCasdoorUser(request)
      userService.updateUserSurfaceColor(user, color)
      casdoorService.clearAccessToken(response)
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
  ) {
    MetricsHelper.recordTime("API.User.DarkMode.POST").use {
      log.debug("updateUserDarkMode")
      val user = casdoorService.getCasdoorUser(request)
      userService.updateUserDarkMode(user, darkMode.getBool())
      casdoorService.clearAccessToken(response)
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
  ) {
    MetricsHelper.recordTime("API.User.FontSize.POST").use {
      log.debug("updateUserFontSize")
      val user = casdoorService.getCasdoorUser(request)
      userService.updateUserFontSize(user, fontSize)
      casdoorService.clearAccessToken(response)
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
  ) {
    MetricsHelper.recordTime("API.User.RecentActivity.POST").use {
      log.debug("updateUserRecentActivity")
      val user = casdoorService.getCasdoorUser(request)
      userService.updateUserRecentActivity(user, recentActivity)
      casdoorService.clearAccessToken(response)
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
  ) {
    MetricsHelper.recordTime("API.User.Favourites.POST").use {
      log.debug("updateUserFavourites")
      val user = casdoorService.getCasdoorUser(request)
      userService.updateUserFavourites(user, favourites)
      casdoorService.clearAccessToken(response)
    }
  }

  @Operation(
    summary = "Update user organisations",
    description = "Updates the list of organisations for a user. Requires admin authority."
  )
  @PostMapping(value = ["/organisations"], produces = ["application/json"])
  @PreAuthorize("@guard.hasPermission('USER','WRITE')")
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(Exception::class)
  open fun updateUserOrganisations(
    @RequestParam("UserId") userId: String,
    @RequestBody organisations: List<String>
  ) {
    MetricsHelper.recordTime("API.User.Organisations.POST").use {
      log.debug("updateUserOrganisations")
      if (!casdoorService.userExists(userId)) throw GeneralCustomException("user not found", HttpStatus.BAD_REQUEST)
      userService.updateUserOrganisations(userId, organisations)
    }
  }

  @Operation(
    summary = "User has edit access",
    description = "Checks if the user has edit access."
  )
  @GetMapping(value = ["/editAccess"], produces = ["application/json"])
  @Throws(UserNotFoundException::class)
  open fun getEditAccess(request: HttpServletRequest, @RequestParam("iri") iri: String): Boolean {
    MetricsHelper.recordTime("API.User.EditAccess.GET").use {
      log.debug(("getEditAccess"))
      val user = casdoorService.getUser(request)
      return userService.getEditAccess(user, iri)
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