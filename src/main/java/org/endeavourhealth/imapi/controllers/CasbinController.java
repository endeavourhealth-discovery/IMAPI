package org.endeavourhealth.imapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.casbin.Action;
import org.endeavourhealth.imapi.model.casbin.Resource;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/casbin")
@CrossOrigin(origins = "*")
@Slf4j
public class CasbinController {
  private CasdoorService casdoorService = new CasdoorService();
  private CasbinEnforcer casbinEnforcer = new CasbinEnforcer();

  @GetMapping("public/hasPermission")
  public boolean hasPermission(HttpServletRequest request, @RequestParam(name = "resource") Resource resource, @RequestParam(name = "action") Action action) throws UserNotFoundException, UserAuthorisationException, IOException {
    User user = casdoorService.getUser(request);
    return casbinEnforcer.enforce(user, resource, action);
  }
}
