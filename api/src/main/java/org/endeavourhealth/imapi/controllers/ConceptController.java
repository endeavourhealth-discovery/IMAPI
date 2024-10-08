package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.ConceptService;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("api/concept")
@CrossOrigin(origins = "*")
@Tag(name = "Concept Controller")
@RequestScope
public class ConceptController {

  private static final Logger LOG = LoggerFactory.getLogger(ConceptController.class);
  private final ConceptService conceptService = new ConceptService();

  @GetMapping(value = "/public/matchedFrom", produces = "application/json")
  public Collection<SimpleMap> getMatchedFrom(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedFrom.GET")) {
      LOG.debug("getMatchedFrom");
      return conceptService.getMatchedFrom(iri);
    }
  }

  @GetMapping(value = "/public/matchedTo", produces = "application/json")
  public Collection<SimpleMap> getMatchedTo(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedTo.GET")) {
      LOG.debug("getMatchedTo");
      return conceptService.getMatchedTo(iri);
    }
  }

  @GetMapping("/public/termCode")
  public List<SearchTermCode> getTermCodes(@RequestParam(name = "iri") String iri, @RequestParam(name = "includeInactive") Optional<Boolean> includeInactive) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.TermCode.GET")) {
      LOG.debug("getTermCodes");
      return conceptService.getEntityTermCodes(iri, includeInactive.orElseGet(() -> false));
    }
  }

  @GetMapping(value = "/public/superiorPropertiesPaged")
  @Operation(summary = "Get top level properties for an entity as a tree node", description = "Finds the highest parent (superior) properties for an entity and returns then in a tree node format for use in a hierarchy tree")
  public Pageable<EntityReferenceNode> getSuperiorPropertiesPaged(@RequestParam(name = "conceptIri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "inactive", required = false) boolean inactive) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SuperiorPropertiesPaged.GET")) {
      LOG.debug("getSuperiorPropertiesPaged");
      if (null == page) page = 1;
      if (null == size) size = EntityService.MAX_CHILDREN;
      if (null == schemeIris) schemeIris = new ArrayList<>(Arrays.asList(IM.NAMESPACE, SNOMED.NAMESPACE));
      return conceptService.getSuperiorPropertiesPaged(iri, schemeIris, page, size, inactive);
    }
  }

  @GetMapping(value = "/public/superiorPropertiesBoolFocusPaged")
  @Operation(summary = "Get top level properties for an entity as a tree node", description = "Finds the highest parent (superior) properties for an entity and returns then in a tree node format for use in a hierarchy tree")
  public Pageable<EntityReferenceNode> getSuperiorPropertiesBoolFocusPaged(@RequestParam(name = "conceptIris") List<String> conceptIris, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "inactive", required = false) boolean inactive) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SuperiorPropertiesBoolFocusPaged.GET")) {
      LOG.debug("getSuperiorPropertiesBoolFocusPaged");
      if (null == page) page = 1;
      if (null == size) size = EntityService.MAX_CHILDREN;
      if (null == schemeIris) schemeIris = new ArrayList<>(Arrays.asList(IM.NAMESPACE, SNOMED.NAMESPACE));
      return conceptService.getSuperiorPropertiesBoolFocusPaged(conceptIris, schemeIris, page, size, inactive);
    }
  }

  @GetMapping(value = "/public/superiorPropertyValuesPaged")
  @Operation(summary = "Get top level property values for an entity as a tree node", description = "Finds the highest parent (superior) property value for an entity and returns then in a tree node format for use in a hierarchy tree")
  public Pageable<EntityReferenceNode> getSuperiorPropertyValuesPaged(@RequestParam(name = "propertyIri") String iri, @RequestParam(name = "schemeIris", required = false) List<String> schemeIris, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "size", required = false) Integer size, @RequestParam(name = "inactive", required = false) boolean inactive) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.SuperiorPropertyValuesPaged.GET")) {
      LOG.debug("getSuperiorPropertyValuesPaged");
      if (null == page) page = 1;
      if (null == size) size = EntityService.MAX_CHILDREN;
      if (null == schemeIris) schemeIris = new ArrayList<>(Arrays.asList(IM.NAMESPACE, SNOMED.NAMESPACE));
      return conceptService.getSuperiorPropertyValuesPaged(iri, schemeIris, page, size, inactive);
    }
  }

  @GetMapping(value = "/public/conceptContextMaps")
  public List<ConceptContextMap> getConceptContextMaps(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ConceptContextMaps.GET")) {
      LOG.debug("getConceptContextMaps");
      return conceptService.getConceptContextMaps(iri);
    }
  }
}
