package org.endeavourhealth.imapi.controllers;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.entity.User;
import org.casbin.casdoor.exception.AuthException;
import org.casbin.casdoor.service.AuthService;
import org.casbin.casdoor.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/casdoor")
@CrossOrigin(origins = "*")
@RequestScope
@Slf4j
public class CasdoorController {
  @Resource
  private AuthService casdoorAuthService;

  @Resource
  private UserService casdoorUserService;

  @GetMapping("user")
  public void getUser(HttpSession session) {
    User user = casdoorAuthService.parseJwtToken((String) session.getAttribute("token"));
    session.setAttribute("casdoorUser", user);
  }

  @GetMapping("public/login")
  public void callback(String code, String state, HttpSession session) {
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

  @GetMapping("logout")
  public void logout(HttpSession session) {
    session.setAttribute("casdoorUser", null);
  }
}
