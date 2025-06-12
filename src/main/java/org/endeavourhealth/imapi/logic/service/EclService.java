package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.set.EclSearchRequest;
import org.endeavourhealth.imapi.transforms.ECLToIMQ;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class EclService {
  private final SetRepository setRepository = new SetRepository();

  public Query validateModel(Query query) {
    query.setValid(validateMatchWheres(query));
    return query;
  }

  private boolean validateMatchWheres(Match match){
    boolean valid=true;
    if (match.getWhere()!=null){
      Set<String> focusConcepts= new HashSet<>();
      getFocusConcepts(match,focusConcepts);
      if (!validateWhere(match.getWhere(),focusConcepts)){
        valid= false;
      }
    }
    for (List<Match> matches : Arrays.asList(match.getOr(),match.getAnd(),match.getNot())){
      if (matches!=null){
        for (Match m : matches){
          if (!validateMatchWheres(m)){
            valid=false;
          }
        }
      }
    }
    return valid;
  }

  private boolean validateWhere(Where where, Set<String> focusConcepts) {
    boolean valid=true;
    if (where.getIri()!=null){
      if (!setRepository.isValidPropertyForDomains(where.getIri(),focusConcepts)){
        where.setValid(false);
        valid= false;
      }
    }
    if (valid){
      if (where.getIs()!=null) {
        for (Node node : where.getIs()) {
          if (node.getIri() != null) {
            if (!setRepository.isValidRangeForProperty(where.getIri(),node.getIri())) {
              valid= false;
            }
          }
        }
      }
    }
    return valid;
  }

  private void getFocusConcepts(Match match,Set<String> focusConcepts){
    if (match.getInstanceOf()!=null){
      for (Node node : match.getInstanceOf()){
        if (node.getIri()!=null){
          focusConcepts.add(node.getIri());
        }
      }
    }
    for (List<Match> matches : Arrays.asList(match.getOr(),match.getAnd(),match.getNot())){
      if (matches!=null){
        for (Match m : matches){
          getFocusConcepts(m,focusConcepts);
        }
      }
    }
  }

  public String getEcl(Query inferred) throws QueryException {
    if (inferred == null) throw new QueryException("Missing data for ECL conversion");
    else return new IMQToECL().getECLFromQuery(inferred, true);
  }

  public int getEclSearchTotalCount(EclSearchRequest request) throws QueryException {
    return setRepository.getSetExpansionTotalCount(request.getEclQuery(), request.getStatusFilter());
  }

  public Set<Concept> evaluateECLQuery(EclSearchRequest request) throws QueryException {
    return setRepository.getSetExpansionFromQuery(request.getEclQuery(), request.isIncludeLegacy(), request.getStatusFilter(), List.of(), new Page().setPageNumber(request.getPage()).setPageSize(request.getSize()));
  }

  public SearchResponse eclSearch(EclSearchRequest request) throws QueryException {
    int totalCount = getEclSearchTotalCount(request);
    Set<Concept> evaluated = evaluateECLQuery(request);
    List<SearchResultSummary> evaluatedAsSummary = evaluated
      .stream()
      .map(concept ->
        new SearchResultSummary()
          .setIri(concept.getIri())
          .setName(concept.getName())
          .setCode(concept.getCode())
          .setScheme(concept.getScheme())
          .setStatus(concept.getStatus())
          .setType(concept.getType())
      ).toList();
    SearchResponse result = new SearchResponse();
    result.setEntities(evaluatedAsSummary);
    result.setCount(totalCount);
    result.setPage(request.getPage());
    return result;
  }

  public String getECLFromQuery(Query query, Boolean includeNames) throws QueryException {
    return new IMQToECL().getECLFromQuery(query, includeNames);
  }

  public Query getQueryFromEcl(String ecl) throws EclFormatException, QueryException, JsonProcessingException {
    Query query= new ECLToIMQ().getQueryFromECL(ecl);
    new QueryDescriptor().describeQuery(query, DisplayMode.ORIGINAL);
    return query;
  }

  public ECLStatus validateEcl(String ecl) {
    return new ECLToIMQ().validateEcl(ecl);
  }
}
