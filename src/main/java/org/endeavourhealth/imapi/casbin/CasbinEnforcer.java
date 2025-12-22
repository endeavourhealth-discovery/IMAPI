package org.endeavourhealth.imapi.casbin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.casbin.casdoor.service.EnforcerService;
import org.endeavourhealth.imapi.config.CasdoorConfig;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.casbin.Action;
import org.endeavourhealth.imapi.model.casbin.Resource;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CasbinEnforcer {
  private CasdoorService casdoorService = new CasdoorService();
  private ObjectMapper om = new ObjectMapper();
  private EnforcerService enforcerService = new EnforcerService(new CasdoorConfig().getCasdoorConfiguration());

  public CasbinEnforcer() {
  }

  public void enforceWithError(User user, Resource resource, Action action) throws UserAuthorisationException {
    try {
      boolean result = enforcerService.enforce(null, null, null, "IMAPI", null, new Object[]{user, resource.name(), action.name()});
      if (!result) {
        throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, resource, action));
      }
    } catch (Exception e) {
      throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, resource, action));
    }
  }

  public void enforceWithError(HttpServletRequest request, Resource resource, Action action) throws UserAuthorisationException, UserNotFoundException, IOException {
    User user = casdoorService.getUser(request);
    enforceWithError(user, resource, action);
  }

  public boolean enforce(User user, Resource resource, Action action) throws IOException {
    return enforcerService.enforce(null, null, null, "IMAPI", null, new Object[]{user, resource.name(), action.name()});
  }

  public void enforceOr(HttpServletRequest request, Resource resource, List<Action> accessRights) throws UserAuthorisationException, UserNotFoundException, IOException {
    User user = casdoorService.getUser(request);
    List<Boolean> results = new ArrayList<>();
    for (Action accessRight : accessRights) {
      results.add(enforce(user, resource, accessRight));
    }
    if (results.stream().noneMatch(r -> r)) {
      throw new UserAuthorisationException(String.format("User %s not authorised to access resource %s with rights %s", user, resource, accessRights));
    }
  }
}
