package org.endeavourhealth.imapi.logic.service;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class CasdoorService {
  @Resource
  private AuthService casdoorAuthService;

  @Resource
  private UserService casdoorUserService;

  public CasdoorService() {
  }

  public void setUserCookie(HttpSession session) {
    User user = casdoorAuthService.parseJwtToken((String) session.getAttribute("token"));
    session.setAttribute("casdoorUser", user);
  }

  public User getUser(HttpSession session) {
    return casdoorAuthService.parseJwtToken((String) session.getAttribute("token"));
  }

  public void loginUser(String code, String state, HttpSession session) {
    String token = "";
    User casdoorUser = null;
    try {
      token = casdoorAuthService.getOAuthToken(code, state);
      casdoorUser = casdoorAuthService.parseJwtToken(token);
    } catch (AuthException e) {
      e.printStackTrace();
    }
    session.setAttribute("casdoorToken", token);
    session.setAttribute("casdoorUser", casdoorUser);
  }

  public void logout(HttpSession session) {
    session.setAttribute("casdoorUser", null);
  }
}
