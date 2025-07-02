package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.DataModelService;
import org.endeavourhealth.imapi.model.PropertyDisplay;
import org.endeavourhealth.imapi.model.dto.UIProperty;
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/dataModel")
@CrossOrigin(origins = "*")
@Tag(name = "Data model Controller")
@RequestScope
@Slf4j
public class DataModelController {

  private final DataModelService dataModelService = new DataModelService();

  @Operation(
    summary = "Retrieve a node shape with data model properties",
    description = "Fetches the data model properties for the given IRI."
  )
  @GetMapping("/public/dataModelProperties")
  public NodeShape getDataModelProperties(
    @Parameter(description = "IRI of the data model") @RequestParam(name = "iri") String iri,
    @RequestParam(name = "pathsOnly", required = false, defaultValue = "false") boolean pathsOnly,
    @RequestParam(name = "graph") String graph
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.DataModelProperties.GET")) {
      log.debug("getDataModelProperties " + (pathsOnly ? "paths only" : "") + "for " + iri);
      return dataModelService.getDataModelDisplayProperties(iri, pathsOnly, Graph.from(graph));
    }
  }

  @Operation(
    summary = "Fetches the property display information",
    description = "Returns a list of properties displayed for the given IRI."
  )
  @GetMapping(value = "/public/propertiesDisplay")
  public List<PropertyDisplay> getPropertiesDisplay(
    @Parameter(description = "IRI of the data model") @RequestParam(name = "iri") String iri
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.PropertiesDisplay.GET")) {
      log.debug("getPropertiesDisplay");
      return dataModelService.getPropertiesDisplay(iri);
    }
  }


  @Operation(
    summary = "Retrieve UI property for query builder",
    description = "Returns the UI property metadata for a given data model IRI and property IRI."
  )
  @GetMapping(value = "public/UIPropertyForQB")
  public UIProperty getUIPropertyForQB(
    @Parameter(description = "IRI of the data model") @RequestParam(name = "dmIri") String dmIri,
    @Parameter(description = "IRI of the property") @RequestParam(name = "propIri") String propIri,
    @RequestParam(name = "graph") String graph
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.GetUIPropertyForQB.GET")) {
      log.debug("getUIPropertyForQB");
      return dataModelService.getUIPropertyForQB(dmIri, propIri, Graph.from(graph));
    }
  }

  @Operation(
    summary = "Retrieve data models from a property",
    description = "Returns a list of data models that reference the given property IRI."
  )
  @GetMapping(value = "/public/dataModels")
  public List<TTIriRef> getDataModelsFromProperty(
    @Parameter(description = "IRI of the property")
    @RequestParam(name = "propIri") String propIri,
    @RequestParam(name = "graph") String graph
  ) {
    log.debug("getDataModelsFromProperty");
    return dataModelService.getDataModelsFromProperty(propIri, Graph.from(graph));
  }

  @Operation(
    summary = "Check the type of a property",
    description = "Determines the type of the property for the given IRI."
  )
  @GetMapping(value = "public/checkPropertyType")
  public String checkPropertyType(
    @Parameter(description = "IRI of the property") @RequestParam(name = "propertyIri") String iri, @RequestParam(name = "graph") String graph
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.CheckPropertyType.GET")) {
      log.debug("checkPropertyType");
      return dataModelService.checkPropertyType(iri, Graph.from(graph));
    }
  }
}
