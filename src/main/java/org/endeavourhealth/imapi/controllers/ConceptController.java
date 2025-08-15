package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.logic.service.ConceptService;
import org.endeavourhealth.imapi.logic.service.RequestObjectService;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.utility.MetricsHelper;
import org.endeavourhealth.imapi.utility.MetricsTimer;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
  public Collection<SimpleMap> getMatchedFrom(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "graph", defaultValue = "http://endhealth.info/im#") String graph) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedFrom.GET")) {
      log.debug("getMatchedFrom");
      return conceptService.getMatchedFrom(iri);
    }
  }

  @GetMapping(value = "/public/matchedTo", produces = "application/json")
  @Operation(summary = "Get matched terms to the specified entity", description = "Retrieves terms that are matched to the given entity IRI for further processing or analysis.")
  public Collection<SimpleMap> getMatchedTo(HttpServletRequest request, @RequestParam(name = "iri") String iri) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.MatchedTo.GET")) {
      log.debug("getMatchedTo");
      return conceptService.getMatchedTo(iri);
    }
  }

  @GetMapping("/public/termCode")
  @Operation(summary = "Retrieve term codes for the specified entity", description = "Gets a list of term codes associated with the given entity IRI, including the option to include inactive codes.")
  public List<SearchTermCode> getTermCodes(HttpServletRequest request, @RequestParam(name = "iri") String iri, @RequestParam(name = "includeInactive") Optional<Boolean> includeInactive) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.TermCode.GET")) {
      log.debug("getTermCodes");
      return conceptService.getEntityTermCodes(iri, includeInactive.orElseGet(() -> false));
    }
  }

  @GetMapping(value = "/public/conceptContextMaps")
  @Operation(summary = "Get concept context maps for the specified entity", description = "Retrieves mappings to various contexts for the given entity IRI, which can be used for contextual analysis.")
  public List<ConceptContextMap> getConceptContextMaps(
    HttpServletRequest request,
    @RequestParam(name = "iri") String iri
  ) throws IOException {
    try (MetricsTimer t = MetricsHelper.recordTime("API.Entity.ConceptContextMaps.GET")) {
      log.debug("getConceptContextMaps");
      return conceptService.getConceptContextMaps(iri);
    }
  }
}
