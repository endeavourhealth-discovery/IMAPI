package org.endeavourhealth.imapi.logic.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.casbin.casdoor.config.CasdoorConfiguration;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.UserService;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CasdoorService {
  private CasdoorConfiguration casdoorConfiguration;
  private AuthService casdoorAuthService;
  private UserService casdoorUserService;

  private String clientId = System.getenv("CASDOOR_CLIENT_ID");
  private String endpoint = System.getenv("CASDOOR_ENDPOINT");
  private String certificate;
  private String applicationName = System.getenv("CASDOOR_APPLICATION_NAME");
  private String clientSecret = System.getenv("CASDOOR_CLIENT_SECRET");
  private String organisationName = System.getenv("CASDOOR_ORGANISATION_NAME");

  public CasdoorService() {
    casdoorConfiguration = new CasdoorConfiguration();
    casdoorConfiguration.setApplicationName(applicationName);
    casdoorConfiguration.setClientId(clientId);
    casdoorConfiguration.setEndpoint(endpoint);
    try {
      this.certificate = new String(Files.readAllBytes(Paths.get("src/main/resources/casdoor-cert.txt")));
    } catch (IOException e) {
      e.printStackTrace();
    }
    casdoorConfiguration.setCertificate(certificate);
    casdoorConfiguration.setClientSecret(clientSecret);
    casdoorConfiguration.setOrganizationName(organisationName);
    casdoorAuthService = new AuthService(casdoorConfiguration);
    casdoorUserService = new UserService(casdoorConfiguration);
  }

  public boolean validateToken(String token) {
    try {
      org.casbin.casdoor.entity.User user = casdoorAuthService.parseJwtToken(token);
      return user != null;
    } catch (AuthException e) {
      return false;
    }
  }

  public User getUser(HttpServletRequest request) throws UserNotFoundException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("casdoorToken")) {
          String token = cookie.getValue();
          org.casbin.casdoor.entity.User user = casdoorAuthService.parseJwtToken(token);
          return parseUser(user);
        }
      }
    }
    throw new UserNotFoundException("User not found");
  }

  public String getUserUrl(HttpServletRequest request) throws UserNotFoundException {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("casdoorToken")) {
          String token = cookie.getValue();
          return casdoorAuthService.getMyProfileUrl(token);
        }
      }
    }
    throw new UserNotFoundException("User token not found");
  }

  private User parseUser(org.casbin.casdoor.entity.User casdoorUser) {
    User user = new User();
    user.setId(casdoorUser.id);
    user.setFirstName(casdoorUser.firstName);
    user.setLastName(casdoorUser.lastName);
    user.setEmail(casdoorUser.email);
    user.setUsername(casdoorUser.name);
    user.setAvatar(casdoorUser.avatar);
    user.setRoles(casdoorUser.roles.stream().map(role -> UserRole.valueOf(role.name)).collect(Collectors.toList()));
    return user;
  }

  public void loginUser(String code, String state, HttpServletResponse response) {
    String token = casdoorAuthService.getOAuthToken(code, state);
    Cookie cookie = new Cookie("casdoorToken", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    response.addCookie(cookie);
  }

  public void logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("casdoorToken", "");
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    response.addCookie(cookie);
  }

  public User adminGetUser(String userId) throws UserNotFoundException {
    try {
      org.casbin.casdoor.entity.User casdoorUser = casdoorUserService.getUser(userId);
      return parseUser(casdoorUser);
    } catch (IOException e) {
      throw new UserNotFoundException(userId);
    }
  }

  public List<User> adminGetUsersInGroup(UserRole group) throws UserNotFoundException {
    try {
      List<org.casbin.casdoor.entity.User> casdoorUsers = casdoorUserService.getUsers();
      return casdoorUsers.stream().filter(user -> user.roles.contains(group)).map(user -> parseUser(user)).collect(Collectors.toList());
    } catch (IOException e) {
      throw new UserNotFoundException("Failed to get all users");
    }
  }

  public List<UserRole> adminGetGroups() {
    return Arrays.stream(UserRole.values()).toList();
  }
}
