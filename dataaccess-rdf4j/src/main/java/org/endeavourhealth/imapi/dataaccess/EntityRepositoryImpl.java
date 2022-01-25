package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class EntityRepositoryImpl implements EntityRepository {

    @Override
    public TTIriRef getEntityReferenceByIri(String iri) {
        TTIriRef result = new TTIriRef();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname")
            .add("WHERE {")
            .add("  ?s rdfs:label ?sname")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.setIri(iri).setName(bs.getValue("sname").stringValue());
                }
            }
        }
        return result;
    }

    private TTArray getTypesByIri(String iri) {
        TTArray result = new TTArray();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?o ?oname")
                .add("WHERE {")
                .add("?s rdf:type ?o .")
                .add("?o rdfs:label ?oname .")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("o").stringValue(), bs.getValue("oname").stringValue()));
                }
            }
        }
        return result;
    }

    @Override
    public SearchResultSummary getEntitySummaryByIri(String iri) {
        SearchResultSummary result = new SearchResultSummary();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname ?scode ?sstatus ?sstatusname ?g ?gname")
            .add("WHERE {")
            .add("  GRAPH ?g {")
            .add("    ?s rdfs:label ?sname .")
            .add("  }")
            .add("  OPTIONAL { ?s im:code ?scode . }")
            .add("  OPTIONAL { ?s im:status ?sstatus . ?sstatus rdfs:label ?sstatusname . }")
            .add("  OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    TTArray types = getTypesByIri(iri);
                    BindingSet bs = rs.next();
                    result
                        .setIri(iri)
                        .setName(bs.getValue("sname").stringValue())
                        .setCode(bs.getValue("scode") == null ? "" : bs.getValue("scode").stringValue())
                        .setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())))
                        .setEntityType(types)
                        .setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue()));
                }
            }
        }
        return result;
    }

    /**
     * returns the full definition of an entity of a certain type using the shape type information
     * @param iri iri of the entity
     * @param type rdf:type of thr entity
     * @return the entity
     */
    @Override
    public TTEntity getEntityByType(String iri, TTIriRef type) {
        if (!type.equals(IM.PROFILE))
            return null;
        //To replace with the typ specific sub predicste cache
        Set<TTIriRef> subPredicates = Stream.of(IM.AND, IM.OR, IM.NOT, IM.VALUE_FUNCTION,
          IM.ARGUMENT, IM.FUNCTION, IM.TEST, IM.VALUE_RANGE, IM.FROM,IM.MATCH,
          IM.TO).collect(Collectors.toCollection(HashSet::new));

        TTEntity entity = getEntityWithSubPredicates(iri, null, subPredicates);
        return entity;
    }

    /**
     * returns and entity from a recursive SPARQL query with property paths based on the
     * required sub predicates
     * @param iri iri of the entity
     * @param mainPredicates set of predicates for the main body, null if all
     * @param subPredicates set of predicates for recursive blank node/ sub node
     * @return the entity populated
     */
    private static TTEntity getEntityWithSubPredicates(String iri, Set<TTIriRef> mainPredicates,Set<TTIriRef> subPredicates){
        RepositoryConnection conn =  ConnectionManager.getConnection();
        StringJoiner sql = new StringJoiner("\n");
        String mainPredVar=null;
        String subPredVar="?p2";
        if (mainPredicates!=null){
            int i = 0;
            mainPredVar="(";
            for (TTIriRef pred : mainPredicates) {
                mainPredVar= mainPredVar + (i>0 ? "," :"")+ "<"+pred.getIri()+"> ";
                i++;
            }
            mainPredVar=mainPredVar+")";
        }
        if (subPredicates!=null){
            int i = 0;
            subPredVar="(";
            for (TTIriRef pred : subPredicates) {
                subPredVar= subPredVar+ (i>0 ? "|" :"")+ "<"+pred.getIri()+"> ";
                i++;
            }
            subPredVar= subPredVar+")+ ";
        }

        sql.add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
          .add("PREFIX im: <http://endhealth.info/im#>")
          .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
          .add("CONSTRUCT {")
          .add("     ?entity ?p1 ?o1.")
          .add("     ?o1 rdfs:label ?iriLabel1.")
          .add("    ?s2 ?p2 ?o2.")
          .add("    ?o2 rdfs:label ?iriLabel2.")
          .add("    ?o2 ?p3 ?o3.")
          .add("    ?o3 rdfs:label ?iriLabel3.")
          .add("}")
          .add("where {")
          .add("    ?entity ?p1 ?o1.");
        if (mainPredVar!=null)
            sql.add("   FILTER (?1predicate IN (" + mainPredVar + "))");
        sql.add("    optional {");
        sql.add("    ?o1 "+subPredVar+ "?o2.");
        sql.add("   ?s2 ?p2 ?o2.")
          .add("        filter(isBlank(?o1))")
          .add("        Optional { ?o1 rdfs:label ?iriLabel1.")
          .add("            filter(isIri(?o1))}")
          .add("    Optional {?o2 ?p3 ?o3")
          .add("            filter(isBlank(?o2))}")
          .add("        Optional {?o2 rdfs:label ?iriLabel2.")
          .add("            filter(isIri(?o2))}    ")
          .add("        Optional {?o3 rdfs:label ?iriLabel3.")
          .add("            filter (isIri(?o3))}}}");
        GraphQuery qry = conn.prepareGraphQuery(sql.toString());
        qry.setBinding("entity",
          iri((String) Objects.requireNonNull(iri,
            "iri may not be null")));
        GraphQueryResult gs = qry.evaluate();
        Map<Value, TTValue> valueMap = new HashMap<>();
        TTEntity entity = new TTEntity().setIri(iri);
        for (org.eclipse.rdf4j.model.Statement st : gs) {
            populateEntity(st, entity,valueMap);
        }
        return entity;
    }

    /**
     * populates an entity or sub nodes with statement
     * @param st statement to add to entity
     * @param entity the entity to add the statement to
     * @param valueMap the map from statement to node that builds
     */
    private static void populateEntity(Statement st, TTEntity entity, Map<Value, TTValue> valueMap ){
        Resource subject = st.getSubject();
        TTIriRef predicate = TTIriRef.iri(st.getPredicate().stringValue());
        Value value = st.getObject();
        valueMap.put(st.getPredicate(), predicate);
        if (valueMap.get(subject) == null) {
            if (subject.stringValue().equals(entity.getIri()))
                valueMap.put(subject,entity);
            else if (subject.isIRI())
                valueMap.put(subject,TTIriRef.iri(subject.stringValue()));
            else
                valueMap.put(subject, new TTNode());
        }
        TTValue ttValue= valueMap.get(subject);
        if (ttValue.isIriRef()) {
            if (predicate.equals(RDFS.LABEL))
                ttValue.asIriRef().setName(value.stringValue());
        } else {
            TTNode node = valueMap.get(subject).asNode();
            if (value.isLiteral()) {
                node.set(TTIriRef.iri(st.getPredicate().stringValue()), literal(value.stringValue()));
            } else if (value.isIRI()) {
                TTIriRef objectIri = null;
                if (valueMap.get(value) != null)
                    objectIri = valueMap.get(value).asIriRef();
                if (objectIri == null)
                    objectIri = TTIriRef.iri(value.stringValue());
                if (node.get(predicate) == null)
                    node.set(predicate, objectIri);
                else
                    node.addObject(predicate, objectIri);
                valueMap.putIfAbsent(value, objectIri);
            } else if (value.isBNode()) {
                TTNode ob = new TTNode();
                if (node.get(predicate) == null)
                    node.set(predicate, ob);
                else
                    node.addObject(predicate, ob);
                valueMap.put(value, ob);
            }
        }

    }


}
