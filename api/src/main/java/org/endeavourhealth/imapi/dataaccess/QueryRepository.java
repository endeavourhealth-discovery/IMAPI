package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import joptsimple.internal.Strings;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.logic.service.OSQuery;
import org.endeavourhealth.imapi.model.iml.Query;
import org.endeavourhealth.imapi.model.iml.QueryRequest;
import org.endeavourhealth.imapi.model.iml.Select;
import org.endeavourhealth.imapi.model.iml.Where;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

/**
 * Methods to convert a Query object to its Sparql equivalent and return results as a json object
 */
public class QueryRepository {
    private int o = 0;
    private Query query;
    private TTDocument result= new TTDocument();
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
    public TTDocument queryIM(QueryRequest queryRequest) throws DataFormatException, JsonProcessingException {

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            unpackQueryRequest(queryRequest, conn);
            if (null != queryRequest.getPathQuery())
                return new PathRepository().queryIM(queryRequest, conn);

            if (null != queryRequest.getTextSearch()) {
                ObjectNode osResult = new OSQuery().openSearchQuery(queryRequest);
                if (null != osResult)
                    return convertToEntity(osResult);
            }
            checkReferenceDate();
            SparqlConverter converter = new SparqlConverter(queryRequest);
            String spq = converter.getSelectSparql();
            return graphSelectSearch(spq, conn);

        }
    }

    private void unpackQueryRequest(QueryRequest queryRequest, RepositoryConnection conn) throws DataFormatException, JsonProcessingException {
        this.queryRequest = queryRequest;
        this.query = unpackQuery(queryRequest.getQuery(), conn);
        queryRequest.setQuery(query);
        if (null != query.getPrefix())
            result.setContext(query.getPrefix());
    }

    private Query unpackQuery(Query query, RepositoryConnection conn) throws JsonProcessingException, DataFormatException {
        if (null == query.getSelect()) {
            if (null == query.getWhere()) {
                if (null == query.getIri()) {
                    throw new DataFormatException("No query iri or body in request");
                } else {
                    TTEntity entity = getEntity(query.getIri(), conn);
                    try (CachedObjectMapper om = new CachedObjectMapper()) {
                        return om.readValue(entity.get(IM.DEFINITION).asLiteral().getValue(), Query.class);
                    }
                }
            }
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
        String alias = select.getProperty().getAlias();
        TTIriRef predicate= TTIriRef.iri(select.getProperty().getIri());
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

    private void addProperty(ObjectNode node, String property, String value) {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            if (node.get(property) == null) {
                node.set(property, om.createArrayNode());
                ((ArrayNode) node.get(property)).add(value);
            }
            ArrayNode already = (ArrayNode) node.get(property);
            for (JsonNode n : already) {
                if (n.asText().equals(value))
                    return;
            }
            already.add(value);
        }

    }

    private void addProperty(ObjectNode node, String property, ObjectNode value) {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            if (node.get(property) == null)
                node.set(property, om.createArrayNode());
            ((ArrayNode) node.get(property)).add(value);
        }
    }

    private void addProperty(ObjectNode node, String property, JsonNode value) {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            if (node.get(property) == null)
                node.set(property, om.createArrayNode());
            ((ArrayNode) node.get(property)).add(value);
        }
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

    private Set<TTIriRef> getTTValues(JsonNode fieldValue, RepositoryConnection conn) {
        Set<TTIriRef> values = new HashSet<>();
        if (fieldValue.isArray()) {
            for (Iterator<JsonNode> it = fieldValue.elements(); it.hasNext(); ) {
                JsonNode node = it.next();
                if (node.isObject()) {
                    processNode(node, values, conn);
                }
            }
        }
        if (fieldValue.isObject()) {
            processNode(fieldValue, values, conn);
        }
        return values;
    }

    private TTIriRef getFirst(JsonNode fieldValue, RepositoryConnection conn) throws DataFormatException {
        Set<TTIriRef> set = getTTValues(fieldValue, conn);
        Optional<TTIriRef> first = set.stream().findFirst();
        if (first.isPresent())
            return first.get();
        else
            throw new DataFormatException("Invalid iri set in " + fieldValue);
    }

    private void processNode(JsonNode node, Set<TTIriRef> values, RepositoryConnection conn) {
        String name;
        if (node.has("name")) {
            name = node.get("name").asText();
        } else {
            name = getEntityReferenceByIri(node.get("@id").asText(), conn).getIri();

        }
        values.add(TTIriRef.iri(node.get("@id").asText(), name));
    }

    private TTIriRef getEntityReferenceByIri(String iri, RepositoryConnection conn) {
        TTIriRef result = new TTIriRef();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname WHERE {")
            .add("  ?s rdfs:label ?sname")
            .add("}");

        TupleQuery qry = prepareSparql(conn, sql.toString());
        qry.setBinding("s", Values.iri(iri));
        try (TupleQueryResult rs = qry.evaluate()) {
            if (rs.hasNext()) {
                BindingSet bs = rs.next();
                result.setIri(iri).setName(bs.getValue("sname").stringValue());
            }
        }
        return result;
    }

    private String getTextValue(JsonNode fieldValue) {
        if (fieldValue.isArray()) {
            Iterator<JsonNode> it = fieldValue.elements();
            JsonNode node = it.next();
            return node.asText();
        }
        return fieldValue.asText();
    }

    private void checkReferenceDate() {
        if (queryRequest.getReferenceDate() == null) {
            String now = LocalDate.now().toString();
            queryRequest.setReferenceDate(now);
        }

    }

    private TTEntity getEntity(String iri, RepositoryConnection conn) throws DataFormatException {
        String predList = "<" + IM.DEFINITION.getIri() + ">,<" + RDF.TYPE.getIri() + ">,<" + IM.FUNCTION_DEFINITION.getIri() + ">";
        String sql = new StringJoiner(System.lineSeparator())
            .add("Select ?entity ?p ?o")
            .add("where {")
            .add("  ?entity ?p ?o.")
            .add("  filter (?p in(" + predList + "))")
            .add("}")
            .toString();
        TupleQuery qry = conn.prepareTupleQuery(sql);
        qry.setBinding("entity", Values.iri(iri));
        qry.setBinding("type", Values.iri(RDF.TYPE.getIri()));
        qry.setBinding("json", Values.iri(SHACL.VALUE.getIri()));
        try (TupleQueryResult rs = qry.evaluate()) {
            if (!rs.hasNext())
                throw new DataFormatException("unknown query or function iri : " + iri);
            TTEntity entity = new TTEntity();
            entity.setIri(iri);
            while (rs.hasNext()) {
                BindingSet bs = rs.next();
                String predicate = bs.getValue("p").stringValue();
                String object = bs.getValue("o").stringValue();
                if (predicate.equals(RDF.TYPE.getIri()))
                    entity.set(RDF.TYPE, TTIriRef.iri(object));
                else if (predicate.equals(IM.DEFINITION.getIri())) {
                    entity.set(IM.DEFINITION, TTLiteral.literal(bs.getValue("o").stringValue()));
                } else
                    entity.set(IM.FUNCTION_DEFINITION, TTLiteral.literal(object));
            }
            return entity;
        }
    }

    private String localName(String iri) {
        String del = "#";
        if (!iri.contains("#"))
            del = ":";
        String[] iriSplit = iri.split(del);
        return iriSplit[iriSplit.length - 1];
    }

    /**
     * is the variable im:id or iri i.e. the id
     *
     * @param predicate the predicate to test
     * @return tru of the predicate is for an iri
     */
    public boolean isId(String predicate) {
        if (predicate.equals("entity"))
            return true;
        if (predicate.equals("id"))
            return true;
        if (predicate.equals(IM.NAMESPACE + "id"))
            return true;
        if (predicate.equals(IM.NAMESPACE + "iri"))
            return true;
        if (predicate.contains(":")) {
            if (predicate.substring(predicate.indexOf(":") + 1).equals("id"))
                return true;
            return predicate.substring(predicate.indexOf(":") + 1).equals("iri");
        }
        return false;
    }

    private String nextObject() {
        o++;
        return "o" + o;
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
            String sql="Select ?entity ?label where { ?entity <"+ RDFS.LABEL.getIri()+"> ?label.\n"+
             "filter (?entity in ("+ iris+"))\n}";
            TupleQuery qry= conn.prepareTupleQuery(sql);
            TupleQueryResult rs= qry.evaluate();
            while (rs.hasNext()){
                BindingSet bs= rs.next();
                String label= bs.getValue("label").stringValue();
                String iri= bs.getValue("entity").stringValue();
                iriLabels.put(iri,label);
            }
        }
        for (TTIriRef ttIri:ttIris){
            ttIri.setName(iriLabels.get(ttIri.getIri()));
        }
    }
    private void gatherQueryLabels(Query query,List<TTIriRef> ttIris, Map<String,String> iris ){
        if (query.getFrom()!=null)
            for (TTAlias from:query.getFrom())
                addToIriList(from,ttIris,iris);
        if (query.getWhere()!=null)
            gatherWhereLabels(query.getWhere(),ttIris,iris);
        if (query.getSelect()!=null)
            for (Select select:query.getSelect())
                gatherSelectLabels(select,ttIris,iris);
        if (query.getSubQuery()!=null)
            for (Query subQuery: query.getSubQuery())
                gatherQueryLabels(subQuery,ttIris,iris);
    }

    private void gatherSelectLabels(Select select, List<TTIriRef> ttIris, Map<String,String> iris) {
        if (select.getProperty()!=null)
            addToIriList(select.getProperty(),ttIris,iris);
        if (select.getSelect()!=null)
            for (Select sub:select.getSelect())
                gatherSelectLabels(sub,ttIris,iris);
    }

    private void gatherWhereLabels(Where where, List<TTIriRef> ttIris, Map<String,String> iris) {
        if (where.getFrom()!=null){
            for (TTAlias from:where.getFrom())
                addToIriList(from,ttIris,iris);
        }
        if (where.getNotExist()!=null)
            gatherWhereLabels(where.getNotExist(),ttIris,iris);
        if (where.getProperty()!=null)
            addToIriList(where.getProperty(),ttIris,iris);
        if (where.getIs()!=null)
            addToIriList(where.getIs(),ttIris,iris);
        if (where.getIn()!=null)
            for (TTAlias in:where.getIn())
                addToIriList(in,ttIris,iris);
        if (where.getAnd()!=null)
            for (Where and:where.getAnd())
                gatherWhereLabels(and,ttIris,iris);
        if (where.getOr()!=null)
            for (Where or:where.getOr())
                gatherWhereLabels(or,ttIris,iris);

    }

    private void addToIriList(TTIriRef ttIriRef,List<TTIriRef> ttIris, Map<String,String> iris){
        if (ttIriRef.getIri()!=null) {
            ttIris.add(ttIriRef);
            iris.put(ttIriRef.getIri(),null);
        }
    }
}
