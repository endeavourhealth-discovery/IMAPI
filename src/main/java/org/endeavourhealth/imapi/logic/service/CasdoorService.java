package org.endeavourhealth.imapi.logic.service;

import jakarta.servlet.http.HttpSession;
import org.casbin.casdoor.config.CasdoorConfiguration;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.UserService;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
  private String certificate = System.getenv("CASDOOR_CERTIFICATE");
  private String applicationName = System.getenv("CASDOOR_APPLICATION_NAME");
  private String clientSecret = System.getenv("CASDOOR_CLIENT_SECRET");
  private String organisationName = System.getenv("CASDOOR_ORGANISATION_NAME");

  public CasdoorService() {
    casdoorConfiguration = new CasdoorConfiguration();
    casdoorConfiguration.setApplicationName(applicationName);
    casdoorConfiguration.setClientId(clientId);
    casdoorConfiguration.setEndpoint(endpoint);
    casdoorConfiguration.setCertificate(certificate);
    casdoorConfiguration.setClientSecret(clientSecret);
    casdoorConfiguration.setOrganizationName(organisationName);
    casdoorAuthService = new AuthService(casdoorConfiguration);
    casdoorUserService = new UserService(casdoorConfiguration);
  }

  public User getUser(HttpSession session) throws UserNotFoundException {
    User user = (User) session.getAttribute("casdoorUser");
    if (user == null) throw new UserNotFoundException("User session not found");
    return user;
  }

  private User parseUser(org.casbin.casdoor.entity.User casdoorUser) {
    User user = new User();
    user.setId(casdoorUser.id);
    user.setFirstName(casdoorUser.firstName);
    user.setLastName(casdoorUser.lastName);
    user.setEmail(casdoorUser.email);
    user.setUsername(casdoorUser.name);
    user.setAvatar(casdoorUser.avatar);
    return user;
  }

  public void loginUser(String code, String state, HttpSession session) {
    String token = "";
    User user = null;
    try {
      token = casdoorAuthService.getOAuthToken(code, state);
      org.casbin.casdoor.entity.User casdoorUser = casdoorAuthService.parseJwtToken(token);
      user = parseUser(casdoorUser);
    } catch (AuthException e) {
      e.printStackTrace();
    }
    session.setAttribute("casdoorUser", user);
  }

  public void logout(HttpSession session) {
    session.invalidate();
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
