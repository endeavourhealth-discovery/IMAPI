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
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.GraphHelper.*;

public class EntityTripleRepositoryImpl implements EntityTripleRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EntityTripleRepositoryImpl.class);
    private static final List<Namespace> namespaceCache = new ArrayList<>();

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
        try {
            addTriples(result, iri(iri), null, predicates);
        } catch (RepositoryException e) {
            throw new DALException("Failed to recursive triples");
        }
        return result;
    }

    @Override
    public List<Tpl> getTriplesRecursiveByExclusions(String iri, Set<String> exclusionPredicates, int limit) {
        row = 0;
        List<Tpl> result = new ArrayList<>();
        try {
            addTriplesExcluding(result, iri(iri), null, exclusionPredicates);

        } catch (RepositoryException e) {
            throw new DALException("Failed to recursive triples");
        }
        return result;
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
            sql.add("LIMIT " + pageSize + " OFFSET " + rowNumber);
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
    public Set<TTIriRef> getSubjectByObjectAndPredicateAsTTIriRef(String objectIri, String predicateIri) {
        Set<TTIriRef> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?s ?sname WHERE {")
                .add("    ?s ?p ?o .")
                .add("    ?s rdfs:label ?sname .")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(objectIri));
            qry.setBinding("p", iri(predicateIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("sname").stringValue()));
                }
            }
        }

        return result;
    }

    @Override
    public Set<EntitySummary> getSubclassesAndReplacements(String iri) {
        Set<EntitySummary> result = new HashSet<>();
        String isa = "(<" + RDFS.SUBCLASSOF.getIri() +
                ">|<" + RDFS.SUBPROPERTYOF.getIri() +
                ">|<" + SNOMED.REPLACED_BY.getIri() + ">)*";

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT DISTINCT ?s ?sname ?scode ?g ?gname")
                .add("WHERE {")
                .add("  ?s " + isa + " ?o .")
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
                LOG.debug(String.format("Finished (%d rows)", result.size()));
            }
        }

        return result;
    }


    @Override
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
                LOG.debug(String.format("Finished (%d rows)", result.size()));
            }
        }

        return result;
    }

    @Override
    public boolean hasChildren(String parentIri, List<String> schemeIris, boolean inactive) throws DALException {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?c")
                .add("WHERE {")
                .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf) ?p .")
                .add("GRAPH ?g { ?c rdfs:label ?name } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql
                    .add(valueList("g", schemeIris));
        }

        if (!inactive)
            sql
                    .add("  OPTIONAL { ?c im:status ?s}")
                    .add("  FILTER (?s != im:Inactive) .");

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
    public List<TTIriRef> findImmediateChildrenByIri(String parentIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?c ?cname")
                .add("WHERE {")
                .add("  ?c (rdfs:subClassOf | rdfs:subPropertyOf | im:isContainedIn|im:isChildOf) ?p .")
                .add("GRAPH ?g { ?c rdfs:label ?cname } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql
                    .add(valueList("g", schemeIris));
        }

        if (!inactive)
            sql
                    .add("  OPTIONAL { ?c im:status ?s}")
                    .add("  FILTER (?s != im:Inactive) .");

        sql.add("}");

        sql.add("ORDER BY ?cname");

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
    public List<TTIriRef> findImmediateParentsByIri(String childIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?p ?pname")
                .add("WHERE {")
                .add("  ?c (rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf) ?p .")
                .add("GRAPH ?g { ?p rdfs:label ?pname } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql
                    .add(valueList("g", schemeIris));
        }

        if (!inactive)
            sql
                    .add("  OPTIONAL { ?p im:status  ?s}")
                    .add("  FILTER (?s != im:Inactive) .");

        sql.add("}");

        if (rowNumber != null && pageSize != null) {
            sql
                    .add("LIMIT " + pageSize)
                    .add("OFFSET " + rowNumber);
        }

        sql.add("ORDER BY ?pname");

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
                LOG.debug(String.format("Finished (%d rows)", result.size()));
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
                LOG.debug("Finished ({} rows)", result.size());
            }
        }

        return result;
    }

    @Override
    public List<Namespace> findNamespaces() {
        synchronized (namespaceCache) {
            if (namespaceCache.isEmpty()) {
                Map<String, Namespace> result = new HashMap<>();
                try (RepositoryConnection conn = ConnectionManager.getConnection()) {
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
                try (RepositoryConnection conn = ConnectionManager.getConnection()) {
                    TupleQuery qry = conn.prepareTupleQuery(sql.toString());
                    try (TupleQueryResult rs = qry.evaluate()) {
                        while (rs.hasNext()) {
                            BindingSet bs = rs.next();
                            Namespace ns = result.get(bs.getValue("g").stringValue());
                            if (ns == null) {
                                result.put(bs.getValue("g").stringValue(), new Namespace(bs.getValue("g").stringValue(), "PFX" + (i++), bs.getValue("name").stringValue()));
                            } else {
                                ns.setName(bs.getValue("name").stringValue());
                            }
                        }
                    }
                }

                namespaceCache.addAll(result.values());
            }
            return namespaceCache;
        }
    }

    @Override
    public List<SimpleMap> getSubjectFromObjectPredicate(String objectIri, TTIriRef predicate) {
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

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
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

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());

            qry.setBinding("s", subject);
            if (excludePredicates != null && !excludePredicates.isEmpty()) {
                int i = 0;
                for (String predicate : excludePredicates) {
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

    @Override
    public List<SimpleMap> findSimpleMapsByIri(String iri, List<String> schemeIris) {
        List<SimpleMap> simpleMaps = new ArrayList<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(" SELECT ?o ?oname ?ocode ?osch WHERE{")
                .add(" ?s rdfs:subClassOf ?o .")
                .add(" ?o im:code ?ocode ; ")
                .add("    im:scheme ?osch . ")
                .add("GRAPH ?g { ?o rdfs:label ?oname } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql
                    .add(valueList("g", schemeIris));
        }
        sql.add("}");

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());

            qry.setBinding("s", iri(iri));

            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    simpleMaps.add(new SimpleMap(getString(bs, "o"), getString(bs, "oname"), getString(bs, "ocode"), getString(bs, "osch")));
                }
            }
        }
        return simpleMaps;
    }
}
