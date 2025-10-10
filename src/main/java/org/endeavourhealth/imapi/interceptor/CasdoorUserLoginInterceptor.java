package org.endeavourhealth.imapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class CasdoorUserLoginInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    if (session.getAttribute("casdoorUser") == null) {
      response.sendRedirect(request.getContextPath() + "toLogin");
      return false;
    }
    return true;
  }
}
