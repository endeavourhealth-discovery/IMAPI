package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.casbin.Action;
import org.endeavourhealth.imapi.model.casbin.PolicyRequest;
import org.endeavourhealth.imapi.model.casbin.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/casbin")
@CrossOrigin(origins = "*")
@Slf4j
public class CasbinController {
  private CasbinEnforcer casbinEnforcer = new CasbinEnforcer();
  private CasdoorService casdoorService = new CasdoorService();

  @PostMapping("addPolicy")
  @PreAuthorize("@guard.hasPermission('POLICY','WRITE')")
  public void addPolicy(HttpServletRequest request, PolicyRequest policyRequest) throws UserNotFoundException {
    casbinEnforcer.addPolicy(policyRequest.getUserRole(), policyRequest.getResource(), policyRequest.getAction());
  }

  @GetMapping("hasPermission")
  public boolean hasPermission(HttpServletRequest request, @RequestParam(name = "resource") Resource resource, @RequestParam(name = "action") Action action) throws UserNotFoundException, UserAuthorisationException, JsonProcessingException {
    User user = casdoorService.getUser(request);
    return casbinEnforcer.enforce(user, resource, action);
  }
}
