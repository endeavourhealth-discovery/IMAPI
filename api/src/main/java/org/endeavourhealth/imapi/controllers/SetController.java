package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.logic.service.SetService;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.SetContent;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/set")
@CrossOrigin(origins = "*")
@Tag(name = "SetController")
@RequestScope
public class SetController {

  private final EntityService entityService = new EntityService();
  private final SetExporter setExporter = new SetExporter();
  private static final Logger LOG = LoggerFactory.getLogger(SetController.class);
  private static final String ATTACHMENT = "attachment;filename=\"";
  private static final String FORCE_DOWNLOAD = "force-download";
  private static final String APPLICATION = "application";
  private final SetService setService = new SetService();
  private final ConfigManager configManager = new ConfigManager();
  private final RequestObjectService reqObjService = new RequestObjectService();

  @GetMapping(value = "/publish")
  @Operation(
    summary = "Publish set",
    description = "Publishes an expanded set to IM1"
  )
  @PreAuthorize("hasAuthority('IM1_PUBLISH')")
  public void publish(@RequestParam(name = "iri") String iri) throws IOException, QueryException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.Publish.GET")) {
      setExporter.publishSetToIM1(iri);
    }
  }

  @GetMapping(value = "/public/export")
  @Operation(
    summary = "Export set",
    description = "Exporting an expanded set to IM1"
  )
  public HttpEntity<Object> exportSet(@RequestParam(name = "iri") String iri) throws DownloadException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Set.Export.GET")) {
      TTIriRef entity = entityService.getEntityReference(iri);
      String filename = entity.getName() + " " + LocalDate.now();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType("application", "force-download"));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + filename + ".txt\"");
      try {
        String result = setExporter.generateForIm1(iri).toString();
        return new HttpEntity<>(result, headers);
      } catch (QueryException | JsonProcessingException e) {
        throw new DownloadException(("Failed to generate export."));
      }
    }
  }

  @GetMapping("/public/expandedMembers")
  public Set<Concept> getFullyExpandedMembers(
    @RequestParam(name = "iri") String iri,
    @RequestParam(name = "legacy", required = false) boolean legacy,
    @RequestParam(name = "includeSubsets", required = false) boolean includeSubsets,
    @RequestParam(name = "schemes", required = false) List<String> schemes) throws QueryException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ExpandedMembers.GET")) {
      LOG.debug("getFullyExpandedMembers");
      return entityService.getFullyExpandedMembers(iri, legacy, includeSubsets, schemes);
    }
  }

  @GetMapping("/public/subsets")
  public Set<TTIriRef> getSubsets(
    @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Subsets.GET")) {
      LOG.debug("getSubsets");
      return entityService.getSubsets(iri);
    }
  }

  @PostMapping(value = "public/distillation")
  public List<TTIriRef> getDistillation(@RequestBody List<TTIriRef> conceptList) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Distillation.POST")) {
      LOG.debug("getDistillation");
      return entityService.getDistillation(conceptList);
    }
  }

  @GetMapping("/public/setExport")
  public HttpEntity<Object> getSetExport(
    @RequestParam(name = "iri") String iri,
    @RequestParam(name = "definition", defaultValue = "false") boolean definition,
    @RequestParam(name = "core", defaultValue = "false") boolean core,
    @RequestParam(name = "legacy", defaultValue = "false") boolean legacy,
    @RequestParam(name = "includeSubsets", defaultValue = "false") boolean subsets,
    @RequestParam(name = "ownRow", defaultValue = "false") boolean ownRow,
    @RequestParam(name = "im1id", defaultValue = "false") boolean im1id,
    @RequestParam(name = "format") String format,
    @RequestParam(name = "schemes", defaultValue = "") List<String> schemes
  ) throws DownloadException, IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SetExport.GET")) {
      LOG.debug("getSetExport");
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
      headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + "setExport." + format + "\"");

      SetOptions setOptions = new SetOptions(iri, definition, core, legacy, subsets, schemes);
      SetExporterOptions exportOptions = new SetExporterOptions(setOptions, ownRow, im1id);

      try {
        if ("xlsx".equals(format)) {
          XSSFWorkbook workbook = entityService.getSetExport(exportOptions);
          try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            workbook.close();
            return new HttpEntity<>(outputStream.toByteArray(), headers);
          } catch (IOException e) {
            throw new DownloadException("Failed to write to excel document");
          }
        } else if ("csv".equals(format)) {
          String result = setService.getCSVSetExport(exportOptions);
          return new HttpEntity<>(result, headers);
        } else if ("tsv".equals(format)) {
          String result = setService.getTSVSetExport(exportOptions);
          return new HttpEntity<>(result, headers);
        } else if ("object".equals(format)) {
          SetContent result = setService.getSetContent(setOptions);
          return getSetHttpEntity(headers, result);
        } else if ("FHIR".equals(format)) {
          String result = setService.getFHIRSetExport(exportOptions);
          return new HttpEntity<>(result, headers);
        } else {
          return null;
        }
      } catch (IOException e) {
        throw new DownloadException("Failed to write to excel document.");
      } catch (QueryException e) {
        throw new DownloadException("Failed to get set details for download.");
      }
    }
  }

  @PostMapping(value = "/updateSubsetsFromSuper")
  @PreAuthorize("hasAuthority('edit') or hasAuthority('create')")
  public void updateSubsetsFromSuper(@RequestBody TTEntity entity, HttpServletRequest request) throws IOException, TTFilerException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.UpdateSubsetsFromSuper.POST")) {
      LOG.debug("updateSubsetsFromSuper");
      String agentName = reqObjService.getRequestAgentName(request);
      entityService.updateSubsetsFromSuper(agentName, entity);
    }
  }

  private HttpEntity<Object> getSetHttpEntity(HttpHeaders headers, SetContent set) throws JsonProcessingException {

    try (CachedObjectMapper objectMapper = new CachedObjectMapper()) {
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
      String json = objectMapper.writerWithDefaultPrettyPrinter().withAttribute(TTContext.OUTPUT_CONTEXT, true).writeValueAsString(set);
      headers.setContentType(MediaType.APPLICATION_JSON);
      return new HttpEntity<>(json, headers);
    }
  }
}
