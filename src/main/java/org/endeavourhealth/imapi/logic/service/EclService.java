package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.logic.reasoner.SparqlOptimizer;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.ECLQueryRequest;
import org.endeavourhealth.imapi.model.imq.ECLStatus;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.queryengine.QueryDescriptor;
import org.endeavourhealth.imapi.transforms.ECLToIMQ;
import org.endeavourhealth.imapi.transforms.IMQToECL;
import org.endeavourhealth.interfacemanager.model.DisplayMode;
import org.endeavourhealth.interfacemanager.model.ValidationLevel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class EclService {
  private final SetRepository setRepository = new SetRepository();

  public ECLQueryRequest validateModelFromQuery(ECLQueryRequest eclQuery) {
    eclQuery.setStatus(new ECLQueryValidator().validateQuery(eclQuery.getQuery(), ValidationLevel.ECL));
    return eclQuery;
  }


  public ECLQueryRequest validateModelFromECL(ECLQueryRequest eclQuery) {
    try {
      getQueryFromECL(eclQuery);
      validateModelFromQuery(eclQuery);
      if (!eclQuery.getStatus().isValid()) {
        new IMQToECL().getECLFromQuery(eclQuery);
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


  public String getEcl(ECLQueryRequest inferred) throws QueryException {
    if (inferred == null) throw new QueryException("Missing data for ECL conversion");
    ECLQueryRequest eclQueryRequest = new ECLQueryRequest();
    eclQueryRequest.setShowNames(true);
    eclQueryRequest.setQuery(inferred.getQuery());
    new IMQToECL().getECLFromQuery(eclQueryRequest);
    return eclQueryRequest.getEcl();
  }

  public int getEclSearchTotalCount(ECLQueryRequest request) throws QueryException {
    return setRepository.getSetExpansionTotalCount(request.getQuery(), request.getStatusFilter());
  }

  public Set<Concept> evaluateECLQuery(ECLQueryRequest request) throws QueryException {

    return setRepository.getSetExpansionFromQuery(
      request.getQuery(),
      request.getStatusFilter(),
      List.of(),
      new Page()
        .setPageNumber(request.getPage())
        .setPageSize(request.getSize()));

  }

  public SearchResponse eclSearch(ECLQueryRequest request) throws QueryException {
    new SparqlOptimizer().optimizeQuery(request.getQuery());
    int totalCount = 0;
    if (request.getPage() == 1)
      totalCount = getEclSearchTotalCount(request);
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

  public ECLQueryRequest getECLFromQuery(ECLQueryRequest eclQuery) throws QueryException {
    try {
      new QueryDescriptor().describeQuery(eclQuery.getQuery(), DisplayMode.ORIGINAL);
      new IMQToECL().getECLFromQuery(eclQuery);
    } catch (Exception e) {
      throw new QueryException(e.getMessage(), e);

    }
    return eclQuery;
  }


  public ECLQueryRequest getQueryFromECL(ECLQueryRequest eclQuery) {
    String ecl = eclQuery.getEcl();
    if (ecl == null || ecl.isEmpty())
      return eclQuery;
    new ECLToIMQ().getQueryFromECL(eclQuery, true);
    Query query = eclQuery.getQuery();
    if (query != null && !query.isInvalid()) {
      try {
        new QueryDescriptor().describeQuery(query, DisplayMode.ORIGINAL);
      } catch (Exception e) {
        eclQuery.getStatus().setValid(false);
        eclQuery.getStatus().setMessage(e.getMessage());
      }
    }
    return eclQuery;
  }

  public ECLQueryRequest validateEcl(ECLQueryRequest eclQuery) throws QueryException {
    String ecl = eclQuery.getEcl();
    if (ecl == null || ecl.isEmpty())
      return eclQuery;
    try {
      new ECLToIMQ().getQueryFromECL(eclQuery, true);
      new QueryDescriptor().describeQuery(eclQuery.getQuery(), DisplayMode.ORIGINAL);
    } catch (Exception e) {
      ECLStatus eclStatus = new ECLStatus();
      eclStatus.setValid(false);
      eclStatus.setMessage(e.getMessage());
      eclQuery.setStatus(eclStatus);
      return eclQuery;
    }
    return eclQuery;
  }

  public ECLQueryRequest getEclFromEcl(ECLQueryRequest eclQuery) {
    String ecl = eclQuery.getEcl();
    if (ecl == null || ecl.isEmpty())
      return eclQuery;
    new ECLToIMQ().getQueryFromECL(eclQuery, true);
    new IMQToECL().getECLFromQuery(eclQuery);
    return eclQuery;
  }
}
