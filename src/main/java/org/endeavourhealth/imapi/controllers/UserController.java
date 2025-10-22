package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.logic.service.UserService;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.casbin.AccessRequest;
import org.endeavourhealth.imapi.model.dto.BooleanBody;
import org.endeavourhealth.imapi.model.dto.RecentActivityItemDto;
import org.endeavourhealth.imapi.model.dto.UserDataDto;
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
      return userService.getUserData(user.getId());
    }
  }

  @Operation(summary = "Get user preset", description = "Fetches the user preset configuration based on the request.")
  @GetMapping(value = "/preset")
  public String getUserPreset(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserPreset");
      User user = casdoorService.getUser(session);
      return userService.getUserPreset(user.getId());
    }
  }

  @Operation(summary = "Update user preset", description = "Updates the user preset configuration.")
  @PostMapping(value = "/preset", consumes = "text/plain")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserPreset(HttpSession session, @RequestBody String preset) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserPreset");
      User user = casdoorService.getUser(session);
      userService.updateUserPreset(user.getId(), preset);
    }
  }

  @Operation(summary = "Get user primary color", description = "Fetches the primary color configuration for the user.")
  @GetMapping(value = "/primaryColor")
  public String getUserPrimaryColor(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserPrimaryColor");
      User user = casdoorService.getUser(session);
      return userService.getUserPrimaryColor(user.getId());
    }
  }

  @Operation(summary = "Update user primary color", description = "Updates the primary color configuration for the user.")
  @PostMapping(value = "/primaryColor", consumes = "text/plain")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserPriaryColor(HttpSession request, @RequestBody String color) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserPrimaryColor");
      User user = casdoorService.getUser(request);
      userService.updateUserPrimaryColor(user.getId(), color);
    }
  }

  @GetMapping(value = "/surfaceColor")
  public String getUserSurfaceColor(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserSurfaceColor");
      User user = casdoorService.getUser(session);
      return userService.getUserSurfaceColor(user.getId());
    }
  }

  @PostMapping(value = "/surfaceColor", consumes = "text/plain")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserSurfaceColor(HttpSession session, @RequestBody String color) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserSurfaceColor");
      User user = casdoorService.getUser(session);
      userService.updateUserSurfaceColor(user.getId(), color);
    }
  }

  @GetMapping(value = "/darkMode")
  public Boolean getUserDarkMode(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.GET")) {
      log.debug("getUserDarkMode");
      User user = casdoorService.getUser(session);
      return userService.getUserDarkMode(user.getId());
    }
  }

  @PostMapping(value = "/darkMode")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserDarkMode(HttpSession session, @RequestBody BooleanBody darkMode) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Theme.POST")) {
      log.debug("updateUserDarkMode");
      User user = casdoorService.getUser(session);
      userService.updateUserDarkMode(user.getId(), darkMode.getBool());
    }
  }

  @GetMapping(value = "/scale", produces = "application/json")
  public String getUserScale(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Scale.GET")) {
      log.debug("getUserScale");
      User user = casdoorService.getUser(session);
      return userService.getUserScale(user.getId());
    }
  }

  @PostMapping(value = "/scale", consumes = "text/plain", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserScale(HttpSession session, @RequestBody String scale) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Scale.POST")) {
      log.debug("updateUserScale");
      User user = casdoorService.getUser(session);
      userService.updateUserScale(user.getId(), scale);
    }
  }

  @GetMapping(value = "/MRU", produces = "application/json")
  public List<RecentActivityItemDto> getUserMRU(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.MRU.GET")) {
      log.debug("getUserMRU");
      User user = casdoorService.getUser(session);
      return userService.getUserMRU(user.getId());
    }
  }

  @PostMapping(value = "/MRU", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserMRU(HttpSession session, @RequestBody List<RecentActivityItemDto> mru) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.MRU.POST")) {
      log.debug("updateUserMRU");
      User user = casdoorService.getUser(session);
      userService.updateUserMRU(user.getId(), mru);
    }
  }

  @GetMapping(value = "/favourites", produces = "application/json")
  public List<String> getUserFavourites(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Favourites.GET")) {
      log.debug("getUserFavourites");
      User user = casdoorService.getUser(session);
      return userService.getUserFavourites(user.getId());
    }
  }

  @PostMapping(value = "/favourites", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserFavourites(HttpSession session, @RequestBody List<String> favourites) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Favourites.POST")) {
      log.debug("updateUserFavourites");
      User user = casdoorService.getUser(session);
      userService.updateUserFavourites(user.getId(), favourites);
    }
  }

  @GetMapping(value = "/organisations", produces = "application/json")
  public List<String> getOrganisations(HttpSession session) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Organisations.GET")) {
      log.debug(("getOrganisations"));
      User user = casdoorService.getUser(session);
      return userService.getUserOrganisations(user.getId());
    }
  }

  @Operation(summary = "Update user organisations", description = "Updates the list of organisations for a user. Requires admin authority.")
  @PostMapping(value = "/organisations", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserOrganisations(@RequestParam("UserId") String userId, @RequestBody List<String> organisations, HttpServletRequest request) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Organisations.POST")) {
      log.debug("updateUserOrganisations");
      casbinEnforcer.enforce(request, AccessRequest.WRITE);
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
      return userService.getUserGraphs(user.getId());
    }
  }

  @Operation(summary = "Update user graphs", description = "Updates the list of graphs for a user. Requires admin authority.")
  @PostMapping(value = "/graphs", produces = "application/json")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void updateUserGraphs(@RequestParam("UserId") String userId, @RequestBody List<Graph> graphs, HttpServletRequest request) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Graphs.POST")) {
      log.debug("updateUserGraphs");
      casbinEnforcer.enforce(request, AccessRequest.WRITE);
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
      return userService.getEditAccess(user.getId(), iri);
    }
  }

  @PostMapping(value = "/valid")
  public void isValidUser() {
    try (MetricsTimer t = MetricsHelper.recordTime("API.User.Valid.GET")) {
      log.debug("isValidUser");
    }
  }
}
