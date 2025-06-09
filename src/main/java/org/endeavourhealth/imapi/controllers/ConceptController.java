package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.ConceptService;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.SuperiorPropertiesBoolFocusPagedRequest;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/concept")
@CrossOrigin(origins = "*")
@Tag(name = "Concept Controller")
@RequestScope
@Slf4j
public class ConceptController {

  private final ConceptService conceptService = new ConceptService();

  @GetMapping(value = "/public/matchedFrom", produces = "application/json")
  @Operation(summary = "Get matched terms from the specified entity", description = "Retrieves terms that are matched from the given entity IRI for further processing or analysis.")
  public Collection<SimpleMap> getMatchedFrom(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedFrom.GET")) {
      log.debug("getMatchedFrom");
      return conceptService.getMatchedFrom(iri);
    }
  }

  @GetMapping(value = "/public/matchedTo", produces = "application/json")
  @Operation(summary = "Get matched terms to the specified entity", description = "Retrieves terms that are matched to the given entity IRI for further processing or analysis.")
  public Collection<SimpleMap> getMatchedTo(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedTo.GET")) {
      log.debug("getMatchedTo");
      return conceptService.getMatchedTo(iri);
    }
  }

  @GetMapping("/public/termCode")
  @Operation(summary = "Retrieve term codes for the specified entity", description = "Gets a list of term codes associated with the given entity IRI, including the option to include inactive codes.")
  public List<SearchTermCode> getTermCodes(@RequestParam(name = "iri") String iri, @RequestParam(name = "includeInactive") Optional<Boolean> includeInactive) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.TermCode.GET")) {
      log.debug("getTermCodes");
      return conceptService.getEntityTermCodes(iri, includeInactive.orElseGet(() -> false));
    }
  }

  @GetMapping(value = "/public/propertiesForDomains")
  @Operation(summary = "Get top level properties for an entity as a tree node", description = "Finds the highest parent (superior) properties for an entity and returns then in a tree node format for use in a hierarchy tree")
  public Set<String> getPropertiesForDomains(@RequestParam(name = "conceptIri") Set<String> iris) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.propertiesForDomains.GET")) {
      log.debug("getPropertiesForDomains");
      return conceptService.getPropertiesForDomains(iris);
    }
  }


  @GetMapping(value = "/public/rangesForProperty")
  @Operation(summary = "Get top level property ranges for an entity as a tree node", description = "Finds the highest parent (superior) property value for an entity and returns then in a tree node format for use in a hierarchy tree")
  public Set<String> getRangesForProperty(@RequestParam(name = "propertyIri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.rangesForProperty.GET")) {
      log.debug("getRangesForProperty");
      return conceptService.getRangesForProperty(iri);
    }
  }

  @GetMapping(value = "/public/conceptContextMaps")
  @Operation(summary = "Get concept context maps for the specified entity", description = "Retrieves mappings to various contexts for the given entity IRI, which can be used for contextual analysis.")
  public List<ConceptContextMap> getConceptContextMaps(@RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ConceptContextMaps.GET")) {
      log.debug("getConceptContextMaps");
      return conceptService.getConceptContextMaps(iri);
    }
  }
}
