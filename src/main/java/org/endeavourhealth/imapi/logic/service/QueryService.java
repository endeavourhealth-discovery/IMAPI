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
import org.endeavourhealth.imapi.model.iml.NodeShape;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.postgres.DBEntry;
import org.endeavourhealth.imapi.model.postgres.QueryExecutorStatus;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.mysql.MYSQLConnectionManager;
import org.endeavourhealth.imapi.postgres.PostgresService;
import org.endeavourhealth.imapi.rabbitmq.ConnectionManager;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArray;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@Component
@Slf4j
public class QueryService {
  public static final String ENTITIES = "entities";
  private final EntityRepository entityRepository = new EntityRepository();
  private final DataModelRepository dataModelRepository = new DataModelRepository();
  private final PostgresService postgresService = new PostgresService();
  private final Map<Integer, Set<String>> queryResultsMap = new HashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();
  private ConnectionManager connectionManager;

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


  public String getSQLFromIMQ(QueryRequest queryRequest) throws SQLConversionException, QueryException, JsonProcessingException {
    QueryRequest queryRequestForSql = getQueryRequestForSqlConversion(queryRequest);
    return new IMQtoSQLConverter(queryRequestForSql).getSql();
  }

  public String getSQLFromIMQIri(String queryIri, DatabaseOption lang) throws JsonProcessingException, QueryException, SQLConversionException {
    QueryRequest queryRequest = new QueryRequest().setQuery(new Query().setIri(queryIri)).setLanguage(lang);
    QueryRequest queryRequestForSql = getQueryRequestForSqlConversion(queryRequest);
    return new IMQtoSQLConverter(queryRequestForSql).getSql();
  }

  public QueryRequest getQueryRequestForSqlConversion(QueryRequest queryRequest) throws SQLConversionException, QueryException, JsonProcessingException {
    if (null == queryRequest.getQuery()) throw new SQLConversionException("Query in query request cannot be null");

    if (null != queryRequest.getLanguage() && !queryRequest.getLanguage().equals(DatabaseOption.MYSQL) && !queryRequest.getLanguage().equals(DatabaseOption.POSTGRESQL)) {
      throw new SQLConversionException("'" + queryRequest.getLanguage() + "' is not currently supported for query to SQL. Supported languages are MYSQL and POSTGRESQL.");
    }
    Query query;
    if (queryRequest.getQuery().getIri() != null && !queryRequest.getQuery().getIri().isEmpty()) {
      TTEntity queryEntity = entityRepository.getEntityPredicates(queryRequest.getQuery().getIri(), asHashSet(IM.DEFINITION)).getEntity();
      if (!queryEntity.has(iri(IM.DEFINITION)))
        throw new SQLConversionException("Query: " + queryRequest.getQuery().getIri() + " not found.");
      query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
      query.setIri(queryEntity.getIri());
    } else {
      query = queryRequest.getQuery();
    }

    query = describeQuery(query, DisplayMode.LOGICAL);
    if (query == null) return null;
    if (null == query.getIri()) query.setIri(UUID.randomUUID().toString());
    return new QueryRequest().setQuery(query).setLanguage(queryRequest.getLanguage()).setArgument(queryRequest.getArgument()); // need to add update info instead of queryString
  }

  public void handleSQLConversionException(UUID userId, String userName, QueryRequest queryRequest, String error) throws SQLException {
    DBEntry entry = new DBEntry().setId(UUID.randomUUID()).setQueryRequest(queryRequest).setQueryIri(queryRequest.getQuery().getIri()).setQueryName(queryRequest.getQuery().getName()).setStatus(QueryExecutorStatus.ERRORED).setUserId(userId).setUserName(userName).setQueuedAt(LocalDateTime.now()).setKilledAt(LocalDateTime.now()).setError(error);
    postgresService.create(entry);
  }

  public void reAddToExecutionQueue(UUID userId, String userName, RequeueQueryRequest requeueQueryRequest) throws Exception {
    DBEntry entry = postgresService.getById(requeueQueryRequest.getQueueId());
    if (!QueryExecutorStatus.QUEUED.equals(entry.getStatus()) && !QueryExecutorStatus.RUNNING.equals(entry.getStatus())) {
      postgresService.delete(userId, requeueQueryRequest.getQueueId());
      QueryRequest queryRequest = requeueQueryRequest.getQueryRequest();
      addToExecutionQueue(userId, userName, queryRequest);
    }
  }

