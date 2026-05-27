package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.logic.reasoner.TextMatcher;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.responses.SearchResponse;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

/**
 * Methods to convert a Query object to its Sparql equivalent and return results as a json object
 */
public class QueryRepository {
  public static final String COUNT = "count";
  public static final String TOTAL_COUNT = "totalCount";
  public static final String ENTITIES = "entities";
  private final ObjectMapper mapper = new ObjectMapper();
  @Getter
  private final Set<String> predicates = new HashSet<>();


  /**
   * Generic query of IM with the select statements determining the response
   *
   * @param queryRequest QueryRequest object
   * @return A document consisting of a list of TTEntity and predicate look ups
   * @throws QueryException if query syntax is invalid
   */

  public JsonNode queryIM(QueryRequest queryRequest, boolean highestUsage) throws QueryException {
    ObjectNode result = mapper.createObjectNode();
    Integer page = queryRequest.getPage() != null ? queryRequest.getPage().getPageNumber() : 1;
    Integer count = queryRequest.getPage() != null ? queryRequest.getPage().getPageSize() : 0;
    try (IMDB conn = IMDB.getConnection()) {
      SparqlConverter converter = new SparqlConverter(queryRequest);
      String spq = converter.getSelectSparql(queryRequest.getQuery(), null, false, highestUsage);
      ObjectNode resultNode = graphSelectSearch(queryRequest, spq, conn, result);
      if (queryRequest.getPage() != null) {
        resultNode.put("page", page);
        resultNode.put(COUNT, count);
        resultNode.put(TOTAL_COUNT, (page * count) + 1);
      }
      return resultNode;
    }
  }


  public Boolean askQueryIM(QueryRequest queryRequest) throws QueryException {
    try (IMDB conn = IMDB.getConnection()) {
      new QueryValidator().validateQuery(queryRequest.getQuery());
      SparqlConverter converter = new SparqlConverter(queryRequest);
      String spq = converter.getAskSparql(null);
      return graphAskSearch(spq, conn);
    }
  }

  /**
   * Generic query of IM with the select statements determining the response
   *
   * @param queryRequest QueryRequest object
   * @throws QueryException          if query syntax is invalid
   * @throws JsonProcessingException if the json is invalid
   */
  public void updateIM(QueryRequest queryRequest, GRAPH insertGraph) throws JsonProcessingException, QueryException {
    try (IMDB conn = IMDB.getConnection()) {
      if (queryRequest.getUpdate() == null)
        throw new QueryException("Missing update in query request");
      if (queryRequest.getUpdate().getIri() == null)
        throw new QueryException("Update queries must reference a predefined definition. Dynamic update based queries not supported");
      TTEntity updateEntity = getEntity(queryRequest.getUpdate().getIri());
      queryRequest.setUpdate(updateEntity.get(TTIriRef.iri(IM.UPDATE_PROCEDURE)).asLiteral().objectValue(Update.class));
      SparqlConverter converter = new SparqlConverter(queryRequest);
      String spq = converter.getUpdateSparql();
      graphDeleteSearch(spq, conn);
    }
  }

  private void graphDeleteSearch(String spq, IMDB conn) {
    org.eclipse.rdf4j.query.Update update = conn.prepareDeleteSparql(spq);
    update.execute();
  }

  public void unpackQueryRequest(QueryRequest queryRequest, ObjectNode result) throws QueryException {
    Query unpackedQuery = unpackQuery(queryRequest.getQuery(), queryRequest);
    queryRequest.setQuery(unpackedQuery);
    if (null != queryRequest.getContext() && null != result)
      result.set("context", mapper.convertValue(queryRequest.getContext(), JsonNode.class));
  }

  public void unpackQueryRequest(QueryRequest queryRequest) throws QueryException {
    unpackQueryRequest(queryRequest, null);
  }

  private Query unpackQuery(Query query, QueryRequest queryRequest) throws QueryException {
    if (query.getIri() != null && query.getReturn() == null && query.getAnd() == null && query.getOr() == null) {
      TTEntity entity = getEntity(query.getIri());
      if (entity.get(TTIriRef.iri(SHACL.PARAMETER)) != null) {
        for (TTValue param : entity.get(TTIriRef.iri(SHACL.PARAMETER)).getElements()) {
          processParam(param, queryRequest);
        }
      }
      TTArray definition = entity.get(TTIriRef.iri(IM.DEFINITION));

      if (null == definition)
        throw new QueryException("Query: '" + query.getIri() + "' was not found");

      try {
        Query expandedQuery = definition.asLiteral().objectValue(Query.class);
        expandedQuery.setIri(query.getIri());
        return expandedQuery;
      } catch (JsonProcessingException e) {
        throw new QueryException("Could not parse query definition", e);
      }
    }

    return query;
  }

