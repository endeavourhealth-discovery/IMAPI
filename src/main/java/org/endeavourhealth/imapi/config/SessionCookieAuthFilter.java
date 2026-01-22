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

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.endeavourhealth.imapi.utility.IpExtractor.getIpAddress;

public class SessionCookieAuthFilter extends OncePerRequestFilter {
  CasdoorService casdoorService = new CasdoorService();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    try {
      String sessionId = extractValueFromCookie(request, "session_id");
      if (sessionId != null) {
        Session session = casdoorService.getSession(sessionId, getIpAddress(request));
        boolean isValid = casdoorService.validateToken(session.getAccess_token());
        if (isValid) {
          TokenAuthenticated authentication = new TokenAuthenticated();
          authentication.setAuthenticated(true);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          throw new AuthenticationException("Invalid or expired token");
        }
      }
      filterChain.doFilter(request, response);
    } catch (NoSuchElementException e) {
      Cookie sessionIdCookie = new Cookie("session_id", "");
      sessionIdCookie.setPath("/");
      sessionIdCookie.setHttpOnly(true);
      sessionIdCookie.setMaxAge(0);
      response.addCookie(sessionIdCookie);
      throw new AuthenticationException("Invalid or expired session");
    } catch (Exception e) {
      throw new AuthenticationException("Authentication failed");
    }
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
