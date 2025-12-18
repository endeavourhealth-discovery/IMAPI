package org.endeavourhealth.imapi.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/casdoor")
@CrossOrigin(origins = "*")
@RequestScope
@Slf4j
public class CasdoorController {
  private CasdoorService casdoorService = new CasdoorService();

  @GetMapping("/user")
  public User getUser(HttpServletRequest request) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.USER.GET")) {
      log.debug("getUser");
      return casdoorService.getUser(request);
    }
  }

  @GetMapping("/user/profileUrl")
  public String getUserProfileUrl(HttpServletRequest request) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.USER.PROFILEURL.GET")) {
      log.debug("getUserProfileUrl");
      return casdoorService.getUserUrl(request);
    }
  }

  @GetMapping("/public/login")
  public void callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletResponse response) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.PUBLIC.LOGIN.GET")) {
      log.debug("login");
      casdoorService.loginUser(code, state, response);
    }
  }

  @GetMapping("/public/loginWithBearerToken")
  public void loginWithToken(HttpServletRequest request, HttpServletResponse response) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.PUBLIC.LOGINWITHBEARERTOKEN.GET")) {
      log.debug("loginWithBearerToken");
      casdoorService.loginWithBearerToken(request, response);
    }
  }

  @GetMapping("/logout")
  public void logout(HttpServletResponse response) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.PUBLIC.LOGOUT.GET")) {
      log.debug("logout");
      casdoorService.logout(response);
    }
  }

  @GetMapping("/getUsersInGroup")
  @PreAuthorize("@guard.hasPermission('CASDOOR_USER','READ')")
  public List<User> getUsersInGroup(HttpServletRequest request, @RequestParam(name = "group") UserRole group) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.GETUSERSINGROUP.GET")) {
      log.debug("getUsersInGroup");
      return casdoorService.adminGetUsersInGroup(group);
    }
  }

  @GetMapping("/getGroups")
  @PreAuthorize("@guard.hasPermission('CASDOR_USER','READ')")
  public List<UserRole> getGroups(HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.GETGROUPS.GET")) {
      log.debug("getGroups");
      return casdoorService.adminGetGroups();
    }
  }

  @GetMapping(value = "/emailTemporaryPasswords")
  @PreAuthorize("@guard.hasPermission('ADMIN','WRITE')")
  public void emailTemporaryPasswords(@RequestParam(name = "filePath") String path) throws IOException, MessagingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.CASDOOR.emailTemporaryPasswords.POST")) {
      log.debug("emailTemporaryPasswords");
      casdoorService.emailTemporaryPasswords(path);
    }
  }
}
