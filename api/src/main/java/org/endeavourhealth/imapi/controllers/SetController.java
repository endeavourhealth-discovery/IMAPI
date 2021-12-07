package org.endeavourhealth.imapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.apache.poi.ss.usermodel.Workbook;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/set")
@CrossOrigin(origins = "*")
@Api(value="SetController")
@SwaggerDefinition(tags = {
    @Tag(name = "Set Controller", description = "Main Set endpoint")
})
public class SetController {
    private static final Logger LOG = LoggerFactory.getLogger(SetController.class);

    private final EntityService entityService = new EntityService();
    private final SetService setService = new SetService();

	@GetMapping(value = "/download")
    @ApiOperation(
        value = "Download set",
        notes = "Returns a download for a set"
    )
	public HttpEntity<Object> downloadSet(@RequestParam(name = "iri") String iri,
                                          @RequestParam(name = "expandMembers") boolean expanded,
                                          @RequestParam(name = "v1") boolean v1) throws IOException {
        LOG.debug("downloadSet");

        TTIriRef entity = entityService.getEntityReference(iri);
        String filename = entity.getName() + " " + LocalDate.now();

        Workbook wb = setService.getExcelDownload(iri, expanded, v1);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            wb.write(outputStream);
            wb.close();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "force-download"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".xlsx\"");

            return new HttpEntity<>(outputStream.toByteArray(), headers);
        }
    }

    @GetMapping(value = "/evaluate")
    @ApiOperation(
        value = "Evaluate set",
        notes = "Evaluates a given set"
    )
    public Set<EntitySummary> evaluate(@RequestParam(name = "iri") String iri, @RequestParam(name = "includeLegacy", defaultValue = "false") boolean includeLegacy) {
	    return setService.evaluateConceptSet(iri, includeLegacy);
    }

    @PostMapping(value = "/evaluateEcl", consumes = "text/plain", produces = "application/json")
    @ApiOperation(
        value = "Evaluate ECL",
        notes = "Evaluates an query"
    )
    public Set<EntitySummary> evaluateEcl(@RequestParam(name = "includeLegacy", defaultValue = "false") boolean includeLegacy, @RequestBody String ecl) throws DataFormatException {
        return setService.evaluateDefinition(ecl, includeLegacy);
    }

    @PostMapping(value="/eclSearch", consumes="text/plain", produces="application/json")
    @ApiOperation(
        value="ECL search",
        notes="Search entities using ECL string"
    )
    public SearchResponse eclSearch(
            @RequestParam(name="includeLegacy", defaultValue="false") boolean includeLegacy,
            @RequestParam(name="limit", required = false) Integer limit,
            @RequestBody String ecl
    ) throws DataFormatException {
        try {
            return setService.eclSearch(includeLegacy,limit,ecl);
        } catch (UnknownFormatConversionException e) {
            throw new DataFormatException("Invalid ECL format");
        }
    }
}
