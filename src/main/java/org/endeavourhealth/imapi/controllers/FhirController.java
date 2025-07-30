package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.FhirService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/fhir/r4")
@CrossOrigin(origins = "*")
@Tag(name = "FHIR Controller")
@RequestScope
@Slf4j
public class FhirController {
  private final FhirService fhirService = new FhirService();

  @GetMapping(value = "/ValueSet", produces = "application/json")
  @Operation(summary = "Retrieves the specified value set")
  public String getValueSet(@RequestParam(name = "url") String iri, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Fhir.ValueSet.GET")) {
      log.info("Retrieving valueSet");
      return fhirService.getFhirValueSet(iri, false, Graph.from(graph));
    }
  }

  @GetMapping(value = "/ValueSet/$expand", produces = "application/json")
  @Operation(summary = "Retrieves the specified value set and expands any subsets & members")
  public String getValueSetExpanded(@RequestParam(name = "url") String iri, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Fhir.ValueSet.Expand.GET")) {
      log.info("Retrieving expanded valueSet");
      return fhirService.getFhirValueSet(iri, true, Graph.from(graph));
    }
  }

  @GetMapping(value = "/ValueSet/ECL", consumes = "text/plain", produces = "application/json")
  @Operation(summary = "Evaluates an ECL expression and returns the result as a FHIR r4 value set")
  public String getEclToFhir(@RequestParam(name = "ecl") String ecl) throws IOException, QueryException, DataFormatException, EclFormatException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Fhir.ValueSet.Ecl.POST")) {
      log.info("Evaluating valueset ECL expression");
      return fhirService.eclToFhir(ecl);
    }
  }

}
