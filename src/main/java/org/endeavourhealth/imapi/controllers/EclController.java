package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.ECLStatus;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.UnknownFormatConversionException;

@RestController
@RequestMapping("api/ecl")
@CrossOrigin(origins = "*")
@Tag(name = "Ecl Controller", description = "Controller to handle ECL-related requests")
@RequestScope
@Slf4j
public class EclController {

  private final EclService eclService = new EclService();

  @PostMapping("/public/ecl")
  @Operation(
    summary = "Retrieve ECL string",
    description = "Generates an ECL string from the provided IMQ Query object"
  )
  public String getEcl(@RequestBody EclSearchRequest inferred) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("ECL.Ecl.POST")) {
      log.debug("getEcl");
      return eclService.getEcl(inferred);
    }
  }

  @PostMapping(value = "/public/eclSearch", consumes = "application/json", produces = "application/json")
  @Operation(
    summary = "Execute an ECL search",
    description = "Performs a search for entities based on the provided ECL string query"
  )
  public SearchResponse eclSearch(
    @RequestBody EclSearchRequest request
  ) throws EclFormatException, IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("ECL.EclSearch.POST")) {
      return eclService.eclSearch(request);
    } catch (UnknownFormatConversionException ex) {
      throw new EclFormatException("Invalid ECL format", ex);
    }
  }

  @PostMapping("/public/eclFromQuery")
  public String getECLFromQuery(@RequestBody QueryRequest request) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclFromQuery.POST")) {
      log.debug("getEclFromQuery");
      return eclService.getECLFromQuery(request.getQuery(), request.isIncludeNames(), request.getGraph());
    }
  }


  @GetMapping(value = "/public/queryFromEcl", consumes = "text/plain", produces = "application/json")
  @Operation(
    summary = "Convert ECL to Query",
    description = "Transforms a provided ECL string into an IM Query object"
  )
  public Query getQueryFromECL(@RequestParam(name = "ecl") String ecl, @RequestParam(name = "graph", defaultValue = GRAPH.DISCOVERY) String graph) throws IOException, EclFormatException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.QueryFromEcl.POST")) {
      log.debug("getQueryFromEcl");
      return eclService.getQueryFromEcl(ecl, graph);
    }
  }

  @GetMapping(value = "/public/eclFromEcl", consumes = "text/plain", produces = "application/json")
  @Operation(
    summary = "Convert ECL to ECL with names",
    description = "Transforms a provided ECL string into an IM Query object"
  )
  public String getEclFromEcl(@RequestParam(name = "ecl") String ecl,
                              @RequestParam(name = "showNames", required = false) Boolean showNames,
                              @RequestParam(name = "graph", defaultValue = GRAPH.DISCOVERY) String graph
  ) throws IOException, QueryException, EclFormatException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclWithNames.POST")) {
      log.debug("getEcl from ecl");
      Query query = eclService.getQueryFromEcl(ecl, graph);
      return eclService.getECLFromQuery(query, showNames, graph);
    }
  }


  @PostMapping(value = "public/validateEcl", consumes = "text/plain", produces = "application/json")
  @Operation(
    summary = "Validate ECL format",
    description = "Checks if the provided ECL string is valid"
  )
  public ECLStatus validateEcl(@RequestBody String ecl) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.ValidateEcl.POST")) {
      return eclService.validateEcl(ecl);
    }
  }
}
