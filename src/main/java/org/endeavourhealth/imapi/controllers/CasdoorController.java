package org.endeavourhealth.imapi.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
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
  private CasdoorService casdoorService = new CasdoorService();

  @GetMapping("user")
  public void setUserCookie(HttpSession session) {
    casdoorService.setUserCookie(session);
  }

  @GetMapping("public/login")
  public void callback(String code, String state, HttpSession session) {
    casdoorService.loginUser(code, state, session);
  }

  @GetMapping("logout")
  public void logout(HttpSession session) {
    casdoorService.logout(session);
  }
}
