package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.UserAuthorisationException;
import org.endeavourhealth.imapi.errorhandling.UserNotFoundException;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.FilerService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.logic.service.SecurityService;
import org.endeavourhealth.imapi.model.ProblemDetailResponse;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.requests.EditRequest;
import org.endeavourhealth.imapi.model.requests.FileDocumentRequest;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.security.NamespacePermission;
import org.endeavourhealth.imapi.model.security.Permission;
import org.endeavourhealth.imapi.model.security.Resource;
import org.endeavourhealth.imapi.model.security.User;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.workflow.roleRequest.UserRole;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@RestController
@RequestMapping("api/filer/private")
@CrossOrigin(origins = "*")
@Tag(name = "FilerController")
@RequestScope
@Slf4j
public class FilerController {

  private final FilerService filerService = new FilerService();
  private final EntityService entityService = new EntityService();
  private final SearchService searchService = new SearchService();
  private final SecurityService securityService = new SecurityService();

  @PostMapping("file/document")
  @Operation(summary = "Files a document and returns the task ID.")
  public ResponseEntity<Map<String, String>> fileDocument(@RequestBody FileDocumentRequest fileDocumentRequest, HttpServletRequest request) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Filer.File.Document.POST")) {
      log.debug("fileDocument");
      securityService.requiresPermission(new Permission(Resource.DOCUMENT, List.of(UserRole.EDITOR), List.of(new NamespacePermission(fileDocumentRequest.getInsertNamespace(), true, true))), request);
      User user = securityService.getUser(request);
      String taskId = UUID.randomUUID().toString();
      Map<String, String> response = new HashMap<>();

      if (!filerService.userCanFile(user, Graph.IM))
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

