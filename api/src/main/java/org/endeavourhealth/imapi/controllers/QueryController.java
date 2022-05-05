package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.sets.DataSet;
import org.endeavourhealth.imapi.model.sets.QNode;
import org.endeavourhealth.imapi.queryengine.QueryGenerator;
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


    @GetMapping( "/public/queryIM")
    @Operation(
      summary = "Query IM",
      description = "Runs a query on IM"
    )
    public QNode queryIM(@RequestBody DataSet query) throws DataFormatException, JsonProcessingException {
        return new SearchService().queryIM(query);
    }
}
