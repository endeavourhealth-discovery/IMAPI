package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.FhirService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/fhir/r4")
@CrossOrigin(origins = "*")
@Tag(name = "FHIR Controller")
@RequestScope
@Slf4j
public class FhirController {
  private final FhirService fhirService = new FhirService();
  private final RequestObjectService requestObjectService = new RequestObjectService();

  @GetMapping(value = "/ValueSet", produces = "application/json")
  @Operation(summary = "Retrieves the specified value set")
  public String getValueSet(
    HttpServletRequest request,
    @RequestParam(name = "url") String iri
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Fhir.ValueSet.GET")) {
      log.info("Retrieving valueSet");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return fhirService.getFhirValueSet(iri, false, graphs);
    }
  }

  @GetMapping(value = "/ValueSet/$expand", produces = "application/json")
  @Operation(summary = "Retrieves the specified value set and expands any subsets & members")
  public String getValueSetExpanded(
    HttpServletRequest request,
    @RequestParam(name = "url") String iri
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Fhir.ValueSet.Expand.GET")) {
      log.info("Retrieving expanded valueSet");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return fhirService.getFhirValueSet(iri, true, graphs);
    }
  }

  @GetMapping(value = "/ValueSet/ECL", consumes = "text/plain", produces = "application/json")
  @Operation(summary = "Evaluates an ECL expression and returns the result as a FHIR r4 value set")
  public String getEclToFhir(HttpServletRequest request, @RequestParam(name = "ecl") String ecl) throws IOException, QueryException, DataFormatException, EclFormatException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Fhir.ValueSet.Ecl.POST")) {
      log.info("Evaluating valueset ECL expression");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return fhirService.eclToFhir(ecl, graphs);
    }
  }

}
