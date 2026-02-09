package org.endeavourhealth.imapi.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.EndeavourSecurityService;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.security.Permission;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.endeavourhealth.imapi.utility.IpExtractor.getIpAddress;

@Slf4j
@Component("guard")
public class APIGuard {
  private SecurityService securityService = new SecurityService();
  private EndeavourSecurityService endeavourSecurityService = new EndeavourSecurityService();

  public boolean hasPermission(Permission permission) throws JsonProcessingException, UserAuthorisationException, UserNotFoundException {
    log.debug("Checking permission [{}]", permission);
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    String sessionId = securityService.getSessionId(request);
    String ipAddress = getIpAddress(request);
    return endeavourSecurityService.hasPermission(ipAddress, sessionId, permission);
  }
}