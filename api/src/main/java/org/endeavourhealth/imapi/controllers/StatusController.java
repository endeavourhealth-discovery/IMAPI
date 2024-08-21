package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
}
