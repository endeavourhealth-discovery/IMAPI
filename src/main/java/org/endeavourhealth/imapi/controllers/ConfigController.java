package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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
  public String getMonitoring() throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.Monitoring.GET")) {
      log.debug("getMonitoring");
      return configManager.getConfig(CONFIG.MONITORING, new TypeReference<>() {
      });
    }
  }

  @GetMapping(value = "public/graphs")
  @Operation(summary = "Get the list of available graphs")
  public List<Graph> getGraphs() throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Config.Monitoring.GET")) {
      log.debug("getGraphs");
      List<Graph> graphs = new ArrayList<>(EnumSet.allOf(Graph.class));
      graphs.remove(Graph.CONFIG);
      graphs.remove(Graph.WORKFLOW);
      graphs.remove(Graph.USER);
      graphs.remove(Graph.PROV);
      return graphs;
    }
  }
}
