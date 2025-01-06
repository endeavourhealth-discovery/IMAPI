package org.endeavourhealth.imapi.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.DataModelService;
import org.endeavourhealth.imapi.model.dto.UIProperty;
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/dataModel")
@CrossOrigin(origins = "*")
@Tag(name = "Data model Controller")
@RequestScope
public class DataModelController {

  private final DataModelService dataModelService = new DataModelService();
  private static final Logger LOG = LoggerFactory.getLogger(DataModelController.class);

  @GetMapping("/public/dataModelProperties")
  public NodeShape getDataModelProperties(@RequestParam(name = "iri") String iri, @RequestParam(name = "parent", required = false) String parent) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DataModelProperties.GET")) {
      LOG.debug("getDataModelProperties");
      return dataModelService.getDataModelDisplayProperties(iri);
    }
  }

  @GetMapping("/public/properties")
  public List<TTIriRef> getProperties() throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.Properties.GET")) {
      LOG.debug("getProperties");
      return dataModelService.getProperties();
    }
  }

  @GetMapping(value = "/public/dataModels")
   public List<TTIriRef> getDataModelsFromProperty(@RequestParam(name = "propIri") String propIri) {
      LOG.debug("getDataModelsFromProperty");
      return dataModelService.getDataModelsFromProperty(propIri);
  }

  @GetMapping(value = "public/checkPropertyType")
  public String checkPropertyType(@RequestParam(name = "propertyIri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.CheckPropertyType.GET")) {
      LOG.debug("checkPropertyType");
      return dataModelService.checkPropertyType(iri);
    }
  }

  @GetMapping(value = "public/UIPropertyForQB")
  public UIProperty getUIPropertyForQB(@RequestParam(name = "dmIri") String dmIri, @RequestParam(name = "propIri") String propIri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.GetUIPropertyForQB.GET")) {
      LOG.debug("getUIPropertyForQB");
      return dataModelService.getUIPropertyForQB(dmIri, propIri);
    }
  }

}
