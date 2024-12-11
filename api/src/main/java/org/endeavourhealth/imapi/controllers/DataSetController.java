package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.DatasetService;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/dataset")
@CrossOrigin(origins = "*")
@Tag(name = "Dataset Controller")
@RequestScope
public class DataSetController {
  private static final Logger LOG = LoggerFactory.getLogger(DataSetController.class);
  private DatasetService datasetService = new DatasetService();

  @GetMapping("/public/searchAllowableDataMetProperties")
  public SearchResponse searchAllowableDatamodelProperties(@RequestParam(name = "datamodelIri") String datamodelIri, @RequestParam(name = "searchTerm") String searchTerm) throws Exception {
    try (MetricsTimer t = MetricsHelper.recordTime("API.dataset.searchAllowableDataMetProperties.POST")) {
      LOG.debug("searchAllowableDataMetProperties");
      return datasetService.searchAllowableDatamodelProperties(datamodelIri, searchTerm);
    }
  }
}
