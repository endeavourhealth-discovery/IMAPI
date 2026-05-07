package org.endeavourhealth.imapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.endeavourhealth.imapi.logic.service.EndeavourSecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionCookieAuthFilterTest {

    private SessionCookieAuthFilter filter;
    private EndeavourSecurityService endeavourSecurityService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        endeavourSecurityService = mock(EndeavourSecurityService.class);
        filter = new SessionCookieAuthFilter().setEndeavourSecurityService(endeavourSecurityService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_WithValidSession_SetsAuthentication() throws ServletException, IOException {
        // Arrange
        String sessionId = "valid-session-id";
        String ip = "127.0.0.1";
        Cookie cookie = new Cookie("session_id", sessionId);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(request.getRemoteAddr()).thenReturn(ip);
        when(endeavourSecurityService.introspect(ip, sessionId)).thenReturn(true);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidSession_ClearsCookieAndSets401() throws ServletException, IOException {
        // Arrange
        String sessionId = "invalid-session-id";
        String ip = "127.0.0.1";
        Cookie cookie = new Cookie("session_id", sessionId);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(request.getRemoteAddr()).thenReturn(ip);
        when(endeavourSecurityService.introspect(ip, sessionId)).thenReturn(false);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(response).addCookie(any(Cookie.class));
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNoSessionCookie_ContinuesChain() throws ServletException, IOException {
        // Arrange
        when(request.getCookies()).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(endeavourSecurityService, never()).introspect(anyString(), anyString());
    }

    @Test
    void doFilterInternal_WithOtherCookies_ContinuesChain() throws ServletException, IOException {
        // Arrange
        Cookie cookie = new Cookie("other_cookie", "value");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
        verify(endeavourSecurityService, never()).introspect(anyString(), anyString());
    }

    @Test
    void doFilterInternal_WithMultipleCookies_FindsSessionCookie() throws ServletException, IOException {
        // Arrange
        String sessionId = "valid-session-id";
        String ip = "127.0.0.1";
        Cookie otherCookie = new Cookie("other", "value");
        Cookie sessionCookie = new Cookie("session_id", sessionId);
        when(request.getCookies()).thenReturn(new Cookie[]{otherCookie, sessionCookie});
        when(request.getRemoteAddr()).thenReturn(ip);
        when(endeavourSecurityService.introspect(ip, sessionId)).thenReturn(true);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(endeavourSecurityService).introspect(ip, sessionId);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithForwardedIp_UsesForwardedIp() throws ServletException, IOException {
        // Arrange
        String sessionId = "valid-session-id";
        String forwardedIp = "10.0.0.1";
        Cookie cookie = new Cookie("session_id", sessionId);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(request.getHeader("x-forwarded-for")).thenReturn(forwardedIp);
        when(endeavourSecurityService.introspect(forwardedIp, sessionId)).thenReturn(true);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(endeavourSecurityService).introspect(forwardedIp, sessionId);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
