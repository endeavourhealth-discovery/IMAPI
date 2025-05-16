package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.postgress.PostgresService;
import org.endeavourhealth.imapi.postgress.QueryExecutorStatus;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
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
      log.debug("queryIMSearch : {}",queryRequest.getTextSearch());
      return searchService.queryIMSearch(queryRequest);
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
      log.debug("getQueryDisplay");
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
  public String getSQLFromIMQ(@RequestBody Query query) throws IOException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQ.POST")) {
      log.debug("getSQLFromIMQ");
      return queryService.getSQLFromIMQ(query);
    }
  }

  @GetMapping("/public/sql")
  @Operation(
    summary = "Generate SQL from IRI",
    description = "Generates SQL from the given IMQ query IRI."
  )
  public String getSQLFromIMQIri(@RequestParam(name = "queryIri") String queryIri) throws IOException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetSQLFromIMQIri.GET")) {
      log.debug("getSQLFromIMQIri");
      return queryService.getSQLFromIMQIri(queryIri);
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
      try {
        String userId = requestObjectService.getRequestAgentId(request);
        String userName = requestObjectService.getRequestAgentName(request);
        queryService.getSQLFromIMQ(queryRequest.getQuery());
        queryService.addToExecutionQueue(userId, userName, queryRequest);
      } catch (SQLConversionException e) {
        throw new QueryException(e.getMessage());
      }
    }
  }

  @GetMapping("/userQueryQueue")
  @Operation(
    summary = "Get the query queue items and status for a user"
  )
  public Pageable<DBEntry> userQueryQueue(HttpServletRequest request, @PathVariable(name = "page") int page, @PathVariable(name = "size") int size) throws IOException, SQLConversionException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.UserQueryQueue.GET")) {
      log.debug("getUserQueryQueue");
      String userId = requestObjectService.getRequestAgentId(request);
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

  @PostMapping("/cancelQuery")
  @Operation(
    summary = "Cancel a query from running either whilst in the queue or runner using the query uuid"
  )
  public void cancelQuery(@RequestBody UUID id) throws IOException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.CancelQuery.POST")) {
      log.debug("cancelQuery");
      DBEntry entry = postgresService.getById(id);
      entry.setStatus(QueryExecutorStatus.CANCELLED);
      postgresService.update(entry);
    }
  }

  @DeleteMapping("/deleteFromQueue")
  @Operation(
    summary = "Delete a query from the queue using the query uuid"
  )
  public void deleteFromQueue(@RequestParam(name = "id") UUID id) throws IOException, SQLException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.DeleteFromQueue.DELETE")) {
      log.debug("deleteFromQueue");
      DBEntry entry = postgresService.getById(id);
      if (QueryExecutorStatus.CANCELLED.equals(entry.getStatus())) {
        postgresService.delete(id);
      } else {
        throw new IllegalArgumentException("Can only delete an item that has already been cancelled");
      }
    }
  }

  @PostMapping("/requeueQuery")
  @Operation(
    summary = "Requeue a cancelled or errored query"
  )
  public void requeueQuery(HttpServletRequest request, @RequestBody RequeueQueryRequest requeueQueryRequest) throws Exception, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.RequeueQuery.POST")) {
      log.debug("requeueQuery");
      DBEntry entry = postgresService.getById(requeueQueryRequest.getQueueId());
      if (QueryExecutorStatus.CANCELLED.equals(entry.getStatus()) || QueryExecutorStatus.ERRORED.equals(entry.getStatus())) {
        postgresService.delete(requeueQueryRequest.getQueueId());
        String userId = requestObjectService.getRequestAgentId(request);
        String userName = requestObjectService.getRequestAgentName(request);
        queryService.getSQLFromIMQ(requeueQueryRequest.getQueryRequest().getQuery());
        queryService.addToExecutionQueue(userId, userName, requeueQueryRequest.getQueryRequest());
      }
    }
  }

  @PostMapping("/killActiveQuery")
  @Operation(
    summary = "Kills the active running query"
  )
  @PreAuthorize("hasAuthority('IMAdmin')")
  public void killActiveQuery() throws SQLConversionException, SQLException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.KillActiveQuery.POST")) {
      log.debug("killActiveQuery");
      queryService.killActiveQuery();
    }
  }

  @PostMapping("/getQueryResults")
  @Operation(
    summary = "Get query results using a hash of the query request"
  )
  public List<String> getQueryResults(@RequestBody QueryRequest queryRequest) throws IOException, SQLException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQueryResults.GET")) {
      log.debug("getQueryResults");
      return queryService.getQueryResults(queryRequest);
    }
  }

  @PostMapping("/getQueryResultsPaged")
  @Operation(
    summary = "Get paged query results using a hash of the query request"
  )
  public Pageable<String> getQueryResultsPaged(@RequestBody QueryRequest queryRequest) throws IOException, SQLException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQueryResults.GET")) {
      log.debug("getQueryResultsPaged");
      return queryService.getQueryResultsPaged(queryRequest);
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

  @PostMapping("/testRunQuery")
  @Operation(summary = "Run a query with results limited results to test query")
  public List<String> testRunQuery(@RequestBody Query query) throws IOException, SQLException, SQLConversionException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.TestRunQuery.POST")) {
      log.debug("testRunQuery");
      return queryService.testRunQuery(query);
    }
  }
}
