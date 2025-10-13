package org.endeavourhealth.imapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.casbin.casdoor.entity.User;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.logic.service.UserService;
import org.endeavourhealth.imapi.utility.ThreadContext;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class RestInterceptor implements HandlerInterceptor {
  private final UserService userService = new UserService();
  private final CasdoorService casdoorService = new CasdoorService();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    User user = casdoorService.getUser(request.getSession());
    List<Graph> userGraphs = userService.getUserGraphs(user.id);
    ThreadContext.setUserGraphs(userGraphs);
    return true; // Continue processing the request
  }
}
