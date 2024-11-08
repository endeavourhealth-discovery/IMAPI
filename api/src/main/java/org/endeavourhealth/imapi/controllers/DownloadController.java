package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.dataaccess.helpers.XlsHelper;
import org.endeavourhealth.imapi.logic.service.DownloadService;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.DownloadEntityOptions;
import org.endeavourhealth.imapi.model.dto.DownloadDto;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/download")
@CrossOrigin(origins = "*")
@Tag(name = "Download Controller")
@RequestScope
public class DownloadController {
  private static final Logger LOG = LoggerFactory.getLogger(DownloadController.class);
  private static final String ATTACHMENT = "attachment;filename=\"";
  private static final String FORCE_DOWNLOAD = "force-download";
  private static final String APPLICATION = "application";
  private final ConfigManager configManager = new ConfigManager();
  private DownloadService downloadService = new DownloadService();
  private EntityService entityService = new EntityService();

  @PostMapping(value = "/public/download")
  public HttpEntity<Object> download(@RequestBody DownloadEntityOptions downloadEntityOptions) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Download.Download.POST")) {
      LOG.debug("download");
      String iri = downloadEntityOptions.getEntityIri();
      String format = downloadEntityOptions.getFormat();
      if (iri == null || iri.isEmpty() || format == null || format.isEmpty()) return null;
      TTIriRef entity = entityService.getEntityReference(iri);
      List<String> configs = List.of("http://endhealth.info/im#definition", "subtypes", "http://endhealth.info/im#isChildOf", "http://endhealth.info/im#hasChildren", "termCodes");
      String filename = entity.getName() + " " + LocalDate.now();
      HttpHeaders headers = new HttpHeaders();
      if ("excel".equals(format)) {
        XlsHelper xls = downloadService.getExcelDownload(iri, configs, downloadEntityOptions);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
          xls.getWorkbook().write(outputStream);
          xls.getWorkbook().close();
          headers.setContentType(new MediaType(APPLICATION, FORCE_DOWNLOAD));
          headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".xlsx\"");
          return new HttpEntity<>(outputStream.toByteArray(), headers);
        }
      } else {
        DownloadDto json = downloadService.getJsonDownload(iri, configs, downloadEntityOptions);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, ATTACHMENT + filename + ".json\"");
        return new HttpEntity<>(json, headers);
      }
    }
  }
}
