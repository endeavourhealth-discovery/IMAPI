package org.endeavourhealth.imapi.errorhandling;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
    log.debug("handle");
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    try (OutputStream out = httpServletResponse.getOutputStream()) {
      out.write("Access denied".getBytes(StandardCharsets.UTF_8));
    }
  }
}
