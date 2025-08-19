package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.ConceptService;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.ECLQueryRequest;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.Set;
import java.util.UnknownFormatConversionException;

@RestController
@RequestMapping("api/ecl")
@CrossOrigin(origins = "*")
@Tag(name = "Ecl Controller", description = "Controller to handle ECL-related requests")
@RequestScope
@Slf4j
public class EclController {

  private final EclService eclService = new EclService();
  private final ConceptService conceptService = new ConceptService();

  @PostMapping("/public/ecl")
  @Operation(
    summary = "Retrieve ECL string",
    description = "Generates an ECL string from the provided IMQ Query object"
  )
  public String getEcl(HttpServletRequest request, @RequestBody EclSearchRequest inferred) throws QueryException, IOException {
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
    HttpServletRequest request,
    @RequestBody EclSearchRequest eclSearchRequest
  ) throws EclFormatException, IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("ECL.EclSearch.POST")) {
      log.debug("eclSearch");
      return eclService.eclSearch(eclSearchRequest);
    } catch (UnknownFormatConversionException ex) {
      throw new EclFormatException("Invalid ECL format", ex);
    }
  }

  @PostMapping("/public/eclFromQuery")
  public ECLQueryRequest getECLFromQuery(
    HttpServletRequest request,
    @RequestBody ECLQueryRequest eclQueryRequest
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclFromQuery.POST")) {
      log.debug("getEclFromQuery");
      return eclService.getECLFromQuery(eclQueryRequest);
    }
  }

  @PostMapping("/public/validateModelFromQuery")
  public ECLQueryRequest validateModelFromQuery(
    HttpServletRequest request,
    @RequestBody ECLQueryRequest eclQueryRequest
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.ValidateModelFromQuery.POST")) {
      log.debug("validatesEclQuerymodel");
      return eclService.validateModelFromQuery(eclQueryRequest);
    }
  }


  @PostMapping("/public/validateModelFromECL")
  public ECLQueryRequest validateModelFromEcl(HttpServletRequest request, @RequestBody ECLQueryRequest eclQueryRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.ValidateModelFromECL.POST")) {
      log.debug("validatesModelFromECL");
      return eclService.validateModelFromECL(eclQueryRequest);
    }
  }

  @PostMapping(value = "/public/queryFromEcl", consumes = "application/json", produces = "application/json")
  @Operation(
    summary = "Convert ECL to Query",
    description = "Transforms a provided ECL string into an IM Query object"
  )
  public ECLQueryRequest getQueryFromECL(HttpServletRequest request, @RequestBody ECLQueryRequest eclQueryRequest) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.QueryFromEcl.POST")) {
      log.debug("getQueryFromEcl");
      return eclService.getQueryFromECL(eclQueryRequest);
    }
  }

  @PostMapping(value = "/public/eclFromEcl", consumes = "application/json", produces = "application/json")
  @Operation(
    summary = "Convert ECL to ECL with names",
    description = "Transforms a provided ECL string into an IM Query object"
  )
  public ECLQueryRequest getEclFromEcl(
    HttpServletRequest request,
    @RequestBody ECLQueryRequest eclQueryRequest
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclWithNames.POST")) {
      log.debug("getEcl from ecl");
      return eclService.getEclFromEcl(eclQueryRequest);
    }
  }

  @PostMapping(value = "/public/validateEcl", consumes = "application/json", produces = "application/json")
  @Operation(
    summary = "Validate ECL format",
    description = "Checks if the provided ECL string is valid"
  )
  public ECLQueryRequest validateEcl(
    HttpServletRequest request,
    @RequestBody ECLQueryRequest eclQueryRequest
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.ValidateEcl.POST")) {
      log.debug("validatesEcl");
      return eclService.validateEcl(eclQueryRequest);
    }
  }

  @GetMapping(value = "/public/propertiesForDomains")
  @Operation(summary = "Get top level properties for an entity as a tree node", description = "Finds the highest parent (superior) properties for an entity and returns then in a tree node format for use in a hierarchy tree")
  public Set<String> getPropertiesForDomains(
    HttpServletRequest request,
    @RequestParam(name = "conceptIri") Set<String> iris
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.propertiesForDomains.GET")) {
      log.debug("getPropertiesForDomains");
      return conceptService.getPropertiesForDomains(iris);
    }
  }


  @GetMapping(value = "/public/rangesForProperty")
  @Operation(summary = "Get top level property ranges for an entity as a tree node", description = "Finds the highest parent (superior) property value for an entity and returns then in a tree node format for use in a hierarchy tree")
  public Set<String> getRangesForProperty(
    HttpServletRequest request,
    @RequestParam(name = "propertyIri") String iri
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.rangesForProperty.GET")) {
      log.debug("getRangesForProperty");
      return conceptService.getRangesForProperty(iri);
    }
  }
}
