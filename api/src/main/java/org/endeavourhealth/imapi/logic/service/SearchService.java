package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.PathRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.vocabulary.IM;

/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {


  public static final String USAGE_TOTAL = "usageTotal";

  private static QueryRequest getHighestUseRequestFromQuery(QueryRequest queryRequest, ObjectMapper om, QueryRepository repo) throws JsonProcessingException, QueryException {
    QueryRequest highestUsageRequest = om.readValue(om.writeValueAsString(queryRequest), QueryRequest.class);
    repo.unpackQueryRequest(highestUsageRequest, om.createObjectNode());
    if (null != highestUsageRequest.getQuery().getReturn()) {
      highestUsageRequest.getQuery().getReturn().get(0).addProperty(new ReturnProperty().setIri(IM.USAGE_TOTAL).setValueRef(USAGE_TOTAL));
    } else {
      highestUsageRequest.getQuery().addReturn(new Return().addProperty(new ReturnProperty().setIri(IM.USAGE_TOTAL).setValueRef(USAGE_TOTAL)));
    }
    OrderDirection od = new OrderDirection().setDirection(Order.descending);
    od.setValueVariable(USAGE_TOTAL);
    highestUsageRequest.getQuery().setOrderBy(new OrderLimit().setProperty(od));
    highestUsageRequest.setPage(new Page().setPageNumber(1).setPageSize(1));
    return highestUsageRequest;
  }

  /**
   * Queries any IM entity using the query model
   *
   * @param queryRequest Query inside a request with parameters
   * @return a generic JSONDocument containing the results in a format defined by the select statement and including predicate map
   * @throws QueryException if query format is invalid
   */
  public JsonNode queryIM(QueryRequest queryRequest) throws QueryException {
    ObjectNode result = new ObjectMapper().createObjectNode();
    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, result);
    if (null != queryRequest.getTextSearch()) {
      OSQuery osq = new OSQuery();
      JsonNode osResult = osq.imQuery(queryRequest);
      if (osResult != null)
        return osResult;
    }

    return repo.queryIM(queryRequest, false);
  }

  public Boolean askQueryIM(QueryRequest queryRequest) throws QueryException {
    if (null == queryRequest.getAskIri()) throw new IllegalArgumentException("Query request missing askIri");
    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest);
    return repo.askQueryIM(queryRequest);
  }

  /**
   * Queries any IM entity using the query model
   *
   * @param queryRequest Query inside a request with parameters
   * @return a list of SearchResultSummary
   * @throws QueryException if query format is invalid
   */
  public SearchResponse queryIMSearch(QueryRequest queryRequest) throws JsonProcessingException, OpenSearchException, QueryException {
    ObjectMapper om = new ObjectMapper();

    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, om.createObjectNode());

    if (null != queryRequest.getTextSearch()) {
      return new OSQuery().openSearchQuery(queryRequest);
    } else {
      QueryRequest highestUsageRequest = getHighestUseRequestFromQuery(queryRequest, om, repo);

      JsonNode queryResults = repo.queryIM(queryRequest, false);
      JsonNode highestUsageResults = repo.queryIM(highestUsageRequest, true);

      return new QueryService().convertQueryIMResultsToSearchResultSummary(queryResults, highestUsageResults);
    }

  }

  public void validateQueryRequest(QueryRequest queryRequest) throws QueryException {
    if (queryRequest.getQuery() == null && queryRequest.getPathQuery() == null)
      throw new QueryException("Query request must have a Query or an Query object with an iri or a pathQuery");
  }

  /**
   * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
   *
   * @param request holding the search term (multi or single word) + type/status/scheme filters
   * @return A set of Summaries of entity documents from the store
   */
  public SearchResponse getEntitiesByTerm(QueryRequest request) throws OpenSearchException, QueryException {
    return new OSQuery().openSearchQuery(request);
  }

  /**
   * Queries any IM entity using the query model
   *
   * @param pathQuery Query inside a request with parameters
   * @return a generic JSONDocument containing the results in a format defined by the selecr staement and including predicate map
   */
  public PathDocument pathQuery(PathQuery pathQuery) {
    return new PathRepository().pathQuery(pathQuery);
  }

  /**
   * Queries and updates IM entity using the query model
   *
   * @param queryRequest Query inside a request with parameters
   * @throws QueryException if query format is invalid
   */
  public void updateIM(QueryRequest queryRequest) throws JsonProcessingException, QueryException {
    new QueryRepository().updateIM(queryRequest);
  }

}


