package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/ecl")
@CrossOrigin(origins = "*")
@Tag(name = "Ecl Controller")
@RequestScope
public class EclController {
    private static final Logger LOG = LoggerFactory.getLogger(EclController.class);

    private final EclService eclService = new EclService();

    @PostMapping("/public/ecl")
    public String getEcl(@RequestBody Query inferred) throws DataFormatException, JsonProcessingException {
        LOG.debug("getEcl");
        return eclService.getEcl(inferred);
    }

    @PostMapping(value = "/public/evaluateEclQuery", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Evaluate ECL",
        description = "Evaluates an query"
    )
    public Set<Concept> evaluateEcl(@RequestBody EclSearchRequest request) throws DataFormatException, QueryException,EclFormatException {
        try {
            return eclService.evaluateECLQuery(request);
        } catch (UnknownFormatConversionException | JsonProcessingException ex) {
            throw new EclFormatException("Invalid ECL format", ex);
        }
    }

    @PostMapping(value = "/public/eclSearch", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "ECL search",
        description = "Search entities using ECL string"
    )
    public SearchResponse eclSearch(
        @RequestBody EclSearchRequest request
    ) throws DataFormatException, EclFormatException, JsonProcessingException,QueryException {
        try {
            return eclService.eclSearch(request);
        } catch (UnknownFormatConversionException ex) {
            throw new EclFormatException("Invalid ECL format", ex);
        }
    }

    @PostMapping(value = "/public/eclFromQuery")
    @Operation(
        summary = "Get ecl from query",
        description = "MapObject an IM query to ecl"
    )
    public String getECLFromQuery(@RequestBody Query query) throws DataFormatException {
        return eclService.getECLFromQuery(query);
    }
}
