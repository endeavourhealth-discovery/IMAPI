package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.security.Action;
import org.endeavourhealth.imapi.model.security.PolicyRequest;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.security.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/casbin")
@CrossOrigin(origins = "*")
@Slf4j
public class CasbinController {
  private SecurityService securityService = new SecurityService();

  @PostMapping("addPolicy")
  @PreAuthorize("@guard.hasPermission('POLICY','WRITE')")
  public void addPolicy(HttpServletRequest request, PolicyRequest policyRequest) throws UserNotFoundException {
    securityService.addPolicy(policyRequest.getUserRole(), policyRequest.getResource(), policyRequest.getAction());
  }

  @GetMapping("public/hasPermission")
  public boolean hasPermission(HttpServletRequest request, @RequestParam(name = "resource") Resource resource, @RequestParam(name = "action") Action action) throws UserNotFoundException, UserAuthorisationException, JsonProcessingException {
    User user = securityService.getUser(request);
    return securityService.enforce(user, resource, action);
  }
}
