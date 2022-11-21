package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class EntityRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EntityRepository.class);

    public TTIriRef getEntityReferenceByIri(String iri) {
        TTIriRef result = new TTIriRef();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname WHERE {")
            .add("  ?s rdfs:label ?sname")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
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

    private Set<TTIriRef> getTypesByIri(String iri) {
        Set<TTIriRef> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?o ?oname WHERE {")
                .add("?s rdf:type ?o .")
                .add("?o rdfs:label ?oname .")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
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

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    Set<TTIriRef> types = getTypesByIri(iri);
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
    private static TTEntity getEntityWithSubPredicates(String iri, Set<TTIriRef> mainPredicates, Set<TTIriRef> subPredicates){
        RepositoryConnection conn =  ConnectionManager.getIMConnection();
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
                LOG.debug("{} {} {} {}", i, st.getSubject().stringValue(), st.getPredicate().stringValue(), st.getObject().stringValue());
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
            processNode(value, valueMap, subject, st, predicate);
        }
    }

    private static void processNode(Value value, Map<Value, TTValue> valueMap, Resource subject, Statement st, TTIriRef predicate) {
        TTNode node = valueMap.get(subject).asNode();
        if (value.isLiteral()) {
            node.set(TTIriRef.iri(st.getPredicate().stringValue()), TTLiteral.literal(value.stringValue(), ((Literal)value).getDatatype().stringValue()));
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


    public List<TTIriRef> getPathBetweenNodes(String descendant, String ancestor) {
        List<TTIriRef> result = new ArrayList<>();

        String spql = new StringJoiner(System.lineSeparator())
            .add("select *")
            .add("where {")
            .add("  ?descendant (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf)+ ?m .")
            .add("  ?m (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf)+ ?ancestor ;")
            .add("     rdfs:label ?name .")
            .add("}")
            .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
             TupleQuery qry = prepareSparql(conn, spql);
            qry.setBinding("descendant", iri(descendant));
            qry.setBinding("ancestor", iri(ancestor));
            try (TupleQueryResult rs = qry.evaluate()) {
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(TTIriRef.iri(bs.getValue("m").stringValue(), bs.getValue("name").stringValue()));
                }
            }
        }

        return result;
    }

    public List<TTEntity> findEntitiesByName(String name) {
        List<TTEntity> result = new ArrayList<>();

        String spql = new StringJoiner(System.lineSeparator())
                .add("select *")
                .add("where {")
                .add("  ?s rdfs:label ?name ;")
                .add("  rdf:type ?type .")
                .add("  ?type rdfs:label ?typeName .")
                .add("}")
                .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, spql);
            qry.setBinding("name", literal(name));
            try (TupleQueryResult rs = qry.evaluate()) {
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    Optional<TTEntity> optionalEntity= result.stream()
                            .filter(entity -> bs.getValue("s").stringValue().equals(entity.getIri()))
                            .findAny();
                    if(optionalEntity.isEmpty()) {
                        TTEntity entity = new TTEntity()
                                .setIri(bs.getValue("s").stringValue())
                                .setName(bs.getValue("name").stringValue())
                                .addType(new TTIriRef(bs.getValue("type").stringValue(), bs.getValue("typeName").stringValue()));
                        result.add(entity);
                    } else {
                        optionalEntity.get().addType(new TTIriRef(bs.getValue("type").stringValue(), bs.getValue("typeName").stringValue()));
                    }

                }
            }
        }

        return result;
    }

    public List<ParentDto> findParentHierarchies(String iri) {
        List<ParentDto> result = new ArrayList<>();

        String spql = new StringJoiner(System.lineSeparator())
                .add("SELECT * {")
                .add("?s (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf) ?o .")
                .add("?o rdfs:label ?name .")
                .add("}")
                .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, spql);
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new ParentDto(bs.getValue("o").stringValue(), bs.getValue("name").stringValue(), null));
                }
            }
        }

        return result;
    }

    public Boolean iriExists(String iri) {
        Boolean result = false;

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, "SELECT * WHERE { ?s ?p ?o.} limit 1");
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if(rs.hasNext()) {
                    result = true;
                }
            }
        }
        return result;
    }

    public boolean predicatePathExists(String subject, TTIriRef predicate, String object) {
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            BooleanQuery qry = conn.prepareBooleanQuery("ASK { ?s (<" + predicate.getIri() + ">)* ?o.}");
            qry.setBinding("s", iri(subject));
            qry.setBinding("o", iri(object));
            return qry.evaluate();
        }
    }

    public EntityDocument getOSDocument(String iri) {
        EntityDocument result = new EntityDocument().setIri(iri);
        hydrateCoreProperties(result);
        hydrateTerms(result);
        hydrateIsAs(result);
        return result;
    }

    private void hydrateCoreProperties(EntityDocument entityDocument){
        String spql = new StringJoiner(System.lineSeparator())
            .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
            .add("PREFIX im: <http://endhealth.info/im#>")
            .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
            .add("select ?iri ?name ?status ?statusName ?code ?scheme ?schemeName ?type ?typeName ?weighting")
            .add("?extraType ?extraTypeName")
            .add("where {")
            .add("  graph ?scheme { ?iri rdf:type ?type }")
            .add("    Optional { ?iri rdfs:label ?name.}")
            .add("    Optional {?iri im:isA ?extraType.")
            .add("      ?extraType rdfs:label ?extraTypeName.")
            .add("      filter (?extraType in (im:dataModelProperty, im:DataModelEntity))}")
            .add("    Optional {?type rdfs:label ?typeName}")
            .add("    Optional {?iri im:status ?status.")
            .add("    Optional {?status rdfs:label ?statusName} }")
            .add("    Optional {?scheme rdfs:label ?schemeName }")
            .add("    Optional {?iri im:code ?code.}")
            .add("    Optional {?iri im:weighting ?weighting.}")
            .add("}").toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery tupleQuery = conn.prepareTupleQuery(spql);
            tupleQuery.setBinding("iri", iri(entityDocument.getIri()));
            try (TupleQueryResult qr = tupleQuery.evaluate()) {
                if (qr.hasNext()) {
                    BindingSet rs = qr.next();
                    entityDocument.setName(rs.getValue("name").stringValue());
                    entityDocument.addTermCode(entityDocument.getName(), null, null);

                    if (rs.hasBinding("code"))
                        entityDocument.setCode(rs.getValue("code").stringValue());

                    if (rs.hasBinding("schemeName"))
                        entityDocument.setScheme(new TTIriRef(rs.getValue("schemeName").stringValue()));

                    if (rs.hasBinding("status")) {
                        TTIriRef status = TTIriRef.iri(rs.getValue("status").stringValue());
                        if (rs.hasBinding("statusName"))
                            status.setName(rs.getValue("statusName").stringValue());
                        entityDocument.setStatus(status);
                    }

                    TTIriRef type = TTIriRef.iri(rs.getValue("type").stringValue());
                    if (rs.hasBinding("typeName"))
                        type.setName(rs.getValue("typeName").stringValue());
                    entityDocument.addType(type);

                    if (rs.hasBinding("extraType")) {
                        TTIriRef extraType = TTIriRef.iri(rs.getValue("extraType").stringValue(), rs.getValue("extraTypeName").stringValue());
                        entityDocument.addType(extraType);
                        if (extraType.equals(TTIriRef.iri(IM.NAMESPACE + "DataModelEntity"))) {
                            int weighting = 2000000;
                            entityDocument.setWeighting(weighting);
                        }
                    }
                    if (rs.hasBinding("weighting")) {
                        entityDocument.setWeighting(((Literal)rs.getValue("weighting")).intValue());
                    }
                }
            }
        }
    }
    private void hydrateTerms(EntityDocument entityDocument) {
        String spql = new StringJoiner(System.lineSeparator())
            .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
            .add("PREFIX im: <http://endhealth.info/im#>")
            .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
            .add("select ?termCode ?synonym ?termCodeStatus")
            .add("where {")
            .add("  ?iri im:hasTermCode ?tc.")
            .add("  Optional {?tc im:code ?termCode}")
            .add("  Optional  {?tc rdfs:label ?synonym}")
            .add("  Optional  {?tc im:status ?termCodeStatus}")
            .add("}").toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery tupleQuery = conn.prepareTupleQuery(spql);
            tupleQuery.setBinding("iri", literal(entityDocument.getIri()));
            try (TupleQueryResult qr = tupleQuery.evaluate()) {
                while (qr.hasNext()) {
                    BindingSet rs = qr.next();
                    String termCode = null;
                    String synonym = null;
                    TTIriRef status = null;
                    if (rs.hasBinding("synonym"))
                        synonym = rs.getValue("synonym").stringValue().toLowerCase();
                    if (rs.hasBinding("termCode"))
                        termCode = rs.getValue("termCode").stringValue();
                    if (rs.hasBinding("termCodeStatus"))
                        status = TTIriRef.iri(rs.getValue("termCodeStatus").stringValue());

                    if (synonym != null) {
                        SearchTermCode tc = getTermCode(entityDocument, synonym);
                        if (tc == null) {
                            entityDocument.addTermCode(synonym, termCode, status);
                            addKey(entityDocument,synonym);
                        }
                        else if (termCode != null) {
                            tc.setCode(termCode);
                        }
                    }
                    else if (termCode != null) {
                        SearchTermCode tc = getTermCodeFromCode(entityDocument, termCode);
                        if (tc == null)
                            entityDocument.addTermCode(null, termCode, status);
                    }
                }
            }
        }
    }

    private SearchTermCode getTermCode(EntityDocument blob,String term){
        for (SearchTermCode tc:blob.getTermCode()){
            if (tc.getTerm()!=null)
                if (tc.getTerm().equals(term))
                    return tc;
        }
        return null;
    }

    private void  addKey(EntityDocument blob, String key) {
        key= key.split(" ")[0];
        if (key.length()>1) {
            if (key.length() > 20)
                key = key.substring(0, 20);
            key = key.toLowerCase();
            List<String> deletes = new ArrayList<>();
            boolean skip = false;
            if (blob.getKey() != null) {
                for (String already : blob.getKey()) {
                    if (key.startsWith(already))
                        deletes.add(already);
                    if (already.startsWith(key))
                        skip = true;
                }
                if (!deletes.isEmpty())
                    deletes.forEach(d -> blob.getKey().remove(d));
            }
            if (!skip)
                blob.addKey(key);
        }
    }

    private SearchTermCode getTermCodeFromCode(EntityDocument blob,String code){
        for (SearchTermCode tc:blob.getTermCode()){
            if (tc.getCode()!=null && tc.getCode().equals(code))
                    return tc;
        }
        return null;
    }

    private void hydrateIsAs(EntityDocument entityDocument) {
        String spql = new StringJoiner(System.lineSeparator())
                .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
                .add("PREFIX im: <http://endhealth.info/im#>")
                .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
                .add("select ?superType")
                .add("where {")
                .add(" ?iri im:isA ?superType.")
                .add("}").toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery tupleQuery = conn.prepareTupleQuery(spql);
            tupleQuery.setBinding("iri", literal(entityDocument.getIri()));
            try (TupleQueryResult qr = tupleQuery.evaluate()) {
                while (qr.hasNext()) {
                    BindingSet rs = qr.next();
                    entityDocument.getIsA().add(TTIriRef.iri(rs.getValue("superType").stringValue()));
                }
            }
        }
    }
    public List<TTIriRef> getProperties() {
        List<TTIriRef> result = new ArrayList<>();

        String spql = new StringJoiner(System.lineSeparator())
                .add("select ?s ?name {")
                .add("  ?s rdf:type rdf:Property ;")
                .add("  rdfs:label ?name .")
                .add("}")
                .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, spql);
            try (TupleQueryResult rs = qry.evaluate()) {
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
                }
            }
        }

        return result;
    }

    public List<TTIriRef> getClasses() {
        List<TTIriRef> result = new ArrayList<>();

        String spql = new StringJoiner(System.lineSeparator())
                .add("select ?s ?name {")
                .add("  ?s rdf:type rdfs:Class ;")
                .add("  rdfs:label ?name .")
                .add("}")
                .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, spql);
            try (TupleQueryResult rs = qry.evaluate()) {
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
                }
            }
        }

        return result;
    }

    public List<TTIriRef> getStatuses() {
        List<TTIriRef> result = new ArrayList<>();

        String spql = new StringJoiner(System.lineSeparator())
                .add("select ?s ?name {")
                .add("  ?s rdfs:subClassOf im:Status ;")
                .add("  rdfs:label ?name .")
                .add("}")
                .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, spql);
            try (TupleQueryResult rs = qry.evaluate()) {
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
                }
            }
        }

        return result;
    }

    public List<TTIriRef> getDistillation(List<TTIriRef> conceptList) {
        List<String> iriList= conceptList.stream().map(c-> "<"+ c.getIri()+">").collect(Collectors.toList());
        String iris= String.join(",",iriList);
        Set<String> isas = new HashSet<>();

        for (TTIriRef c : conceptList) {
            try(RepositoryConnection conn = ConnectionManager.getIMConnection()) {
                StringJoiner sql = new StringJoiner(System.lineSeparator())
                        .add("SELECT ?s WHERE {")
                        .add("?s <http://endhealth.info/im#isA> ?iri .")
                        .add("FILTER (?s IN(" + iris + "))}");
                TupleQuery qry= conn.prepareTupleQuery(String.valueOf(sql));
                qry.setBinding("iri", iri(c.getIri()));
                TupleQueryResult rs= qry.evaluate();
                while (rs.hasNext()){
                    BindingSet bs= rs.next();
                    if(!c.getIri().equals(bs.getValue("s").stringValue())){
                        isas.add(bs.getValue("s").stringValue());
                    }
                }

            }

        }
        conceptList.removeIf(c -> isas.contains(c.getIri()));
        return conceptList;
    }
}
