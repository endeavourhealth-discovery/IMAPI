package org.endeavourhealth.imapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.endeavourhealth.imapi.logic.service.CasdoorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtCookieAuthFilter extends OncePerRequestFilter {
  CasdoorService casdoorService = new CasdoorService();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    try {
      String token = extractTokenFromCookie(request, "access_token");
      if (token != null) {
        boolean isValid = casdoorService.validateToken(token);
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
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Token validation failed: " + e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
    if (request.getCookies() == null) return null;
    for (Cookie cookie : request.getCookies()) {
      if (cookie.getName().equals(cookieName)) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
