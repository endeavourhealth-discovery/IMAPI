package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.FilerService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

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

    @PostMapping("folder/move")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    public boolean moveFolder(
        @RequestParam(name = "entity") String entityIri,
        @RequestParam(name = "oldFolder") String oldFolderIri,
        @RequestParam(name = "newFolder") String newFolderIri,
        HttpServletRequest request) throws Exception {
        LOG.debug("moveFolder");

        if (!entityService.iriExists(entityIri) || !entityService.iriExists(oldFolderIri) || !entityService.iriExists(newFolderIri)) {
            LOG.warn("Cannot move, one of the IRIs does not exist");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot move, one of the IRIs does not exist");
        }

        TTEntity entity = entityService.getBundle(entityIri, Set.of(IM.IS_CONTAINED_IN.getIri(), IM.HAS_SCHEME.getIri())).getEntity();
        if (!entity.has(IM.IS_CONTAINED_IN)) {
            LOG.warn("Cannot move, entity is not currently in a folder");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot move, entity is not currently in a folder");
        }

        TTArray folders = entity.get(IM.IS_CONTAINED_IN);
        if (!folders.contains(iri(oldFolderIri))) {
            LOG.warn("Cannot move, entity is not currently in the specified folder");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot move, entity is not currently in the specified folder");
        }

        folders.remove(iri(oldFolderIri));
        folders.add(iri(newFolderIri));


        String agentName = reqObjService.getRequestAgentName(request);
        entity.setCrud(IM.UPDATE_PREDICATES);

        TTDocument doc = new TTDocument(IM.GRAPH_DISCOVERY).setCrud(IM.UPDATE_PREDICATES);
        doc.addEntity(entity);

        filerService.fileTransactionDocument(doc, agentName);

        return true;
    }

    @PostMapping("folder/create")
    @PreAuthorize("hasAuthority('CONCEPT_WRITE')")
    public String moveFolder(
        @RequestParam(name = "container") String container,
        @RequestParam(name = "name") String name,
        HttpServletRequest request) throws Exception {
        LOG.debug("createFolder");

        if (!entityService.iriExists(container)) {
            LOG.error("Cannot create, container does not exist");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create, container does not exist");
        }

        String iri = IM.NAMESPACE + "FLDR_" + URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
        if (entityService.iriExists(iri)) {
            LOG.error("Entity with that name already exists");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Entity with that name already exists");
        }

        TTEntity entity = new TTEntity(iri)
            .setName(name)
            .addType(IM.FOLDER)
            .set(IM.IS_CONTAINED_IN, iri(container))
            .setCrud(IM.UPDATE_PREDICATES);

        String agentName = reqObjService.getRequestAgentName(request);

        TTDocument doc = new TTDocument(IM.GRAPH_DISCOVERY).setCrud(IM.ADD_QUADS);
        doc.addEntity(entity);

        filerService.fileTransactionDocument(doc, agentName);

        return iri;
    }

    @GetMapping("deltas/download")
    @PreAuthorize("hasAuthority('IMAdmin')")
    public HttpEntity<Object> downloadDeltas() throws Exception {
        LOG.debug("downloadDeltas");
        HttpHeaders headers = new HttpHeaders();

        // Collect files into Zip
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(ZipOutputStream zos = new ZipOutputStream(baos)) {

            File directory = new File(System.getenv("DELTA_PATH"));
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                if (!file.isDirectory()) {
                    String name = file.getName();
                    if (name.startsWith("TTLog-")) {
                        zos.putNextEntry(new ZipEntry(name));
                        byte[] fileData = Files.readAllBytes(file.toPath());
                        zos.write(fileData);
                        zos.closeEntry();
                    }
                }
            }
            headers.setContentType(new MediaType("application", "force-download"));
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"deltas.zip\"");

        }
        return new HttpEntity<>(baos.toByteArray(), headers);
    }
}
