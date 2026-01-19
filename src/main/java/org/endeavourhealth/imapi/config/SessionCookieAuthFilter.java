package org.endeavourhealth.imapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.endeavourhealth.imapi.model.casdoor.Session;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

public class SessionCookieAuthFilter extends OncePerRequestFilter {
  CasdoorService casdoorService = new CasdoorService();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    try {
      String sessionId = extractValueFromCookie(request, "session_id");
      if (sessionId != null) {
        Session session = casdoorService.getSession(sessionId);
        boolean isValid = casdoorService.validateToken(session.getAccess_token());
        if (isValid) {
          TokenAuthenticated authentication = new TokenAuthenticated();
          authentication.setAuthenticated(true);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.getWriter().write("Invalid or expired token");
          return;
        }
      }
    } catch (NoSuchElementException e) {
      Cookie sessionIdCookie = new Cookie("session_id", "");
      sessionIdCookie.setPath("/");
      sessionIdCookie.setHttpOnly(true);
      sessionIdCookie.setMaxAge(0);
      response.addCookie(sessionIdCookie);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Session validation failed: " + e.getMessage());
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token validation failed: " + e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  private String extractValueFromCookie(HttpServletRequest request, String cookieName) {
    if (request.getCookies() == null) return null;
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals(cookieName)) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
