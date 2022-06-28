package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.*;

public class EntityTripleRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EntityTripleRepository.class);
    private static final List<Namespace> namespaceCache = new ArrayList<>();

    private final EntityRepository2 entityRepository2 = new EntityRepository2();
    private final Map<String, Integer> bnodes = new HashMap<>();
    private int row = 0;

    private String EXECUTING = "Executing...";
    private String RETRIEVING = "Retrieving...";

    public TTBundle getEntityPredicates(String iri, Set<String> predicates) {
        return entityRepository2.getBundle(iri, predicates);
    }

    public List<TTIriRef> getActiveSubjectByObjectExcludeByPredicate(String objectIri, Integer rowNumber, Integer pageSize, String excludePredicateIri) {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT DISTINCT ?s ?name WHERE {")
            .add("    ?s ?p ?o .")
            .add("    ?s rdfs:label ?name .")
            .add("    ?s im:status ?status .")
            .add("    FILTER (?p != ?e && ?status != im:Inactive)")
            .add("}");

        if (rowNumber != null && pageSize != null) {
            sql.add("LIMIT " + pageSize + " OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(objectIri));
            qry.setBinding("e", iri(excludePredicateIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef()
                        .setIri(bs.getValue("s").stringValue())
                        .setName(bs.getValue("name").stringValue())
                    );
                }
            }
        }
        return result;
    }

    public Integer getCountOfActiveSubjectByObjectExcludeByPredicate(String objectIri, String excludePredicateIri) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT (COUNT(DISTINCT ?s) AS ?cnt) WHERE {")
            .add("    ?s ?p ?o .")
            .add("    ?s im:status ?status .")
            .add("    FILTER (?p != ?e && ?status != im:Inactive)")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(objectIri));
            qry.setBinding("e", iri(excludePredicateIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return ((Literal) bs.getValue("cnt")).intValue();
                } else {
                    return 0;
                }
            }
        }
    }

    public Set<EntitySummary> getSubclassesAndReplacements(String iri) {
        Set<EntitySummary> result = new HashSet<>();
        String isa = "(<" + RDFS.SUBCLASSOF.getIri() +
            ">|<" + RDFS.SUBPROPERTYOF.getIri() +
            ">|<" + SNOMED.REPLACED_BY.getIri() + ">)*";

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT DISTINCT ?s ?sname ?scode ?g ?gname WHERE {")
            .add("  ?s " + isa + " ?o .")
            .add("  ?s rdfs:label ?sname .")
            .add("  GRAPH ?g { ?s im:code ?scode } .")
            .add("  OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(iri));
            execute(result, qry);
        }

        return result;
    }

    public Collection<EntitySummary> getSubjectAndDescendantSummariesByPredicateObjectRelType(String predicate, String object) {
        Set<EntitySummary> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT DISTINCT ?s ?sname ?scode ?g ?gname")
            .add("WHERE {")
            .add("  ?p (rdfs:subClassOf)* ?bp .")
            .add("  ?o (rdfs:subClassOf)* ?bo .")
            .add("  ?bs ?p ?o .")
            .add("  ?s (rdfs:subClassOf|sn:370124000)* ?bs .")
            .add("  FILTER isUri(?s)")
            .add("  GRAPH ?g {")
            .add("    ?s im:code ?scode ;")
            .add("       rdfs:label ?sname .")
            .add("  }")
            .add("  OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("bp", iri(predicate));
            qry.setBinding("bo", iri(object));
            execute(result, qry);
        }

        return result;
    }

    private void execute(Set<EntitySummary> result, TupleQuery qry) {
        LOG.debug(EXECUTING);
        try (TupleQueryResult rs = qry.evaluate()) {
            LOG.debug(RETRIEVING);
            while (rs.hasNext()) {
                BindingSet bs = rs.next();

                result.add(new EntitySummary()
                    .setIri(bs.getValue("s").stringValue())
                    .setName(bs.getValue("sname").stringValue())
                    .setCode(bs.getValue("scode").stringValue())
                    .setScheme(
                        new TTIriRef(
                            bs.getValue("g").stringValue(),
                            (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())
                        )
                    )
                );
            }
            LOG.debug("Finished ({} rows) ", result.size());
        }
    }

    public boolean hasChildren(String parentIri, List<String> schemeIris, boolean inactive) throws DALException {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?c")
            .add("WHERE {")
            .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf|im:isSubsetOf) ?p .")
            .add("GRAPH ?g { ?c rdfs:label ?name } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }

        sql.add("}").add("LIMIT 1");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("p", iri(parentIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                return rs.hasNext();
            }
        }
    }

    public Pageable<TTIriRef> findImmediateChildrenPagedByIriWithTotalCount(String parentIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> children = new ArrayList<>();
        Pageable<TTIriRef> result = new Pageable<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?count ?c ?cname")
            .add("WHERE {")
            .add("{ SELECT (COUNT(?c) as ?count) {")
            .add("  ?c (rdfs:subClassOf | rdfs:subPropertyOf | im:isContainedIn | im:isChildOf | im:inTask | im:isSubsetOf) ?p }}")
            .add("UNION ")
            .add("{ SELECT ?c ?cname {")
            .add("  ?c (rdfs:subClassOf | rdfs:subPropertyOf | im:isContainedIn | im:isChildOf | im:inTask | im:isSubsetOf) ?p .")
            .add("GRAPH ?g { ?c rdfs:label ?cname } .");
        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }

        sql.add("}}}");
        sql.add("ORDER BY ?cname");

        if (rowNumber != null && pageSize != null) {
            sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("p", iri(parentIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                BindingSet bs = rs.next();
                if(rowNumber==0){
                    result.setTotalCount(((Literal) bs.getValue("count")).intValue());
                } else {
                    children.add(new TTIriRef(bs.getValue("c").stringValue(), bs.getValue("cname").stringValue()));
                }
                result.setPageSize(pageSize);
                while (rs.hasNext()) {
                    bs = rs.next();
                    children.add(new TTIriRef(bs.getValue("c").stringValue(), bs.getValue("cname").stringValue()));
                }
                result.setResult(children);
            }
        }
        return result;
    }

    public Pageable<TTIriRef> findPartialWithTotalCount(String parentIri,String predicateIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> children = new ArrayList<>();
        Pageable<TTIriRef> result = new Pageable<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?count ?p ?pname")
            .add("WHERE {")
            .add("{ SELECT (COUNT(?p) as ?count) {")
            .add("  ?c ?pr ?p }}")
            .add("UNION ")
            .add("{ SELECT ?p ?pname {")
            .add("  ?c ?pr ?p .")
            .add("?p rdfs:label ?pname .");
        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }

        sql.add("}}}");
        sql.add("ORDER BY ?pname");

        if (rowNumber != null && pageSize != null) {
            sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("c", iri(parentIri));
            qry.setBinding("pr", iri( predicateIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                BindingSet bs = rs.next();
                if(rowNumber==0){
                    result.setTotalCount(((Literal) bs.getValue("count")).intValue());
                }else{
                    children.add(new TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue()));
                }
                result.setPageSize(pageSize);
                while (rs.hasNext()) {
                    bs = rs.next();
                    children.add(new TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue()));
                }
                result.setResult(children);
            }
        }
        return result;
    }

    public List<TTIriRef> findImmediateChildrenByIri(String parentIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT DISTINCT ?c ?cname {")
            .add("  ?c (rdfs:subClassOf | rdfs:subPropertyOf | im:isContainedIn | im:isChildOf | im:inTask | im:isSubsetOf) ?p .")
            .add("GRAPH ?g { ?c rdfs:label ?cname } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }

        sql.add("}");
        sql.add("ORDER BY ?cname");

        if (rowNumber != null && pageSize != null) {
            sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("p", iri(parentIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("c").stringValue(), bs.getValue("cname").stringValue()));
                }
            }
        }

        return result;
    }

    public List<TTIriRef> findImmediateParentsByIri(String childIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?p ?pname")
            .add("WHERE {")
            .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf|im:isSubsetOf) ?p .")
            .add("GRAPH ?g { ?p rdfs:label ?pname } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?p im:status  ?s}").add("  FILTER (?s != im:Inactive) .");
        }

        sql.add("}");

        if (rowNumber != null && pageSize != null) {
            sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        sql.add("ORDER BY ?pname");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("c", iri(childIri));

            LOG.debug(EXECUTING);
            try (TupleQueryResult rs = qry.evaluate()) {
                LOG.debug(RETRIEVING);
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue()));
                }
                LOG.debug("Finished ({} rows)", result.size());
            }
        }
        return result;
    }

    public Set<TTIriRef> getObjectIriRefsBySubjectAndPredicate(String subjectIri, String predicateIri) {
        Set<TTIriRef> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?o ?oname")
            .add("WHERE {")
            .add("  ?s ?p ?o .")
            .add("  ?o im:label ?oname .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(subjectIri));
            qry.setBinding("p", iri(predicateIri));
            LOG.debug(EXECUTING);
            try (TupleQueryResult rs = qry.evaluate()) {
                LOG.debug(RETRIEVING);
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("o").stringValue(), bs.getValue("oname").stringValue()));
                }
                LOG.debug("Finished ({} rows)", result.size());
            }
        }
        return result;
    }

    public List<Namespace> findNamespaces() {
        synchronized (namespaceCache) {
            if (namespaceCache.isEmpty()) {
                Map<String, Namespace> result = new HashMap<>();
                try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
                    RepositoryResult<org.eclipse.rdf4j.model.Namespace> namespaces = conn.getNamespaces();
                    while (namespaces.hasNext()) {
                        org.eclipse.rdf4j.model.Namespace ns = namespaces.next();
                        result.put(ns.getName(), new Namespace(ns.getName(), ns.getPrefix(), ns.getName()));
                    }
                }

                // Get/add schemes
                StringJoiner sql = new StringJoiner(System.lineSeparator())
                    .add("SELECT *")
                    .add("WHERE {")
                    .add("    GRAPH ?g {")
                    .add("        ?g rdfs:label ?name")
                    .add("    }")
                    .add("}");

                int i = 1;
                try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
                    TupleQuery qry = conn.prepareTupleQuery(sql.toString());
                    try (TupleQueryResult rs = qry.evaluate()) {
                        while (rs.hasNext()) {
                            BindingSet bs = rs.next();
                            Namespace ns = result.get(bs.getValue("g").stringValue());
                            if (ns == null) {
                                result.put(
                                    bs.getValue("g").stringValue(),
                                    new Namespace(bs.getValue("g").stringValue(), "PFX" + (i++), bs.getValue("name").stringValue())
                                );
                            } else {
                                ns.setName(bs.getValue("name").stringValue());
                            }
                        }
                    }
                }
                namespaceCache.addAll(result.values());
            }
            Collections.sort(namespaceCache, Comparator.comparing(Namespace::getName));
            return namespaceCache;
        }
    }

    public Set<EntitySummary> getLegacyConceptSummaries(Set<EntitySummary> coreEntities) {
        Set<EntitySummary> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?s ?sname ?scode ?g ?gname WHERE {")
            .add("    ?s im:matchedTo ?o .")
            .add("    ?s rdfs:label ?sname .")
            .add("    GRAPH ?g { ?s im:code ?scode } .")
            .add("    OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            for (EntitySummary core : coreEntities) {
                qry.setBinding("o", iri(core.getIri()));
                execute(result, qry);
            }
        }
        return result;
    }

    private void addTriples(List<Tpl> triples, Resource subject, Integer parent, Set<String> predicates) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname ?p ?pname ?o ?oname")
            .add("WHERE {")
            .add("    ?s ?p ?o ;")
            .add("    OPTIONAL { ?s rdfs:label ?sname }")
            .add("    OPTIONAL { ?p rdfs:label ?pname }")
            .add("    OPTIONAL { ?o rdfs:label ?oname }");

        if (predicates != null && !predicates.isEmpty()) {
            sql.add("    FILTER ( ?p IN " + inList("p", predicates.size()) + " )");
        }

        sql.add("}");
        setAndEvaluate(triples, subject, parent, predicates, sql);
    }

    private void setAndEvaluate(List<Tpl> triples, Resource subject, Integer parent, Set<String> predicates, StringJoiner sql) {
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());

            qry.setBinding("s", subject);
            if (predicates != null && !predicates.isEmpty()) {
                int i = 0;
                for (String predicate : predicates) {
                    qry.setBinding("p" + i++, iri(predicate));
                }
            }

            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    Tpl tpl = new Tpl()
                        .setDbid(row++)
                        .setParent(parent)
                        .setPredicate(TTIriRef.iri(getString(bs, "p"), getString(bs, "pname")));

                    triples.add(tpl);
                    Value object = bs.getValue("o");
                    if (object.isIRI()) {
                        tpl.setObject(TTIriRef.iri(object.stringValue(), getString(bs, "oname")));
                    } else if (object.isLiteral()) {
                        tpl.setLiteral(object.stringValue())
                                .setObject(TTIriRef.iri(((Literal) object).getDatatype().stringValue()));
                    } else if (object.isBNode()) {
                        bnodes.put(object.stringValue(), row - 1);
                        addTriples(triples, (BNode) object, row - 1, null);
                    } else {
                        throw new DALException("ARRAY!");
                    }
                }
            }
        }
    }

    private void addTriplesExcluding(List<Tpl> triples, Resource subject, Integer parent, Set<String> excludePredicates) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?sname ?p ?pname ?o ?oname")
            .add("WHERE {")
            .add("    ?s ?p ?o ;")
            .add("    OPTIONAL { ?s rdfs:label ?sname }")
            .add("    OPTIONAL { ?p rdfs:label ?pname }")
            .add("    OPTIONAL { ?o rdfs:label ?oname }");

        if (excludePredicates != null && !excludePredicates.isEmpty()) {
            sql.add("    FILTER ( ?p NOT IN " + inList("p", excludePredicates.size()) + " )");
        }

        sql.add("}");
        setAndEvaluate(triples, subject, parent, excludePredicates, sql);
    }

    public List<SimpleMap> findSimpleMapsByIri(String iri, List<String> schemeIris) {
        List<SimpleMap> simpleMaps = new ArrayList<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add(" SELECT ?s ?code ?scheme ?name  WHERE{")
            .add(" ?s im:matchedTo ?o .")
            .add(" ?s im:code ?code .")
            .add(" ?s im:scheme ?scheme .  ")
            .add("GRAPH ?g { ?s rdfs:label ?name } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }
        sql.add("}");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    simpleMaps.add(new SimpleMap(getString(bs, "s"), getString(bs, "name"), getString(bs, "code"), getString(bs, "scheme")));
                }
            }
        }
        return simpleMaps;
    }

    public List<String> getConceptIrisByGraph(String iri) {
        List<String> iris = new ArrayList<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add(" SELECT DISTINCT ?s WHERE{")
            .add(" GRAPH ?g { ?s ?p ?o } .")
            .add("}")
            .add(" LIMIT 20 ");
        try( RepositoryConnection conn = ConnectionManager.getIMConnection()){
            TupleQuery qry = prepareSparql(conn, sql.toString());

            qry.setBinding("g", iri(iri));
            try(TupleQueryResult rs = qry.evaluate()){
                while (rs.hasNext()){
                    BindingSet bs = rs.next();
                    iris.add(getString(bs, "s"));
                }
            }
        }
        return iris;
    }

    public TTIriRef findParentFolderRef(String iri) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?p ?pname")
            .add("WHERE {")
            .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf) ?p .")
            .add("  ?p rdfs:label ?pname .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("c", iri(iri));

            LOG.debug(EXECUTING);
            try (TupleQueryResult rs = qry.evaluate()) {
                LOG.debug(RETRIEVING);
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return new TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue());
                }
            }
        }
        return null;
    }

    public boolean hasGrandChildren(String iri, List<String> schemeIris, boolean inactive) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?gc")
            .add("WHERE {")
            .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf) ?p .")
            .add(" ?gc (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf) ?c .")
            .add("GRAPH ?g { ?c rdfs:label ?name } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }

        sql.add("}").add("LIMIT 1");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("p", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                return rs.hasNext();
            }
        }
    }
}
