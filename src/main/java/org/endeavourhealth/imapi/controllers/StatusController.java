package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.utility.EnvHelper;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
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
@Slf4j
public class StatusController {

  @Operation(summary = "Check the health status of the application")
  @GetMapping("/public/healthCheck")
  public ResponseEntity<String> healthCheck() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Status.HealthCheck.GET")) {
      log.debug("healthCheck");
      return ResponseEntity.ok("OK");
    }
  }

  @Operation(summary = "Check if the application is running in public mode")
  @GetMapping("/public/isPublicMode")
  public ResponseEntity<Boolean> isPublicMode() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Status.isPublicMode.GET")) {
      log.debug("isPublicMode");
      return ResponseEntity.ok(EnvHelper.isPublicMode());
    }
  }

  @Operation(summary = "Check if the application is running in development mode")
  @GetMapping("/public/isDevMode")
  public ResponseEntity<Boolean> isDevMode() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Status.isDevMode.GET")) {
      log.debug("isDevMode");
      return ResponseEntity.ok(EnvHelper.isDevMode());
    }
  }
}
