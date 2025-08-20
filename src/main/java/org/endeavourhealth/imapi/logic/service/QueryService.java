package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.DataModelRepository;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.iml.NodeShape;
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
import org.endeavourhealth.imapi.postgres.PostgresService;
import org.endeavourhealth.imapi.rabbitmq.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

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
  private Map<Integer, Set<String>> queryResultsMap = new HashMap<>();

  public Query describeQuery(Query query, DisplayMode displayMode) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query, displayMode);
  }

  public Match describeMatch(Match match) throws QueryException {
    return new QueryDescriptor().describeSingleMatch(match, null);
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

  public SqlWithSubqueries getSQLFromIMQ(QueryRequest queryRequest) throws SQLConversionException {
    queryRequest.resolveArgs();
    return new IMQtoSQLConverter(queryRequest).IMQtoSQL();
  }

  public SqlWithSubqueries getSQLFromIMQIri(String queryIri, DatabaseOption lang) throws JsonProcessingException, QueryException, SQLConversionException {
    if (!lang.equals(DatabaseOption.MYSQL) && !lang.equals(DatabaseOption.POSTGRESQL)) {
      throw new SQLConversionException("'" + lang + "' is not currently supported for query to SQL. Supported languages are MYSQL and POSTGRESQL.");
    }
    Query query = describeQuery(queryIri, DisplayMode.LOGICAL);
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
      postgresService.delete(userId, requeueQueryRequest.getQueueId());
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

  public Set<String> executeQuery(QueryRequest queryRequest) throws SQLConversionException, SQLException, QueryException {
    queryRequest.resolveArgs();
    int qrHashCode = getQueryRequestHashCode(queryRequest);
    log.info("Executing query: {} with a hash code: {}", queryRequest.getQuery().getIri(), qrHashCode);
    // TODO: if query has is rules needs to be converted to match based query
    try {
      Set<String> results = getQueryResults(queryRequest);
      if (results != null) return results;

      SqlWithSubqueries sqlWithSubqueries = getSQLFromIMQ(queryRequest);
      for (String subqueryIri : sqlWithSubqueries.getSubqueryIris()) {
        Query subquery = describeQuery(subqueryIri, DisplayMode.LOGICAL);
        QueryRequest subqueryRequest = new QueryRequest().setQuery(subquery);
        subqueryRequest.setArgument(queryRequest.getArgument());
        int subqrHashCode = getQueryRequestHashCode(subqueryRequest);
        executeQuery(subqueryRequest);
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
      String queryStringDefinition = entityRepository.getQueryStringDefinition(queryRequest.getQuery().getIri());
      queryRequest.setQueryStringDefinition(queryStringDefinition);
    }
    return queryRequest.hashCode();
  }

  public void storeQueryResultsAndCache(QueryRequest queryRequest, Set<String> results) throws SQLException {
    queryResultsMap.put(queryRequest.hashCode(), results);
    MYSQLConnectionManager.saveResults(queryRequest.hashCode(), results);
  }

  public Set<String> getQueryResults(QueryRequest queryRequest) throws SQLException {
    int hashCode = getQueryRequestHashCode(queryRequest);
    Set<String> queryResults = queryResultsMap.get(hashCode);
    if (queryResults != null) {
      log.debug("Query Results for hashcode {} found in local cache", hashCode);
      return queryResults;
    }
    if (!MYSQLConnectionManager.tableExists(hashCode)) return null;
    queryResults = MYSQLConnectionManager.getResults(queryRequest);
    if (queryResults != null) {
      log.debug("Query Results for hashcode {} found in db cache", hashCode);
      return queryResults;
    }
    return null;
  }

  public void killActiveQuery() throws SQLException {
    MYSQLConnectionManager.killCurrentQuery();
  }

  public Query getDefaultQuery() throws JsonProcessingException {
    List<TTEntity> children = entityRepository.getFolderChildren(Namespace.IM + "Q_DefaultCohorts", asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL,
      IM.DEFINITION));
    if (children.isEmpty()) {
      return new Query().setTypeOf(Namespace.IM + "Patient");
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
        List<TTEntity> subchildren = entityRepository.getFolderChildren(Namespace.IM + "DefaultCohorts", asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL,
          IM.DEFINITION));
        if (subchildren == null || subchildren.isEmpty()) {
          return null;
        }
        TTEntity cohort = findFirstQuery(subchildren);
        if (cohort != null) return cohort;
      }
    }
    return null;
  }

  public Set<String> testRunQuery(Query query) throws SQLException, SQLConversionException, QueryException {
    QueryRequest queryRequest = new QueryRequest();
    Page page = new Page();
    page.setPageNumber(1);
    page.setPageSize(10);
    queryRequest.setPage(page);
    queryRequest.setQuery(query);
    return executeQuery(queryRequest);
  }

  public Query flattenQuery(Query query) {
    LogicOptimizer.flattenQuery(query);
    return query;
  }

  public Query optimiseECLQuery(Query query) {
    LogicOptimizer.optimiseECLQuery(query);
    return query;
  }

  public Query getQueryFromIri(String iri) throws JsonProcessingException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION.toString())).getEntity();
    return queryEntity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
  }

  public List<ArgumentReference> findMissingArguments(QueryRequest queryRequest) throws QueryException, JsonProcessingException {
    List<ArgumentReference> missingArguments = new ArrayList<>();
    Query query = describeQuery(queryRequest.getQuery().getIri(), DisplayMode.LOGICAL);
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


  private NodeShape getTypeFromPath(Path path, Set<String> nodeRefs) {
    if (path.getVariable() != null) {
      if (nodeRefs.contains(path.getVariable())) {
        return dataModelRepository.getDataModelDisplayProperties(path.getTypeOf().getIri(), false);
      }
      if (path.getPath() != null) {
        for (Path subPath : path.getPath()) {
          NodeShape nodeShape = getTypeFromPath(subPath, nodeRefs);
          if (nodeShape != null) return nodeShape;
        }
      }
    }
    return null;
  }

  private void getNodeRefs(Match match, Set<String> nodeRefs) {
    Where where = match.getWhere();
    if (where != null) {
      if (where.getNodeRef() != null) {
        nodeRefs.add(where.getNodeRef());
      }
      for (List<Where> whereList : Arrays.asList(where.getAnd(), where.getOr(), where.getNot())) {
        if (whereList != null) {
          for (Where subWhere : whereList)
            getNodeRefs(subWhere, nodeRefs);
        }
      }
    }
  }

  private void getNodeRefs(Where where, Set<String> nodeRefs) {
    if (where.getNodeRef() != null) {
      nodeRefs.add(where.getNodeRef());
    }
    for (List<Where> whereList : Arrays.asList(where.getAnd(), where.getOr(), where.getNot())) {
      if (whereList != null) {
        for (Where subWhere : whereList)
          getNodeRefs(subWhere, nodeRefs);
      }
    }
  }

}
