package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Tag(name = "ConfigController")
@RequestScope
public class ConfigController {
  private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

  @Autowired
  ConfigManager configManager;

  @GetMapping(value = "public/monitoring")
  @Operation(
    summary = "Retrieve monitoring configuration",
    description = "Fetches monitoring configuration details from the config manager"
  )
  public String getMonitoring() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.Monitoring.GET")) {
      LOG.debug("getMonitoring");
      return configManager.getConfig(CONFIG.MONITORING, new TypeReference<>() {
      });
    }
  }
}
