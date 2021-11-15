package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.RepositoryResult;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntitySummary;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class EntityTripleRepositoryImpl implements EntityTripleRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EntityTripleRepositoryImpl.class);

    private final Map<String, Integer> bnodes = new HashMap<>();
    private int row = 0;

    @Override
    public TTBundle getEntityPredicates(String iri, Set<String> predicates, int limit) {
        List<Tpl> triples = getTriplesRecursive(iri, predicates, limit);
        LOG.debug("Entity triples : {}", triples.size());
        return Tpl.toBundle(iri, triples);
    }

    @Override
    public List<Tpl> getTriplesRecursive(String iri, Set<String> predicates, int limit) throws DALException {
        row = 0;
        List<Tpl> result = new ArrayList<>();
        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            addTriples(conn, result, iri(iri), null, predicates);

        } catch (RepositoryException e) {
            throw new DALException("Failed to recursive triples");
        }
        return result;
    }

    @Override
    public List<Tpl> getTriplesRecursiveByExclusions(String iri, Set<String> exclusionPredicates, int limit) {
        return new ArrayList<>();
    }

    @Override
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
            sql.add("LIMIT " + rowNumber + ", " + pageSize);
        }

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
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

    @Override
    public Integer getCountOfActiveSubjectByObjectExcludeByPredicate(String objectIri, String excludePredicateIri) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT (COUNT(?s) AS ?cnt) WHERE {")
            .add("    ?s ?p ?o .")
            .add("    ?s im:status ?status .")
            .add("    FILTER (?p != ?e && ?status != im:Inactive)")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
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


    @Override
    public Set<ValueSetMember> getSubjectByObjectAndPredicateAsValueSetMembers(String objectIri, String predicateIri) {
        Set<ValueSetMember> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?s ?sname ?scode ?g ?gname WHERE {")
            .add("    ?s ?p ?o .")
            .add("    ?s rdfs:label ?sname .")
            .add("    GRAPH ?g { ?s im:code ?scode } .")
            .add("    OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(objectIri));
            qry.setBinding("p", iri(predicateIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    result.add(new ValueSetMember()
                        .setEntity(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("sname").stringValue()))
                        .setCode(bs.getValue("scode").stringValue())
                        .setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())))
                    );
                }
            }
        }

        return result;
    }

    @Override
    public Set<EntitySummary> getSubclassesAndReplacements(String iri) {
        Set<EntitySummary> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT DISTINCT ?s") //  ?sname ?scode ?g ?gname")
            .add("WHERE {")
            .add("  ?s (rdfs:subClassOf|sn:370124000)* ?o .")
            .add("  ?s rdfs:label ?sname .")
            .add("  GRAPH ?g { ?s im:code ?scode } .")
            .add("  OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(iri));
            LOG.debug("Executing...");
            try (TupleQueryResult rs = qry.evaluate()) {
                LOG.debug("Retrieving...");
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    result.add(new EntitySummary()
                        .setIri(bs.getValue("s").stringValue())
                        .setName(bs.getValue("sname").stringValue())
                        .setCode(bs.getValue("scode").stringValue())
                        .setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())))
                    );
                }
                LOG.debug("Finished (" + result.size() + " rows)");
            }
        }

        return result;
    }


    @Override
    public Collection<EntitySummary> getSubjectAndDescendantSummariesByPredicateObjectRelType(String predicate, String object) {
        Set<EntitySummary> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT DISTINCT ?s") //  ?sname ?scode ?g ?gname")
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

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("bp", iri(predicate));
            qry.setBinding("bo", iri(object));
            LOG.debug("Executing...");
            try (TupleQueryResult rs = qry.evaluate()) {
                LOG.debug("Retrieving...");
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    result.add(new EntitySummary()
                        .setIri(bs.getValue("s").stringValue())
                        .setName(bs.getValue("sname").stringValue())
                        .setCode(bs.getValue("scode").stringValue())
                        .setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())))
                    );
                }
                LOG.debug("Finished (" + result.size() + " rows)");
            }
        }

        return result;
    }

    @Override
    public boolean hasChildren(String parentIri, boolean inactive) throws DALException {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?c")
            .add("WHERE {")
            .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf) ?p .")
            .add("  ?c im:status ?s .");

        if (!inactive)
            sql.add("  FILTER (?s != im:Inactive) .");

        sql.add("}")
            .add("LIMIT 1");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("p", iri(parentIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                return rs.hasNext();
            }
        }
    }

    @Override
    public List<TTIriRef> findImmediateChildrenByIri(String parentIri, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> result = new ArrayList<>();

            StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?c ?cname")
            .add("WHERE {")
            .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf) ?p .")
            .add("  ?c rdfs:label ?cname ;")
            .add("     im:status  ?s .");

        if (!inactive)
            sql.add("  FILTER (?s != im:Inactive) .");

        sql.add("}");

        if (rowNumber != null && pageSize != null) {
            sql
                .add("LIMIT " + pageSize)
                .add("OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
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

    @Override
    public List<TTIriRef> findImmediateParentsByIri(String childIri, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?p ?pname")
            .add("WHERE {")
            .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf) ?p .")
            .add("  ?p rdfs:label ?pname ;")
            .add("     im:status  ?s .");

        if (!inactive)
            sql.add("  FILTER (?s != im:Inactive) .");

        sql.add("}");

        if (rowNumber != null && pageSize != null) {
            sql
                .add("LIMIT " + pageSize)
                .add("OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("c", iri(childIri));
            LOG.debug("Executing...");
            try (TupleQueryResult rs = qry.evaluate()) {
                LOG.debug("Retrieving...");
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    result.add(new TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue()));
                }
                LOG.debug("Finished (" + result.size() + " rows)");
            }
        }

        return result;
    }

    @Override
    public Set<TTIriRef> getObjectIriRefsBySubjectAndPredicate(String subjectIri, String predicateIri) {
        Set<TTIriRef> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?o ?oname")
            .add("WHERE {")
            .add("  ?s ?p ?o .")
            .add("  ?o im:label ?oname .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(subjectIri));
            qry.setBinding("p", iri(predicateIri));
            LOG.debug("Executing...");
            try (TupleQueryResult rs = qry.evaluate()) {
                LOG.debug("Retrieving...");
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    result.add(new TTIriRef(bs.getValue("o").stringValue(), bs.getValue("oname").stringValue()));
                }
                LOG.debug("Finished (" + result.size() + " rows)");
            }
        }

        return result;
    }

    @Override
    public List<Namespace> findNamespaces() {
        List<Namespace> result = new ArrayList<>();
        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            RepositoryResult<org.eclipse.rdf4j.model.Namespace> namespaces = conn.getNamespaces();
            while (namespaces.hasNext()) {
                org.eclipse.rdf4j.model.Namespace ns = namespaces.next();
                result.add(new Namespace(ns.getName(), ns.getPrefix(), ns.getName()));
            }
        }

        return result;
    }

    @Override
    public Collection<SimpleMap> getSubjectFromObjectPredicate(String objectIri, TTIriRef predicate) {
        List<SimpleMap> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?s ?sname ?scode ?g ?gname WHERE {")
            .add("    ?s ?p ?o .")
            .add("    ?s rdfs:label ?sname .")
            .add("    GRAPH ?g { ?s im:code ?scode } .")
            .add("    OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(objectIri));
            qry.setBinding("p", iri(predicate.getIri()));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    result.add(new SimpleMap()
                        .setIri(bs.getValue("s").stringValue())
                        .setName(bs.getValue("sname").stringValue())
                        .setCode(bs.getValue("scode").stringValue())
                        .setScheme((bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue()))
                    );
                }
            }
        }

        return result;
    }


    @Override
    public Set<EntitySummary> getLegacyConceptSummaries(Set<EntitySummary> coreEntities) {
        Set<EntitySummary> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?s ?sname ?scode ?g ?gname WHERE {")
            .add("    ?s im:matchedTo ?o .")
            .add("    ?s rdfs:label ?sname .")
            .add("    GRAPH ?g { ?s im:code ?scode } .")
            .add("    OPTIONAL { ?g rdfs:label ?gname } .")
            .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());

            for (EntitySummary core : coreEntities) {
                qry.setBinding("o", iri(core.getIri()));
                try (TupleQueryResult rs = qry.evaluate()) {
                    while (rs.hasNext()) {
                        BindingSet bs = rs.next();

                        result.add(new EntitySummary()
                            .setIri(bs.getValue("s").stringValue())
                            .setName(bs.getValue("sname").stringValue())
                            .setCode(bs.getValue("scode").stringValue())
                            .setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())))
                        );
                    }
                }
            }
        }

        return result;
    }

    private void addTriples(RepositoryConnection conn, List<Tpl> triples, Resource subject, Integer parent, Set<String> predicates) {
        List<Statement> statements = new ArrayList<>();

        if (predicates == null) {
            RepositoryResult<Statement> s = conn.getStatements(subject, null, null);
            statements.addAll(s.asList());
        } else {
            for (String predicate : predicates) {
                RepositoryResult<Statement> s = conn.getStatements(subject, iri(predicate), null);
                statements.addAll(s.asList());
            }
        }

        for(Statement stmt: statements) {
            Tpl tpl = new Tpl()
                .setDbid(row++)
                .setParent(parent)
                .setPredicate(TTIriRef.iri(stmt.getPredicate().stringValue()));

            triples.add(tpl);

            Value object = stmt.getObject();
            if (object.isIRI()) {
                tpl.setObject(TTIriRef.iri(object.stringValue()));
            } else if (object.isLiteral()) {
                tpl.setLiteral(object.stringValue())
                    .setObject(TTIriRef.iri(((Literal) object).getDatatype().stringValue()));
            } else if (object.isBNode()) {
                bnodes.put(object.stringValue(), row - 1);
                addTriples(conn, triples, (BNode) object, row - 1, null);
            } else {
                throw new DALException("ARRAY!");
            }

            // LOG.debug(tpl.getDbid() + ": " + tpl.getParent() + " - " + tpl.getPredicate().getIri() + " => " + tpl.getLiteral() + "/" + tpl.getObject());
        }
    }
}
