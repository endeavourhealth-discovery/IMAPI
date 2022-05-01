package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.queryengine.QueryGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

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
}
