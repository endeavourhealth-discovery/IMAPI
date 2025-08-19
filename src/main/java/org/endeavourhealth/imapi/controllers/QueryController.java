package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
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
import org.endeavourhealth.imapi.postgres.PostgresService;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
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
      return searchService.queryIM(queryRequest);
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
      return searchService.askQueryIM(queryRequest);
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
      SearchResponse response = searchService.queryIMSearch(queryRequest);
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
      return searchService.pathQuery(pathQuery);
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
      return queryService.describeQuery(iri, displayMode);
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
      return queryService.getQueryFromIri(iri);
    }
  }

  @PostMapping("/public/queryDisplayFromQuery")
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
      return queryService.describeMatch(matchDisplayRequest.getMatch());
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
      return queryService.getSQLFromIMQIri(queryIri, lang).getSql();
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
  @PreAuthorize("hasAuthority('ADMIN')")
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
  public void requeueQuery(HttpServletRequest request, @RequestBody RequeueQueryRequest requeueQueryRequest) throws Exception {
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
  @PreAuthorize("hasAuthority('ADMIN')")
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
  public Set<String> getQueryResults(HttpServletRequest request, @RequestBody QueryRequest queryRequest) throws IOException, SQLException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQueryResults.GET")) {
      log.debug("getQueryResults");
      return queryService.getQueryResults(queryRequest);
    }
  }

  @GetMapping(value = "/public/defaultQuery")
  @Operation(summary = "Gets the default parent cohort", description = "Fetches a query with the 1st cohort in the default cohort folder")
  public Query getDefaultQuery(HttpServletRequest request) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.DefaultQuery.GET")) {
      log.debug("getDefaultCohort");
      return queryService.getDefaultQuery();
    }
  }

  @PostMapping("/testRunQuery")
  @Operation(summary = "Run a query with results limited results to test query")
  public Set<String> testRunQuery(HttpServletRequest request, @RequestBody QueryRequest query) throws IOException, SQLException, SQLConversionException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.TestRunQuery.POST")) {
      log.debug("testRunQuery");
      return queryService.testRunQuery(query.getQuery());
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
      return queryService.findMissingArguments(queryRequest);
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
      return queryService.getArgumentType(referenceIri);
    }
  }
}
