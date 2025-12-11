package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.casbin.CasbinEnforcer;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.IMLLanguage;
import org.endeavourhealth.imapi.model.iml.Indicator;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.MatchDisplayRequest;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.sql.SubQueryDependency;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.postgres.PostgresService;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/query")
@CrossOrigin(origins = "*")
@Tag(name = "Query APIs", description = "APIs for querying the Information Model")
@RequestScope
@Slf4j
public class QueryController {

  private final SearchService searchService = new SearchService();
  private final QueryService queryService = new QueryService();
  private final PostgresService postgresService = new PostgresService();
  private final CasbinEnforcer casbinEnforcer = new CasbinEnforcer();

  @PostMapping("/private/queryIM")
  @Operation(
    summary = "Query IM",
    description = "Runs a generic query on IM"
  )
  public JsonNode queryIM(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws QueryException, OpenSearchException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryIM.POST")) {
      log.debug("queryIM");
      return searchService.queryIM(queryRequest);
    }
  }

  @PostMapping("/private/askQueryIM")
  @Operation(summary = "Check if an iri is within a query's results")
  public Boolean askQueryIM(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.AskQueryIM.POST")) {
      log.debug("askQueryIM");
      return searchService.askQueryIM(queryRequest);
    }
  }

  @PostMapping("/private/queryIMSearch")
  @Operation(
    summary = "Query IM returning conceptSummaries",
    description = "Runs a generic query on IM and returns results as ConceptSummary items."
  )
  public SearchResponse queryIMSearch(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws OpenSearchException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryIMSearch.POST")) {
      log.debug("queryIMSearch  {} : {} ", queryRequest.getTextSearchStyle(), queryRequest.getTextSearch());
      if (queryRequest.getPage() != null) {
        log.debug("page {} rows per page {}", queryRequest.getPage().getPageNumber(), queryRequest.getPage().getPageSize());
      }
      SearchResponse response = searchService.queryIMSearch(queryRequest);
      log.debug("queryIMSearch response {}", response.getEntities() != null ? response.getEntities().size() : 0);
      return response;
    }
  }

  @PostMapping("/private/pathQuery")
  @Operation(
    summary = "Path Query ",
    description = "Query IM for a path between source and target"
  )
  public PathDocument pathQuery(HttpServletRequest request, @RequestBody PathQuery pathQuery) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.PathQuery.POST")) {
      log.debug("pathQuery");
      return searchService.pathQuery(pathQuery);
    }
  }


  @GetMapping(value = "/private/queryDisplay", produces = "application/json")
  @Operation(
    summary = "Describe a query",
    description = "Retrieves the details of a query based on the given query IRI."
  )
  public Query describeQuery(
    HttpServletRequest request,
    @RequestParam(name = "queryIri") String iri,
    @RequestParam(name = "displayMode", defaultValue = "ORIGINAL") DisplayMode displayMode
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.Display.GET")) {
      log.debug("describeQuery");
      return queryService.describeQuery(iri, displayMode);
    }
  }

  @GetMapping(value = "/private/indicatorDisplay", produces = "application/json")
  @Operation(
    summary = "Describe a query",
    description = "Retrieves the details of a query based on the given query IRI."
  )
  public Indicator describeIndicator(
    HttpServletRequest request,
    @RequestParam(name = "queryIri") String iri
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.Display.GET")) {
      log.debug("describeQuery");
      return queryService.describeIndicator(iri);
    }
  }

  @GetMapping(value = "/private/expandCohort", produces = "application/json")
  @Operation(
    summary = "Expands a cohort reference from a source query",
    description = "Retrieves the details of a query based on the given source and cohort IRI."
  )
  public Query expandCohort(
    HttpServletRequest request,
    @RequestParam(name = "queryIri") String queryIri,
    @RequestParam(name = "cohortIri") String cohortIri,
    @RequestParam(name = "displayMode", defaultValue = "ORIGINAL") DisplayMode displayMode
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.Display.GET")) {
      log.debug("expandCohort");
      return queryService.expandCohort(queryIri, cohortIri, displayMode);
    }
  }

  @GetMapping(value = "/private/queryFromIri", produces = "application/json")
  @Operation(
    summary = "gets an original IM  query from its iri",
    description = "Retrieves the original details of a query based on the given query IRI."
  )
  public Query queryFromIri(
    HttpServletRequest request,
    @RequestParam(name = "queryIri") String iri
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.Display.GET")) {
      log.debug("getQueryfromIri");
      return queryService.getQueryFromIri(iri);
    }
  }

  @PostMapping("/private/queryDisplayFromQuery")
  @Operation(
    summary = "Describe query content",
    description = "Returns a query view, transforming an IMQ query into a viewable object."
  )
  public Query describeQueryContent(
    HttpServletRequest request,
    @RequestBody JsonNode body
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.GET");
         CachedObjectMapper mapper = new CachedObjectMapper()) {
      log.debug("getQueryDisplayFromQuery");

      DisplayMode displayMode = DisplayMode.valueOf(body.get("displayMode").asText());
      Query query = mapper.treeToValue(body.get("query"), Query.class);
      log.debug("displayMode: {}", displayMode);

      return queryService.describeQuery(query, displayMode);
    }
  }

  @PostMapping("/private/flattenBooleans")
  @Operation(
    summary = "optimises logical boolean of query",
    description = "Returns the query with boolean optimisation"
  )
  public Query flattenBooleans(
    @RequestBody Query query) {

    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("optimizeQuery");
      return queryService.flattenQuery(query);
    }
  }

  @PostMapping("/private/optimiseECLQuery")
  @Operation(
    summary = "optimises logical boolean of query",
    description = "Returns the query with boolean optimisation"
  )
  public Query optimiseECLQuery(
    @RequestBody Query query
  ) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("optimiseECLQuery");
      return queryService.optimiseECLQuery(query);
    }
  }


  @PostMapping("/private/matchDisplayFromMatch")
  @Operation(
    summary = "Describe query content",
    description = "Returns a query view, transforming an IMQ query into a viewable object."
  )
  public Match describeMatchContent(
    HttpServletRequest request,
    @RequestBody MatchDisplayRequest matchDisplayRequest
  ) throws QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("getMatchDisplayFromMatch");
      return queryService.describeMatch(matchDisplayRequest.getMatch());
    }
  }


  @PostMapping("/private/sql")
  @Operation(
    summary = "Generate SQL",
    description = "Generates SQL from the provided IMQ query request."
  )
  public String getSQLFromIMQR(@RequestBody QueryRequest queryRequest) throws SQLConversionException, QueryException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQ.POST")) {
      log.debug("getSQLFromIMQR");
      return queryService.getSQLFromIMQ(queryRequest);
    }
  }

  @GetMapping("/private/sql")
  @Operation(
    summary = "Generate SQL from IRI",
    description = "Generates SQL from the given IMQ query IRI."
  )
  public String getSQLFromIMQIri(
    HttpServletRequest request,
    @RequestParam(name = "queryIri") String queryIri,
    @RequestParam(name = "lang", defaultValue = "MYSQL") DatabaseOption lang
  ) throws IOException, QueryException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQIri.GET")) {
      log.debug("getSQLFromIMQIri");
      return queryService.getSQLFromIMQIri(queryIri, lang);
    }
  }

  @GetMapping("/private/imlFromIri")
  @Operation(
    summary = "Generate IML from a query iri",
    description = "Generates IMQ from the given IMQ query IRI."
  )
  public IMLLanguage getIMLFromIMQIri(
    HttpServletRequest request,
    @RequestParam(name = "queryIri") String queryIri
  ) throws QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQIri.GET")) {
      log.debug("getIMLFromIMQIri");
      return queryService.getIMLFromIMQIri(queryIri);
    }
  }

  @GetMapping(value = "/private/defaultQuery")
  @Operation(summary = "Gets the default parent cohort", description = "Fetches a query with the 1st cohort in the default cohort folder")
  public Query getDefaultQuery(HttpServletRequest request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.DefaultQuery.GET")) {
      log.debug("getDefaultCohort");
      return queryService.getDefaultQuery();
    }
  }

  @PostMapping("/private/testRunQuery")
  @Operation(summary = "Run a query with results limited results to test query")
  public Set<String> testRunQuery(HttpServletRequest request, @RequestBody QueryRequest queryRequest) throws SQLException, SQLConversionException, QueryException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.TestRunQuery.POST")) {
      log.debug("testRunQuery");
      return queryService.testRunQuery(queryRequest);
    }
  }

  @PostMapping("/findRequestMissingArguments")
  @Operation(summary = "Check that a query request has argument values for all required query parameters")
  @PreAuthorize("@guard.hasPermission('QUERY','EXECUTE')")
  public List<ArgumentReference> findRequestMissingArguments(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws QueryException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.FindMissingArguments.POST")) {
      log.debug("findRequestMissingArguments");
      return queryService.findMissingArguments(queryRequest);
    }
  }

  @GetMapping("/argumentType")
  @Operation(summary = "Get the data type for a query argument by using the reference iri")
  @PreAuthorize("@guard.hasPermission('QUERY','EXECUTE')")
  public TTIriRef getArgumentType(
    HttpServletRequest request,
    @RequestParam String referenceIri
  ) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.ArgumentType.GET")) {
      log.debug("getArgumentType");
      return queryService.getArgumentType(referenceIri);
    }
  }

  @GetMapping("/private/subQueries")
  @Operation(summary = "Get all subQueries ordered of a query using the query iri")
  public Collection<SubQueryDependency> getSubQueries(
    HttpServletRequest request,
    @RequestParam(name = "queryIri") String queryIri
  ) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.ArgumentType.GET")) {
      log.debug("getSubQueries");
      return queryService.getOrderedSubqueries(queryIri);
    }
  }

  @GetMapping("/private/queryRequestForSQL")
  @Operation(summary = "Get all subQueries ordered of a query using the query iri")
  public QueryRequest getQueryRequestForSql(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws SQLConversionException, JsonProcessingException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.ArgumentType.GET")) {
      log.debug("getSubQueries");
      return queryService.getQueryRequestForSqlConversion(queryRequest);
    }
  }
}
