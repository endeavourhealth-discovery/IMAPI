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

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.zip.DataFormatException;


/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {


  /**
   * Queries any IM entity using the query model
   *
   * @param queryRequest Query inside a request with parameters
   * @return a generic JSONDocument containing the results in a format defined by the select statement and including predicate map
   * @throws DataFormatException if query format is invalid
   */
  public JsonNode queryIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
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

  public Boolean askQueryIM(QueryRequest queryRequest) throws QueryException, DataFormatException, JsonProcessingException {
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
   * @throws DataFormatException if query format is invalid
   */
  public SearchResponse queryIMSearch(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, QueryException {
    ObjectMapper om = new ObjectMapper();

    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, om.createObjectNode());

    if (null != queryRequest.getTextSearch()) {
      SearchResponse results = new OSQuery().openSearchQuery(queryRequest);
      //	if (results==null) {
      //throw new QueryException("IM Query not compatible with open search index.");
      //}
      return results;
      //return new OSQuery().openSearchQuery(queryRequest);
    } else {
      QueryRequest highestUsageRequest = getHighestUseRequestFromQuery(queryRequest, om, repo);

      JsonNode queryResults = repo.queryIM(queryRequest, false);
      JsonNode highestUsageResults = repo.queryIM(highestUsageRequest, true);

      return new QueryService().convertQueryIMResultsToSearchResultSummary(queryResults, highestUsageResults);
    }


  }

  private static QueryRequest getHighestUseRequestFromQuery(QueryRequest queryRequest, ObjectMapper om, QueryRepository repo) throws JsonProcessingException, DataFormatException, QueryException {
    QueryRequest highestUsageRequest = om.readValue(om.writeValueAsString(queryRequest), QueryRequest.class);
    repo.unpackQueryRequest(highestUsageRequest, om.createObjectNode());
    if (null != highestUsageRequest.getQuery().getReturn()) {
      highestUsageRequest.getQuery().getReturn().get(0).addProperty(new ReturnProperty().setIri(IM.USAGE_TOTAL).setValueRef("usageTotal"));
    } else {
      highestUsageRequest.getQuery().addReturn(new Return().addProperty(new ReturnProperty().setIri(IM.USAGE_TOTAL).setValueRef("usageTotal")));
    }
    OrderDirection od = new OrderDirection().setDirection(Order.descending);
    od.setValueVariable("usageTotal");
    highestUsageRequest.getQuery().setOrderBy(new OrderLimit().setProperty(od));
    highestUsageRequest.setPage(new Page().setPageNumber(1).setPageSize(1));
    return highestUsageRequest;
  }

  public void validateQueryRequest(QueryRequest queryRequest) throws DataFormatException {
    if (queryRequest.getQuery() == null && queryRequest.getPathQuery() == null)
      throw new DataFormatException("Query request must have a Query or an Query object with an iri or a pathQuery");
  }

  /**
   * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
   *
   * @param request holding the search term (multi or single word) + type/status/scheme filters
   * @return A set of Summaries of entity documents from the store
   */
  public SearchResponse getEntitiesByTerm(QueryRequest request) throws InterruptedException, OpenSearchException, URISyntaxException, ExecutionException, JsonProcessingException, QueryException, DataFormatException {
    return new OSQuery().openSearchQuery(request);
  }

  /**
   * Queries any IM entity using the query model
   *
   * @param pathQuery Query inside a request with parameters
   * @return a generic JSONDocument containing the results in a format defined by the selecr staement and including predicate map
   * @throws DataFormatException if query format is invalid
   */
  public PathDocument pathQuery(PathQuery pathQuery) throws DataFormatException {
    return new PathRepository().pathQuery(pathQuery);
  }

  /**
   * Queries and updates IM entity using the query model
   *
   * @param queryRequest Query inside a request with parameters
   * @throws DataFormatException if query format is invalid
   */
  public void updateIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, QueryException {
    new QueryRepository().updateIM(queryRequest);
  }

}