  public UUID addToExecutionQueue(UUID userId, String userName, QueryRequest queryRequest) throws Exception {
    String url = System.getenv("RUNNER_URL");
    String clientId = System.getenv("CLIENT_ID");
    String clientSecret = System.getenv("CLIENT_SECRET");

    try (HttpClient client = HttpClient.newBuilder()
      .followRedirects(HttpClient.Redirect.NORMAL)
      .build()) {

      Map<String, String> params = new HashMap<>();
      params.put("client_id", clientId);
      params.put("client_secret", clientSecret);

      String formData = params.entrySet()
        .stream()
        .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
        .collect(Collectors.joining("&"));

      URI tokenURI = URI.create(url + "/api/oauth/token");

      HttpRequest tokenHttpRequest = HttpRequest.newBuilder()
        .uri(tokenURI)
        .setHeader("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(formData))
        .build();

      HttpResponse<String> token = client.send(tokenHttpRequest, HttpResponse.BodyHandlers.ofString());

      String requestBody = objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(queryRequest);

      URI queryRunURI = URI.create(url + "/api/query/run");

      HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(queryRunURI)
        .setHeader("Content-Type", "application/json")
        .setHeader("Cookie", "authentication_token=" + token)
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();

      HttpResponse<String> runResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

      return UUID.fromString(runResponse.body());
    }
  }

  public Set<String> executeQuery(QueryRequest queryRequest) throws SQLConversionException, SQLException, QueryException, JsonProcessingException {
    int qrHashCode = queryRequest.hashCode();
    log.info("Received query to execute: {} with a hash code: {}", queryRequest.getQuery().getIri(), qrHashCode);
    try {
      Set<String> results = getQueryResults(queryRequest);
      if (results != null) return results;
      Map<String, Integer> queryIrisToHashCodes = runSubQueries(queryRequest);
      String resolvedSql = new IMQtoSQLConverter(queryRequest).getResolvedSql(queryIrisToHashCodes);
      log.info("Executing query: {} with a hash code: {}", queryRequest.getQuery().getIri(), qrHashCode);
      results = MYSQLConnectionManager.executeQuery(resolvedSql);
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

  private Map<String, Integer> runSubQueries(QueryRequest queryRequest) throws QueryException, JsonProcessingException, SQLConversionException, SQLException {
    List<String> subQueries = getSubqueryIris(queryRequest.getQuery());
    Map<String, Integer> queryIrisToHashCodes = getQueryIrisToHashCodes(subQueries, queryRequest.getArgument());
    if (!subQueries.isEmpty())
      for (String subQueryIri : subQueries) {
        Query subquery = describeQuery(subQueryIri, DisplayMode.LOGICAL);
        QueryRequest subqueryRequest = new QueryRequest().setQuery(subquery);
        subqueryRequest.setArgument(queryRequest.getArgument());
        int hashCode = subqueryRequest.hashCode();
        log.debug("Subquery found: {} with hash: {}", subQueryIri, hashCode);
        if (!queryResultsMap.containsKey(hashCode) && !MYSQLConnectionManager.tableExists(hashCode)) {
          log.debug("Executing subquery: {} with hash: {}", subQueryIri, hashCode);
          String resolvedSql = new IMQtoSQLConverter(subqueryRequest).getResolvedSql(queryIrisToHashCodes);
          Set<String> results = MYSQLConnectionManager.executeQuery(resolvedSql);
          storeQueryResultsAndCache(subqueryRequest, results);
        } else {
          log.debug("Query results already exist for subquery: {} with hash: {}", subQueryIri, hashCode);
        }
      }
    return queryIrisToHashCodes;
  }

  private Map<String, Integer> getQueryIrisToHashCodes(List<String> subQueries, Set<Argument> argument) throws QueryException, JsonProcessingException {
    Map<String, Integer> queryIrisToHashCodes = new HashMap<>();
    for (String subQueryIri : subQueries) {
      Query subquery = describeQuery(subQueryIri, DisplayMode.LOGICAL);
      QueryRequest subqueryRequest = new QueryRequest().setQuery(subquery);
      subqueryRequest.setArgument(argument);
      int hashCode = subqueryRequest.hashCode();
      queryIrisToHashCodes.put(subQueryIri, hashCode);
    }
    return queryIrisToHashCodes;
  }

  public void storeQueryResultsAndCache(QueryRequest queryRequest, Set<String> results) throws SQLException {
    queryResultsMap.put(queryRequest.hashCode(), results);
    MYSQLConnectionManager.saveResults(queryRequest.hashCode(), results);
  }

  public Set<String> getQueryResults(QueryRequest queryRequest) throws SQLException {
    int hashCode = queryRequest.hashCode();
    Set<String> queryResults = queryResultsMap.get(hashCode);
    if (queryResults != null) {
      log.debug("Query Results for hashcode {} found in local cache", hashCode);
      return queryResults;
    }
    if (!MYSQLConnectionManager.tableExists(hashCode)) return null;

    queryResults = MYSQLConnectionManager.getResults(queryRequest);
    log.debug("Query Results for hashcode {} found in db cache", hashCode);
    return queryResults;
  }

  public void killActiveQuery() throws SQLException {
    MYSQLConnectionManager.killCurrentQuery();
  }

  public Query getDefaultQuery() throws JsonProcessingException {
    List<TTEntity> children = entityRepository.getFolderChildren(Namespace.IM + "Q_DefaultCohorts", asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL, IM.DEFINITION));
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
        List<TTEntity> subchildren = entityRepository.getFolderChildren(Namespace.IM + "DefaultCohorts", asArray(SHACL.ORDER, RDF.TYPE, RDFS.LABEL, IM.DEFINITION));
        if (subchildren == null || subchildren.isEmpty()) {
          return null;
        }
        TTEntity cohort = findFirstQuery(subchildren);
        if (cohort != null) return cohort;
      }
    }
    return null;
  }

