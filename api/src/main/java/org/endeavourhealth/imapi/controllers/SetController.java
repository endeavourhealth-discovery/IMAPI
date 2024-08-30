package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.exporters.SetExporter;
import org.endeavourhealth.imapi.model.customexceptions.DownloadException;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("api/set")
@CrossOrigin(origins = "*")
@Tag(name = "SetController")
@RequestScope
public class SetController {

  private final EntityService entityService = new EntityService();
  private final SetExporter setExporter = new SetExporter();

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
}
