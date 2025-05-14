package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
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
  public String getEcl(@RequestBody Query inferred) throws QueryException, IOException {
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

  @PostMapping(value = "/public/eclFromQuery")
  @Operation(
    summary = "Generate ECL text from Query",
    description = "Converts an IM Query into an equivalent ECL string"
  )
  public String getECLFromQuery(@RequestBody Query query) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclFromQuery.POST")) {
      return eclService.getECLFromQuery(query, false);
    }
  }

  @PostMapping(value = "/public/eclFromQueryWithNames")
  @Operation(
    summary = "Generate ECL text with concept names",
    description = "Converts an IM query to an ECL string while including named concepts"
  )
  public String getECLFromQueryWithNames(@RequestBody Query query) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclFromQueryWithNames.POST")) {
      return eclService.getECLFromQuery(query, true);
    }
  }

  @PostMapping(value = "/public/queryFromEcl", consumes = "text/plain", produces = "application/json")
  @Operation(
    summary = "Convert ECL to Query",
    description = "Transforms a provided ECL string into an IM Query object"
  )
  public Query getQueryFromECL(@RequestBody String ecl) throws IOException, EclFormatException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.QueryFromEcl.POST")) {
      log.debug("getQueryFromEcl");
      return eclService.getQueryFromEcl(ecl);
    }
  }




  @PostMapping(value = "public/validateEcl", consumes = "text/plain", produces = "application/json")
  @Operation(
    summary = "Validate ECL format",
    description = "Checks if the provided ECL string is valid"
  )
  public Boolean validateEcl(@RequestBody String ecl) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.ValidateEcl.POST")) {
      return eclService.validateEcl(ecl);
    }
  }
}
