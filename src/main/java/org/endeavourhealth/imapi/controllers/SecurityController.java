package org.endeavourhealth.imapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.security.Action;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

/**
 * Signing in and out is handled by the frontends directly against Casdoor (OIDC); the API only validates the resulting access token.
 */
@RestController
@RequestMapping("api/security")
@CrossOrigin(origins = "*")
@RequestScope
@Slf4j
public class SecurityController {
  private SecurityService securityService = new SecurityService();

  @GetMapping("/private/getUsersInGroup")
  public List<User> getUsersInGroup(HttpServletRequest request, @RequestParam(name = "group") UserRole group) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.GETUSERSINGROUP.GET")) {
      log.debug("getUsersInGroup");
      securityService.requiresPermission(Resource.USER, Action.READ);
      return securityService.adminGetUsersInGroup(group);
    }
  }

  @GetMapping("/private/getGroups")
  public List<UserRole> getGroups(HttpServletRequest request) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.GETGROUPS.GET")) {
      log.debug("getGroups");
      securityService.requiresPermission(Resource.USER, Action.READ);
      return securityService.adminGetGroups();
    }
  }

  @GetMapping("/private/user")
  public User getUser(HttpServletRequest request) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.USER.GET")) {
      log.debug("getUser");
      return securityService.getUser();
    }
  }

  @GetMapping("/private/user/profileUrl")
  public String getUserProfileUrl(HttpServletRequest request) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.USER.PROFILEURL.GET")) {
      log.debug("getUserProfileUrl");
      return securityService.getUserUrl();
    }
  }
}
