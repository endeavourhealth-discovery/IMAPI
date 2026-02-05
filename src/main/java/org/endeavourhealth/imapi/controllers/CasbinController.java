package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.security.Permission;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/casbin")
@CrossOrigin(origins = "*")
@Slf4j
public class CasbinController {
  private SecurityService securityService = new SecurityService();

  @PostMapping("public/hasPermission")
  public boolean hasPermission(HttpServletRequest request, @RequestBody Permission permission) throws UserNotFoundException, UserAuthorisationException, JsonProcessingException {
    return securityService.hasPermission(permission, request);
  }
}
