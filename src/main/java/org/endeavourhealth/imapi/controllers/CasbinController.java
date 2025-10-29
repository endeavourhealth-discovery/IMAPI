package org.endeavourhealth.imapi.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.casbin.PolicyRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
