package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.eclBuilder.BoolGroup;
import org.endeavourhealth.imapi.model.eclBuilder.EclBuilderException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/ecl")
@CrossOrigin(origins = "*")
@Tag(name = "Ecl Controller")
@RequestScope
public class EclController {
  private static final Logger LOG = LoggerFactory.getLogger(EclController.class);

  private final EclService eclService = new EclService();

  @PostMapping("/public/ecl")
  public String getEcl(@RequestBody Query inferred) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("ECL.Ecl.POST")) {
      LOG.debug("getEcl");
      return eclService.getEcl(inferred);
    }
  }

  @PostMapping(value = "/public/eclSearch", consumes = "application/json", produces = "application/json")
  @Operation(
    summary = "ECL search",
    description = "Search entities using ECL string"
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
    summary = "Get ecl from query",
    description = "MapObject an IM query to ecl"
  )
  public String getECLFromQuery(@RequestBody Query query) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclFromQuery.POST")) {
      return eclService.getECLFromQuery(query, false);
    }
  }

  @PostMapping(value = "/public/eclFromQueryWithNames")
  @Operation(
    summary = "Get ecl from query",
    description = "MapObject an IM query to ecl"
  )
  public String getECLFromQueryWithNames(@RequestBody Query query) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclFromQueryWithNames.POST")) {
      return eclService.getECLFromQuery(query, true);
    }
  }

  @PostMapping(value = "/public/queryFromEcl", consumes = "text/plain", produces = "application/json")
  @Operation(
    summary = "Get IMQ query from ecl",
    description = "Map ecl test to an IM query object"
  )
  public Query getQueryFromECL(@RequestBody String ecl) throws IOException, EclFormatException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.QueryFromEcl.POST")) {
      LOG.debug("getQueryFromEcl");
      return eclService.getQueryFromEcl(ecl);
    }
  }

  @PostMapping(value = "public/eclBuilderFromQuery", produces = "application/json")
  @Operation(
    summary = "Get ecl builder component objects from an imq query"
  )
  public BoolGroup getEclBuilderFromQuery(@RequestBody Query query) throws QueryException, EclBuilderException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.EclBuilderFromQuery.POST")) {
      LOG.debug("getEclBuilderFromQuery");
      return eclService.getEclBuilderFromQuery(query);
    }
  }

  @PostMapping(value = "public/queryFromEclBuilder", produces = "application/json")
  @Operation(
    summary = "Get query from ecl builder component objects"
  )
  public Query getQueryFromEclBuilder(@RequestBody BoolGroup boolGroup) throws IOException, EclBuilderException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.QueryFromEclBuilder.POST")) {
      return eclService.getQueryFromEclBuilder(boolGroup);
    }
  }

  @PostMapping(value = "public/validateEcl", consumes = "text/plain", produces = "application/json")
  @Operation(
    summary = "Checks that validity of an ecl string"
  )
  public Boolean validateEcl(@RequestBody String ecl) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.ECL.ValidateEcl.POST")) {
      return eclService.validateEcl(ecl);
    }
  }
}
