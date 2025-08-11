package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.EclSearchRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.transforms.ECLToIMQ;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class EclService {
  private final SetRepository setRepository = new SetRepository();

  public ECLQueryRequest validateModelFromQuery(ECLQueryRequest eclQuery, List<Graph> graphs) {
    eclQuery.setStatus(new ECLQueryValidator().validateQuery(eclQuery.getQuery(), ValidationLevel.ECL, graphs));
    return eclQuery;
  }


  public ECLQueryRequest validateModelFromECL(ECLQueryRequest eclQuery, List<Graph> graphs) {
    try {
      getQueryFromECL(eclQuery, graphs);
      validateModelFromQuery(eclQuery, graphs);
      if (!eclQuery.getStatus().isValid()) {
        new IMQToECL().getECLFromQuery(eclQuery, graphs);
      }
      return eclQuery;
    } catch (Exception e) {
      ECLStatus eclStatus = new ECLStatus();
      eclStatus.setValid(false);
      eclStatus.setMessage(e.getMessage());
      eclQuery.setStatus(eclStatus);
      return eclQuery;
    }
  }


  public String getEcl(EclSearchRequest inferred, List<Graph> graphs) throws QueryException {
    if (inferred == null) throw new QueryException("Missing data for ECL conversion");
    ECLQueryRequest eclQueryRequest = new ECLQueryRequest();
    eclQueryRequest.setQuery(inferred.getEclQuery());
    new IMQToECL().getECLFromQuery(eclQueryRequest, graphs);
    return eclQueryRequest.getEcl();
  }

  public int getEclSearchTotalCount(EclSearchRequest request, List<Graph> graphs) throws QueryException {
    return setRepository.getSetExpansionTotalCount(request.getEclQuery(), request.getStatusFilter(), graphs);
  }

  public Set<Concept> evaluateECLQuery(EclSearchRequest request, List<Graph> graphs) throws QueryException {
    return setRepository.getSetExpansionFromQuery(
      request.getEclQuery(),
      request.isIncludeLegacy(),
      request.getStatusFilter(),
      List.of(),
      new Page()
        .setPageNumber(request.getPage())
        .setPageSize(request.getSize()),
      graphs);
  }

  public SearchResponse eclSearch(EclSearchRequest request, List<Graph> graphs) throws QueryException {
    int totalCount = getEclSearchTotalCount(request, graphs);
    Set<Concept> evaluated = evaluateECLQuery(request, graphs);
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

  public ECLQueryRequest getECLFromQuery(ECLQueryRequest eclQuery, List<Graph> graphs) throws QueryException {
    new IMQToECL().getECLFromQuery(eclQuery, graphs);
    return eclQuery;
  }


  public ECLQueryRequest getQueryFromECL(ECLQueryRequest eclQuery, List<Graph> graphs) throws QueryException {
    String ecl = eclQuery.getEcl();
    if (ecl == null || ecl.isEmpty())
      return eclQuery;
    new ECLToIMQ().getQueryFromECL(eclQuery, true, graphs);
    Query query = eclQuery.getQuery();
    if (query != null && !query.isInvalid()) {
      try {
        new QueryDescriptor().describeQuery(query, DisplayMode.ORIGINAL, graphs);
      } catch (Exception e) {
        eclQuery.getStatus().setValid(false);
        eclQuery.getStatus().setMessage(e.getMessage());
      }
    }
    return eclQuery;
  }

  public ECLQueryRequest validateEcl(ECLQueryRequest eclQuery, List<Graph> graphs) throws QueryException {
    String ecl = eclQuery.getEcl();
    if (ecl == null || ecl.isEmpty())
      return eclQuery;
    new ECLToIMQ().getQueryFromECL(eclQuery, true, graphs);
    return eclQuery;
  }

  public ECLQueryRequest getEclFromEcl(ECLQueryRequest eclQuery, List<Graph> graphs) {
    String ecl = eclQuery.getEcl();
    if (ecl == null || ecl.isEmpty())
      return eclQuery;
    new ECLToIMQ().getQueryFromECL(eclQuery, true, graphs);
    new IMQToECL().getECLFromQuery(eclQuery, graphs);
    return eclQuery;
  }
}
