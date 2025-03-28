package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.cache.TimedCache;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.QueryRepository;
import org.endeavourhealth.imapi.errorhandling.SQLConversionException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.search.SearchResponse;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.transforms.Context;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.model.sql.IMQtoSQLConverter;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Component
public class QueryService {
  public static final String ENTITIES = "entities";
  private final EntityRepository entityRepository = new EntityRepository();

  public Query getQueryFromIri(String queryIri) throws JsonProcessingException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    return queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
  }

  public Query describeQuery(Query query,DisplayMode displayMode) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeQuery(query,displayMode);
  }

  public Match describeMatch(Match match) throws QueryException, JsonProcessingException {
    return new QueryDescriptor().describeSingleMatch(match);
  }

  public Query describeQuery(String queryIri, DisplayMode displayMode) throws JsonProcessingException, QueryException {
    return new QueryDescriptor().describeQuery(queryIri,displayMode);
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

  public String getSQLFromIMQ(Query query) {
    try {
      return new IMQtoSQLConverter().IMQtoSQL(query);
    } catch (SQLConversionException e) {
      return e.getMessage();
    }
  }

  public String getSQLFromIMQIri(String queryIri) throws JsonProcessingException {
    TTEntity queryEntity = entityRepository.getEntityPredicates(queryIri, Set.of(RDFS.LABEL, IM.DEFINITION)).getEntity();
    if (queryEntity.get(iri(IM.DEFINITION)) == null)
      return null;
    Query query = queryEntity.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
    return getSQLFromIMQ(query);
  }

  public Query getDefaultQuery() throws JsonProcessingException {
    List<TTEntity> children = entityRepository.getFolderChildren(IM.NAMESPACE+"Q_DefaultCohorts", SHACL.ORDER,RDF.TYPE,RDFS.LABEL,
      IM.DEFINITION);
    if (children.isEmpty()) {
      return new Query().setTypeOf(IM.NAMESPACE+"Patient");
    };
    TTEntity cohort = findFirstQuery(children);
    Query defaultQuery= new Query();
    defaultQuery.setMatch(new ArrayList<>());
    if (cohort!=null) {
      Query cohortQuery = cohort.get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class);
      defaultQuery.setTypeOf(cohortQuery.getTypeOf());
      defaultQuery.addInstanceOf(new Node().setIri(cohort.getIri()));
      return defaultQuery;
    }  else return null;
  }
  private TTEntity findFirstQuery(List<TTEntity> children) throws JsonProcessingException {
    for (TTEntity child : children) {
      if (child.isType(iri(IM.COHORT_QUERY))){
      if (child.get(iri(IM.DEFINITION)) != null){
          return child;
        }
      }
    }
    for (TTEntity child : children) {
      if (child.isType(iri(IM.FOLDER))){
        List<TTEntity> subchildren = entityRepository.getFolderChildren(IM.NAMESPACE+"DefaultCohorts", SHACL.ORDER,RDF.TYPE,RDFS.LABEL,
          IM.DEFINITION);
        if (subchildren==null|| subchildren.isEmpty()) {
          return null;
        }
        TTEntity cohort = findFirstQuery(subchildren);
        if (cohort!=null) return cohort ;
      }
    }
    return null;
  }


}
