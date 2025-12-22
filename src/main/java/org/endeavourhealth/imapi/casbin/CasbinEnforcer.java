package org.endeavourhealth.imapi.casbin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import org.casbin.casdoor.config.CasdoorConfiguration;
import org.casbin.casdoor.service.EnforcerService;
import org.casbin.casdoor.util.http.HttpClient;
import org.endeavourhealth.imapi.config.CasdoorConfig;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.casbin.Action;
import org.endeavourhealth.imapi.model.casbin.Resource;
import org.endeavourhealth.imapi.model.casdoor.CasdoorResponseExt;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CasbinEnforcer {
  private CasdoorService casdoorService = new CasdoorService();
  private ObjectMapper om = new ObjectMapper();
  private CasdoorConfiguration casdoorConfiguration = new CasdoorConfig().getCasdoorConfiguration();
  private EnforcerService enforcerService = new EnforcerService(casdoorConfiguration);

  public CasbinEnforcer() {
  }

  public static String mapToUrlParams(@Nullable java.util.Map<String, String> map) {
    if (map == null || map.isEmpty()) {
      return "";
    }
    return map.entrySet()
      .stream()
      .map(entry -> entry.getKey() + "=" + entry.getValue())
      .collect(Collectors.joining("&"));
  }

  public void enforceWithError(User user, Resource resource, Action action) throws UserAuthorisationException {
    try {
      boolean result = enforce(user, resource, action);
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
//    return enforcerService.enforce(null, null, null, "IMAPI", null, new Object[]{user, resource.name(), action.name()});
    byte[] postBytes = om.writeValueAsBytes(new Object[]{user, resource.name(), action.name()});
    Map<String, String> queryParams = org.casbin.casdoor.util.Map.of(
      "enforcerId", "IMAPI"
    );
    String url = String.format("%s/api/%s?%s", casdoorConfiguration.endpoint, "enforce", mapToUrlParams(queryParams));
    String credential = Credentials.basic(casdoorConfiguration.clientId, casdoorConfiguration.clientSecret);
    String response = HttpClient.postString(url, new String(postBytes, StandardCharsets.UTF_8), credential);
    CasdoorResponseExt<Boolean[], Object> resp = om.readValue(response, CasdoorResponseExt.class);
    if (!Objects.equals(resp.getStatus(), "ok")) {
      throw new org.casbin.casdoor.exception.Exception(String.format("Failed fetching %s : %s", url, resp.getMsg()));
    }
    return Arrays.stream(resp.getData()).allMatch(Boolean::booleanValue);
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
