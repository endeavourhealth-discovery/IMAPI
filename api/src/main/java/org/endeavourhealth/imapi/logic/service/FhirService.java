package org.endeavourhealth.imapi.logic.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.endeavourhealth.imapi.config.ConfigManager;
import org.endeavourhealth.imapi.controllers.FhirController;
import org.endeavourhealth.imapi.controllers.GithubController;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.exporters.SetExporterOptions;
import org.endeavourhealth.imapi.model.github.GithubRelease;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.CONFIG;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class FhirService {
  private static final Logger LOG = LoggerFactory.getLogger(FhirController.class.getName());
  ConfigManager configManager = new ConfigManager();
  SetService setService = new SetService();
  EclService eclService = new EclService();

  public String getFhirValueSet(String iri, boolean expanded) throws JsonProcessingException, QueryException {
    List<String> schemes = new ArrayList<>();
    SetOptions setOptions = new SetOptions(iri, true, expanded, false, true, schemes);
    SetExporterOptions exportOptions = new SetExporterOptions(setOptions, false, false);
    return setService.getFHIRSetExport(exportOptions);
  }

  public String eclToFhir(String data) throws DataFormatException, EclFormatException, QueryException {
    ValueSet result = new ValueSet();
    ValueSet.ValueSetExpansionComponent expansion = new ValueSet.ValueSetExpansionComponent();
    ValueSet.ConceptSetFilterComponent filter = new ValueSet.ConceptSetFilterComponent();
    ValueSet.ValueSetComposeComponent compose = new ValueSet.ValueSetComposeComponent();
    ValueSet.ConceptSetComponent includeConcept = new ValueSet.ConceptSetComponent();
    List<ValueSet.ValueSetExpansionContainsComponent> contains = new ArrayList<>();
    List<ValueSet.ConceptSetFilterComponent> filters = new ArrayList<>();
    List<ValueSet.ConceptSetComponent> includes = new ArrayList<>();

    filter.setValue(data.replace("\r", " "));
    filters.add(filter);
    includeConcept.setFilter(filters);
    includes.add(includeConcept);
    compose.setInclude(includes);
    result.setCompose(compose);

    Query eclQuery = eclService.getQueryFromEcl(data);
    EclSearchRequest request = new EclSearchRequest();
    request.setEclQuery(eclQuery);
    request.setIncludeLegacy(false);
    request.setSize(0);
    SearchResponse evaluated = eclService.eclSearch(request);
    if (!evaluated.getEntities().isEmpty()) {
      for (SearchResultSummary entity : evaluated.getEntities()) {
        ValueSet.ValueSetExpansionContainsComponent contained = new ValueSet.ValueSetExpansionContainsComponent();
        contained.setId(entity.getIri());
        contained.setDisplay(entity.getName());
        contained.setCode(entity.getCode());
        contained.setSystem(entity.getScheme().getIri());
        contains.add(contained);
      }
    }
    expansion.setContains(contains);
    result.setExpansion(expansion);

    FhirContext ctx = FhirContext.forR4();
    IParser parser = ctx.newJsonParser();

    return parser.encodeResourceToString(result);
  }
}