      try {
        filerService.fileDocument(fileDocumentRequest.getDocument(), user.getUsername(), taskId);
        response.put("taskId", taskId);
      } catch (Exception e) {
        Integer taskProgress = filerService.getTaskProgress(taskId);
        response.put("progress", taskProgress == null ? "NONE" : taskProgress.toString());
      }
      return ResponseEntity.ok(response);
    }
  }

  @GetMapping("file/document/{taskId}")
  @Operation(summary = "Retrieves the progress of a document file operation.")
  public ResponseEntity<Map<String, Integer>> getProgress(@PathVariable("taskId") String taskId, HttpServletRequest request) throws UserAuthorisationException {
    log.debug("getProgress");
    securityService.requiresPermission(new Permission(Resource.DOCUMENT, List.of(UserRole.EDITOR), List.of()), request);
    Integer progress = filerService.getTaskProgress(taskId);
    Map<String, Integer> response = new HashMap<>();
    response.put("progress", progress);
    return ResponseEntity.ok(response);
  }

  @PostMapping("file/entity")
  @Operation(summary = "Files an entity with specified graph and CRUD operation.")
  public ResponseEntity<Void> fileEntity(@RequestBody EditRequest editRequest, HttpServletRequest request) throws TTFilerException, IOException, UserAuthorisationException, UserNotFoundException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Filer.File.Entity.POST")) {
      log.debug("fileEntity");
      securityService.requiresPermission(new Permission(Resource.ENTITY, List.of(UserRole.EDITOR), List.of(new NamespacePermission(editRequest.getNamespace(), true, true))), request);
      User user = securityService.getUser(request);
      TTEntity usedEntity = null;
      TTEntity entity = editRequest.getEntity();
      String crud = editRequest.getCrud();
      if (entityService.iriExists(entity.getIri())) {
        usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
        entity.setVersion(usedEntity.getVersion() + 1);
      }

      if (crud != null && !crud.isEmpty()) entity.setCrud(iri(crud));
      filerService.fileEntity(entity, user.getUsername(), usedEntity);
      return ResponseEntity.ok().build();
    }
  }

  @PostMapping("folder/move")
  @Operation(summary = "Moves an entity from one folder to another.")
  public ResponseEntity<ProblemDetailResponse> moveFolder(
    @RequestParam(name = "entity") String entityIri,
    @RequestParam(name = "oldFolder") String oldFolderIri,
    @RequestParam(name = "newFolder") String newFolderIri,
    @RequestParam(name = "namespace", defaultValue = "http://endhealth.info/im#") Namespace namespace,
    HttpServletRequest request
  ) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Filer.Folder.Move.POST")) {
      log.debug("moveFolder");
      securityService.requiresPermission(new Permission(Resource.FOLDER, List.of(UserRole.EDITOR), List.of(new NamespacePermission(namespace, true, true))), request);

      if (!entityService.iriExists(entityIri) || !entityService.iriExists(oldFolderIri) || !entityService.iriExists(newFolderIri)) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot move", "One of the IRIs does not exist");
      }

      if (entityIri.equals(newFolderIri)) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot move", "Cannot move entity into itself");
      }

      if (oldFolderIri.equals(newFolderIri)) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot move", "Source and target are the same");
      }

      TTEntity entity = entityService.getBundle(entityIri, asHashSet(IM.IS_CONTAINED_IN, IM.HAS_SCHEME)).getEntity();
      if (!entity.has(iri(IM.IS_CONTAINED_IN))) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot move", "Entity is not currently in a folder");
      }

      TTArray folders = entity.get(iri(IM.IS_CONTAINED_IN));
      if (!folders.contains(iri(oldFolderIri))) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot move", "Entity is not currently in the specified folder");
      }

      if (entityService.isLinked(newFolderIri, iri(IM.IS_CONTAINED_IN), oldFolderIri)) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot move", "Target folder is a descendant of the Entity");
      }
      TTEntity usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();

      folders.remove(iri(oldFolderIri));
      folders.add(iri(newFolderIri));
      entity.setVersion(usedEntity.getVersion() + 1).setCrud(iri(IM.UPDATE_PREDICATES));

      User user = securityService.getUser(request);
      filerService.fileEntity(entity, user.getUsername(), usedEntity);

      return ResponseEntity.ok().build();
    }
  }

  @PostMapping("folder/add")
  @Operation(summary = "Adds an entity to a specified folder.")
  public ResponseEntity<ProblemDetailResponse> addToFolder(
    @RequestParam(name = "entity") String entityIri,
    @RequestParam(name = "folder") String folderIri,
    @RequestParam(name = "namespace", defaultValue = "http://endhealth.info/im#") Namespace namespace,
    HttpServletRequest request
  ) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Filer.Folder.Add.POST")) {
      log.debug("addToFolder");
      securityService.requiresPermission(new Permission(Resource.FOLDER, List.of(UserRole.EDITOR), List.of(new NamespacePermission(namespace, true, true))), request);
      if (!entityService.iriExists(entityIri) || !entityService.iriExists(folderIri)) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot add to folder", "One of the IRIs does not exist");
      }

      if (entityIri.equals(folderIri)) {
        return ProblemDetailResponse.create(HttpStatus.BAD_REQUEST, "Cannot move", "Cannot move entity into itself");
      }

      TTEntity entity = entityService.getBundle(entityIri, asHashSet(IM.IS_CONTAINED_IN, IM.HAS_SCHEME)).getEntity();
      TTArray folders = entity.get(iri(IM.IS_CONTAINED_IN));
      if (folders == null) folders = new TTArray();
      folders.add(iri(folderIri));

      User user = securityService.getUser(request);
      TTEntity usedEntity = entityService.getBundle(entity.getIri(), null).getEntity();
      entity.setVersion(usedEntity.getVersion() + 1).setCrud(iri(IM.UPDATE_PREDICATES));
      filerService.fileEntity(entity, user.getUsername(), usedEntity);

      return ResponseEntity.ok().build();
    }
  }

  @PostMapping("folder/create")
  @Operation(summary = "Creates a new folder within a specified container.")
  public String createFolder(
    @RequestParam(name = "container") String container,
    @RequestParam(name = "name") String name,
    @RequestParam(name = "namespace", defaultValue = "http://endhealth.info/im#") Namespace namespace,
    HttpServletRequest request
  ) throws Exception {
    securityService.requiresPermission(new Permission(Resource.FOLDER, List.of(UserRole.CREATOR), List.of(new NamespacePermission(namespace, true, true))), request);
    try (MetricsTimer t = MetricsHelper.recordTime("API.Filer.Folder.Create.POST")) {
      log.debug("createFolder");
      if (name.isBlank()) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create, name is null");
      }

      if (!entityService.iriExists(container)) {
        log.error("Cannot create, container does not exist");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create, container does not exist");
      }

      String iri = Namespace.IM + "FLDR_" + URLEncoder.encode(name.replaceAll(" ", ""), StandardCharsets.UTF_8);
      if (entityService.iriExists(iri)) {
        log.error("Entity with that name already exists");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Entity with that name already exists");
      }

      Query query = new Query()
        .setName("Allowable child types for a folder")
        .setIri(Namespace.IM + "Query_AllowableChildTypes");
      QueryRequest queryRequest = new QueryRequest()
        .setQuery(query)
        .argument(a -> a
          .setParameter("this")
          .setValueIri(TTIriRef.iri(container)));
      JsonNode results = searchService.queryIM(queryRequest);

      TTEntity entity = new TTEntity(iri)
        .setName(name)
        .setScheme(iri(Graph.IM))
        .addType(iri(IM.FOLDER))
        .set(iri(IM.IS_CONTAINED_IN), iri(container))
        .setVersion(1)
        .setCrud(iri(IM.ADD_QUADS));

      TTArray contentTypes = new TTArray();
      for (JsonNode j : results.get("entities")) {
        TTIriRef contentType = new TTIriRef();
        contentType.setIri(j.get("iri").asText());
        contentType.setName(j.get(RDFS.LABEL.toString()).asText());
        contentTypes.add(contentType);
      }
      entity.set(iri(IM.CONTENT_TYPE), contentTypes);

      User user = securityService.getUser(request);
      filerService.fileEntity(entity, user.getUsername(), null);
      return iri;
    }
  }

  @GetMapping("deltas/download")
  @Operation(summary = "Downloads deltas as a zip file.")
  public HttpEntity<Object> downloadDeltas(HttpServletRequest request) throws NullPointerException, IOException, UserAuthorisationException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Filer.Deltas.Download.GET")) {
      log.debug("downloadDeltas");
      securityService.requiresPermission(new Permission(Resource.DELTA, List.of(UserRole.ADMIN), List.of()), request);
      HttpHeaders headers = new HttpHeaders();

      // Collect files into Zip
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      try (ZipOutputStream zos = new ZipOutputStream(baos)) {
        try {
          File directory = new File(System.getenv("DELTA_PATH"));
          if (directory.exists()) {
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
            return new HttpEntity<>(baos.toByteArray(), headers);
          }
        } catch (NullPointerException e) {
          log.error("Unable to find environment variable path for delta download.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to find environment variable path for delta download.");
      }
    }
  }
}
