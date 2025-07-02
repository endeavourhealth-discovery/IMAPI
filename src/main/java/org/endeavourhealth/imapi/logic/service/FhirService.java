package org.endeavourhealth.imapi.logic.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.ECLQueryRequest;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.List;

public class FhirService {
  SetService setService = new SetService();
  EclService eclService = new EclService();

  public String getFhirValueSet(String iri, boolean expanded, Graph graph) throws JsonProcessingException, QueryException {
    List<String> schemes = new ArrayList<>();
    SetOptions setOptions = new SetOptions(iri, true, expanded, false, true, schemes, new ArrayList<>(), graph);
    return setService.getFHIRSetExport(setOptions);
  }

  public String eclToFhir(String data) throws QueryException {
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
    ECLQueryRequest eclQueryRequest = new ECLQueryRequest();
    eclQueryRequest.setEcl(data);
    eclQueryRequest = eclService.getQueryFromECL(eclQueryRequest);
    EclSearchRequest request = new EclSearchRequest();
    request.setEclQuery(eclQueryRequest.getQuery());
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
