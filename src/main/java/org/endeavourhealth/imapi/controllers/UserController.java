package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.entity.User;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.casbin.DataSource;
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.logic.service.UserService;
import org.endeavourhealth.imapi.model.dto.BooleanBody;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.dto.UserDataDto;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "*")
@Tag(name = "UserController", description = "Controller for managing user preferences, accessibility features, and other user details.")
@RequestScope
@Slf4j
public class UserController {
  private final UserService userService = new UserService();
  private final CasbinEnforcer casbinEnforcer = new CasbinEnforcer();
  private final CasdoorService casdoorService = new CasdoorService();

  @GetMapping(value = "/data")
  public UserDataDto getUserData(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserData");
      User user = casdoorService.getUser(session);
      return userService.getUserData(user.id);
    }
  }

  @Operation(summary = "Get user preset", description = "Fetches the user preset configuration based on the request.")
  @GetMapping(value = "/preset")
  public String getUserPreset(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserPreset");
      User user = casdoorService.getUser(session);
      return userService.getUserPreset(user.id);
    }
  }

  @Operation(summary = "Update user preset", description = "Updates the user preset configuration.")
  @PostMapping(value = "/preset", consumes = "text/plain")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserPreset(HttpSession session, @RequestBody String preset) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserPreset");
      User user = casdoorService.getUser(session);
      userService.updateUserPreset(user.id, preset);
    }
  }

  @Operation(summary = "Get user primary color", description = "Fetches the primary color configuration for the user.")
  @GetMapping(value = "/primaryColor")
  public String getUserPrimaryColor(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserPrimaryColor");
      User user = casdoorService.getUser(session);
      return userService.getUserPrimaryColor(user.id);
    }
  }

  @Operation(summary = "Update user primary color", description = "Updates the primary color configuration for the user.")
  @PostMapping(value = "/primaryColor", consumes = "text/plain")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserPriaryColor(HttpSession request, @RequestBody String color) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserPrimaryColor");
      User user = casdoorService.getUser(request);
      userService.updateUserPrimaryColor(user.id, color);
    }
  }

  @GetMapping(value = "/surfaceColor")
  public String getUserSurfaceColor(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserSurfaceColor");
      User user = casdoorService.getUser(session);
      return userService.getUserSurfaceColor(user.id);
    }
  }

  @PostMapping(value = "/surfaceColor", consumes = "text/plain")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserSurfaceColor(HttpSession session, @RequestBody String color) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserSurfaceColor");
      User user = casdoorService.getUser(session);
      userService.updateUserSurfaceColor(user.id, color);
    }
  }

  @GetMapping(value = "/darkMode")
  public Boolean getUserDarkMode(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserDarkMode");
      User user = casdoorService.getUser(session);
      return userService.getUserDarkMode(user.id);
    }
  }

  @PostMapping(value = "/darkMode")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserDarkMode(HttpSession session, @RequestBody BooleanBody darkMode) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserDarkMode");
      User user = casdoorService.getUser(session);
      userService.updateUserDarkMode(user.id, darkMode.getBool());
    }
  }

  @GetMapping(value = "/scale", produces = "application/json")
  public String getUserScale(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Scale.GET")) {
      log.debug("getUserScale");
      User user = casdoorService.getUser(session);
      return userService.getUserScale(user.id);
    }
  }

  @PostMapping(value = "/scale", consumes = "text/plain", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserScale(HttpSession session, @RequestBody String scale) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Scale.POST")) {
      log.debug("updateUserScale");
      User user = casdoorService.getUser(session);
      userService.updateUserScale(user.id, scale);
    }
  }

  @GetMapping(value = "/MRU", produces = "application/json")
  public List<RecentActivityItemDto> getUserMRU(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.MRU.GET")) {
      log.debug("getUserMRU");
      User user = casdoorService.getUser(session);
      return userService.getUserMRU(user.id);
    }
  }

  @PostMapping(value = "/MRU", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserMRU(HttpSession session, @RequestBody List<RecentActivityItemDto> mru) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.MRU.POST")) {
      log.debug("updateUserMRU");
      User user = casdoorService.getUser(session);
      userService.updateUserMRU(user.id, mru);
    }
  }

  @GetMapping(value = "/favourites", produces = "application/json")
  public List<String> getUserFavourites(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Favourites.GET")) {
      log.debug("getUserFavourites");
      User user = casdoorService.getUser(session);
      return userService.getUserFavourites(user.id);
    }
  }

  @PostMapping(value = "/favourites", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserFavourites(HttpSession session, @RequestBody List<String> favourites) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Favourites.POST")) {
      log.debug("updateUserFavourites");
      User user = casdoorService.getUser(session);
      userService.updateUserFavourites(user.id, favourites);
    }
  }

  @GetMapping(value = "/organisations", produces = "application/json")
  public List<String> getOrganisations(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Organisations.GET")) {
      log.debug(("getOrganisations"));
      User user = casdoorService.getUser(session);
      return userService.getUserOrganisations(user.id);
    }
  }

  @Operation(summary = "Update user organisations", description = "Updates the list of organisations for a user. Requires admin authority.")
  @PostMapping(value = "/organisations", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserOrganisations(@RequestParam("UserId") String userId, @RequestBody List<String> organisations, HttpSession session) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Organisations.POST")) {
      log.debug("updateUserOrganisations");
      casbinEnforcer.enforce(session, DataSource.IM, UserRole.ADMIN);
      if (!userService.userIdExists(userId))
        throw new GeneralCustomException("user not found", HttpStatus.BAD_REQUEST);
      userService.updateUserOrganisations(userId, organisations);
    }
  }

  @GetMapping(value = "/graphs", produces = "application/json")
  public List<Graph> getGraphs(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Graphs.GET")) {
      log.debug(("getGraphs"));
      User user = casdoorService.getUser(session);
      return userService.getUserGraphs(user.id);
    }
  }

  @Operation(summary = "Update user graphs", description = "Updates the list of graphs for a user. Requires admin authority.")
  @PostMapping(value = "/graphs", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserGraphs(@RequestParam("UserId") String userId, @RequestBody List<Graph> graphs, HttpSession session) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Graphs.POST")) {
      log.debug("updateUserGraphs");
      casbinEnforcer.enforce(session, DataSource.USER, UserRole.ADMIN);
      if (!userService.userIdExists(userId))
        throw new GeneralCustomException("user not found", HttpStatus.BAD_REQUEST);
      userService.updateUserGraphs(userId, graphs);
    }
  }

  @GetMapping(value = "/editAccess", produces = "application/json")
  public boolean getEditAccess(HttpSession session, @RequestParam("iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.EditAccess.GET")) {
      log.debug(("getEditAccess"));
      User user = casdoorService.getUser(session);
      return userService.getEditAccess(user.id, iri);
    }
  }

  @PostMapping(value = "/valid")
  public void isValidUser() {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Valid.GET")) {
      log.debug("isValidUser");
    }
  }
}
