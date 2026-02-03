package org.endeavourhealth.imapi.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.security.Action;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.security.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component("guard")
public class APIGuard {
  private SecurityService securityService = new SecurityService();

  public boolean hasPermission(Resource resource, Action action) throws JsonProcessingException, UserAuthorisationException, UserNotFoundException {
    log.debug("Checking permission [{}]/[{}]", resource, action);
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    if (attributes == null) return false;
    HttpServletRequest request = attributes.getRequest();
    User user = securityService.getUser(request);
    return securityService.enforce(user, resource, action);
  }
}