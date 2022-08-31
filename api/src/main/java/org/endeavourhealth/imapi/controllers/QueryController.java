package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sets.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/query")
@CrossOrigin(origins = "*")
@Tag(name="QueryController")
@RequestScope
public class QueryController {
    private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

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
    @GetMapping(value = "/public/booleanQueryIM", produces = "text/plain")
    @Operation(
      summary = "Boolean query IM",
      description = "SPARQL ASK query passing in iri of query entity and map of query variables- value"
    )
    public String booleanQuery(
      @RequestParam(name = "iri") String iri,
      @RequestParam()Map<String,String> testVariables) throws DataFormatException, JsonProcessingException {
        LOG.debug("booleanQueryIM");
        return new SearchService().booleanQueryIM(iri, testVariables) ?"true" : "false";
    }






    @PostMapping( "/public/queryIM")
    @Operation(
      summary = "Query IM",
      description = "Runs a generic query on IM"
    )
    public ObjectNode queryIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
        LOG.debug("queryIM");
        return new SearchService().queryIM(queryRequest);
    }


    @PostMapping( "/public/summaryEntityQuery")
    @Operation(
      summary = "Query IM returning a standard entity summary response",
      description = "Runs a generic query on IM but limited to a standard list of entity summaries as a response"
    )
    public List<SearchResultSummary> summaryEntityQueryIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
        LOG.debug("entityQuery");
        return new SearchService().summmaryEntityQuery(queryRequest);
    }


    @PostMapping( "/public/entityQuery")
    @Operation(
      summary = "Query IM returning a standard entity summary response",
      description = "Runs a generic query on IM but limited to a standard list of entity summaries as a response"
    )
    public List<TTEntity> entityQueryIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
        LOG.debug("entityQuery");
        return new SearchService().entityQuery(queryRequest);
    }
}
