package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.model.config.ComponentLayoutItem;
import org.endeavourhealth.imapi.model.config.FilterDefault;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/config")
@CrossOrigin(origins = "*")
@Tag(name = "ConfigController")
@RequestScope
public class ConfigController {
  private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);

  @Autowired
  ConfigManager configManager;

  @GetMapping(value = "/public/componentLayout")
  public List<ComponentLayoutItem> getComponentLayout(@RequestParam(name = "name") String name) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.ComponentLayout.GET")) {
      LOG.debug("getComponentLayout");
      if ("definition".equals(name))
        return configManager.getConfig(CONFIG.DEFINITION, new TypeReference<>() {
        });
      if ("summary".equals(name))
        return configManager.getConfig(CONFIG.SUMMARY, new TypeReference<>() {
        });
      else
        throw new IllegalArgumentException("Unknown component layout config");
    }
  }

  @GetMapping(value = "public/monitoring")
  public String getMonitoring() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.Monitoring.GET")) {
      LOG.debug("getMonitoring");
      return configManager.getConfig(CONFIG.MONITORING, new TypeReference<>() {
      });
    }
  }

  @GetMapping(value = "/public/filterDefaults")
  public FilterDefault getFilterDefaults() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.FilterDefaults.GET")) {
      LOG.debug("getFilterDefaults");
      return configManager.getConfig(CONFIG.FILTER_DEFAULTS, new TypeReference<>() {
      });
    }
  }

  @GetMapping(value = "/public/coreSchemes")
  public List<String> getCoreSchemes() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.CoreSchemes.GET")) {
      LOG.debug("getCoreSchemes");
      return configManager.getConfig(CONFIG.CORE_SCHEMES, new TypeReference<>() {
      });
    }
  }
}
