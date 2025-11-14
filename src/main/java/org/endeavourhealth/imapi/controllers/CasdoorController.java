package org.endeavourhealth.imapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

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
    return casdoorService.getUser(request);
  }

  @GetMapping("user/profileUrl")
  public String getUserProfileUrl(HttpServletRequest request) throws UserNotFoundException {
    return casdoorService.getUserUrl(request);
  }

  @GetMapping("/public/login")
  public void callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletResponse response) {
    casdoorService.loginUser(code, state, response);
  }

  @GetMapping("/public/loginWithBearerToken")
  public void loginWithToken(HttpServletRequest request, HttpServletResponse response) {
    casdoorService.loginWithBearerToken(request, response);
  }

  @GetMapping("/logout")
  public void logout(HttpServletResponse response) {
    casdoorService.logout(response);
  }

  @GetMapping("getUsersInGroup")
  @PreAuthorize("@guard.hasPermission('CASDOOR_USER','READ')")
  public List<User> getUsersInGroup(HttpServletRequest request, @RequestParam(name = "group") UserRole group) throws UserNotFoundException, UserAuthorisationException {
    return casdoorService.adminGetUsersInGroup(group);
  }

  @GetMapping("getGroups")
  @PreAuthorize("@guard.hasPermission('CASDOR_USER','READ')")
  public List<UserRole> getGroups(HttpServletRequest request) throws UserNotFoundException, UserAuthorisationException {
    return casdoorService.adminGetGroups();
  }
}
