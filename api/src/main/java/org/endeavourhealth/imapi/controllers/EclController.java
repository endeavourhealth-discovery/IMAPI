package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.EclService;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
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

    @PostMapping(value = "/public/evaluateEcl", consumes = "application/json", produces = "application/json")
    @Operation(
        summary = "Evaluate ECL",
        description = "Evaluates an query"
    )
    public Set<Concept> evaluateEcl(@RequestBody EclSearchRequest request) throws DataFormatException, EclFormatException {
        try {
            return eclService.evaluateECL(request);
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
    ) throws DataFormatException, EclFormatException, JsonProcessingException {
        try {
            return eclService.eclSearch(request);
        } catch (UnknownFormatConversionException ex) {
            throw new EclFormatException("Invalid ECL format", ex);
        }
    }

    @GetMapping(value = "/public/queryFromEcl")
    @Operation(
        summary = "Get query from ecl",
        description = "MapObject ecl to an IM query"
    )
    public Query getQueryFromECL(@RequestParam(name = "ecl") String ecl) throws DataFormatException {
        return eclService.getQueryFromECL(ecl);
    }

    @PostMapping(value = "/public/eclFromQuery")
    @Operation(
        summary = "Get ecl from query",
        description = "MapObject an IM query to ecl"
    )
    public String getECLFromQuery(@RequestBody Query query) throws DataFormatException {
        return eclService.getECLFromQuery(query);
    }

    @GetMapping(value = "/public/validateEcl")
    @Operation(
        summary = "Validate ecl",
        description = "Return validity of ecl string"
    )
    public boolean isValidECL(@RequestParam(name = "ecl") String ecl) {
        return eclService.validateECL(ecl);
    }
}