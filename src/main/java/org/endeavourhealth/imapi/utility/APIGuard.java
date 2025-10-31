package org.endeavourhealth.imapi.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.admin.User;
import org.endeavourhealth.imapi.model.casbin.Action;
import org.endeavourhealth.imapi.model.casbin.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component("guard")
public class APIGuard {
  private CasbinEnforcer casbinEnforcer = new CasbinEnforcer();
  private CasdoorService casdoorService = new CasdoorService();

  public boolean hasPermission(Resource resource, Action action) throws JsonProcessingException, UserAuthorisationException, UserNotFoundException {
    log.debug("Checking permission [{}]/[{}]", resource, action);
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    if (attributes == null) return false;
    HttpServletRequest request = attributes.getRequest();
    User user = casdoorService.getUser(request);
    return casbinEnforcer.enforce(user, resource, action);
  }
}