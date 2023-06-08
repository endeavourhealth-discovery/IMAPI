package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.QueryService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.PathDocument;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

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

    @GetMapping(value = "/public/generateSQL", produces = "text/plain")
    @Operation(
        summary = "Generate SQL",
        description = "Generates SQL statement for given query"
    )
    public String generateSQL(@RequestParam(name = "iri") String iri) throws JsonProcessingException {
        /*
        QueryGenerator result = new QueryGenerator().getSelect(iri);
        return result.build();

         */
        return null;
    }


    @PostMapping( "/public/queryIM")
    @Operation(
      summary = "Query IM",
      description = "Runs a generic query on IM"
    )
    public JsonNode queryIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
        LOG.debug("queryIM");
        return searchService.queryIM(queryRequest);
    }


    @PostMapping( "/public/pathQuery")
    @Operation(
      summary = "Path Query ",
      description = "Query IM for a path between source and target"
    )
    public PathDocument pathQuery(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException {
        LOG.debug("pathQuery");
        return searchService.pathQuery(queryRequest);
    }

    @PostMapping(value = "/public/labelQuery")
    @Operation(
        summary = "Add labels to query",
        description = "Add names to iri's within a query"
    )
    public Query labelQuery(@RequestBody Query query) throws DataFormatException {
        return queryService.labelQuery(query);
    }

    @PostMapping( "/public/updateIM")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    @Operation(
      summary = "update  IM",
      description = "Runs a query based update on IM"
    )
    public void updateIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
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
}
