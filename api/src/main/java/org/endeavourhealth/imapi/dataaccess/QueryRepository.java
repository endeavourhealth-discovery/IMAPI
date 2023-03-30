package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import joptsimple.internal.Strings;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.service.OSQuery;
import org.endeavourhealth.imapi.model.customexceptions.OpenSearchException;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

/**
 * Methods to convert a Query object to its Sparql equivalent and return results as a json object
 */
public class QueryRepository {
    private Query query;
    private final TTDocument result= new TTDocument();
    private final Set<String> predicates = new HashSet<>();
    private QueryRequest queryRequest;

    /**
     * Generic query of IM with the select statements determining the response
     *
     * @param queryRequest QueryRequest object
     * @return A document consisting of a list of TTEntity and predicate look ups
     * @throws DataFormatException     if query syntax is invalid
     * @throws JsonProcessingException if the json is invalid
     */
    public TTDocument queryIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException {

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            unpackQueryRequest(queryRequest);
            if (null != queryRequest.getTextSearch()) {
                ObjectNode osResult = new OSQuery().openSearchQuery(queryRequest);
                if (null != osResult)
                    return convertToEntity(osResult);
            }
            checkReferenceDate();
            SparqlConverter converter = new SparqlConverter(queryRequest);
            String spq = converter.getSelectSparql(null);
            return graphSelectSearch(spq, conn);

        }
    }

    /**
     * Generic query of IM with the select statements determining the response
     *
     * @param queryRequest QueryRequest object
     * @return A response entity
     * @throws DataFormatException     if query syntax is invalid
     * @throws JsonProcessingException if the json is invalid
     */
    public void updateIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException, InterruptedException, OpenSearchException, URISyntaxException, ExecutionException {
         this.queryRequest= queryRequest;
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            if (queryRequest.getUpdate()==null)
                throw new DataFormatException("Missing update in query request");
            if (queryRequest.getUpdate().getIri()==null)
                throw new DataFormatException("Update queries must reference a predefined definition. Dynamic update based queries not supported");
            TTEntity updateEntity= getEntity(queryRequest.getUpdate().getIri());
            queryRequest.setUpdate(updateEntity.get(IM.UPDATE_PROCEDURE).asLiteral().objectValue(Update.class));

            checkReferenceDate();
            SparqlConverter converter = new SparqlConverter(queryRequest);
            String spq = converter.getUpdateSparql();
            graphUpdateSearch(spq, conn);

        }
    }

    private void graphUpdateSearch(String spq, RepositoryConnection conn) {
        org.eclipse.rdf4j.query.Update update= conn.prepareUpdate(spq);
        update.execute();
    }

    private void unpackQueryRequest(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {
        this.queryRequest = queryRequest;
        this.query = unpackQuery(queryRequest.getQuery(), queryRequest);
        queryRequest.setQuery(query);
        if (null != queryRequest.getContext())
            result.setContext(queryRequest.getContext());
    }

    private Query unpackQuery(Query query, QueryRequest queryRequest) throws JsonProcessingException, DataFormatException {
        if (query.getIri()!=null&&query.getSelect()==null&&query.getFrom()==null){
                        TTEntity entity = getEntity(query.getIri());
                        if (entity.get(SHACL.PARAMETER)!=null){
                            for (TTValue param:entity.get(SHACL.PARAMETER).getElements()){
                                if (param.asNode().get(SHACL.MINCOUNT)!=null) {
                                    String parameterName = param.asNode().get(RDFS.LABEL).asLiteral().getValue();
                                    TTIriRef parameterType;
                                    if (param.asNode().get(SHACL.DATATYPE)!=null)
                                        parameterType= param.asNode().get(SHACL.DATATYPE).asIriRef();
                                    else
                                        parameterType= param.asNode().get(SHACL.CLASS).asIriRef();
                                    boolean found = false;
                                    for (Argument arg : queryRequest.getArgument())
                                        if (arg.getParameter().equals(parameterName)) {
                                            found = true;
                                            String error = "Query request arguments require parameter name :'" + parameterName + "' ";
                                            if (parameterType.equals(TTIriRef.iri(IM.NAMESPACE+"IriRef"))) {
                                                if (arg.getValueIri() == null)
                                                    throw new DataFormatException(error + " to have a valueIri :{@id : htttp....}");
                                            }
                                            else if (arg.getValueData()==null) {
                                                throw new DataFormatException(error + " to have valueData property with string value");
                                            }
                                        }
                                    if (!found) {
                                        String error = "Query request expects parameter name '" + parameterName + "' which is not present in the query request";
                                        throw new DataFormatException(error);
                                    }
                                }

                            }
                        }
                        return entity.get(IM.DEFINITION).asLiteral().objectValue(Query.class);
                    }
        return query;
    }

    private TTDocument graphSelectSearch(String spq, RepositoryConnection conn) {

        TupleQueryResult rs= sparqlQuery(spq,conn);
        Map<Value, TTEntity> entityMap = new HashMap<>();
            while (rs.hasNext()) {
                BindingSet bs = rs.next();
                bindObjects(bs, result, entityMap);
            }

        if (!predicates.isEmpty()) {
            Set<String> sparqlPredicates = new HashSet<>();
            predicates.forEach(p -> sparqlPredicates.add(SparqlConverter.iriFromString(p)));
            String predlist = Strings.join(sparqlPredicates, ",");
            String predLookup = SparqlConverter.getDefaultPrefixes() +
                "select ?predicate ?label \nwhere {" +
                "?predicate <" + RDFS.LABEL.getIri() + "> ?label.\n" +
                "filter (?predicate in (" + predlist + "))}";
            rs= sparqlQuery(predLookup,conn);
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    Value predicate = bs.getValue("predicate");
                    Value label = bs.getValue("label");
                    if (label != null)
                        result.getPredicates().put(predicate.stringValue(), label.stringValue());
                }

        }
        return result;

    }

    private TupleQueryResult sparqlQuery(String spq, RepositoryConnection conn) {
        TupleQuery qry = conn.prepareTupleQuery(spq);
        return qry.evaluate();
    }



    private void bindObjects(BindingSet bs, TTDocument result, Map<Value, TTEntity> entityMap) {
        Value entityValue = bs.getValue("entity");
        TTEntity root = entityMap.get(entityValue);
        if (root == null) {
            root = new TTEntity();
            root.setIri(entityValue.stringValue());
            entityMap.put(entityValue, root);
            result.addEntity(root);
        }
        Map<String, TTNode> valueMap = new HashMap<>();
        if (query.getSelect() != null) {
            for (Select select : query.getSelect()) {
                bindObject(bs, valueMap, root, select, entityValue.stringValue());
            }
        }
    }

    private void bindObject(BindingSet bs, Map<String, TTNode> valueMap, TTNode node, Select select, String path) {
        String alias = select.getAlias();
        TTIriRef predicate= TTIriRef.iri(select.getIri());
        Value value = bs.getValue(alias);
        if (value == null)
            return;
        if (value.isIRI()) {
            if (select.getSelect() == null) {
                node.addObject(predicate, TTIriRef.iri(resultIri(value.stringValue())));
            } else {
                TTNode subNode = valueMap.get(path + (value.stringValue()));
                if (subNode == null) {
                    subNode = new TTNode();
                    subNode.setIri(resultIri(value.stringValue()));
                    valueMap.put(path + value.stringValue(), subNode);
                    node.addObject(predicate, subNode);
                }
                for (Select subSelect : select.getSelect()) {
                    bindObject(bs, valueMap, subNode, subSelect, path + "/" + predicate.getIri());
                }
            }
        } else if (value.isBNode()) {
            TTNode subNode = valueMap.get(path + (value.stringValue()));
            if (subNode == null) {
                subNode = new TTNode();
                valueMap.put(path + value.stringValue(), subNode);
                node.addObject(predicate, subNode);
            }
            if (select.getSelect() != null) {
                for (Select subSelect : select.getSelect()) {
                    bindObject(bs, valueMap, subNode, subSelect, path + "/" + predicate.getIri());
                }
            }
        } else {
            if (value.isLiteral()) {
                node.addObject(predicate, TTLiteral.literal(value.stringValue()));
            }
        }
        predicates.add(predicate.getIri());
    }

    private String resultIri(String iri) {
        if (!query.isUsePrefixes())
            return iri;
        if (result.getContext() != null)
            return result.getContext().prefix(iri);
        return iri;
    }



    public Set<String> getPredicates() {
        return predicates;
    }

    private TTDocument convertToEntity(ObjectNode genericResult) {
        TTDocument result = new TTDocument();
        if (genericResult != null) {
            ArrayNode entities = (ArrayNode) genericResult.get("entities");
            if (entities != null) {
                for (Iterator<JsonNode> it = entities.elements(); it.hasNext(); ) {
                    JsonNode node = it.next();
                    TTEntity ttEntity = new TTEntity();
                    result.addEntity(ttEntity);
                    if (node.has("@id"))
                        ttEntity.setIri(node.get("@id").asText());
                    nodeToTT(node, ttEntity);
                }
            }
        }
        return result;
    }

    private void nodeToTT(JsonNode node, TTNode ttParent) {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            if (!fieldName.equals("@id")) {
                JsonNode fieldValue = field.getValue();
                TTIriRef predicate = TTIriRef.iri(fieldName);
                if (fieldValue.isObject()) {
                    if (fieldValue.has("@id")) {
                        TTIriRef iri = TTIriRef.iri(fieldValue.get("@id").asText());
                        ttParent.set(predicate, iri);
                    } else {
                        TTNode ttNode = new TTNode();
                        ttParent.set(predicate, ttNode);
                        nodeToTT(fieldValue, ttNode);
                    }
                } else if (fieldValue.isArray()) {
                    ttParent.set(predicate, new TTArray());
                    Iterator<JsonNode> aIt = fieldValue.elements();
                    while (aIt.hasNext()) {
                        JsonNode arrayField = aIt.next();
                        if (arrayField.isObject()) {
                            if (arrayField.has("@id")) {
                                TTIriRef iri = TTIriRef.iri(arrayField.get("@id").asText());
                                ttParent.addObject(predicate, iri);
                            } else {
                                TTNode ttNode = new TTNode();
                                ttParent.addObject(predicate, ttNode);
                                nodeToTT(arrayField, ttNode);
                            }
                        } else
                            ttParent.addObject(predicate, getLiteral(arrayField));
                    }
                } else
                    ttParent.set(predicate, getLiteral(fieldValue));
            }
        }
    }

    private TTValue getLiteral(JsonNode literal) {
        if (literal.isBoolean())
            return TTLiteral.literal(literal.asBoolean());
        else if (literal.isInt())
            return TTLiteral.literal(literal.asInt());
        else
            return TTLiteral.literal(literal.asText());
    }




    private void checkReferenceDate() {
        if (queryRequest.getReferenceDate() == null) {
            String now = LocalDate.now().toString();
            queryRequest.setReferenceDate(now);
        }

    }

    private TTEntity getEntity(String iri) {
        return new EntityRepository2().getBundle(iri,
        Set.of(IM.DEFINITION.getIri(),RDF.TYPE.getIri(),IM.FUNCTION_DEFINITION.getIri(),IM.UPDATE_PROCEDURE.getIri(),SHACL.PARAMETER.getIri())).getEntity();

    }




    /**
     * Method to populate the iris in a query with their  names
     * @param query the query object in iml Query form
     */
    public void labelQuery(Query query) {
        List<TTIriRef> ttIris = new ArrayList<>();
        Map<String,String> iriLabels = new HashMap<>();
        gatherQueryLabels(query,ttIris,iriLabels);
        List<String> iriList= iriLabels.keySet().stream().map(iri-> "<"+ iri+">").collect(Collectors.toList());
        String iris= String.join(",",iriList);
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            String sql = "Select ?entity ?label where { ?entity <" + RDFS.LABEL.getIri() + "> ?label.\n" +
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
        for (TTIriRef ttIri:ttIris){
            ttIri.setName(iriLabels.get(ttIri.getIri()));
        }
    }
    private void gatherQueryLabels(Query query,List<TTIriRef> ttIris, Map<String,String> iris ){
        if (query.getFrom()!=null)
                gatherFromLabels(query.getFrom(), ttIris, iris);
        if (query.getSelect()!=null)
            for (Select select:query.getSelect())
                gatherSelectLabels(select,ttIris,iris);
        if (query.getQuery()!=null)
            for (Query subQuery: query.getQuery())
                gatherQueryLabels(subQuery,ttIris,iris);
    }

    private void gatherSelectLabels(Select select, List<TTIriRef> ttIris, Map<String,String> iris) {
        if (select.getIri()!=null)
            addToIriList(select,ttIris,iris);
        if (select.getSelect()!=null)
            for (Select sub:select.getSelect())
                gatherSelectLabels(sub,ttIris,iris);
    }

    private void gatherWhereLabels(Where where, List<TTIriRef> ttIris, Map<String,String> iris) {
        if (where.getIri()!=null)
            addToIriList(where,ttIris,iris);
        if (where.getIri()!=null){
                addToIriList(where,ttIris,iris);
        }
        if (where.getWhere()!=null){
            for (Where subWhere: where.getWhere()){
                gatherWhereLabels(subWhere,ttIris,iris);
            }
        }
        if (where.getIn()!=null)
            for (TTAlias in:where.getIn())
                addToIriList(in,ttIris,iris);


    }

    private void gatherFromLabels(From from, List<TTIriRef> ttIris, Map<String, String> iris) {
        if (from.getIri()!=null)
            addToIriList(from,ttIris,iris);
        if (from.getWhere()!=null){
            for (Where where:from.getWhere()) {
                gatherWhereLabels(where, ttIris, iris);
            }
        }
        if (from.getFrom()!=null){
            from.getFrom().forEach(f-> gatherFromLabels(f,ttIris,iris));
        }
    }

    private void addToIriList(TTIriRef ttIriRef,List<TTIriRef> ttIris, Map<String,String> iris){
        if (ttIriRef.getIri()!=null) {
            ttIris.add(ttIriRef);
            iris.put(ttIriRef.getIri(),null);
        }
    }
}
