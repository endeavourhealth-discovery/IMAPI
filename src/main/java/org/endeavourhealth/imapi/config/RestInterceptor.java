package org.endeavourhealth.imapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.logic.service.UserService;
import org.endeavourhealth.imapi.model.casdoor.User;
import org.endeavourhealth.imapi.utility.ThreadContext;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestInterceptor implements HandlerInterceptor {
  private final UserService userService = new UserService();
  private final CasdoorService casdoorService = new CasdoorService();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    try {
      User user = casdoorService.getUser(request);
      if (user == null) {
        ThreadContext.setUserGraphs(new ArrayList<>());
        return true;
      }
      List<Graph> userGraphs = userService.getUserGraphs(user.getId());
      ThreadContext.setUserGraphs(userGraphs);
    } catch (UserNotFoundException e) {
      return true;
    }
    return true; // Continue processing the request
  }
}
