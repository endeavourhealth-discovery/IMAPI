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
import org.endeavourhealth.imapi.logic.reasoner.TextMatcher;
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
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      checkReferenceDate(queryRequest);
      SparqlConverter converter = new SparqlConverter(queryRequest);
      String spq = converter.getSelectSparql(queryRequest.getQuery(), null, false, highestUsage);
      ObjectNode resultNode=graphSelectSearch(queryRequest, spq, conn, result);
      if (queryRequest.getPage() != null) {
        resultNode.put("page", page);
        resultNode.put(COUNT, count);
        resultNode.put(TOTAL_COUNT, (page*count)+1);
      }
      return resultNode;
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
      result.set("context", mapper.convertValue(queryRequest.getContext(), JsonNode.class));
  }

  public void unpackQueryRequest(QueryRequest queryRequest) throws QueryException {
    unpackQueryRequest(queryRequest, null);
  }

  private Query unpackQuery(Query query, QueryRequest queryRequest) throws QueryException {
    if (query.getIri() != null && query.getReturn() == null && query.getAnd() == null&&query.getOr() == null) {
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
        Query expandedQuery= definition.asLiteral().objectValue(Query.class);
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
        if (parameterType.equals(TTIriRef.iri(IM.NAMESPACE + "IriRef"))) {
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

  private ObjectNode graphSelectSearch(QueryRequest queryRequest, String spq, RepositoryConnection conn, ObjectNode result) {
    Query query= queryRequest.getQuery();
    ArrayNode entities = result.putArray(ENTITIES);
    ObjectNode lastEntity=null;
    ObjectNode entity=null;
    Map<String, ObjectNode> nodeMap = new HashMap<>();
    Integer start= null;
    Integer end=null;
    if (queryRequest.getPage()!=null){
      Integer page=queryRequest.getPage().getPageNumber();
      Integer pageSize=queryRequest.getPage().getPageSize();
      start=pageSize*(page-1);
      end=pageSize*page;
    }
    int foundCount=-1;
    try (TupleQueryResult rs = sparqlQuery(spq, conn)) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        Return aReturn = query.getReturn();
        entity = bindReturn(bs, aReturn, entities, nodeMap);
        if (queryRequest.getTextSearch() != null) {
          if (lastEntity == null) lastEntity = entity;
          if (entity.get("iri")!=null && (!entity.get("iri").textValue().equals(lastEntity.get("iri").textValue()))) {
            foundCount++;
            if (start!=null){
              if (foundCount<start) continue;
              if (foundCount>end) break;
            }
              if (notMatched(lastEntity, queryRequest.getTextSearch())) {
                entities.remove(entities.size() - 2);
              }
              lastEntity = entity;
              foundCount++;
            }
        }
      }
      if (queryRequest.getTextSearch() != null) {
        if (lastEntity != null) {
          if (notMatched(lastEntity, queryRequest.getTextSearch())) {
            entities.remove(entities.size() - 2);
          }
        }
        if (entity != null) {
          if (notMatched(entity, queryRequest.getTextSearch())) {
            entities.remove(entities.size() - 1);
          }

        }
      }
    }
    return result;
  }



  private boolean notMatched(JsonNode entity, String textSearch) {
    Set<String> tested = new HashSet<>();
    if (entity.get(RDFS.LABEL)!=null) {
      String synonym = entity.get(RDFS.LABEL).asText().split(" \\(")[0];
        tested.add(synonym);
        if (TextMatcher.matchTerm(textSearch,synonym))
          return false;
    }
    if (entity.get(IM.HAS_TERM_CODE)!=null) {
      for (JsonNode termCode : entity.get(IM.HAS_TERM_CODE)) {
        if (termCode.has(RDFS.LABEL)) {
          String synonym = termCode.get(RDFS.LABEL).asText().split(" \\(")[0];
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


  private ObjectNode bindReturn(BindingSet bs, Return aReturn, ArrayNode entities,
                          Map<String, ObjectNode> nodeMap) {
    String subject = aReturn.getNodeRef();
    ObjectNode entity=null;
    if (subject == null) subject = aReturn.getPropertyRef();
    if (subject == null) subject = aReturn.getValueRef();
    Value value = bs.getValue(subject);
    ObjectNode node;
    if (value != null) {
      node = nodeMap.get(value.stringValue());
      if (node == null) {
        node = mapper.createObjectNode();
        entities.add(node);
        entity= node;
        nodeMap.put(value.stringValue(), node);
        if (value.isIRI())
          node.put("iri", value.stringValue());
        else
          node.put("bn", value.stringValue());
      } else entity=node;
      bindNode(bs, aReturn, node);
    }
    return entity;
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
        iriNode.put("iri", nodeValue);
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
            valueNode.put("iri", nodeValue.stringValue());
          else
            valueNode.put("bn", nodeValue.stringValue());
        }
        bindNode(bs, returnNode, valueNode);
      }
    }
  }

  private ObjectNode getValueNode(ArrayNode arrayNode, String nodeId) {
    for (JsonNode entry : arrayNode) {
      if (entry.get("iri")!=null &&(entry.get("iri").textValue().equals(nodeId)))
        return (ObjectNode) entry;
      else if (entry.get("bn")!=null &&entry.get("bn").textValue().equals(nodeId))
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




  private boolean isIri(String iri) {
    return iri.matches("([a-z]+)?:.*");
  }
}
