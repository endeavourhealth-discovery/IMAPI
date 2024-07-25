package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/query")
@CrossOrigin(origins = "*")
@Tag(name = "QueryController")
@RequestScope
public class QueryController {
  private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

  private final SearchService searchService = new SearchService();
  private final QueryService queryService = new QueryService();

  @PostMapping("/public/queryIM")
  @Operation(
    summary = "Query IM",
    description = "Runs a generic query on IM"
  )
  public JsonNode queryIM(@RequestBody QueryRequest queryRequest) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.QueryIM.POST")) {
      LOG.debug("queryIM");
      return searchService.queryIM(queryRequest);
    }
  }

  @PostMapping("/public/askQueryIM")
  @Operation(summary = "Check if an iri is within a query's results")
  public Boolean askQueryIM(@RequestBody QueryRequest queryRequest) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.AskQueryIM.POST")) {
      LOG.debug("askQueryIM");
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
      LOG.debug("queryIMSearch");
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
      LOG.debug("pathQuery");
      return searchService.pathQuery(pathQuery);
    }
  }

  @PostMapping(value = "/public/labelQuery")
  @Operation(
    summary = "Add labels to query",
    description = "Add names to iri's within a query"
  )
  public Query labelQuery(@RequestBody Query query) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.LabelQuery.POST")) {
      return queryService.labelQuery(query);
    }
  }

  @GetMapping(value = "/public/queryDisplay", produces = "application/json")
  public Query describeQuery(
    @RequestParam(name = "queryIri") String iri
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.Display.GET")) {
      LOG.debug("getQueryDisplay");
      return queryService.describeQuery(iri);
    }
  }

  @PostMapping("/public/queryDisplayFromQuery")
  @Operation(summary = "get query view from imq as viewable object")
  public Query describeQueryContent(@RequestBody Query query) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Query.GetQuery.POST")) {
      LOG.debug("getQueryDisplay");
      return queryService.describeQuery(query);
    }
  }
}
