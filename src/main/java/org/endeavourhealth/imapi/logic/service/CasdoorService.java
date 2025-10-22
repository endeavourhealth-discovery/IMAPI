package org.endeavourhealth.imapi.logic.service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.UserService;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.model.admin.User;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CasdoorService {
  @Resource
  private AuthService casdoorAuthService;

  @Resource
  private UserService casdoorUserService;

  public CasdoorService() {
  }

  public void setUserCookie(HttpSession session) {
    User user = getUser(session);
    session.setAttribute("casdoorUser", user);
  }

  public User getUser(HttpSession session) {
    org.casbin.casdoor.entity.User casdoorUser = casdoorAuthService.parseJwtToken((String) session.getAttribute("token"));
    return parseUser(casdoorUser);
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
    session.setAttribute("casdoorToken", token);
    session.setAttribute("casdoorUser", user);
  }

  public void logout(HttpSession session) {
    session.setAttribute("casdoorUser", null);
  }

  public User adminGetUser(String userId) throws UserNotFoundException {
    try {
      org.casbin.casdoor.entity.User casdoorUser = casdoorUserService.getUser(userId);
      return parseUser(casdoorUser);
    } catch (IOException e) {
      throw new UserNotFoundException(userId);
    }
  }
}
