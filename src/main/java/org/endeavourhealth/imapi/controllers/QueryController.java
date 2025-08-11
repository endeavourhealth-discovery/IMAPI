package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.postRequestPrimatives.UUIDBody;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.postgres.QueryExecutorStatus;
import org.endeavourhealth.imapi.model.requests.MatchDisplayRequest;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.postgress.PostgresService;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/query")
@CrossOrigin(origins = "*")
@Tag(name = "Query APIs", description = "APIs for querying the Information Model")
@RequestScope
@Slf4j
public class QueryController {

  private final RequestObjectService requestObjectService = new RequestObjectService();

  private final SearchService searchService = new SearchService();
  private final QueryService queryService = new QueryService();
  private final PostgresService postgresService = new PostgresService();

  @PostMapping("/public/queryIM")
  @Operation(
    summary = "Query IM",
    description = "Runs a generic query on IM"
  )
  public JsonNode queryIM(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws IOException, QueryException, OpenSearchException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryIM.POST")) {
      log.debug("queryIM");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return searchService.queryIM(queryRequest, graphs);
    }
  }

  @PostMapping("/public/askQueryIM")
  @Operation(summary = "Check if an iri is within a query's results")
  public Boolean askQueryIM(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.AskQueryIM.POST")) {
      log.debug("askQueryIM");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return searchService.askQueryIM(queryRequest, graphs);
    }
  }

  @PostMapping("/public/queryIMSearch")
  @Operation(
    summary = "Query IM returning conceptSummaries",
    description = "Runs a generic query on IM and returns results as ConceptSummary items."
  )
  public SearchResponse queryIMSearch(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws IOException, OpenSearchException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryIMSearch.POST")) {
      log.debug("queryIMSearch  {} : {} ", queryRequest.getTextSearchStyle(), queryRequest.getTextSearch());
      if (queryRequest.getPage() != null) {
        log.debug("page {} rows per page {}", queryRequest.getPage().getPageNumber(), queryRequest.getPage().getPageSize());
      }
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      SearchResponse response = searchService.queryIMSearch(queryRequest, graphs);
      log.debug("queryIMSearch response {}", response.getEntities() != null ? response.getEntities().size() : 0);
      return response;
    }
  }

  @PostMapping("/public/pathQuery")
  @Operation(
    summary = "Path Query ",
    description = "Query IM for a path between source and target"
  )
  public PathDocument pathQuery(HttpServletRequest request, @RequestBody PathQuery pathQuery) throws DataFormatException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.PathQuery.POST")) {
      log.debug("pathQuery");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return searchService.pathQuery(pathQuery, graphs);
    }
  }


  @GetMapping(value = "/public/queryDisplay", produces = "application/json")
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
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.describeQuery(iri, displayMode, graphs);
    }
  }

  @GetMapping(value = "/public/queryFromIri", produces = "application/json")
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
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.getQueryFromIri(iri, graphs);
    }
  }

  @PostMapping("/public/queryDisplayFromQuery")
  @Operation(
    summary = "Describe query content",
    description = "Returns a query view, transforming an IMQ query into a viewable object."
  )
  public Query describeQueryContent(
    HttpServletRequest request,
    @RequestBody Query query,
    @RequestParam(value = "displayMode", required = false, defaultValue = "ORIGINAL") DisplayMode displayMode
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.GET")) {
      log.debug("getQueryDisplayFromQuery with displayMode: {}", displayMode);
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.describeQuery(query, displayMode, graphs);
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
    @RequestBody Query query
  ) throws IOException, QueryException {
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
    HttpServletRequest request,
    @RequestBody MatchDisplayRequest matchDisplayRequest
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      log.debug("getMatchDisplayFromMatch");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.describeMatch(matchDisplayRequest.getMatch(), graphs);
    }
  }


  @PostMapping("/public/sql")
  @Operation(
    summary = "Generate SQL",
    description = "Generates SQL from the provided IMQ query."
  )
  public String getSQLFromIMQ(@RequestBody QueryRequest queryRequest) throws IOException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQ.POST")) {
      log.debug("getSQLFromIMQ");
      return queryService.getSQLFromIMQ(queryRequest).getSql();
    }
  }

  @GetMapping("/public/sql")
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
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.getSQLFromIMQIri(queryIri, lang, graphs).getSql();
    }
  }

  @PostMapping("/addToQueue")
  @Operation(
    summary = "Add query to execution queue",
    description = "Transforms query to SQL and adds it to the execution queue"
  )
  public void addToQueue(HttpServletRequest request, @RequestBody QueryRequest queryRequest) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.AddToQueue.POST")) {
      log.debug("addToQueue");
      UUID userId = requestObjectService.getRequestAgentIdAsUUID(request);
      String userName = requestObjectService.getRequestAgentName(request);
      queryService.addToExecutionQueue(userId, userName, queryRequest);
    }
  }

  @GetMapping("/userQueryQueue")
  @Operation(
    summary = "Get the query queue items and status for a user"
  )
  public Pageable<DBEntry> userQueryQueue(HttpServletRequest request, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) throws IOException, SQLConversionException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.UserQueryQueue.GET")) {
      log.debug("getUserQueryQueue");
      UUID userId = requestObjectService.getRequestAgentIdAsUUID(request);
      return postgresService.getAllByUserId(userId, page, size);
    }
  }

  @GetMapping("/userQueryQueueByStatus")
  @Operation(
    summary = "Get query queue items by user id and status"
  )
  public Pageable<DBEntry> userQueryQueueByStatus(HttpServletRequest request, @RequestParam(name = "status") QueryExecutorStatus status, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) throws IOException, SQLConversionException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.UserQueryQueueByStatus.GET")) {
      log.debug("getUserQueryQueueByStatus");
      String userId = requestObjectService.getRequestAgentId(request);
      return postgresService.findAllByUserIdAndStatus(userId, status, page, size);
    }
  }

  @GetMapping("/queryQueueByStatus")
  @Operation(
    summary = "get query queue items by status as admin"
  )
  @PreAuthorize("hasAuthority('IMAdmin')")
  public Pageable<DBEntry> queryQueueByStatus(HttpServletRequest request, @RequestParam(name = "status") QueryExecutorStatus status, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) throws IOException, SQLConversionException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryQueueByStatus.GET")) {
      log.debug("getQueryQueueByStatus");
      return postgresService.findAllByStatus(status, page, size);
    }
  }

  @PostMapping(value = "/cancelQuery", consumes = "application/json")
  @Operation(
    summary = "Cancel a query from running either whilst in the queue or runner using the query uuid"
  )
  public void cancelQuery(@RequestBody UUIDBody id) throws IOException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.CancelQuery.POST")) {
      log.debug("cancelQuery");
      postgresService.cancelQuery(id.getValue());
    }
  }

  @DeleteMapping("/deleteFromQueue")
  @Operation(
    summary = "Delete a query from the queue using the query uuid"
  )
  public void deleteFromQueue(HttpServletRequest request, @RequestParam(name = "id") UUID id) throws IOException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.DeleteFromQueue.DELETE")) {
      log.debug("deleteFromQueue");
      UUID userId = requestObjectService.getRequestAgentIdAsUUID(request);
      postgresService.delete(userId, id);
    }
  }

  @PostMapping("/requeueQuery")
  @Operation(
    summary = "Requeue a cancelled or errored query"
  )
  public void requeueQuery(HttpServletRequest request, @RequestBody RequeueQueryRequest requeueQueryRequest) throws Exception, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.RequeueQuery.POST")) {
      log.debug("requeueQuery");
      UUID userId = requestObjectService.getRequestAgentIdAsUUID(request);
      String userName = requestObjectService.getRequestAgentName(request);
      queryService.reAddToExecutionQueue(userId, userName, requeueQueryRequest);
    }
  }

  @PostMapping("/killActiveQuery")
  @Operation(
    summary = "Kills the active running query"
  )
  @PreAuthorize("hasAuthority('IMAdmin')")
  public void killActiveQuery() throws SQLException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.KillActiveQuery.POST")) {
      log.debug("killActiveQuery");
      queryService.killActiveQuery();
    }
  }

  @PostMapping("/getQueryResults")
  @Operation(
    summary = "Get query results using a hash of the query request"
  )
  public List<String> getQueryResults(HttpServletRequest request, @RequestBody QueryRequest queryRequest) throws IOException, SQLException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQueryResults.GET")) {
      log.debug("getQueryResults");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.getQueryResults(queryRequest, graphs);
    }
  }

  @GetMapping(value = "/public/defaultQuery")
  @Operation(summary = "Gets the default parent cohort", description = "Fetches a query with the 1st cohort in the default cohort folder")
  public Query getDefaultQuery(HttpServletRequest request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.DefaultQuery.GET")) {
      log.debug("getDefaultCohort");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.getDefaultQuery(graphs);
    }
  }

  @PostMapping("/testRunQuery")
  @Operation(summary = "Run a query with results limited results to test query")
  public List<String> testRunQuery(HttpServletRequest request, @RequestBody QueryRequest query) throws IOException, SQLException, SQLConversionException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.TestRunQuery.POST")) {
      log.debug("testRunQuery");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.testRunQuery(query.getQuery(), graphs);
    }
  }

  @PostMapping("/findRequestMissingArguments")
  @Operation(summary = "Check that a query request has argument values for all required query parameters")
  public List<ArgumentReference> findRequestMissingArguments(
    HttpServletRequest request,
    @RequestBody QueryRequest queryRequest
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.FindMissingArguments.POST")) {
      log.debug("findRequestMissingArguments");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.findMissingArguments(queryRequest, graphs);
    }
  }

  @GetMapping("/argumentType")
  @Operation(summary = "Get the data type for a query argument by using the reference iri")
  public TTIriRef getArgumentType(
    HttpServletRequest request,
    @RequestParam String referenceIri
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.ArgumentType.GET")) {
      log.debug("getArgumentType");
      List<Graph> graphs = requestObjectService.getUserGraphs(request);
      return queryService.getArgumentType(referenceIri, graphs);
    }
  }
}
