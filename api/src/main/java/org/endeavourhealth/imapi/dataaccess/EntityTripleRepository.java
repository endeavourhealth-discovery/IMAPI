package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;

import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Namespace;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.module.FindException;
import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.*;

import static org.endeavourhealth.imapi.dataaccess.EntityRepository.PARENT_PREDICATES;

public class EntityTripleRepository {
    private static final Logger LOG = LoggerFactory.getLogger(EntityTripleRepository.class);
    private static final List<Namespace> namespaceCache = new ArrayList<>();
    private static final String EXECUTING = "Executing...";
    private static final String RETRIEVING = "Retrieving...";

    private final EntityRepository2 entityRepository2 = new EntityRepository2();
    private final EntityRepository entityRepository = new EntityRepository();

    private final Map<String, Integer> bnodes = new HashMap<>();
    private int row = 0;

    public TTBundle getEntityPredicates(String iri, Set<String> predicates) {
        return entityRepository2.getBundle(iri, predicates);
    }

    public List<TTIriRef> getConceptUsages(String objectIri, Integer rowNumber, Integer pageSize) {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("PREFIX shacl: <http://www.w3.org/ns/shacl#>")
                .add("SELECT DISTINCT ?s ?name WHERE {")
                .add("    { ?s ?p ?o . }")
                .add("    UNION")
                .add("    { ?s shacl:property ?prop .")
                .add("        ?prop shacl:path ?propIri .")
                .add("        FILTER(?propIri = ?o) }")
                .add("    ?s rdfs:label ?name .")
                .add("    ?s im:status ?status .")
                .add("    FILTER (?p != rdfs:subclassOf && ?status != im:Inactive)")
                .add("}");

        if (rowNumber != null && pageSize != null) {
            sql.add("LIMIT " + pageSize + " OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(objectIri));
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

    public Integer getConceptUsagesCount(String objectIri) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("PREFIX shacl: <http://www.w3.org/ns/shacl#>")
                .add("SELECT (COUNT(DISTINCT ?s) AS ?cnt) WHERE {")
                .add("    { ?s ?p ?o . }")
                .add("    UNION")
                .add("    { ?s shacl:property ?prop .")
                .add("        ?prop shacl:path ?propIri .")
                .add("        FILTER(?propIri = ?o) }")
                .add("    ?s im:status ?status .")
                .add("    FILTER (?p != rdfs:subclassOf && ?status != im:Inactive && ?s != ?o)")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("o", iri(objectIri));
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

    public List<EntityReferenceNode> getEntityReferenceNodes(Set<String> stringIris, List<String> schemeIris, boolean inactive) {
        List<EntityReferenceNode> nodes = new ArrayList<>();
        StringJoiner iriLine = new StringJoiner(" ");
        for (String stringIri : stringIris) {
            iri(stringIri);
            iriLine.add("<" + stringIri + ">");
        }
        Map<String, Set<TTIriRef>> iriToTypesMap = entityRepository.getTypesByIris(stringIris);

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?s ?name ?typeIri ?typeName ?order ?hasChildren ?hasGrandchildren")
                .add("WHERE {")
                .add("  GRAPH ?g { ?s rdfs:label ?name } .")
                .add("  VALUES ?s { " + iriLine + " }")
                .add("  OPTIONAL { ?s sh:order ?order . }")
                .add("    BIND(EXISTS{?child (" + PARENT_PREDICATES + ") ?s} AS ?hasChildren)")
                .add("    BIND(EXISTS{?grandChild (" + PARENT_PREDICATES + ") ?child. ?child (" + PARENT_PREDICATES + ") ?s} AS ?hasGrandchildren)");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?s im:status ?status FILTER (?status != im:Inactive) }");
        }

        sql.add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    String iri = bs.getValue("s").stringValue();
                    EntityReferenceNode result = new EntityReferenceNode(iri).setType(new TTArray());
                    Set<TTIriRef> types = iriToTypesMap.get(iri);
                    for(TTIriRef type: types) {
                        result.getType().add(type);
                    }
                    if (bs.hasBinding("order"))
                        result.setOrderNumber(((Literal) bs.getValue("order")).intValue());
                    else
                        result.setOrderNumber(Integer.MAX_VALUE);

                    result.setHasChildren(((Literal) bs.getValue("hasChildren")).booleanValue())
                            .setHasGrandChildren(((Literal) bs.getValue("hasGrandchildren")).booleanValue())
                            .setName(bs.getValue("name").stringValue());
                    nodes.add(result);
                }
            }
        }

        return nodes;
    }

    public EntityReferenceNode getEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive) {
        TTArray types = new TTArray();
        EntityReferenceNode result = new EntityReferenceNode(iri).setType(types);

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?name ?typeIri ?typeName ?order ?hasChildren ?hasGrandchildren")
                .add("WHERE {")
                .add("  GRAPH ?g { ?s rdfs:label ?name } .")
                .add("  OPTIONAL { ?s sh:order ?order . }")
                .add("  OPTIONAL { ?s rdf:type ?typeIri . OPTIONAL { ?typeIri rdfs:label ?typeName . } }")
                .add("    BIND(EXISTS{?child (" + PARENT_PREDICATES + ") ?s} AS ?hasChildren)")
                .add("    BIND(EXISTS{?grandChild (" + PARENT_PREDICATES + ") ?child. ?child (" + PARENT_PREDICATES + ") ?s} AS ?hasGrandchildren)");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }

        if (!inactive) {
            sql.add("  OPTIONAL { ?s im:status ?status FILTER (?status != im:Inactive) }");
        }

        sql.add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();

                    if (bs.hasBinding("order"))
                        result.setOrderNumber(((Literal) bs.getValue("order")).intValue());
                    else
                        result.setOrderNumber(Integer.MAX_VALUE);

                    result.setHasChildren(((Literal) bs.getValue("hasChildren")).booleanValue())
                            .setHasGrandChildren(((Literal) bs.getValue("hasGrandchildren")).booleanValue())
                            .setName(bs.getValue("name").stringValue());

                    if (bs.getValue("typeIri") != null && bs.getValue("typeName") != null) types.add(new TTIriRef(bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()));

                    while (rs.hasNext()) {
                        bs = rs.next();
                        types.add(new TTIriRef(bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()));
                    }
                }
                else throw new IllegalArgumentException("Iri does not exist");
            }
        }

        return result;
    }

    public Pageable<TTIriRef> findImmediateChildrenPagedByIriWithTotalCount(String parentIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> children = new ArrayList<>();
        Pageable<TTIriRef> result = new Pageable<>();

        StringJoiner sqlCount = new StringJoiner(System.lineSeparator())
                .add("SELECT (COUNT(?c) as ?count)")
                .add("WHERE {")
                .add("  ?c (rdfs:subClassOf | rdfs:subPropertyOf | im:isContainedIn | im:isChildOf | im:inTask | im:isSubsetOf) ?p .");
        if (!inactive) {
            sqlCount.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }
        sqlCount.add("}");

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?c ?cname ")
                .add("WHERE {")
                .add("  ?c (rdfs:subClassOf | rdfs:subPropertyOf | im:isContainedIn | im:isChildOf | im:inTask | im:isSubsetOf) ?p .");
        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add("GRAPH ?g { ?c rdfs:label ?cname } .").add(valueList("g", schemeIris));
        } else {
            sql.add("?c rdfs:label ?cname .");
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

            TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
            qryCount.setBinding("p", iri(parentIri));
            try (TupleQueryResult rsCount = qryCount.evaluate()) {
                BindingSet bsCount = rsCount.next();
                result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
            }

            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("p", iri(parentIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                result.setPageSize(pageSize);
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
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

        StringJoiner sqlCount = new StringJoiner(System.lineSeparator())
                .add("SELECT (COUNT(?p) as ?count)")
                .add("WHERE {")
                .add("  ?c ?pr ?p .");
        if (!inactive) {
            sqlCount.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }
        sqlCount.add("}");

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?p ?pname ")
                .add("WHERE {")
                .add("  ?c ?pr ?p .")
                .add("?p rdfs:label ?pname .");
        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }
        if (!inactive) {
            sql.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .");
        }
        sql.add("}");
        sql.add("ORDER BY ?pname");
        if (rowNumber != null && pageSize != null) {
            sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {

            TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
            qryCount.setBinding("c", iri(parentIri));
            qryCount.setBinding("pr", iri(predicateIri));
            try (TupleQueryResult rsCount = qryCount.evaluate()) {
                BindingSet bsCount = rsCount.next();
                result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
            }

            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("c", iri(parentIri));
            qry.setBinding("pr", iri(predicateIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                result.setPageSize(pageSize);
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
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
                .add("SELECT DISTINCT ?p ?pname")
                .add("WHERE {")
                .add("  ?c (" + PARENT_PREDICATES + ") ?p .")
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
            namespaceCache.sort(Comparator.comparing(Namespace::getName));
            return namespaceCache;
        }
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

    public List<SimpleMap> getMatchedFrom(String iri, List<String> schemeIris) {
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

    public List<SimpleMap> getMatchedTo(String iri, List<String> schemeIris) {
        List<SimpleMap> simpleMaps = new ArrayList<>();
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(" SELECT ?o ?code ?scheme ?name  WHERE{")
                .add(" ?s im:matchedTo ?o .")
                .add(" ?o im:code ?code .")
                .add(" ?o im:scheme ?scheme .  ")
                .add("GRAPH ?g { ?o rdfs:label ?name } .");

        if (schemeIris != null && !schemeIris.isEmpty()) {
            sql.add(valueList("g", schemeIris));
        }
        sql.add("}");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    simpleMaps.add(new SimpleMap(getString(bs, "o"), getString(bs, "name"), getString(bs, "code"), getString(bs, "scheme")));
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
                .add("  ?c (" + PARENT_PREDICATES + ") ?p .")
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

    public TTIriRef getShapeFromType(String iri) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT ?shape")
                .add("WHERE {")
                .add("  ?shape im:targetShape ?iri .")
                .add("}")
                .add("LIMIT 1");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("iri", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()){
                    BindingSet bs = rs.next();
                    return new TTIriRef(bs.getValue("shape").stringValue());
                } else throw new FindException("No shape found for type: " + iri);
            }
        }
    }

    public Pageable<TTIriRef> getSuperiorPropertiesByConceptPagedWithTotalCount(String conceptIri, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> properties = new ArrayList<>();
        Pageable<TTIriRef> result = new Pageable<>();

        StringJoiner sqlCount = new StringJoiner(System.lineSeparator())
                .add("SELECT (COUNT(?a1) as ?count)")
                .add("WHERE {")
                .add("?concept im:isA ?p .")
                .add("?a1 rdfs:domain ?p .")
                .add("FILTER NOT EXISTS {")
                .add("?a2 rdfs:domain ?p .")
                .add("?a1 im:isA ?a2 .")
                .add("FILTER(?a1 != ?a2)")
                .add("}");
        if (!inactive) {
            sqlCount.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
        }
        sqlCount.add("}");

        StringJoiner stringQuery = new StringJoiner(System.lineSeparator())
                .add("SELECT ?a1 ?attributeName")
                .add("WHERE {")
                .add("?concept im:isA ?p .")
                .add("?p rdfs:label ?parentName .")
                .add("?a1 rdfs:domain ?p ;")
                .add("rdfs:label ?attributeName .")
                .add("FILTER NOT EXISTS {")
                .add("?a2 rdfs:domain ?p .")
                .add("?a1 im:isA ?a2 .")
                .add("FILTER(?a1 != ?a2)")
                .add("}");
        if (!inactive) {
            stringQuery.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
        }
        stringQuery.add("}");
        stringQuery.add("ORDER BY ?attributeName");

        if (rowNumber != null && pageSize != null) {
            stringQuery.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        try(RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
            qryCount.setBinding("concept", iri(conceptIri));
            try(TupleQueryResult rsCount = qryCount.evaluate()) {
                BindingSet bsCount = rsCount.next();
                result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
            }

            TupleQuery qry = prepareSparql(conn,stringQuery.toString());
            qry.setBinding("concept", iri(conceptIri));
            try(TupleQueryResult rs = qry.evaluate()) {
                result.setPageSize(pageSize);
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    properties.add(new TTIriRef(bs.getValue("a1").stringValue(),bs.getValue("attributeName").stringValue()));
                }
                result.setResult(properties);
            }
        }
        return result;
    }

    public Pageable<TTIriRef> getSuperiorPropertiesByConceptBoolFocusPagedWithTotalCount(List<String> conceptIris, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> properties = new ArrayList<>();
        Pageable<TTIriRef> result = new Pageable<>();

        StringJoiner sqlCount = new StringJoiner(System.lineSeparator())
                .add("SELECT (COUNT(DISTINCT ?a1) as ?count)")
                .add("WHERE {");
        if (conceptIris != null && !conceptIris.isEmpty()) {
            sqlCount.add(valueList("concept", conceptIris));
        }
        sqlCount.add("?concept im:isA ?p .")
                .add("?a1 rdfs:domain ?p .")
                .add("FILTER NOT EXISTS {")
                .add("?a2 rdfs:domain ?p .")
                .add("?a1 im:isA ?a2 .")
                .add("FILTER(?a1 != ?a2)")
                .add("}");
        if (!inactive) {
            sqlCount.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
        }
        sqlCount.add("}");

        StringJoiner stringQuery = new StringJoiner(System.lineSeparator())
                .add("SELECT DISTINCT ?a1 ?attributeName")
                .add("WHERE {");
        if (conceptIris != null && !conceptIris.isEmpty()) {
            stringQuery.add(valueList("concept", conceptIris));
        }
        stringQuery.add("?concept im:isA ?p .")
                .add("?p rdfs:label ?parentName .")
                .add("?a1 rdfs:domain ?p ;")
                .add("rdfs:label ?attributeName .")
                .add("FILTER NOT EXISTS {")
                .add("?a2 rdfs:domain ?p .")
                .add("?a1 im:isA ?a2 .")
                .add("FILTER(?a1 != ?a2)")
                .add("}");
        if (!inactive) {
            stringQuery.add("OPTIONAL {?a1 im:status ?a1s}").add("FILTER(?a1s != im:Inactive) .");
        }
        stringQuery.add("}");
        stringQuery.add("ORDER BY ?attributeName");

        if (rowNumber != null && pageSize != null) {
            stringQuery.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        try(RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
            try(TupleQueryResult rsCount = qryCount.evaluate()) {
                BindingSet bsCount = rsCount.next();
                result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
            }

            TupleQuery qry = prepareSparql(conn,stringQuery.toString());
            try(TupleQueryResult rs = qry.evaluate()) {
                result.setPageSize(pageSize);
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    properties.add(new TTIriRef(bs.getValue("a1").stringValue(),bs.getValue("attributeName").stringValue()));
                }
                result.setResult(properties);
            }
        }
        return result;
    }

    public Pageable<TTIriRef> getSuperiorPropertyValuesByPropertyPagedWithTotalCount(String propertyIri, Integer rowNumber, Integer pageSize, boolean inactive) {
        List<TTIriRef> values = new ArrayList<>();
        Pageable<TTIriRef> result = new Pageable<>();

        StringJoiner sqlCount = new StringJoiner(System.lineSeparator())
                .add("SELECT (COUNT(?value) as ?count)")
                .add("WHERE {")
                .add("?property rdfs:range ?value .");
        if (!inactive) {
            sqlCount.add("OPTIONAL {?value im:status ?vs}").add("FILTER(?vs != im:Inactive) .");
        }
        sqlCount.add("}");

        StringJoiner stringQuery = new StringJoiner(System.lineSeparator())
                .add("SELECT ?value ?valueName")
                .add("WHERE {")
                .add("?property rdfs:range ?value .")
                .add("?value rdfs:label ?valueName .");
        if (!inactive) {
            stringQuery.add("OPTIONAL {?value im:status ?vs}").add("FILTER(?vs != im:Inactive) .");
        }
        stringQuery.add("}");
        stringQuery.add("ORDER BY ?valueName");

        if (rowNumber != null && pageSize != null) {
            stringQuery.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
        }

        try(RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qryCount = prepareSparql(conn, sqlCount.toString());
            qryCount.setBinding("property", iri(propertyIri));
            try(TupleQueryResult rsCount = qryCount.evaluate()) {
                BindingSet bsCount = rsCount.next();
                result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
            }

            TupleQuery qry = prepareSparql(conn,stringQuery.toString());
            qry.setBinding("property", iri(propertyIri));
            try(TupleQueryResult rs = qry.evaluate()) {
                result.setPageSize(pageSize);
                while(rs.hasNext()) {
                    BindingSet bs = rs.next();
                    values.add(new TTIriRef(bs.getValue("value").stringValue(),bs.getValue("valueName").stringValue()));
                }
                result.setResult(values);
            }
        }
        return result;
    }

    public Boolean hasPredicates(String subjectIri, Set<String> predicateIris) {
        try(RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            StringJoiner stringQuery = new StringJoiner(System.lineSeparator())
                    .add("ASK {");
            for (String predicateIri: predicateIris) {
                stringQuery.add("?subjectIri <" + iri(predicateIri) + "> ?o .");
            }
            stringQuery.add("}");

            BooleanQuery sparql = conn.prepareBooleanQuery(String.valueOf(stringQuery));
            sparql.setBinding("subjectIri", iri(subjectIri));
            return sparql.evaluate();
        }
    }
}