  private void processParam(TTValue param, QueryRequest queryRequest) throws QueryException {
    if (param.asNode().get(TTIriRef.iri(SHACL.MINCOUNT)) == null) return;
    String parameterName = param.asNode().get(TTIriRef.iri(RDFS.LABEL)).asLiteral().getValue();
    TTIriRef parameterType;
    if (param.asNode().get(TTIriRef.iri(SHACL.DATATYPE)) != null)
      parameterType = param.asNode().get(TTIriRef.iri(SHACL.DATATYPE)).asIriRef();
    else
      parameterType = param.asNode().get(TTIriRef.iri(SHACL.CLASS)).asIriRef();
    boolean found = false;
    for (Argument arg : queryRequest.getArgument())
      if (arg.getParameter().equals(parameterName)) {
        found = true;
        String error = "Query request arguments require parameter name :'" + parameterName + "' ";
        if (parameterType.equals(TTIriRef.iri(NAMESPACE.IM + "IriRef"))) {
          if (arg.getValueIri() == null)
            throw new QueryException(error + " to have a valueIri :{iri : http....}");
        } else if (arg.getValueData() == null) {
          throw new QueryException(error + " to have valueData where with string value");
        }
      }
    if (!found) {
      String error = "Query request expects parameter name '" + parameterName + "' which is not present in the query request";
      throw new QueryException(error);
    }
  }