  public Set<String> testRunQuery(QueryRequest queryRequest) throws SQLException, SQLConversionException, QueryException, JsonProcessingException {
//    should connect to test data database (current one)
    return executeQuery(getQueryRequestForSqlConversion(queryRequest));
  }

  public Query flattenQuery(Query query) {
    LogicOptimizer.optimizeQuery(query);
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
    if (null != query.getQuery()) {
      for (Query subquery : query.getQuery()) {
        recursivelyCheckQueryArguments(subquery, missingArguments, arguments);
      }
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

  private List<String> getSubqueryIris(Query query) throws QueryException, JsonProcessingException, SQLConversionException {
    List<String> subQueryIris = new ArrayList<>();
    populateSubqueryIrisConclusively(query, subQueryIris);
    subQueryIris = deduplicateKeepLast(subQueryIris);
    return subQueryIris;
  }

  private List<String> deduplicateKeepLast(List<String> subQueryIris) {
    LinkedHashSet<String> seen = new LinkedHashSet<>();
    ListIterator<String> it = subQueryIris.listIterator(subQueryIris.size());
    while (it.hasPrevious()) {
      String iri = it.previous();
      seen.add(iri);
    }
    List<String> result = new ArrayList<>(seen);
    Collections.reverse(result);
    return result;
  }

  private void populateSubqueryIrisConclusively(Query query, List<String> subQueryIris) throws QueryException, JsonProcessingException, SQLConversionException {
    if (query.getIsCohort() != null) {
      subQueryIris.add(query.getIsCohort().getIri());
    }

    if (query.getAnd() != null) {
      for (Match and : query.getAnd()) {
        processMatch(and, subQueryIris);
      }
    }

    if (query.getOr() != null) {
      for (Match or : query.getOr()) {
        processMatch(or, subQueryIris);
      }
    }

    if (query.getNot() != null) {
      for (Match not : query.getNot()) {
        processMatch(not, subQueryIris);
      }
    }
  }

  private void processMatch(Match match, List<String> subQueryIris) throws QueryException, JsonProcessingException, SQLConversionException {
    if (null != match.getIsCohort()) {
      String iri = match.getIsCohort().getIri();
      subQueryIris.add(iri);
      Query subQuery = describeQuery(iri, DisplayMode.LOGICAL);
      if (null == subQuery) throw new SQLConversionException("Sub query with iri:" + iri + " not found");
      populateSubqueryIrisConclusively(subQuery, subQueryIris);
    }

    if (match.getAnd() != null) {
      for (Match nestedAnd : match.getAnd()) {
        processMatch(nestedAnd, subQueryIris);
      }
    }

    if (match.getOr() != null) {
      for (Match nestedOr : match.getOr()) {
        processMatch(nestedOr, subQueryIris);
      }
    }

    if (match.getNot() != null) {
      for (Match nestedNot : match.getNot()) {
        processMatch(nestedNot, subQueryIris);
      }
    }
  }

  public Query expandCohort(String queryIri, String cohortIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    Query query = new QueryRepository().expandCohort(queryIri, cohortIri, displayMode);
    query = new QueryDescriptor().describeQuery(query, displayMode);
    return query;
  }
}
