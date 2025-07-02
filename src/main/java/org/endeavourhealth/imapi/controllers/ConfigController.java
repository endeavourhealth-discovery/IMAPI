package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Tag(name = "ConfigController")
@RequestScope
@Slf4j
public class ConfigController {

  @Autowired
  ConfigManager configManager;

  @GetMapping(value = "public/monitoring")
  @Operation(
    summary = "Retrieve monitoring configuration",
    description = "Fetches monitoring configuration details from the config manager"
  )
  public String getMonitoring() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.Monitoring.GET")) {
      log.debug("getMonitoring");
      return configManager.getConfig(CONFIG.MONITORING, new TypeReference<>() {
      });
    }
  }
}
