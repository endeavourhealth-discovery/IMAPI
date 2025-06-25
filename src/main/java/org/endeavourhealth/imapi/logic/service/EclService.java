package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.customexceptions.EclFormatException;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.DisplayMode;
import org.endeavourhealth.imapi.model.imq.ECLStatus;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.transforms.ECLToIMQ;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class EclService {
  private final SetRepository setRepository = new SetRepository();

  public String getEcl(EclSearchRequest inferred) throws QueryException {
    if (inferred == null) throw new QueryException("Missing data for ECL conversion");
    else return new IMQToECL().getECLFromQuery(inferred.getEclQuery(), true, inferred.getGraph());
  }

  public int getEclSearchTotalCount(EclSearchRequest request) throws QueryException {
    return setRepository.getSetExpansionTotalCount(request.getEclQuery(), request.getStatusFilter(), request.getGraph());
  }

  public Set<Concept> evaluateECLQuery(EclSearchRequest request) throws QueryException {
    return setRepository.getSetExpansionFromQuery(request.getEclQuery(), request.isIncludeLegacy(), request.getStatusFilter(), List.of(), new Page().setPageNumber(request.getPage()).setPageSize(request.getSize()), request.getGraph());
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

  public String getECLFromQuery(Query query, Boolean includeNames, String graph) throws QueryException {
    return new IMQToECL().getECLFromQuery(query, includeNames, graph);
  }

  public Query getQueryFromEcl(String ecl, String graph) throws EclFormatException, QueryException, JsonProcessingException {
    Query query = new ECLToIMQ().getQueryFromECL(ecl);
    new QueryDescriptor().describeQuery(query, DisplayMode.ORIGINAL, graph);
    return query;
  }

  public ECLStatus validateEcl(String ecl) {
    return new ECLToIMQ().validateEcl(ecl);
  }
}
