package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.endeavourhealth.imapi.errorhandling.GeneralCustomException;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.SetDiffObject;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/set")
@CrossOrigin(origins = "*")
@Tag(name = "SetController")
@RequestScope
public class SetController {

  private final EntityService entityService = new EntityService();
  private final SetService setService = new SetService();
  private final SetExporter setExporter = new SetExporter();
  private static final Logger LOG = LoggerFactory.getLogger(SetController.class);
  private static final String ATTACHMENT = "attachment;filename=\"";
  private static final String FORCE_DOWNLOAD = "force-download";
  private static final String APPLICATION = "application";
  private final RequestObjectService reqObjService = new RequestObjectService();

  @GetMapping(value = "/publish")
  @Operation(summary = "Publish set", description = "Publishes an expanded set to IM1")
  @PreAuthorize("hasAuthority('IM1_PUBLISH')")
  public void publish(@RequestParam(name = "iri") String iri) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.Publish.GET")) {
      setService.publishSetToIM1(iri);
    }
  }

  @GetMapping(value = "/public/members")
  public Pageable<Node> get(@RequestParam(name = "iri") String iri, @RequestParam(name = "entailments", required = false) boolean entailments, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.EntailedMembers.GET")) {
      LOG.debug("getEntailedMembers");
      if (page == null && size == null) {
        page = 1;
        size = 10;
      }
      return setService.getMembers(iri, entailments, page, size);
    }
  }

  @GetMapping(value = "/public/export")
  @Operation(summary = "Export set", description = "Exporting an expanded set to IM1")
  public HttpEntity<Object> exportSet(@RequestParam(name = "iri") String iri) throws DownloadException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.Export.GET")) {
      TTIriRef entity = entityService.getEntityReference(iri);
      String filename = entity.getName() + " " + LocalDate.now();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType("application", "force-download"));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".txt\"");
      try {
        Set<Concept> members = setService.getExpandedSetMembers(iri, true, true, true, List.of());
        String result = setExporter.generateForIm1(iri, entity.getName(), members).toString();
        return new HttpEntity<>(result, headers);
      } catch (QueryException | JsonProcessingException e) {
        throw new DownloadException(("Failed to generate export."));
      }
    }
  }

  @GetMapping("/public/expandedMembers")
  public Set<Concept> getFullyExpandedMembers(@RequestParam(name = "iri") String iri, @RequestParam(name = "legacy", required = false) boolean legacy, @RequestParam(name = "includeSubsets", required = false) boolean includeSubsets, @RequestParam(name = "schemes", required = false) List<String> schemes) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ExpandedMembers.GET")) {
      LOG.debug("getFullyExpandedMembers");
      return setService.getFullyExpandedMembers(iri, legacy, includeSubsets, schemes);
    }
  }

  @GetMapping("/public/subsets")
  public Set<TTIriRef> getSubsets(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Subsets.GET")) {
      LOG.debug("getSubsets");
      return setService.getSubsets(iri);
    }
  }

  @PostMapping(value = "public/distillation")
  public List<TTIriRef> getDistillation(@RequestBody List<TTIriRef> conceptList) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Distillation.POST")) {
      LOG.debug("getDistillation");
      return setService.getDistillation(conceptList);
    }
  }

  @GetMapping("/public/setExport")
  public HttpEntity<Object> getSetExport(@RequestParam(name = "iri") String iri, @RequestParam(name = "definition", defaultValue = "false") boolean definition, @RequestParam(name = "core", defaultValue = "false") boolean core, @RequestParam(name = "legacy", defaultValue = "false") boolean legacy, @RequestParam(name = "includeSubsets", defaultValue = "false") boolean subsets, @RequestParam(name = "im1id", defaultValue = "false") boolean im1id, @RequestParam(name = "format") String format, @RequestParam(name = "schemes", defaultValue = "") List<String> schemes) throws DownloadException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SetExport.GET")) {
      LOG.debug("getSetExport");
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "setExport." + format + "\"");
      SetOptions setOptions = new SetOptions(iri, definition, core, legacy, subsets, schemes);

      try {
        byte[] setExport = setService.getSetExport(format, im1id, setOptions);
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
  public SetDiffObject getSetComparison(@RequestParam(name = "setIriA") Optional<String> setIriA, @RequestParam(name = "setIriB") Optional<String> setIriB) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.SetDiff.GET")) {
      LOG.debug("getSetComparison");
      return setService.getSetComparison(setIriA, setIriB);
    }
  }

  @PostMapping(value = "/updateSubsetsFromSuper")
  @PreAuthorize("hasAuthority('edit') or hasAuthority('create')")
  public void updateSubsetsFromSuper(@RequestBody TTEntity entity, HttpServletRequest request) throws IOException, TTFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.UpdateSubsetsFromSuper.POST")) {
      LOG.debug("updateSubsetsFromSuper");
      String agentName = reqObjService.getRequestAgentName(request);
      setService.updateSubsetsFromSuper(agentName, entity);
    }
  }
}
