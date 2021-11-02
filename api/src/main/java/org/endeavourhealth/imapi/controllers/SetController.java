package org.endeavourhealth.imapi.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.apache.poi.ss.usermodel.Workbook;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
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
import java.util.Set;

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
    public Set<TTIriRef> evaluate(@RequestParam(name = "iri") String iri) throws SQLException {
	    return setService.evaluateConceptSet(iri);
    }
}
