package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.imq.DisplayMode;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.mysql.MYSQLConnectionManager;
import org.endeavourhealth.imapi.rabbitmq.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
@Slf4j
public class QueryService {
  public static final String ENTITIES = "entities";
  private final EntityRepository entityRepository = new EntityRepository();
  private ConnectionManager connectionManager;
  private MYSQLConnectionManager mySQLConnectionManager;
  private ObjectMapper objectMapper = new ObjectMapper();

  private Map<Integer, List<String>> queryResultsMap = new HashMap<>();

  public Query getQueryFromIri(String queryIri) throws JsonProcessingException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    return queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
  }

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query, displayMode);
  }

  public Query describeQuery(String queryIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    return new QueryDescriptor().describeQuery(queryIri, displayMode);
  }

  public SearchResponse convertQueryIMResultsToSearchResultSummary(JsonNode queryResults, JsonNode highestUsageResults) {
    SearchResponse searchResponse = new SearchResponse();

    if (queryResults.has(ENTITIES)) {
      JsonNode entities = queryResults.get(ENTITIES);
      if (entities.isArray()) {
        Set<String> iris = new HashSet<>();
        for (JsonNode entity : queryResults.get(ENTITIES)) {
          iris.add(entity.get("@id").asText());
        }
        List<SearchResultSummary> summaries = entityRepository.getEntitySummariesByIris(iris);
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

  public String getSQLFromIMQ(Query query) throws SQLConversionException {
    return new IMQtoSQLConverter().IMQtoSQL(query);
  }

  public String getSQLFromIMQIri(String queryIri) throws JsonProcessingException, SQLConversionException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    Query query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
    return getSQLFromIMQ(query);
  }

  public void addToExecutionQueue(String userId, String userName, QueryRequest queryRequest) throws Exception {
    if (null == connectionManager) connectionManager = new ConnectionManager();
    connectionManager.publishToQueue(userId, userName, queryRequest);
  }

  public void executeQuery(QueryRequest queryRequest) throws SQLConversionException, SQLException {
    log.info("Executing query: {}", queryRequest.getQuery().toString());
    String sql = getSQLFromIMQ(queryRequest.getQuery());
    List<String> results = mySQLConnectionManager.executeQuery(sql);
    QueryRequest queryRequestCopy = cloneQueryRequestWithoutPage(queryRequest);
    queryResultsMap.put(Objects.hash(queryRequestCopy), results);
  }

  public List<String> getQueryResults(QueryRequest queryRequest) throws SQLConversionException, SQLException {
    QueryRequest queryRequestCopy = cloneQueryRequestWithoutPage(queryRequest);
    return queryResultsMap.get(Objects.hash(queryRequestCopy));
  }

  private QueryRequest cloneQueryRequestWithoutPage(QueryRequest queryRequest) {
    QueryRequest queryRequestCopy = objectMapper.convertValue(queryRequest, QueryRequest.class);
    queryRequestCopy.setPage(null);
    return queryRequestCopy;
  }

  public Pageable<String> getQueryResultsPaged(QueryRequest queryRequest) throws SQLConversionException, SQLException {
    QueryRequest queryRequestCopy = cloneQueryRequestWithoutPage(queryRequest);
    List<String> results = queryResultsMap.get(Objects.hash(queryRequestCopy));
    if (results == null) return null;
    if (queryRequest.getPage().getPageNumber() > 0 && queryRequest.getPage().getPageSize() > 0) {
      Pageable<String> pageable = new Pageable<>();
      pageable.setPageSize(queryRequest.getPage().getPageSize());
      pageable.setCurrentPage(queryRequest.getPage().getPageNumber());
      if (queryRequest.getPage().getPageSize() < results.size()) {
        List<String> subList = results.subList((queryRequest.getPage().getPageNumber() - 1) * queryRequest.getPage().getPageSize(), queryRequest.getPage().getPageSize());
        pageable.setResult(subList);
      } else {
        pageable.setResult(results);
      }
      return pageable;
    }
    throw new IllegalArgumentException("Page number and page size are required");
  }

  public void killActiveQuery() throws SQLException {
    mySQLConnectionManager.killCurrentQuery();
  }
}
