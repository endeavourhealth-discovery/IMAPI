package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/query")
@CrossOrigin(origins = "*")
@Tag(name="QueryController")
@RequestScope
public class QueryController {
    private static final Logger LOG = LoggerFactory.getLogger(QueryController.class);

    private final SearchService searchService = new SearchService();

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
    public TTDocument queryIM(@RequestBody QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
        LOG.debug("queryIM");
        return searchService.queryIM(queryRequest);
    }

}
