package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.SetDiffObject;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EditRequest;
import org.endeavourhealth.imapi.model.requests.SetDistillationRequest;
import org.endeavourhealth.imapi.model.requests.SetExportRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArrayList;

@RestController
@RequestMapping("api/set")
@CrossOrigin(origins = "*")
@Tag(name = "SetController")
@RequestScope
@Slf4j
public class SetController {

  private static final String ATTACHMENT = "attachment;filename=\"";
  private static final String FORCE_DOWNLOAD = "force-download";
  private static final String APPLICATION = "application";
  private final EntityService entityService = new EntityService();
  private final SetService setService = new SetService();
  private final SetExporter setExporter = new SetExporter();
  private final RequestObjectService reqObjService = new RequestObjectService();

  @GetMapping(value = "/publish")
  @Operation(summary = "Publish set", description = "Publishes an expanded set to IM1")
  @PreAuthorize("hasAuthority('PUBLISHER')")
  public void publish(
    HttpServletRequest request,
    @RequestParam(name = "iri") String iri
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.Publish.GET")) {
      log.debug("publish {}", iri);
      setService.publishSetToIM1(iri);
    }
  }

  @GetMapping(value = "/public/members")
  @Operation(summary = "Get entailed members", description = "Retrieves direct or entailed members from a given IRI with pagination support.")
  public Pageable<Node> getMembers(
    HttpServletRequest request,
    @RequestParam(name = "iri") String iri,
    @RequestParam(name = "entailments", required = false) Boolean entailments,
    @RequestParam(name = "page", required = false) Integer page,
    @RequestParam(name = "size", required = false) Integer size
  ) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.EntailedMembers.GET")) {
      log.debug("getEntailedMembers");
      if (page == null && size == null) {
        page = 1;
        size = 10;
      }
      return setService.getDirectOrEntailedMembersFromIri(iri, entailments, page, size);
    }
  }

  @GetMapping(value = "/public/export")
  @Operation(summary = "Export set", description = "Exporting an expanded set to IM1")
  public HttpEntity<Object> exportSet(
    HttpServletRequest request,
    @RequestParam(name = "iri") String iri
  ) throws DownloadException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.Export.GET")) {
      log.debug("exportSet");
      TTIriRef entity = entityService.getEntityReference(iri);
      String filename = entity.getName() + " " + LocalDate.now();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType("application", "force-download"));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".txt\"");
      try {
        Set<Concept> members = setService.getExpandedSetMembers(iri, true, true, true, List.of(), asArrayList(IM.SUBSUMED_BY));
        String result = setExporter.generateForIm1(iri, entity.getName(), members).toString();
        return new HttpEntity<>(result, headers);
      } catch (QueryException | JsonProcessingException e) {
        throw new DownloadException(("Failed to generate export."));
      }
    }
  }


  @GetMapping("/public/subsets")
  @Operation(summary = "Get subsets of entity", description = "Fetches all subsets for the given IRI.")
  public Set<TTIriRef> getSubsets(HttpServletRequest request, @RequestParam(name = "iri") String iri) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Subsets.GET")) {
      log.debug("getSubsets");
      return setService.getSubsets(iri);
    }
  }

  @PostMapping(value = "public/distillation")
  @Operation(summary = "Get semantic distillation", description = "Performs a semantic distillation process for the given list of concepts.")
  public List<TTIriRef> getDistillation(HttpServletRequest request, @RequestBody SetDistillationRequest setDistillationRequest) {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Distillation.POST")) {
      log.debug("getDistillation");
      return setService.getDistillation(setDistillationRequest.getConceptList());
    }
  }

  @PostMapping(value = "/public/setExport")
  @Operation(
    summary = "Export a set in the specified format",
    description = "Exports a set of data according to the provided options, including various flags such as definition, core, legacy, subsets, etc."
  )
  public HttpEntity<Object> getSetExport(
    HttpServletRequest request,
    @RequestBody SetExportRequest setExportRequest
  ) throws DownloadException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SetExport.GET")) {
      log.debug("getSetExport");
      if (setExportRequest.getOptions().getSubsumptions() == null || setExportRequest.getOptions().getSubsumptions().isEmpty()) {
        setExportRequest.getOptions().setSubsumptions(asArrayList(IM.SUBSUMED_BY));
      }
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "setExport." + setExportRequest.getFormat() + "\"");

      try {
        byte[] setExport = setService.getSetExport(setExportRequest.getFormat(), setExportRequest.getOptions().isIncludeIM1id(), setExportRequest.getOptions());
        return new HttpEntity<>(setExport, headers);
      } catch (IOException e) {
        throw new DownloadException("Failed to write to document.");
      } catch (QueryException e) {
        throw new DownloadException("Failed to get set details for download.");
      } catch (GeneralCustomException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @GetMapping(value = "/public/setDiff")
  @Operation(summary = "Compare two sets", description = "Compares two sets identified by the provided IRIs and returns their differences.")
  public SetDiffObject getSetComparison(
    HttpServletRequest request,
    @RequestParam(name = "setIriA") Optional<String> setIriA,
    @RequestParam(name = "setIriB") Optional<String> setIriB
  ) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.SetDiff.GET")) {
      log.debug("getSetComparison");
      return setService.getSetComparison(setIriA, setIriB);
    }
  }

  @PostMapping(value = "/updateSubsetsFromSuper")
  @Operation(summary = "Update subsets from super", description = "Updates subsets from a superclass according to the provided entity details.")
  @PreAuthorize("hasAuthority('EDITOR') or hasAuthority('CREATOR')")
  public void updateSubsetsFromSuper(@RequestBody EditRequest editRequest, HttpServletRequest request) throws IOException, TTFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.UpdateSubsetsFromSuper.POST")) {
      log.debug("updateSubsetsFromSuper");
      String agentName = reqObjService.getRequestAgentName(request);
      setService.updateSubsetsFromSuper(agentName, editRequest.getEntity(), editRequest.getGraph());
    }
  }
}
