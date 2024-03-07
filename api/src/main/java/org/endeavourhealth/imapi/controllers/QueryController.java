package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/query")
@CrossOrigin(origins = "*")
@Tag(name="QueryController")
@RequestScope
public class QueryController {
    private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

    private final SearchService searchService = new SearchService();
    private final QueryService queryService = new QueryService();

    @PostMapping( "/public/queryIM")
    @Operation(
      summary = "Query IM",
      description = "Runs a generic query on IM"
    )
    public JsonNode queryIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, IOException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
        LOG.debug("queryIM");
        return searchService.queryIM(queryRequest);
    }

    @PostMapping("/public/askQueryIM")
    @Operation(summary = "Check if an iri is within a query's results")
    public Boolean askQueryIM(@RequestBody QueryRequest queryRequest) throws QueryException, DataFormatException, JsonProcessingException {
        LOG.debug("askQueryIM");
        return searchService.askQueryIM(queryRequest);
    }

    @PostMapping("/public/queryIMSearch")
    @Operation(
        summary = "Query IM returning conceptSummaries",
        description = "Runs a generic query on IM and returns results as ConceptSummary items."
    )
    public SearchResponse queryIMSearch(@RequestBody QueryRequest queryRequest) throws DataFormatException, IOException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
        LOG.debug("queryIMSearch");
        return searchService.queryIMSearch(queryRequest);
    }

    @PostMapping("/public/getQuery")
    @Operation(summary = "get query as query object")
    public Query getQuery(@RequestBody QueryRequest queryRequest) throws QueryException, DataFormatException, JsonProcessingException {
        LOG.debug("getQuery");
        return searchService.getQuery(queryRequest);
    }


    @PostMapping( "/public/pathQuery")
    @Operation(
      summary = "Path Query ",
      description = "Query IM for a path between source and target"
    )
    public PathDocument pathQuery(@RequestBody QueryRequest queryRequest) throws DataFormatException {
        LOG.debug("pathQuery");
        return searchService.pathQuery(queryRequest);
    }

    @PostMapping(value = "/public/labelQuery")
    @Operation(
        summary = "Add labels to query",
        description = "Add names to iri's within a query"
    )
    public Query labelQuery(@RequestBody Query query) {
        return queryService.labelQuery(query);
    }

    @PostMapping( "/public/updateIM")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    @Operation(
      summary = "update  IM",
      description = "Runs a query based update on IM"
    )
    public void updateIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, QueryException {
        LOG.debug("updateIM");
        searchService.updateIM(queryRequest);
    }

    @GetMapping("/public/allQueries")
    @Operation(
            summary = "Get all queries",
            description = "Get all queries"
    )
    public List<TTIriRef> getAllQueries() {
        return queryService.getAllQueries();
    }

    @GetMapping("/public/allByType")
    @Operation(
            summary = "Get entities by type",
            description = "Get entities by type"
    )
    public List<TTIriRef> getAllByType(@RequestParam(name = "iri") String typeIri) {
        return queryService.getAllByType(typeIri);
    }
}
