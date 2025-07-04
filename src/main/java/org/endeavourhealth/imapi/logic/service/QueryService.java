package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.logic.reasoner.LogicOptimizer;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.vocabulary.*;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.*;

@Component
public class QueryService {
  public static final String ENTITIES = "entities";
  private final EntityRepository entityRepository = new EntityRepository();

  public static void generateUUIdsForQuery(Query query) {
    new QueryDescriptor().generateUUIDs(query);
  }

  public Query getQueryFromIri(String queryIri) throws JsonProcessingException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, asHashSet(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    return queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
  }

  public Query describeQuery(Query query, DisplayMode displayMode, Graph graph) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query, displayMode, graph);
  }

  public Match describeMatch(Match match, Graph graph) throws QueryException {
    return new QueryDescriptor().describeSingleMatch(match, graph);
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

  public String getSQLFromIMQ(QueryRequest queryRequest, String lang, Map<String, String> iriToUuidMap) {
    try {
      return new IMQtoSQLConverter(queryRequest, lang, iriToUuidMap).IMQtoSQL();
    } catch (SQLConversionException e) {
      return e.getMessage();
    }
  }

  public String getSQLFromIMQIri(String queryIri, String lang, Map<String, String> iriToUuidMap, Graph graph) throws JsonProcessingException, QueryException {
    Query query = describeQuery(queryIri, DisplayMode.LOGICAL, graph);
    if (query == null) return null;
    query = flattenQuery(query);
    QueryRequest queryRequest = new QueryRequest().setQuery(query);
    return getSQLFromIMQ(queryRequest, lang, iriToUuidMap);
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


  public Query flattenQuery(Query query) throws JsonProcessingException {
    LogicOptimizer.flattenQuery(query);
    return query;
  }

  public Query optimiseECLQuery(Query query) {
    LogicOptimizer.optimiseECLQuery(query);
    return query;
  }
}
