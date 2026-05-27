package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.OSQuery;
import org.endeavourhealth.imapi.dataaccess.PathRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.interfacemanager.model.GRAPH;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.Order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Methods for searching open search / elastic repositories
 */

public class SearchService {

  public static final String USAGE_TOTAL = "usageTotal";
  public static final String ENTITIES = "entities";

  private static QueryRequest getHighestUseRequestFromQuery(QueryRequest queryRequest, ObjectMapper om, QueryRepository repo) throws JsonProcessingException, QueryException {
    QueryRequest highestUsageRequest = om.readValue(om.writeValueAsString(queryRequest), QueryRequest.class);
    repo.unpackQueryRequest(highestUsageRequest, om.createObjectNode());
    highestUsageRequest.getQuery().addReturn(new Return().setIri(IM.USAGE_TOTAL));
    OrderDirection od = new OrderDirection().setDirection(Order.descending);
    highestUsageRequest.getQuery().setOrderBy(new OrderLimit().addProperty(od));
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
  public JsonNode queryIM(QueryRequest queryRequest) throws QueryException, OpenSearchException {
    ObjectNode result = new ObjectMapper().createObjectNode();
    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, result);
    if (null != queryRequest.getTextSearch()) {
      OSQuery osq = new OSQuery();
      JsonNode osResult = osq.OSQueryAsJsonNode(queryRequest);
      if (osResult != null && osResult.get("entities") != null)
        return osResult;
      else {
        return repo.queryIM(queryRequest, false);
      }
    }

    return repo.queryIM(queryRequest, false);
  }


  public Boolean askQueryIM(QueryRequest queryRequest) throws QueryException {
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
  public SearchResponse queryIMSearch(QueryRequest queryRequest) throws OpenSearchException, QueryException {
    ObjectMapper om = new ObjectMapper();

    QueryRepository repo = new QueryRepository();
    repo.unpackQueryRequest(queryRequest, om.createObjectNode());

    if (null != queryRequest.getTextSearch()) {
      if (queryRequest.getQuery() != null && queryRequest.getQuery().getImQuery() != null) {
        List<String> imResults = getIMResults(repo, queryRequest);
        if (imResults != null && !imResults.isEmpty()) {
          Query query = queryRequest.getQuery();
          Where where = new Where();
          where.setIri(IM.IM_IRI.toString());
          for (String iri : imResults) {
            where.addIs(new Node().setIri(iri));
          }
          if (query.getWhere() != null) {
            query.getWhere().addAnd(where);
          } else
            query.setWhere(where);
        } else return new SearchResponse();
      }
      SearchResponse results = new OSQuery().OSQueryAsSearchResponse(queryRequest);
      if (results != null) {
        if (results.getEntities().size() == 1 && queryRequest.getPage() != null && queryRequest.getPage().getPageNumber() == 1) {
          SearchResultSummary summary = results.getEntities().getFirst();
          if (summary.getCode() != null && summary.getCode().equals(queryRequest.getTextSearch()))
            results.setExactMatch(true);
          if (summary.getIri().equals(queryRequest.getTextSearch()))
            results.setExactMatch(true);
          return results;
        }
      }
      return results;
    }
    JsonNode queryResults = repo.queryIM(queryRequest, false);
    return convertQueryIMResultsToSearchResultSummary(queryResults, queryResults);
  }

  public SearchResponse convertQueryIMResultsToSearchResultSummary(JsonNode queryResults, JsonNode highestUsageResults) {
    SearchResponse searchResponse = new SearchResponse();

    if (queryResults.has(ENTITIES)) {
      JsonNode entities = queryResults.get(ENTITIES);
      if (entities.isArray()) {
        Set<String> iris = new HashSet<>();
        for (JsonNode entity : queryResults.get(ENTITIES)) {
          iris.add(entity.get("iri").asText());
        }
        List<SearchResultSummary> summaries = new EntityRepository().getEntitySummariesByIris(iris);
        searchResponse.setEntities(summaries);
      }
    }
    if (queryResults.has("totalCount")) searchResponse.setTotalCount(queryResults.get("totalCount").asInt());
    if (queryResults.has("count")) searchResponse.setCount(queryResults.get("count").asInt());
    if (queryResults.has("page")) searchResponse.setPage(queryResults.get("page").asInt());
    if (queryResults.has("term")) searchResponse.setTerm(queryResults.get("term").asText());

    if (highestUsageResults.has(ENTITIES)) {
      JsonNode entities = queryResults.get(ENTITIES);
      if (entities.isArray() && !entities.isEmpty() && entities.get(0).has("usageTotal")) {
        searchResponse.setHighestUsage(Integer.parseInt(entities.get(0).get("usageTotal").asText()));
      } else searchResponse.setHighestUsage(0);
    }
    return searchResponse;
  }


  private List<String> getIMResults(QueryRepository repo, QueryRequest queryRequest) throws QueryException {
    Query query = queryRequest.getQuery();
    String imQuery = query.getImQuery();
    if (imQuery != null) {
      QueryRequest request = new QueryRequest();
      request.setQuery(new Query().setIri(imQuery));
      request.setArgument(queryRequest.getArgument());
      ObjectMapper om = new ObjectMapper();
      repo.unpackQueryRequest(request, om.createObjectNode());
      JsonNode results = repo.queryIM(request, false);
      if (results != null & results.get(ENTITIES) != null) {
        List<String> iriResults = new ArrayList<>();
        for (JsonNode entity : results.get(ENTITIES)) {
          if (entity.has("iri")) iriResults.add(entity.get("iri").asText());
        }
        return iriResults;
      }
      return null;
    } else return null;
  }


  /**
   * Performs a search on a submitted term looking for name, synonyms, or code, with filters applied
   *
   * @param request holding the search term (multi or single word) + type/status/scheme filters
   * @return A set of Summaries of entity documents from the store
   */
  public SearchResponse getEntitiesByTerm(QueryRequest request) throws OpenSearchException {
    return new OSQuery().OSQueryAsSearchResponse(request);
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
  public void updateIM(QueryRequest queryRequest, GRAPH insertGraph) throws JsonProcessingException, QueryException {
    new QueryRepository().updateIM(queryRequest, insertGraph);
  }


}


