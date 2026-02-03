package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.responses.LoginResponse;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @GetMapping("/user")
  public User getUser(HttpServletRequest request) throws UserNotFoundException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.USER.GET")) {
      log.debug("getUser");
      return securityService.getUser(request);
    }
  }

  @GetMapping("/user/profileUrl")
  public String getUserProfileUrl(HttpServletRequest request) throws UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.USER.PROFILEURL.GET")) {
      log.debug("getUserProfileUrl");
      return securityService.getUserUrl(request);
    }
  }

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

  @GetMapping("/public/logout")
  public void logout(HttpServletRequest request, HttpServletResponse response) throws HttpException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.PUBLIC.LOGOUT.GET")) {
      log.debug("logout");
      securityService.logout(request, response);
    }
  }

  @GetMapping("/getUsersInGroup")
  @PreAuthorize("@guard.hasPermission('USER','READ')")
  public List<User> getUsersInGroup(HttpServletRequest request, @RequestParam(name = "group") UserRole group) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.GETUSERSINGROUP.GET")) {
      log.debug("getUsersInGroup");
      return securityService.adminGetUsersInGroup(group, request);
    }
  }

  @GetMapping("/getGroups")
  @PreAuthorize("@guard.hasPermission('USER','READ')")
  public List<UserRole> getGroups(HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.SECURITY.GETGROUPS.GET")) {
      log.debug("getGroups");
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
}
