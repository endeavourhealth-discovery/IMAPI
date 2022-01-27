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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class EntityRepositoryImpl implements EntityRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EntityRepositoryImpl.class);

    @Override
    public TTIriRef getEntityReferenceByIri(String iri) {
        TTIriRef result = new TTIriRef();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname WHERE {")
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
                .add("SELECT ?o ?oname WHERE {")
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
            .add("SELECT ?sname ?scode ?sstatus ?sstatusname ?g ?gname WHERE {")
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
     * returns and entity from a recursive SPARQL query with property paths based on the
     * required sub predicates
     * @param iri iri of the entity
     * @param mainPredicates set of predicates for the main body, null if all
     * @param subPredicates set of predicates for recursive blank node/ sub node
     * @return the entity populated
     */
    private static TTEntity getEntityWithSubPredicates(String iri, Set<TTIriRef> mainPredicates,Set<TTIriRef> subPredicates){
        RepositoryConnection conn =  ConnectionManager.getConnection();
        StringBuilder mainPredVar;
        StringBuilder subPredVar= new StringBuilder("?p2");
        if (mainPredicates!=null){
            int i = 0;
            mainPredVar = new StringBuilder("(");
            for (TTIriRef pred : mainPredicates) {
                mainPredVar.append(i > 0 ? "," : "").append("<").append(pred.getIri()).append("> ");
                i++;
            }
            mainPredVar.append(")");
        }
        if (subPredicates!=null){
            int i = 0;
            subPredVar = new StringBuilder("(");
            for (TTIriRef pred : subPredicates) {
                subPredVar.append(i > 0 ? "|" : "").append("<").append(pred.getIri()).append("> ");
                i++;
            }
            subPredVar.append(")+ ");
        }

        String sql="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
          "PREFIX im: <http://endhealth.info/im#>\n" +
          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "CONSTRUCT {\n" +
          "   ?entity ?p1 ?o1.\n" +
          "  ?o1 rdfs:label ?iriLabel1.\n" +
          "   ?s2 ?p2 ?o2.\n" +
          "   ?o2 ?p3 ?o3.\n" +
          "    ?o3 rdfs:label ?iriLabel3.\n" +
          "}\n" +
          "\n" +
          "where {\n" +
          "    ?entity ?p1 ?o1.\n" +
          "     Optional { ?o1 rdfs:label ?iriLabel1.\n" +
          "            filter(isIri(?o1))}\n" +
          "    optional {\n" +
          "    ?o1 "+subPredVar+" ?o2.\n"+
          "   ?s2 ?p2 ?o2.\n" +
          "        filter(isBlank(?o1))\n" +
          "       Optional {?o2 ?p3 ?o3.\n" +
          "            filter(isBlank(?o2))}\n" +
          "       \n" +
          "        Optional {?o3 rdfs:label ?iriLabel3.\n" +
          "            filter (isIri(?o3))}\n" +
          "   }\n" +
          "}\n";
        GraphQuery qry = conn.prepareGraphQuery(sql);
        qry.setBinding("entity",
          iri(Objects.requireNonNull(iri,
            "iri may not be null")));

        GraphQueryResult gs = qry.evaluate();
        Map<Value, TTValue> valueMap = new HashMap<>();
        TTEntity entity = new TTEntity().setIri(iri);
        int i=0;
        for (org.eclipse.rdf4j.model.Statement st : gs) {
            i++;
            if (i%100==0) {
                LOG.debug(i+ " "+st.getSubject().stringValue()+" " + st.getPredicate().stringValue()+" "+st.getObject().stringValue());
            }
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
