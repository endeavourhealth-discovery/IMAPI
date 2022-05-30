package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.FilerService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;

@RestController
@PreAuthorize("hasAuthority('CONCEPT_WRITE')")
@RequestMapping("api/filer")
@CrossOrigin(origins = "*")
@Tag(name = "FilerController")
@RequestScope
public class FilerController {
    private static final Logger LOG = LoggerFactory.getLogger(FilerController.class);

    private final FilerService filerService = new FilerService();
    private final EntityService entityService = new EntityService();
    private final RequestObjectService reqObjService = new RequestObjectService();

    @PostMapping("file/document")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    public void fileDocument(@RequestBody TTDocument document, HttpServletRequest request) throws TTFilerException, JsonProcessingException {
        LOG.debug("fileDocument");
        String agentName = reqObjService.getRequestAgentName(request);
        filerService.fileDocument(document, agentName);
    }

    @PostMapping("file/entity")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    public void fileEntity(@RequestBody TTEntity entity, @RequestBody TTIriRef graph, HttpServletRequest request) throws TTFilerException, JsonProcessingException {
        LOG.debug("fileEntity");
        String agentName = reqObjService.getRequestAgentName(request);
        TTEntity usedEntity = null;
        if (entityService.iriExists(entity.getIri())) {
            usedEntity = entityService.getFullEntity(entity.getIri()).getEntity();
            entity.setVersion(usedEntity.getVersion() + 1);
        }
        filerService.fileEntity(entity, graph, agentName, usedEntity);
    }


}
