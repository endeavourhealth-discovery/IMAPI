package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/set")
@CrossOrigin(origins = "*")
@Tag(name = "SetController")
@RequestScope
public class SetController {

    private final EntityService entityService = new EntityService();
    private final SetService setService = new SetService();
    private final SetExporter setExporter = new SetExporter();

    @PostMapping(value = "/public/evaluateEcl", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Evaluate ECL",
            description = "Evaluates an query"
    )
    public Set<Concept> evaluateEcl(@RequestBody EclSearchRequest request) throws DataFormatException, EclFormatException {
        try {
            return setService.evaluateECL(request);
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
            return setService.eclSearch(request);
        } catch (UnknownFormatConversionException ex) {
            throw new EclFormatException("Invalid ECL format", ex);
        }
    }

    @GetMapping(value = "/publish")
    @Operation(
            summary = "Publish set",
            description = "Publishes an expanded set to IM1"
    )
    @PreAuthorize("hasAuthority('IM1_PUBLISH')")
    public void publish(@RequestParam(name = "iri") String iri) throws DataFormatException, JsonProcessingException {
        setExporter.publishSetToIM1(iri);
    }

    @GetMapping(value = "/public/ecl/query")
    @Operation(
            summary = "Get query from ecl",
            description = "MapObject ecl to an IM query"
    )
    public Query getQueryFromECL(@RequestParam(name = "ecl") String ecl) throws DataFormatException {
        return setService.getQueryFromECL(ecl);
    }

    @PostMapping(value = "/public/query/ecl")
    @Operation(
            summary = "Get ecl from query",
            description = "MapObject an IM query to ecl"
    )
    public String getECLFromQuery(@RequestBody Query query) throws DataFormatException {
        return setService.getECLFromQuery(query);
    }

    @GetMapping(value = "/public/ecl/validity")
    @Operation(
            summary = "Validate ecl",
            description = "Return validity of ecl string"
    )
    public boolean isValidECL(@RequestParam(name = "ecl") String ecl) {
        return setService.validateECL(ecl);
    }

    @GetMapping(value = "/public/export")
    @Operation(
            summary = "Export set",
            description = "Exporting an expanded set to IM1"
    )
    public HttpEntity<Object> exportSet(@RequestParam(name = "iri") String iri) throws DataFormatException, JsonProcessingException {
        TTIriRef entity = entityService.getEntityReference(iri);
        String filename = entity.getName() + " " + LocalDate.now();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "force-download"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".txt\"");
        String result = setExporter.generateForIm1(iri).toString();
        return new HttpEntity<>(result, headers);
    }
}
