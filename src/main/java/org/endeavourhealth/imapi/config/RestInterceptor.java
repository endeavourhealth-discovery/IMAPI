package org.endeavourhealth.imapi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.utility.ThreadContext;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class RestInterceptor implements HandlerInterceptor {
  private final RequestObjectService requestObjectService = new RequestObjectService();

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    List<Graph> userGraphs = requestObjectService.getUserGraphs(request);
    ThreadContext.setUserGraphs(userGraphs);
    return true; // Continue processing the request
  }
}
