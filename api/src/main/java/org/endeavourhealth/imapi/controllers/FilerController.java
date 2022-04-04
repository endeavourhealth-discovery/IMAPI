package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.service.FilerService;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@PreAuthorize("hasAuthority('CONCEPT_WRITE')")
@RequestMapping("api/filer")
@CrossOrigin(origins = "*")
@Tag(name="FilerController")
@RequestScope
public class FilerController {
    private static final Logger LOG = LoggerFactory.getLogger(FilerController.class);

    private final FilerService filerService = new FilerService();

    @PostMapping("file/document")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    public void fileDocument(@RequestBody TTDocument document) throws TTFilerException {
        LOG.debug("fileDocument");
        filerService.fileDocument(document);
    }

    @PostMapping("file/entity")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    public void fileEntity(@RequestBody TTEntity entity,@RequestBody TTIriRef graph) throws TTFilerException {
        LOG.debug("fileEntity");
        filerService.fileEntity(entity,graph);
    }


}
