package org.endeavourhealth.imapi.logic.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.model.set.SetOptions;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.ArrayList;
import java.util.List;

public class FhirService {
  SetService setService = new SetService();
  EclService eclService = new EclService();

  public String getFhirValueSet(String iri, boolean expanded) throws JsonProcessingException, QueryException {
    List<String> schemes = new ArrayList<>();
    SetOptions setOptions = new SetOptions(iri, true, expanded, false, true, schemes, new ArrayList<>());
    return setService.getFHIRSetExport(setOptions);
  }

  public String eclToFhir(String data) throws EclFormatException, QueryException {
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
