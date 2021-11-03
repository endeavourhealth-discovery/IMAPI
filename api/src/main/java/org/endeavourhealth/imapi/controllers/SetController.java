package org.endeavourhealth.imapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.apache.poi.ss.usermodel.Workbook;
import org.endeavourhealth.imapi.dataaccess.repository.EntitySearchRepository;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.search.EntitySummary;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.transforms.ECLToTT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

    @Autowired
    EntityService entityService;

    EntitySearchRepository entitySearchRepository = new EntitySearchRepository();

    SetService setService = new SetService();

	@GetMapping(value = "/download")
    @ApiOperation(
        value = "Download set",
        notes = "Returns a download for a set"
    )
	public HttpEntity<Object> downloadSet(@RequestParam(name = "iri") String iri,
                                          @RequestParam(name = "expandMembers") boolean expanded,
                                          @RequestParam(name = "v1") boolean v1) throws SQLException, IOException {
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
    public Set<TTIriRef> evaluate(@RequestParam(name = "iri") String iri, @RequestParam(name = "includeLegacy", defaultValue = "false") boolean includeLegacy) throws SQLException {
	    return setService.evaluateConceptSet(iri, includeLegacy);
    }

    @PostMapping(value = "/evaluateEcl", consumes = "text/plain", produces = "application/json")
    @ApiOperation(
        value = "Evaluate ECL",
        notes = "Evaluates an query"
    )
    public Set<TTIriRef> evaluateEcl(@RequestParam(name = "includeLegacy", defaultValue = "false") boolean includeLegacy, @RequestBody String ecl) throws SQLException, DataFormatException {
        TTValue definition = new ECLToTT().getClassExpression(ecl);
        return setService.evaluateDefinition(definition, includeLegacy);
    }

    @PostMapping(value="/eclSearch", consumes="text/plain", produces="application/json")
    @ApiOperation(
        value="ECL search",
        notes="Search entities using ECL string"
    )
    public SearchResponse elcSearch(
            @RequestParam(name="includeLegacy", defaultValue="false") boolean includeLegacy,
            @RequestParam(name="limit", required = false) Integer limit,
            @RequestBody String ecl
    ) throws DataFormatException, SQLException {
        Set<TTIriRef> evaluated = evaluateEcl(includeLegacy, ecl);
        List<EntitySummary> evaluatedAsSummary = evaluated.stream().limit(limit != null ? limit : 1000).map(ttIriRef -> {
            try {
                return entitySearchRepository.getSummary(ttIriRef.getIri());
            } catch (SQLException e) {
                return new EntitySummary().setIri(ttIriRef.getIri()).setName(ttIriRef.getName());
            }
        }).collect(Collectors.toList());
        SearchResponse result = new SearchResponse();
        result.setEntities(evaluatedAsSummary);
        result.setCount(evaluated.size());
        result.setPage(1);
        return result;
    }
}
