package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.responses.LoginResponse;
import org.endeavourhealth.imapi.model.security.*;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("api/security")
@CrossOrigin(origins = "*")
@RequestScope
@Slf4j
public class SecurityController {
  private SecurityService securityService = new SecurityService();

  @GetMapping("/public/login")
  public LoginResponse login(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletRequest request, HttpServletResponse response) throws HttpException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.PUBLIC.LOGIN.GET")) {
      log.debug("login");
      return securityService.loginUser(code, state, request, response);
    }
  }

  @GetMapping("/public/loginUrl")
  public String getLoginUrl(@RequestParam(name = "redirectUrl") String redirectUrl, HttpServletRequest request) throws HttpException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.PUBLIC.LOGINURL.GET")) {
      log.debug("loginUrl");
      return securityService.getLoginUrl(redirectUrl, request);
    }
  }

  @GetMapping("/public/registerUrl")
  public String getRegisterUrl(HttpServletRequest request, @RequestParam(name = "redirectUrl") String redirectUrl) throws UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.PUBLIC.GETREGISTERURL.GET")) {
      log.debug("getRegisterUrl");
      return securityService.getRegisterUrl(request, redirectUrl);
    }
  }

  @GetMapping("/private/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) throws HttpException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.PUBLIC.LOGOUT.GET")) {
      log.debug("logout");
      securityService.logout(request, response);
    }
  }

  @GetMapping("/private/getUsersInGroup")
  public List<User> getUsersInGroup(HttpServletRequest request, @RequestParam(name = "group") UserRole group) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.GETUSERSINGROUP.GET")) {
      log.debug("getUsersInGroup");
      securityService.requiresPermission(new Permission(Resource.USER, List.of(UserRole.TASK_MANAGER, UserRole.DEVELOPER), List.of()), request);
      return securityService.adminGetUsersInGroup(group, request);
    }
  }

  @GetMapping("/private/getGroups")
  public List<UserRole> getGroups(HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.GETGROUPS.GET")) {
      log.debug("getGroups");
      securityService.requiresPermission(new Permission(Resource.USER, List.of(UserRole.TASK_MANAGER, UserRole.DEVELOPER), List.of()), request);
      return securityService.adminGetGroups();
    }
  }

/*  @GetMapping(value = "/emailTemporaryPasswords")
  @PreAuthorize("@guard.hasPermission('ADMIN','WRITE')")
  public void emailTemporaryPasswords(@RequestParam(name = "filePath") String path) throws MessagingException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.emailTemporaryPasswords.POST")) {
      log.debug("emailTemporaryPasswords");
      securityService.emailTemporaryPasswords(path);
    }
  }*/

/*  @PostMapping("/private/addPolicy")
  public void addPolicy(HttpServletRequest request, PolicyRequest policyRequest) throws UserNotFoundException {
    securityService.addPolicy(policyRequest.getUserRole(), policyRequest.getResource(), policyRequest.getAction());
  }*/

  @GetMapping("/private/user")
  public User getUser(HttpServletRequest request) throws UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.USER.GET")) {
      log.debug("getUser");
      return securityService.getUser(request);
    }
  }

  @GetMapping("/private/user/profileUrl")
  public String getUserProfileUrl(HttpServletRequest request) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.USER.PROFILEURL.GET")) {
      log.debug("getUserProfileUrl");
      return securityService.getUserUrl(request);
    }
  }
}
