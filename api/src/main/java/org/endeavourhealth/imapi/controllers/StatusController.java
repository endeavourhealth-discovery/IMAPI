package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.utility.EnvHelper;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;

@RestController
@RequestMapping("api/status")
@CrossOrigin(origins = "*")
@Tag(name = "StatusController")
@RequestScope
public class StatusController {
  private static final Logger LOG = LoggerFactory.getLogger(StatusController.class);

  @GetMapping("/public/healthCheck")
  public ResponseEntity<String> healthCheck() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Status.HealthCheck.GET")) {
      LOG.debug("healthCheck");
      return ResponseEntity.ok("OK");
    }
  }

  @GetMapping("/public/isPublicMode")
  public ResponseEntity<Boolean> isPublicMode() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Status.isPublicMode.GET")) {
      LOG.debug("isPublicMode");
      return ResponseEntity.ok(EnvHelper.isPublicMode());
    }
  }

  @GetMapping("/public/isDevMode")
  public ResponseEntity<Boolean> isDevMode() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Status.isDevMode.GET")) {
      LOG.debug("isDevMode");
      return ResponseEntity.ok(EnvHelper.isDevMode());
    }
  }
}
