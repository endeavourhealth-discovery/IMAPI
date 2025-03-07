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
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.queryengine.QueryValidator;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
    return queryIM(queryRequest, highestUsage, queryRequest.getQuery());
  }

  public JsonNode queryIM(QueryRequest queryRequest, boolean highestUsage, Query query) throws QueryException {
    ObjectNode result = mapper.createObjectNode();
    Integer page = queryRequest.getPage() != null ? queryRequest.getPage().getPageNumber() : 1;
    Integer count = queryRequest.getPage() != null ? queryRequest.getPage().getPageSize() : 0;
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      checkReferenceDate(queryRequest);
      new QueryValidator().validateQuery(query);
      SparqlConverter converter = new SparqlConverter(queryRequest);
      String spq = converter.getSelectSparql(query, null, false, highestUsage);
      ObjectNode resultNode = graphSelectSearch(query, spq, conn, result);
      return prepareQueryResponse(resultNode, queryRequest, page, count);
    }
  }

  public JsonNode prepareQueryResponse(ObjectNode queryResults, QueryRequest queryRequest, Integer page, Integer count) throws QueryException {
    ObjectNode queryIMResponse = mapper.createObjectNode();
    if (queryRequest.getPage() != null) {
      queryIMResponse.put("page", page);
      queryIMResponse.put(COUNT, count);
      queryIMResponse.put(TOTAL_COUNT, queryIMCount(queryRequest));
      if (count == 0 || count > queryIMResponse.get(TOTAL_COUNT).asInt())
        queryIMResponse.put(COUNT, queryIMResponse.get(TOTAL_COUNT).asInt());
    } else {
      int entityCount = queryResults.get(ENTITIES).size();
      queryIMResponse.put("page", 1);
      queryIMResponse.put(COUNT, entityCount);
      queryIMResponse.put(TOTAL_COUNT, entityCount);
    }

    if (queryRequest.getTextSearch() != null) queryIMResponse.put("term", queryRequest.getTextSearch());
    if (queryResults.has(ENTITIES)) queryIMResponse.set(ENTITIES, queryResults.get(ENTITIES));
    return queryIMResponse;
  }

  public Integer queryIMCount(QueryRequest queryRequest) throws QueryException {
    queryRequest.setPage(null);
    unpackQueryRequest(queryRequest);
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      checkReferenceDate(queryRequest);
      new QueryValidator().validateQuery(queryRequest.getQuery());
      SparqlConverter converter = new SparqlConverter(queryRequest);
      String spq = converter.getSelectSparql(null, true, false);
      return graphTotalSearch(spq, conn);
    }

  }

  public Boolean askQueryIM(QueryRequest queryRequest) throws QueryException {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      checkReferenceDate(queryRequest);
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
  public void updateIM(QueryRequest queryRequest) throws JsonProcessingException, QueryException {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      if (queryRequest.getUpdate() == null)
        throw new QueryException("Missing update in query request");
      if (queryRequest.getUpdate().getIri() == null)
        throw new QueryException("Update queries must reference a predefined definition. Dynamic update based queries not supported");
      TTEntity updateEntity = getEntity(queryRequest.getUpdate().getIri());
      queryRequest.setUpdate(updateEntity.get(TTIriRef.iri(IM.UPDATE_PROCEDURE)).asLiteral().objectValue(Update.class));

      checkReferenceDate(queryRequest);
      SparqlConverter converter = new SparqlConverter(queryRequest);
      String spq = converter.getUpdateSparql();
      graphUpdateSearch(spq, conn);

    }
  }

  private void graphUpdateSearch(String spq, RepositoryConnection conn) {
    org.eclipse.rdf4j.query.Update update = conn.prepareUpdate(spq);
    update.execute();

  }

  public void unpackQueryRequest(QueryRequest queryRequest, ObjectNode result) throws QueryException {
    Query unpackedQuery = unpackQuery(queryRequest.getQuery(), queryRequest);
    queryRequest.setQuery(unpackedQuery);
    if (null != queryRequest.getContext() && null != result)
      result.set("@context", mapper.convertValue(queryRequest.getContext(), JsonNode.class));
  }

  public void unpackQueryRequest(QueryRequest queryRequest) throws QueryException {
    unpackQueryRequest(queryRequest, null);
  }

  private Query unpackQuery(Query query, QueryRequest queryRequest) throws QueryException {
    if (query.getIri() != null && query.getReturn() == null && query.getMatch() == null) {
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
        return definition.asLiteral().objectValue(Query.class);
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
        if (parameterType.equals(TTIriRef.iri(IM.NAMESPACE + "IriRef"))) {
          if (arg.getValueIri() == null)
            throw new QueryException(error + " to have a valueIri :{@id : http....}");
        } else if (arg.getValueData() == null) {
          throw new QueryException(error + " to have valueData where with string value");
        }
      }
    if (!found) {
      String error = "Query request expects parameter name '" + parameterName + "' which is not present in the query request";
      throw new QueryException(error);
    }
  }

  private ObjectNode graphSelectSearch(Query query, String spq, RepositoryConnection conn, ObjectNode result) {
    ArrayNode entities = result.putArray(ENTITIES);
    try (TupleQueryResult rs = sparqlQuery(spq, conn)) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        Return aReturn = query.getReturn();
          Map<String, ObjectNode> nodeMap = new HashMap<>();
          bindReturn(bs, aReturn, entities, nodeMap);
        }
    }
    return result;
  }


  private Integer graphTotalSearch(String spq, RepositoryConnection conn) {
    try (TupleQueryResult rs = sparqlQuery(spq, conn)) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        if (bs.hasBinding(COUNT)) return Integer.parseInt(bs.getValue(COUNT).stringValue());
      }
    }
    return 0;
  }

  private Boolean graphAskSearch(String spq, RepositoryConnection conn) {
    return sparqlAskQuery(spq, conn);
  }

  private TupleQueryResult sparqlQuery(String spq, RepositoryConnection conn) {
    TupleQuery qry = conn.prepareTupleQuery(spq);
    return qry.evaluate();
  }

  private Boolean sparqlAskQuery(String spq, RepositoryConnection conn) {
    BooleanQuery qry = conn.prepareBooleanQuery(spq);
    return qry.evaluate();
  }


  private void bindReturn(BindingSet bs, Return aReturn, ArrayNode entities,
                          Map<String, ObjectNode> nodeMap) {
    String subject = aReturn.getNodeRef();
    Value value = bs.getValue(subject);
    ObjectNode node;
    if (value != null) {
      node = nodeMap.get(value.stringValue());
      if (node == null) {
        node = mapper.createObjectNode();
        entities.add(node);
        nodeMap.put(value.stringValue(), node);
        if (value.isIRI())
          node.put("@id", value.stringValue());
        else
          node.put("@bn", value.stringValue());
      }
      bindNode(bs, aReturn, node);
    }
  }

  private void bindNode(BindingSet bs, Return aReturn, ObjectNode node) {

    if (aReturn.getProperty() != null) {
      for (ReturnProperty path : aReturn.getProperty()) {
        bindPath(bs, path, node);
      }
    }

  }

  private void bindProperty(BindingSet bs, ObjectNode node, ReturnProperty property) {
    String predicate = property.getIri();
    if (property.getAs() != null)
      predicate = property.getAs();
    String objectVariable = property.getValueRef();
    Value object = bs.getValue(objectVariable);
    if (object != null) {
      String nodeValue = object.stringValue();
      if (object.isIRI()) {
        ObjectNode iriNode = mapper.createObjectNode();
        node.set(predicate, iriNode);
        iriNode.put("@id", nodeValue);
      } else if (object.isBNode()) {
        node.put(predicate, nodeValue);
      } else
        node.put(predicate, nodeValue);
    }
  }

  private void bindPath(BindingSet bs, ReturnProperty path, ObjectNode node) {
    if (path.getReturn() == null) {
      bindProperty(bs, node, path);
      return;
    }
    String iri = null;
    if (path.getIri() != null)
      iri = path.getIri();
    else if (path.getPropertyRef() != null) {
      Value pathVariable = bs.getValue(path.getPropertyRef());
      if (pathVariable != null)
        iri = pathVariable.stringValue();
    } else
      iri = path.getAs();
    if (iri != null) {
      Return returnNode = path.getReturn();
      String nodeVariable;
      if (returnNode.getNodeRef() != null)
        nodeVariable = returnNode.getNodeRef();
      else nodeVariable = returnNode.getAs();
      Value nodeValue = bs.getValue(nodeVariable);
      if (nodeValue != null) {
        if (node.get(iri) == null) {
          node.putArray(iri);
        }
        ArrayNode arrayNode = (ArrayNode) node.path(iri);
        if (arrayNode.isMissingNode()) {
          arrayNode = new ObjectMapper().createArrayNode();
          node.set(iri, arrayNode);
        }
        ObjectNode valueNode = getValueNode(arrayNode, nodeValue.stringValue());
        if (valueNode == null) {
          valueNode = mapper.createObjectNode();
          arrayNode.add(valueNode);
          if (nodeValue.isIRI())
            valueNode.put("@id", nodeValue.stringValue());
          else
            valueNode.put("@bn", nodeValue.stringValue());
        }
        bindNode(bs, returnNode, valueNode);
      }
    }
  }

  private ObjectNode getValueNode(ArrayNode arrayNode, String nodeId) {
    for (JsonNode entry : arrayNode) {
      if (entry.get("@id").textValue().equals(nodeId))
        return (ObjectNode) entry;
      if (entry.get("@bn").textValue().equals(nodeId))
        return (ObjectNode) entry;
    }
    return null;

  }


  private void checkReferenceDate(QueryRequest queryRequest) {
    if (queryRequest.getReferenceDate() == null) {
      String now = LocalDate.now().toString();
      queryRequest.setReferenceDate(now);
    }

  }

  private TTEntity getEntity(String iri) {
    return new EntityRepository().getBundle(iri,
      Set.of(IM.DEFINITION, RDF.TYPE, IM.FUNCTION_DEFINITION, IM.UPDATE_PROCEDURE, SHACL.PARAMETER)).getEntity();

  }

  /**
   * Method to populate the iris in a query with their  names
   *
   * @param query the query object in iml Query form
   */
  public void labelQuery(Query query) {
    List<TTIriRef> ttIris = new ArrayList<>();
    Map<String, String> iriLabels = new HashMap<>();
    gatherQueryLabels(query, ttIris, iriLabels);
    List<String> iriList = resolveIris(iriLabels.keySet());
    String iris = String.join(",", iriList);

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sql = "Select ?entity ?label where { ?entity <" + RDFS.LABEL + "> ?label.\n" +
        "filter (?entity in (" + iris + "))\n}";
      TupleQuery qry = conn.prepareTupleQuery(sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String label = bs.getValue("label").stringValue();
          String iri = bs.getValue("entity").stringValue();
          iriLabels.put(iri, label);
        }
      }
    }
    for (TTIriRef ttIri : ttIris) {
      ttIri.setName(iriLabels.get(ttIri.getIri()));
    }
    setQueryLabels(query, iriLabels);
  }

  public List<String> resolveIris(Set<String> iriLabels) {
    List<String> iriList = iriLabels.stream().filter(iri -> iri != null && iri.contains("#")).toList();
    return iriList.stream().map(iri -> "<" + iri + ">").toList();
  }

  private void gatherQueryLabels(Query query, List<TTIriRef> ttIris, Map<String, String> iris) {
    if (query.getMatch() != null) {
      for (Match match : query.getMatch()) {
        gatherFromLabels(match, ttIris, iris);
      }
    }
    if (query.getReturn() != null) {
      Return select = query.getReturn();
      gatherReturnLabels(select, ttIris, iris);
    }
    if (query.getQuery() != null)
      for (Query subQuery : query.getQuery())
        gatherQueryLabels(subQuery, ttIris, iris);
  }

  private void gatherReturnLabels(Return select, List<TTIriRef> ttIris, Map<String, String> iris) {
    if (select.getProperty() != null) {
      for (ReturnProperty property : select.getProperty()) {
        if (property.getIri() != null) {
          addToIriList(property.getIri(), ttIris, iris);
        }
        if (property.getReturn() != null) {
          gatherReturnLabels(property.getReturn(), ttIris, iris);
        }
      }
    }
  }

  private void gatherWhereLabels(Where where, List<TTIriRef> ttIris, Map<String, String> iris) {
    if (where.getId() != null)
      addToIriList(where.getId(), ttIris, iris);

    if (where.getIs() != null)
      for (Element in : where.getIs())
        addToIriList(in.getIri(), ttIris, iris);
    if (where.getMatch() != null)
      gatherFromLabels(where.getMatch(), ttIris, iris);
  }

  private void gatherFromLabels(Match match, List<TTIriRef> ttIris, Map<String, String> iris) {
    if (match.getIri() != null)
      addToIriList(match.getIri(), ttIris, iris);
    else if (match.getTypeOf() != null)
      addToIriList(match.getTypeOf().getIri(), ttIris, iris);
    if (match.getWhere() != null) {
      for (Where where : match.getWhere()) {
        gatherWhereLabels(where, ttIris, iris);
      }
    }
    if (match.getMatch() != null) {
      match.getMatch().forEach(f -> gatherFromLabels(f, ttIris, iris));
    }

    if (match.getWhere() != null) {
      for (Where path : match.getWhere()) {
        addToIriList(path.getIri(), ttIris, iris);
      }
    }
    if (match.getOrderBy() != null) {
      gatherOrderLimitLabels(match.getOrderBy(), ttIris, iris);
    }
  }

  private void gatherOrderLimitLabels(OrderLimit orderBy, List<TTIriRef> ttIris, Map<String, String> iris) {
    if (orderBy.getProperty() != null) {
      addToIriList(orderBy.getProperty().getIri(), ttIris, iris);
    }
  }

  private void setQueryLabels(Query query, Map<String, String> iriLabels) {
    if (query.getMatch() != null) {
      for (Match match : query.getMatch()) {
        setMatchLabels(match, iriLabels);
      }
    }
    if (query.getReturn() != null){
      Return select = query.getReturn();
      setReturnLabels(select, iriLabels);
    }
    if (query.getQuery() != null)
      for (Query subQuery : query.getQuery())
        setQueryLabels(subQuery, iriLabels);
  }

  private void setMatchLabels(Match match, Map<String, String> iriLabels) {
    if (match.getIri() != null) {
      match.setName(iriLabels.get(match.getIri()));
    } else if (match.getTypeOf() != null) {
      if (!isIri(match.getTypeOf().getIri())) {
        match.setTypeOf(IM.NAMESPACE + match.getTypeOf().getIri());
      }
      match.setName(iriLabels.get(match.getTypeOf().getIri()));
    }
    if (match.getWhere() != null) {
      for (Where where : match.getWhere()) {
        setWhereLabels(where, iriLabels);
      }
    }
    if (match.getMatch() != null) {
      match.getMatch().forEach(f -> setMatchLabels(f, iriLabels));
    }

    if (match.getOrderBy() != null) {
      setOrderLimitLabels(match.getOrderBy(), iriLabels);
    }
  }

  private void setOrderLimitLabels(OrderLimit orderBy, Map<String, String> iriLabels) {
    if (orderBy.getProperty() != null) {
      orderBy.getProperty().setName(iriLabels.get(orderBy.getProperty().getIri()));
    }
  }

  private void setWhereLabels(Where where, Map<String, String> iris) {
    if (where.getId() != null)
      where.setName(iris.get(where.getId()));
    if (where.getIs() != null)
      for (Element in : where.getIs())
        in.setName(iris.get(in.getIri()));
    if (where.getMatch() != null) {
      setMatchLabels(where.getMatch(), iris);
    }
  }

  private void setReturnLabels(Return select, Map<String, String> iris) {
    if (select.getProperty() != null) {
      for (ReturnProperty property : select.getProperty()) {
        if (property.getIri() != null) {
          property.setValue(iris.get(property.getIri()));
        }
        if (property.getReturn() != null) {
          setReturnLabels(property.getReturn(), iris);
        }
      }
    }
  }

  private void addToIriList(String iri, List<TTIriRef> ttIris, Map<String, String> iris) {
    if (iri != null && !iri.isEmpty()) {
      if (!isIri(iri)) {
        iri = IM.NAMESPACE + iri;
      }
      ttIris.add(TTIriRef.iri(iri));
      iris.put(iri, null);
    }
  }

  private boolean isIri(String iri) {
    return iri.matches("([a-z]+)?:.*");
  }
}
