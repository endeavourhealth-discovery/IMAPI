package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.postgres.QueryExecutorStatus;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.mysql.MYSQLConnectionManager;
import org.endeavourhealth.imapi.postgress.PostgresService;
import org.endeavourhealth.imapi.rabbitmq.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
  private PostgresService postgresService = new PostgresService();

  private Map<Integer, List<String>> queryResultsMap = new HashMap<>();

  public static void generateUUIdsForQuery(Query query) {
    new QueryDescriptor().generateUUIDs(query);
  }

  public Query getQueryFromIri(String queryIri) throws JsonProcessingException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    return queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
  }

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query, displayMode);
  }

  public Match describeMatch(Match match) throws QueryException {
    return new QueryDescriptor().describeSingleMatch(match);
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
          iris.add(entity.get("iri").asText());
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

  public String getSQLFromIMQ(QueryRequest queryRequest, Map<String, String> iriToUuidMap) throws SQLConversionException {
    return new IMQtoSQLConverter(queryRequest, iriToUuidMap).IMQtoSQL();
  }

  public String getSQLFromIMQIri(String queryIri, Map<String, String> iriToUuidMap) throws JsonProcessingException, QueryException, SQLConversionException {
    Query query = describeQuery(queryIri, DisplayMode.LOGICAL);
    if (query == null) return null;
    query = flattenQuery(query);
    QueryRequest queryRequest = new QueryRequest().setQuery(query);
    return getSQLFromIMQ(queryRequest, iriToUuidMap);
  }

  public void handleSQLConversionException(UUID userId, String userName, QueryRequest queryRequest, String error) throws SQLException {
    DBEntry entry = new DBEntry()
      .setId(UUID.randomUUID())
      .setQueryRequest(queryRequest)
      .setQueryIri(queryRequest.getQuery().getIri())
      .setQueryName(queryRequest.getQuery().getName())
      .setStatus(QueryExecutorStatus.ERRORED)
      .setUserId(userId)
      .setUserName(userName)
      .setQueuedAt(LocalDateTime.now())
      .setKilledAt(LocalDateTime.now())
      .setError(error);
    postgresService.create(entry);
  }

  public void addToExecutionQueue(UUID userId, String userName, QueryRequest queryRequest) throws Exception {
    try {
      getSQLFromIMQ(queryRequest, new HashMap<>());
    } catch (SQLConversionException e) {
      handleSQLConversionException(userId, userName, queryRequest, e.getMessage());
      throw new QueryException("Unable to convert query to SQL", e);
    }
    if (null == connectionManager) connectionManager = new ConnectionManager();
    connectionManager.publishToQueue(userId, userName, queryRequest);
  }

  public List<String> executeQuery(QueryRequest queryRequest) throws SQLConversionException, SQLException {
    log.info("Executing query: {}", queryRequest.getQuery().toString());
    String sql = getSQLFromIMQ(queryRequest, new HashMap<>());
    List<String> results = mySQLConnectionManager.executeQuery(sql);
    storeQueryResults(queryRequest, results);
    return results;
  }

  public void storeQueryResults(QueryRequest queryRequest, List<String> results) {
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

  public Query getDefaultQuery() throws JsonProcessingException {
    List<TTEntity> children = entityRepository.getFolderChildren(IM.NAMESPACE + "Q_DefaultCohorts", SHACL.ORDER, RDF.TYPE, RDFS.LABEL,
      IM.DEFINITION);
    if (children.isEmpty()) {
      return new Query().setTypeOf(IM.NAMESPACE + "Patient");
    }
    TTEntity cohort = findFirstQuery(children);
    Query defaultQuery = new Query();
    if (cohort != null) {
      Query cohortQuery = cohort.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
      defaultQuery.setTypeOf(cohortQuery.getTypeOf());
      defaultQuery.addInstanceOf(new Node().setIri(cohort.getIri()).setMemberOf(true));
      return defaultQuery;
    } else return null;
  }

  private TTEntity findFirstQuery(List<TTEntity> children) {
    for (TTEntity child : children) {
      if (child.isType(iri(IM.QUERY)) && child.get(iri(IM.DEFINITION)) != null) {
        return child;
      }

    }
    for (TTEntity child : children) {
      if (child.isType(iri(IM.FOLDER))) {
        List<TTEntity> subchildren = entityRepository.getFolderChildren(IM.NAMESPACE + "DefaultCohorts", SHACL.ORDER, RDF.TYPE, RDFS.LABEL,
          IM.DEFINITION);
        if (subchildren == null || subchildren.isEmpty()) {
          return null;
        }
        TTEntity cohort = findFirstQuery(subchildren);
        if (cohort != null) return cohort;
      }
    }
    return null;
  }

  public List<String> testRunQuery(Query query) throws SQLException, SQLConversionException {
    QueryRequest queryRequest = new QueryRequest();
    Page page = new Page();
    page.setPageNumber(1);
    page.setPageSize(10);
    queryRequest.setPage(page);
    queryRequest.setQuery(query);
    return executeQuery(queryRequest);
  }

  public Query flattenQuery(Query query) throws JsonProcessingException {
    LogicOptimizer.flattenQuery(query);
    return query;
  }

  public Query optimiseECLQuery(Query query) {
    LogicOptimizer.optimiseECLQuery(query);
    return query;
  }
}
