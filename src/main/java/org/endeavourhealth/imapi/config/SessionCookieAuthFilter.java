package org.endeavourhealth.imapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.endeavourhealth.imapi.logic.service.EndeavourSecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

import static org.endeavourhealth.imapi.utility.IpExtractor.getIpAddress;

public class SessionCookieAuthFilter extends OncePerRequestFilter {
  EndeavourSecurityService endeavourSecurityService = new EndeavourSecurityService();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    String ip = getIpAddress(request);
    String sessionId = extractValueFromCookie(request, "session_id");
    if (sessionId != null) {
      boolean isValid = endeavourSecurityService.introspect(ip, sessionId);
      if (isValid) {
        TokenAuthenticated authentication = new TokenAuthenticated();
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } else {
        Cookie sessionIdCookie = new Cookie("session_id", "");
        sessionIdCookie.setPath("/");
        sessionIdCookie.setHttpOnly(true);
        sessionIdCookie.setMaxAge(0);
        response.addCookie(sessionIdCookie);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
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
