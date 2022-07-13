package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.sets.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

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
        return new SearchService().booleanQueryIM(iri, testVariables)==true ?"true" : "false";
    }


    @GetMapping(value = "/public/queryByIri", produces = "application/json")
    @Operation(
      summary = "Queries the IM using a query entity iri",
      description = "SPARQL select query passing in iri of query entity and map of query variables- value for text pass 'text=theterm' "
    )
    public ObjectNode queryByIri(
      @RequestParam(name = "iri") String iri,
      @RequestParam()Map<String,String> testVariables) throws DataFormatException, JsonProcessingException {
        LOG.debug("queryByIri");
        return new SearchService().queryIM(iri,testVariables);

    }




    @PostMapping( "/public/queryIM")
    @Operation(
      summary = "Query IM",
      description = "Runs a query on IM"
    )
    public ObjectNode queryIM(@RequestBody Query query) throws DataFormatException, JsonProcessingException {
        LOG.debug("queryIM");
        return new SearchService().queryIM(query);
    }
}