  private ObjectNode graphSelectSearch(QueryRequest queryRequest, String spq, IMDB conn, ObjectNode result) {
    Query query = queryRequest.getQuery();
    ArrayNode entities = result.putArray(ENTITIES);
    ObjectNode lastEntity = null;
    ObjectNode entity = null;
    Map<String, ObjectNode> nodeMap = new HashMap<>();
    Integer start = null;
    Integer end = null;
    if (queryRequest.getPage() != null) {
      Integer page = queryRequest.getPage().getPageNumber();
      Integer pageSize = queryRequest.getPage().getPageSize();
      start = pageSize * (page - 1);
      end = pageSize * page;
    }
    int foundCount = -1;
    try (TupleQueryResult rs = sparqlQuery(spq, conn)) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        if (bs.getValue(query.getNode()) != null) {
          entity = nodeMap.get(bs.getValue(query.getNode()).stringValue());
          if (entity == null) {
            entity = mapper.createObjectNode();
            entities.add(entity);
            entity.put("iri", bs.getValue(query.getNode()).stringValue());
          }
        } else {
          entity = null;
        }
        if (query.getReturn() != null) {
          for (Return returnProperty : query.getReturn()) {
            entity = bindReturn(bs, entity, returnProperty, nodeMap, entities);
          }
        } else {
          entity = mapper.createObjectNode();
        }
        if (queryRequest.getTextSearch() != null) {
          if (lastEntity == null) lastEntity = entity;
          if (entity.get("iri") != null && (!entity.get("iri").textValue().equals(lastEntity.get("iri").textValue()))) {
            foundCount++;
            if (start != null) {
              if (foundCount < start) continue;
              if (foundCount > end) break;
            }
            if (notMatched(lastEntity, queryRequest.getTextSearch())) {
              foundCount--;
              entities.remove(entities.size() - 2);
            }
            lastEntity = entity;
            foundCount++;
          }
        }
      }
      if (!entities.isEmpty() && queryRequest.getTextSearch() != null) {
        JsonNode last = entities.get(entities.size() - 1);
        if (notMatched(last, queryRequest.getTextSearch())) {
          entities.remove(entities.size() - 1);
        }
      }
    }
    return result;
  }


  private boolean notMatched(JsonNode entity, String textSearch) {
    Set<String> tested = new HashSet<>();
    if (entity.get(RDFS.LABEL.toString()) != null) {
      String synonym = entity.get(RDFS.LABEL.toString()).asText().split(" \\(")[0];
      tested.add(synonym);
      if (TextMatcher.matchTerm(textSearch, synonym))
        return false;
    }
    if (entity.get(IM.HAS_TERM_CODE.toString()) != null) {
      for (JsonNode termCode : entity.get(IM.HAS_TERM_CODE.toString())) {
        if (termCode.has(RDFS.LABEL.toString())) {
          String synonym = termCode.get(RDFS.LABEL.toString()).asText().split(" \\(")[0];
          if (!tested.contains(synonym)) {
            tested.add(synonym);
            if (TextMatcher.matchTerm(textSearch, synonym))
              return false;
          }
        }
      }
    }
    return true;
  }

  private Boolean graphAskSearch(String spq, IMDB conn) {
    return sparqlAskQuery(spq, conn);
  }

  private TupleQueryResult sparqlQuery(String spq, IMDB conn) {
    TupleQuery qry = conn.prepareTupleSparql(spq);
    return qry.evaluate();
  }

  private Boolean sparqlAskQuery(String spq, IMDB conn) {
    BooleanQuery qry = conn.prepareBooleanSparql(spq);
    return qry.evaluate();
  }


  private ObjectNode bindReturn(BindingSet bs, ObjectNode node, Return property, Map<String, ObjectNode> nodeMap, ArrayNode entities) {
    if (node == null) {
      String ref;
      if (property.getPropertyRef() != null)
        ref = property.getPropertyRef();
      else ref = property.getNodeRef();
      String refIri = bs.getValue(ref).stringValue();
      node = nodeMap.get(refIri);
      if (node == null) {
        node = mapper.createObjectNode();
        entities.add(node);
        node.put("iri", refIri);
        nodeMap.put(refIri, node);
      }
      if (property.getReturn() != null) {
        for (Return returnProperty : property.getReturn()) {
          bindReturn(bs, node, returnProperty, nodeMap, entities);
        }
      }
    }
    String predicate = property.getIri();
    if (property.getPropertyRef() != null) {
      predicate = bs.getValue(property.getPropertyRef()).stringValue();
    }

    String objectVariable = property.getAs();
    Value object = bs.getValue(objectVariable);
    if (object != null) {
      String nodeValue = object.stringValue();
      if (property.getReturn() == null) {
        if (object.isIRI()) {
          ObjectNode iriNode = mapper.createObjectNode();
          node.set(predicate, iriNode);
          iriNode.put("iri", nodeValue);
        } else if (object.isBNode()) {
          node.put(predicate, nodeValue);
        } else
          node.put(predicate, nodeValue);
      } else {
        if (node.path(predicate).isMissingNode()) {
          ArrayNode arrayNode = new ObjectMapper().createArrayNode();
          node.set(predicate, arrayNode);
        }
        ArrayNode arrayNode = (ArrayNode) node.path(predicate);
        ObjectNode valueNode = getValueNode(arrayNode, nodeValue);
        if (valueNode == null) {
          valueNode = mapper.createObjectNode();
          arrayNode.add(valueNode);
        }
        if (object.isIRI()) {
          valueNode.put("iri", nodeValue);
        } else {
          valueNode.put("bn", nodeValue);
        }
        for (Return returnProperty : property.getReturn()) {
          bindReturn(bs, valueNode, returnProperty, nodeMap, entities);
        }
      }
    }
    return node;
  }


  private ObjectNode getValueNode(ArrayNode arrayNode, String nodeId) {
    for (JsonNode entry : arrayNode) {
      if (entry.get("iri") != null && (entry.get("iri").textValue().equals(nodeId)))
        return (ObjectNode) entry;
      else if (entry.get("bn") != null && entry.get("bn").textValue().equals(nodeId))
        return (ObjectNode) entry;
    }
    return null;
  }

  private TTEntity getEntity(String iri) {
    return new EntityRepository().getBundle(iri,
      asHashSet(IM.DEFINITION, RDF.TYPE, IM.FUNCTION_DEFINITION, IM.UPDATE_PROCEDURE, SHACL.PARAMETER)).getEntity();
  }

  public Query expandCohort(String cohortIri, DisplayMode displayMode) throws JsonProcessingException {
    Query cohort=null;
    String sql = """
      select ?cohort
      where {
       Values ?cohortIri {%s} 
       ?cohortIri im:definition ?cohort .
       }
      """.formatted("<" + cohortIri + ">");

    try (IMDB conn = IMDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          cohort = mapper.readValue(bs.getValue("cohort").stringValue(), Query.class);
        }
      }
      return null;
    }
  }

  public List<String> getSubtypeProperties(Set<TTIriRef> iris) {
    List<String> properties = new ArrayList<>();
    String iriList = "<" + iris.stream().map(TTIriRef::getIri).collect(Collectors.joining("> <")) + ">";
    String spq = """
      SELECT distinct ?property
      WHERE {
        Values ?parentConcept {%s}
         ?concept im:isA ?parentConcept.
         ?concept im:roleGroup ?group.
         ?group ?property ?value.
         filter (?property!=im:groupNumber)
         ?property rdf:type rdf:Property.}
      """.formatted(iriList);
    try (IMDB conn = IMDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(spq);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          properties.add(bs.getValue("property").stringValue());
        }
      }
    }
    return properties;
  }
}
