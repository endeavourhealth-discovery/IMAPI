package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.HashMap;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/query")
@CrossOrigin(origins = "*")
@Tag(name = "Query APIs", description = "APIs for querying the Information Model")
@RequestScope
@Slf4j
public class QueryController {

  private final SearchService searchService = new SearchService();
  private final QueryService queryService = new QueryService();

  @PostMapping("/public/queryIM")
  @Operation(
    summary = "Query IM",
    description = "Runs a generic query on IM"
  )
  public JsonNode queryIM(@RequestBody QueryRequest queryRequest) throws IOException, QueryException, OpenSearchException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryIM.POST")) {
      log.debug("queryIM");
      return searchService.queryIM(queryRequest);
    }
  }

  @PostMapping("/public/askQueryIM")
  @Operation(summary = "Check if an iri is within a query's results")
  public Boolean askQueryIM(@RequestBody QueryRequest queryRequest) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.AskQueryIM.POST")) {
      log.debug("askQueryIM");
      return searchService.askQueryIM(queryRequest);
    }
  }

  @PostMapping("/public/queryIMSearch")
  @Operation(
    summary = "Query IM returning conceptSummaries",
    description = "Runs a generic query on IM and returns results as ConceptSummary items."
  )
  public SearchResponse queryIMSearch(@RequestBody QueryRequest queryRequest) throws IOException, OpenSearchException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryIMSearch.POST")) {
      log.debug("queryIMSearch  {} : {} ",queryRequest.getTextSearchStyle(),queryRequest.getTextSearch());
      if (queryRequest.getPage()!=null){
        log.debug("page {} rows per page {}",queryRequest.getPage().getPageNumber(),queryRequest.getPage().getPageSize());
      }
      SearchResponse response= searchService.queryIMSearch(queryRequest);
      log.debug("queryIMSearch response {}",response.getEntities()!=null?response.getEntities().size():0);
      return response;
    }
  }

  @PostMapping("/public/pathQuery")
  @Operation(
    summary = "Path Query ",
    description = "Query IM for a path between source and target"
  )
  public PathDocument pathQuery(@RequestBody PathQuery pathQuery) throws DataFormatException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.PathQuery.POST")) {
      log.debug("pathQuery");
      return searchService.pathQuery(pathQuery);
    }
  }


  @GetMapping(value = "/public/queryDisplay", produces = "application/json")
  @Operation(
    summary = "Describe a query",
    description = "Retrieves the details of a query based on the given query IRI."
  )
  public Query describeQuery(
    @RequestParam(name = "queryIri") String iri,
    @RequestParam(name = "displayMode", defaultValue = "ORIGINAL") DisplayMode displayMode)
    throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.Display.GET")) {
      log.debug("describeQuery");
      return queryService.describeQuery(iri, displayMode);
    }
  }

  @PostMapping("/public/queryDisplayFromQuery")
  @Operation(
    summary = "Describe query content",
    description = "Returns a query view, transforming an IMQ query into a viewable object."
  )
  public Query describeQueryContent(
    @RequestBody Query query,
    @RequestParam(value = "displayMode", required = false, defaultValue = "ORIGINAL") DisplayMode displayMode
  ) throws IOException, QueryException {

    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("getQueryDisplayFromQuery with displayMode: {}", displayMode);
      return queryService.describeQuery(query, displayMode);
    }
  }
  @PostMapping("/public/flattenBooleans")
  @Operation(
    summary = "optimises logical boolean of query",
    description = "Returns the query with boolean optimisation"
  )
  public Query flattenBooleans(
    @RequestBody Query query) throws IOException, QueryException {

    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("flattenQuery");
      return queryService.flattenQuery(query);
    }
  }

  @PostMapping("/public/optimiseECLQuery")
  @Operation(
    summary = "optimises logical boolean of query",
    description = "Returns the query with boolean optimisation"
  )
  public Query optimiseECLQuery(
    @RequestBody Query query) throws IOException, QueryException {

    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("optimiseECLQuery");
      return queryService.optimiseECLQuery(query);
    }
  }



  @PostMapping("/public/matchDisplayFromMatch")
  @Operation(
    summary = "Describe query content",
    description = "Returns a query view, transforming an IMQ query into a viewable object."
  )
  public Match describeMatchContent(
    @RequestBody Match match) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("getMatchDisplayFromMatch");
      return queryService.describeMatch(match);
    }
  }


  @PostMapping("/public/sql")
  @Operation(
    summary = "Generate SQL",
    description = "Generates SQL from the provided IMQ query."
  )
  public String getSQLFromIMQ(@RequestBody QueryRequest queryRequest, @RequestParam(name = "lang", defaultValue = "MYSQL") String lang) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQ.POST")) {
      log.debug("getSQLFromIMQ");
      return queryService.getSQLFromIMQ(queryRequest, lang, new HashMap<>());
    }
  }

  @GetMapping("/public/sql")
  @Operation(
    summary = "Generate SQL from IRI",
    description = "Generates SQL from the given IMQ query IRI."
  )
  public String getSQLFromIMQIri(@RequestParam(name = "queryIri") String queryIri, @RequestParam(name = "lang", defaultValue = "MYSQL") String lang) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQIri.GET")) {
      log.debug("getSQLFromIMQIri");
      return queryService.getSQLFromIMQIri(queryIri, lang, new HashMap<>());
    }
  }


  @GetMapping(value = "/public/defaultQuery")
  @Operation(summary = "Gets the default parent cohort", description = "Fetches a query with the 1st cohort in the default cohort folder")
  public Query getDefaultQuery() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.DefaultQuery.GET")) {
      log.debug("getDefaultCohort");
      return queryService.getDefaultQuery();
    }
  }
}
