package org.endeavourhealth.imapi.controllers

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.endeavourhealth.imapi.casbin.CasbinEnforcer
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException
import org.endeavourhealth.imapi.logic.service.CasdoorService
import org.endeavourhealth.imapi.logic.service.UserService
import org.endeavourhealth.imapi.model.casbin.Action
import org.endeavourhealth.imapi.model.casbin.Resource
import org.endeavourhealth.imapi.model.casdoor.UserSettings
import org.endeavourhealth.imapi.model.dto.BooleanBody
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto
import org.endeavourhealth.imapi.utility.MetricsHelper
import org.endeavourhealth.imapi.vocabulary.Graph
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.context.annotation.RequestScope
import java.io.IOException

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

  @Operation(summary = "Get user settings", description = "Fetches the user preferences settings.")
  @GetMapping(value = ["/settings"])
  @Throws(UserNotFoundException::class)
  open fun getUserSettings(request: HttpServletRequest): UserSettings {
    MetricsHelper.recordTime("API.User.Settings.GET").use {
      log.debug("getUserSettings")
      val user = casdoorService.getUser(request)
      return userService.getUserSettings(user.id)
    }
  }

  @Operation(summary = "Get user preset", description = "Fetches the user preset configuration based on the request.")
  @GetMapping(value = ["/preset"])
  @Throws(UserNotFoundException::class)
  open fun getUserPreset(request: HttpServletRequest): String {
    MetricsHelper.recordTime("API.User.Preset.GET").use {
      log.debug("getUserPreset")
      val user = casdoorService.getUser(request)
      return userService.getUserPreset(user.id)
    }
  }

  @Operation(summary = "Update user preset", description = "Updates the user preset configuration.")
  @PostMapping(value = ["/preset"], consumes = ["text/plain"])
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(UserNotFoundException::class)
  open fun updateUserPreset(request: HttpServletRequest, @RequestBody preset: String) {
    MetricsHelper.recordTime("API.User.Preset.POST").use {
      log.debug("updateUserPreset")
      val user = casdoorService.getUser(request)
      userService.updateUserPreset(user.id, preset)
    }
  }

  @Operation(summary = "Get user primary color", description = "Fetches the primary color configuration for the user.")
  @GetMapping(value = ["/primaryColor"])
  @Throws(UserNotFoundException::class)
  open fun getUserPrimaryColor(request: HttpServletRequest): String {
    MetricsHelper.recordTime("API.User.PrimaryColor.GET").use {
      log.debug("getUserPrimaryColor")
      val user = casdoorService.getUser(request)
      return userService.getUserPrimaryColor(user.id)
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
  open fun updateUserPrimaryColor(request: HttpServletRequest, @RequestBody color: String) {
    MetricsHelper.recordTime("API.User.PrimaryColor.POST").use {
      log.debug("updateUserPrimaryColor")
      val user = casdoorService.getUser(request)
      userService.updateUserPrimaryColor(user.id, color)
    }
  }

  @Operation(
    summary = "Get user surface color",
    description = "Gets the surface color configuration for the user."
  )
  @GetMapping(value = ["/surfaceColor"])
  @Throws(UserNotFoundException::class)
  open fun getUserSurfaceColor(request: HttpServletRequest): String {
    MetricsHelper.recordTime("API.User.SurfaceColor.GET").use {
      log.debug("getUserSurfaceColor")
      val user = casdoorService.getUser(request)
      return userService.getUserSurfaceColor(user.id)
    }
  }

  @Operation(
    summary = "Update user surface color",
    description = "Updates the surface color configuration for the user."
  )
  @PostMapping(value = ["/surfaceColor"], consumes = ["text/plain"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserSurfaceColor(request: HttpServletRequest, @RequestBody color: String) {
    MetricsHelper.recordTime("API.User.Surface.POST").use {
      log.debug("updateUserSurfaceColor")
      val user = casdoorService.getUser(request)
      userService.updateUserSurfaceColor(user.id, color)
    }
  }

  @Operation(
    summary = "Get user dark mode",
    description = "Gets the dark mode configuration for the user."
  )
  @GetMapping(value = ["/darkMode"])
  @Throws(UserNotFoundException::class)
  open fun getUserDarkMode(request: HttpServletRequest): Boolean {
    MetricsHelper.recordTime("API.User.DarkMode.GET").use {
      log.debug("getUserDarkMode")
      val user = casdoorService.getUser(request)
      return userService.getUserDarkMode(user.id)
    }
  }

  @Operation(
    summary = "Update user dark mode",
    description = "Updates the dark mode configuration for the user."
  )
  @PostMapping(value = ["/darkMode"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserDarkMode(request: HttpServletRequest, @RequestBody darkMode: BooleanBody) {
    MetricsHelper.recordTime("API.User.DarkMode.POST").use {
      log.debug("updateUserDarkMode")
      val user = casdoorService.getUser(request)
      userService.updateUserDarkMode(user.id, darkMode.getBool())
    }
  }

  @Operation(
    summary = "Get user font size",
    description = "Gets the font size configuration for the user."
  )
  @GetMapping(value = ["/fontSize"], produces = ["application/json"])
  @Throws(IOException::class, UserNotFoundException::class)
  open fun getUserFontSize(request: HttpServletRequest): String {
    MetricsHelper.recordTime("API.User.FontSize.GET").use {
      log.debug("getUserFontSize")
      val user = casdoorService.getUser(request)
      return userService.getUserFontSize(user.id)
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
  open fun updateUserFontSize(request: HttpServletRequest, @RequestBody fontSize: String) {
    MetricsHelper.recordTime("API.User.FontSize.POST").use {
      log.debug("updateUserFontSize")
      val user = casdoorService.getUser(request)
      userService.updateUserFontSize(user.id, fontSize)
    }
  }

  @Operation(
    summary = "Get user most recent activity",
    description = "Gets the most recent activity configuration for the user."
  )
  @GetMapping(value = ["/MRU"], produces = ["application/json"])
  @Throws(UserNotFoundException::class)
  open fun getUserMRU(request: HttpServletRequest): List<RecentActivityItemDto?> {
    MetricsHelper.recordTime("API.User.MRU.GET").use {
      log.debug("getUserMRU")
      val user = casdoorService.getUser(request)
      return userService.getUserMRUs(user.id)
    }
  }

  @Operation(
    summary = "Update user most recent activity",
    description = "Updates the most recent activity configuration for the user."
  )
  @PostMapping(value = ["/MRU"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserMRU(request: HttpServletRequest, @RequestBody mru: List<RecentActivityItemDto>) {
    MetricsHelper.recordTime("API.User.MRU.POST").use {
      log.debug("updateUserMRU")
      val user = casdoorService.getUser(request)
      userService.updateUserMRU(user.id, mru)
    }
  }

  @Operation(
    summary = "Get user favourites",
    description = "Gets the favourites configuration for the user."
  )
  @GetMapping(value = ["/favourites"], produces = ["application/json"])
  @Throws(UserNotFoundException::class)
  open fun getUserFavourites(request: HttpServletRequest): List<String> {
    MetricsHelper.recordTime("API.User.Favourites.GET").use {
      log.debug("getUserFavourites")
      val user = casdoorService.getUser(request)
      return userService.getUserFavourites(user.id)
    }
  }

  @Operation(
    summary = "Update user favourites",
    description = "Updates the favourites configuration for the user."
  )
  @PostMapping(value = ["/favourites"], produces = ["application/json"])
  @ResponseStatus(HttpStatus.ACCEPTED)
  @Throws(UserNotFoundException::class)
  open fun updateUserFavourites(request: HttpServletRequest, @RequestBody favourites: List<String>) {
    MetricsHelper.recordTime("API.User.Favourites.POST").use {
      log.debug("updateUserFavourites")
      val user = casdoorService.getUser(request)
      userService.updateUserFavourites(user.id, favourites)
    }
  }

  @Operation(
    summary = "Get user organisations",
    description = "Gets the organisations configuration for the user."
  )
  @GetMapping(value = ["/organisations"], produces = ["application/json"])
  @Throws(UserNotFoundException::class)
  open fun getOrganisations(request: HttpServletRequest): List<String> {
    MetricsHelper.recordTime("API.User.Organisations.GET").use {
      log.debug(("getOrganisations"))
      val user = casdoorService.getUser(request)
      return userService.getUserOrganisations(user.id)
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
      if (!userService.userIdExists(userId)) throw GeneralCustomException("user not found", HttpStatus.BAD_REQUEST)
      userService.updateUserOrganisations(userId, organisations)
    }
  }

  @Operation(
    summary = "Get user graphs",
    description = "Gets the graphs configuration for the user."
  )
  @GetMapping(value = ["/graphs"], produces = ["application/json"])
  @Throws(UserNotFoundException::class)
  open fun getGraphs(request: HttpServletRequest): List<Graph> {
    MetricsHelper.recordTime("API.User.Graphs.GET").use {
      log.debug(("getGraphs"))
      val user = casdoorService.getUser(request)
      return userService.getUserGraphs(user.id)
    }
  }

  @Operation(
    summary = "Update user graphs",
    description = "Updates the list of graphs for a user. Requires admin authority."
  )
  @PostMapping(value = ["/graphs"], produces = ["application/json"])
  @PreAuthorize("@guard.hasPermission('USER','WRITE')")
  @ResponseStatus(
    HttpStatus.ACCEPTED
  )
  @Throws(java.lang.Exception::class)
  open fun updateUserGraphs(
    @RequestParam("UserId") userId: String,
    @RequestBody graphs: List<Graph>,
    request: HttpServletRequest
  ) {
    MetricsHelper.recordTime("API.User.Graphs.POST").use {
      log.debug("updateUserGraphs")
      casbinEnforcer.enforceWithError(request, Resource.USER, Action.WRITE)
      if (!userService.userIdExists(userId)) throw GeneralCustomException("user not found", HttpStatus.BAD_REQUEST)
      userService.updateUserGraphs(userId, graphs)
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
      return userService.getEditAccess(user.id, iri)
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