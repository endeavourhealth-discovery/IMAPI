package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.OSQuery;
import org.endeavourhealth.imapi.dataaccess.PathRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;

/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {


  public static final String USAGE_TOTAL = "usageTotal";

  private static QueryRequest getHighestUseRequestFromQuery(QueryRequest queryRequest, ObjectMapper om, QueryRepository repo, List<Graph> graphs) throws JsonProcessingException, QueryException {
    QueryRequest highestUsageRequest = om.readValue(om.writeValueAsString(queryRequest), QueryRequest.class);
    repo.unpackQueryRequest(highestUsageRequest, om.createObjectNode(), graphs);
    highestUsageRequest.getQuery().setReturn(new Return().property(p -> p.setIri(IM.USAGE_TOTAL)));
    OrderDirection od = new OrderDirection().setDirection(Order.descending);
    od.setValueVariable(USAGE_TOTAL);
    highestUsageRequest.getQuery().setReturn(new Return().setOrderBy(new OrderLimit().addProperty(od)));
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
  public JsonNode queryIM(QueryRequest queryRequest, List<Graph> graphs) throws QueryException, OpenSearchException {
    ObjectNode result = new ObjectMapper().createObjectNode();
    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, result, graphs);
    if (null != queryRequest.getTextSearch()) {
      OSQuery osq = new OSQuery();
      JsonNode osResult = osq.imOpenSearchQuery(queryRequest);
      if (osResult != null && osResult.get("entities") != null)
        return osResult;
      else {
        return repo.queryIM(queryRequest, false, graphs);
      }
    }

    return repo.queryIM(queryRequest, false, graphs);
  }


  public Boolean askQueryIM(QueryRequest queryRequest, List<Graph> graphs) throws QueryException {
    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, graphs);
    return repo.askQueryIM(queryRequest, graphs);
  }

  /**
   * Queries any IM entity using the query model
   *
   * @param queryRequest Query inside a request with parameters
   * @return a list of SearchResultSummary
   * @throws QueryException if query format is invalid
   */
  public SearchResponse queryIMSearch(QueryRequest queryRequest, List<Graph> graphs) throws JsonProcessingException, OpenSearchException, QueryException {
    ObjectMapper om = new ObjectMapper();

    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, om.createObjectNode(), graphs);

    if (null != queryRequest.getTextSearch()) {
      SearchResponse results = new OSQuery().openSearchQuery(queryRequest);
      if (results != null) return results;
    }
    JsonNode queryResults = repo.queryIM(queryRequest, false, graphs);
    return new QueryService().convertQueryIMResultsToSearchResultSummary(queryResults, queryResults, graphs);
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
  public PathDocument pathQuery(PathQuery pathQuery, List<Graph> graphs) {
    return new PathRepository().pathQuery(pathQuery, graphs);
  }

  /**
   * Queries and updates IM entity using the query model
   *
   * @param queryRequest Query inside a request with parameters
   * @throws QueryException if query format is invalid
   */
  public void updateIM(QueryRequest queryRequest, List<Graph> userGraphs, Graph insertGraph) throws JsonProcessingException, QueryException {
    new QueryRepository().updateIM(queryRequest, userGraphs, insertGraph);
  }

}


