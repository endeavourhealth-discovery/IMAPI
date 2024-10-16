package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.GraphDtoService;
import org.endeavourhealth.imapi.model.dto.GraphDto;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;

@RestController
@RequestMapping("api/graphDto")
@CrossOrigin(origins = "*")
@Tag(name = "GraphDto Controller")
@RequestScope
public class GraphDtoController {
  private static final Logger LOG = LoggerFactory.getLogger(GraphDtoController.class);
  private final GraphDtoService graphDtoService = new GraphDtoService();

  @GetMapping(value = "/public/graph")
  public GraphDto getGraphData(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Graph.Graph.GET")) {
      LOG.debug("getGraphData");
      return graphDtoService.getGraphData(iri);
    }
  }
}
