package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.postgres.QueryExecutorStatus;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.endeavourhealth.imapi.model.sql.SqlWithSubqueries;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.mysql.MYSQLConnectionManager;
import org.endeavourhealth.imapi.postgress.PostgresService;
import org.endeavourhealth.imapi.rabbitmq.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArray;

@Component
@Slf4j
public class QueryService {
  public static final String ENTITIES = "entities";
  private final EntityRepository entityRepository = new EntityRepository();
  private final DataModelRepository dataModelRepository = new DataModelRepository();
  private ConnectionManager connectionManager;
  private PostgresService postgresService = new PostgresService();
  private Map<Integer, List<String>> queryResultsMap = new HashMap<>();

  public Query describeQuery(Query query, DisplayMode displayMode, Graph graph) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query, displayMode, graph);
  }

  public Match describeMatch(Match match, Graph graph) throws QueryException {
    return new QueryDescriptor().describeSingleMatch(match, null, graph);
  }

  public Query describeQuery(String queryIri, DisplayMode displayMode, Graph graph) throws JsonProcessingException, QueryException {
    return new QueryDescriptor().describeQuery(queryIri, displayMode, graph);
  }

  public SearchResponse convertQueryIMResultsToSearchResultSummary(JsonNode queryResults, JsonNode highestUsageResults, Graph graph) {
    SearchResponse searchResponse = new SearchResponse();

    if (queryResults.has(ENTITIES)) {
      JsonNode entities = queryResults.get(ENTITIES);
      if (entities.isArray()) {
        Set<String> iris = new HashSet<>();
        for (JsonNode entity : queryResults.get(ENTITIES)) {
          iris.add(entity.get("iri").asText());
        }
        List<SearchResultSummary> summaries = entityRepository.getEntitySummariesByIris(iris, graph);
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

  public SqlWithSubqueries getSQLFromIMQ(QueryRequest queryRequest) throws SQLConversionException {
    return new IMQtoSQLConverter(queryRequest).IMQtoSQL();
  }

  public SqlWithSubqueries getSQLFromIMQIri(String queryIri, DatabaseOption lang, Graph graph) throws JsonProcessingException, QueryException, SQLConversionException {
    if (!lang.equals(DatabaseOption.MYSQL) && !lang.equals(DatabaseOption.POSTGRESQL)) {
      throw new SQLConversionException("'" + lang + "' is not currently supported for query to SQL. Supported languages are MYSQL and POSTGRESQL.");
    }
    Query query = describeQuery(queryIri, DisplayMode.LOGICAL, graph);
    if (query == null) return null;
    query = flattenQuery(query);
    QueryRequest queryRequest = new QueryRequest().setQuery(query).setLanguage(lang);
    return getSQLFromIMQ(queryRequest);
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

  public UUID reAddToExecutionQueue(UUID userId, String userName, RequeueQueryRequest requeueQueryRequest) throws Exception {
    DBEntry entry = postgresService.getById(requeueQueryRequest.getQueueId());
    if (!QueryExecutorStatus.QUEUED.equals(entry.getStatus()) && !QueryExecutorStatus.RUNNING.equals(entry.getStatus())) {
      QueryRequest queryRequest = requeueQueryRequest.getQueryRequest();
      return addToExecutionQueue(userId, userName, queryRequest);
    }
    return null;
  }

  public UUID addToExecutionQueue(UUID userId, String userName, QueryRequest queryRequest) throws Exception {
    try {
      getSQLFromIMQ(queryRequest);
    } catch (SQLConversionException e) {
      handleSQLConversionException(userId, userName, queryRequest, e.getMessage());
      throw new QueryException("Unable to convert query to SQL", e);
    }
    if (null == connectionManager) connectionManager = new ConnectionManager();
    return connectionManager.publishToQueue(userId, userName, queryRequest);
  }

  public List<String> executeQuery(QueryRequest queryRequest, Graph graph) throws SQLConversionException, SQLException, QueryException {
    int qrHashCode = getQueryRequestHashCode(queryRequest);
    log.info("Executing query: {} with a hash code: {}", queryRequest.getQuery().getIri(), qrHashCode);
//    TODO: if query has is rules needs to be converted to match based query
    try {
      List<String> results = queryResultsMap.get(qrHashCode);
      if (results != null) return results;

      SqlWithSubqueries sqlWithSubqueries = getSQLFromIMQ(queryRequest);
      for (String subqueryIri : sqlWithSubqueries.getSubqueryIris()) {
        Query subquery = describeQuery(subqueryIri, DisplayMode.LOGICAL, graph);
        QueryRequest subqueryRequest = new QueryRequest().setQuery(subquery);
        subqueryRequest.setArgument(queryRequest.getArgument());
        int subqrHashCode = getQueryRequestHashCode(subqueryRequest);
        executeQuery(subqueryRequest, graph);
        String updatedSql = sqlWithSubqueries.getSql().replace("query_[" + subqueryIri + "]", String.valueOf(subqrHashCode));
        sqlWithSubqueries.setSql(updatedSql);
      }
      results = MYSQLConnectionManager.executeQuery(sqlWithSubqueries.getSql());
      storeQueryResultsAndCache(queryRequest, results);
      return results;
    } catch (SQLConversionException e) {
      log.error("Error converting query: {}", e.getMessage());
      throw e;
    } catch (SQLException e) {
      log.error("Error executing query: {}", e.getMessage());
      throw e;
    } catch (QueryException e) {
      log.error("Error getting query definition: {}", e.getMessage());
      throw e;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public int getQueryRequestHashCode(QueryRequest queryRequest) {
    if (queryRequest.getQueryStringDefinition() == null) {
      String queryStringDefinition = entityRepository.getQueryStringDefinition(queryRequest.getQuery().getIri(), Graph.IM);
      queryRequest.setQueryStringDefinition(queryStringDefinition);
    }
    return queryRequest.hashCode();
  }

  public void storeQueryResultsAndCache(QueryRequest queryRequest, List<String> results) throws SQLException {
    queryResultsMap.put(queryRequest.hashCode(), results);
    MYSQLConnectionManager.saveResults(queryRequest.hashCode(), results);
  }

  public List<String> getQueryResults(QueryRequest queryRequest) throws SQLConversionException, SQLException {
    return queryResultsMap.get(queryRequest.hashCode());
  }

  public Pageable<String> getQueryResultsPaged(QueryRequest queryRequest) throws SQLConversionException, SQLException {
    List<String> results = queryResultsMap.get(queryRequest.hashCode());
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
    MYSQLConnectionManager.killCurrentQuery();
  }

  public Query getDefaultQuery(Graph graph) throws JsonProcessingException {
    List<TTEntity> children = entityRepository.getFolderChildren(Namespace.IM + "Q_DefaultCohorts", graph, asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL,
      IM.DEFINITION));
    if (children.isEmpty()) {
      return new Query().setTypeOf(Namespace.IM + "Patient");
    }
    TTEntity cohort = findFirstQuery(children, graph);
    Query defaultQuery = new Query();
    if (cohort != null) {
      Query cohortQuery = cohort.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
      defaultQuery.setTypeOf(cohortQuery.getTypeOf());
      defaultQuery.addInstanceOf(new Node().setIri(cohort.getIri()).setMemberOf(true));
      return defaultQuery;
    } else return null;
  }

  private TTEntity findFirstQuery(List<TTEntity> children, Graph graph) {
    for (TTEntity child : children) {
      if (child.isType(iri(IM.QUERY)) && child.get(iri(IM.DEFINITION)) != null) {
        return child;
      }

    }
    for (TTEntity child : children) {
      if (child.isType(iri(IM.FOLDER))) {
        List<TTEntity> subchildren = entityRepository.getFolderChildren(Namespace.IM + "DefaultCohorts", graph, asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL,
          IM.DEFINITION));
        if (subchildren == null || subchildren.isEmpty()) {
          return null;
        }
        TTEntity cohort = findFirstQuery(subchildren, graph);
        if (cohort != null) return cohort;
      }
    }
    return null;
  }

  public List<String> testRunQuery(Query query, Graph graph) throws SQLException, SQLConversionException, QueryException {
    QueryRequest queryRequest = new QueryRequest();
    Page page = new Page();
    page.setPageNumber(1);
    page.setPageSize(10);
    queryRequest.setPage(page);
    queryRequest.setQuery(query);
    return executeQuery(queryRequest, graph);
  }

  public Query flattenQuery(Query query) throws JsonProcessingException {
    LogicOptimizer.flattenQuery(query);
    return query;
  }

  public Query optimiseECLQuery(Query query) {
    LogicOptimizer.optimiseECLQuery(query);
    return query;
  }

  public Query getQueryFromIri(String iri, Graph from) throws JsonProcessingException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION.toString())).getEntity();
    return queryEntity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
  }

  public List<ArgumentReference> findMissingArguments(QueryRequest queryRequest) throws JsonProcessingException {
    List<ArgumentReference> missingArguments = new ArrayList<>();
    Query query = queryRequest.getQuery();
    Set<Argument> arguments = queryRequest.getArgument();
    if (null == arguments) arguments = new HashSet<>();
    recursivelyCheckQueryArguments(query, missingArguments, arguments);
    if (!missingArguments.isEmpty()) {
      for (ArgumentReference argument : missingArguments) {
        TTIriRef dataType = dataModelRepository.getPathDatatype(argument.getReferenceIri().getIri());
        if (null != dataType) argument.setDataType(dataType);
      }
    }
    return missingArguments;
  }

  private void recursivelyCheckQueryArguments(Query query, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    recursivelyCheckMatchArguments(query, missingArguments, arguments);
    if (null != query.getSubquery()) {
      recursivelyCheckQueryArguments(query.getSubquery(), missingArguments, arguments);
    }
  }

  private void recursivelyCheckMatchArguments(Match match, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    if (null != match.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(match.getParameter()))) {
      addMissingArgument(missingArguments, match.getParameter(), match.getIri());
    }
    if (null != match.getInstanceOf()) {
      List<Node> instances = match.getInstanceOf();
      instances.forEach(instance -> {
        if (null != instance.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(instance.getParameter()))) {
          addMissingArgument(missingArguments, instance.getParameter(), instance.getIri());
        }
      });
    }
    if (null != match.getAnd()) {
      List<Match> matches = match.getAnd();
      matches.forEach(andMatch -> recursivelyCheckMatchArguments(andMatch, missingArguments, arguments));
    }
    if (null != match.getOr()) {
      List<Match> matches = match.getOr();
      matches.forEach(orMatch -> recursivelyCheckMatchArguments(orMatch, missingArguments, arguments));
    }
    if (null != match.getNot()) {
      List<Match> matches = match.getNot();
      matches.forEach(notMatch -> recursivelyCheckMatchArguments(notMatch, missingArguments, arguments));
    }
    if (null != match.getWhere()) {
      recursivelyCheckWhereArguments(match.getWhere(), missingArguments, arguments);
    }
    if (null != match.getRule()) {
      List<Match> matches = match.getRule();
      matches.forEach(ruleMatch -> recursivelyCheckMatchArguments(ruleMatch, missingArguments, arguments));
    }
  }

  private void recursivelyCheckWhereArguments(Where where, List<ArgumentReference> missingArguments, Set<Argument> arguments) {
    if (null != where.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(where.getParameter()))) {
      missingArguments.add(new ArgumentReference().setParameter(where.getParameter()).setReferenceIri(iri(where.getIri())));
    }
    if (null != where.getAnd()) {
      where.getAnd().forEach(and -> recursivelyCheckWhereArguments(and, missingArguments, arguments));
    }
    if (null != where.getOr()) {
      where.getOr().forEach(or -> recursivelyCheckWhereArguments(or, missingArguments, arguments));
    }
    if (null != where.getNot()) {
      where.getNot().forEach(not -> recursivelyCheckWhereArguments(not, missingArguments, arguments));
    }
    if (null != where.getIs()) {
      where.getIs().forEach(is -> {
        if (null != is.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(is.getParameter()))) {
          addMissingArgument(missingArguments, is.getParameter(), is.getIri());
        }
      });
    }
    if (null != where.getNotIs()) {
      where.getNotIs().forEach(notIs -> {
        if (null != notIs.getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(notIs.getParameter()))) {
          addMissingArgument(missingArguments, notIs.getParameter(), notIs.getIri());
        }
      });
    }
    if (null != where.getRelativeTo() && null != where.getRelativeTo().getParameter() && arguments.stream().noneMatch(argument -> argument.getParameter().equals(where.getRelativeTo().getParameter()))) {
      addMissingArgument(missingArguments, where.getRelativeTo().getParameter(), where.getIri());
    }
  }

  private void addMissingArgument(List<ArgumentReference> missingArguments, String parameter, String referenceIri) {
    if (missingArguments.stream().noneMatch(missingArgument -> missingArgument.getParameter().equals(parameter))) {
      missingArguments.add(new ArgumentReference().setParameter(parameter).setReferenceIri(iri(referenceIri)));
    }
  }

  public TTIriRef getArgumentType(String referenceIri) {
    if (null == referenceIri) {
      throw new IllegalArgumentException("referenceIri is null");
    }
    return dataModelRepository.getPathDatatype(referenceIri);
  }
}
