package org.endeavourhealth.dataaccess.graph;

import org.endeavourhealth.dataaccess.IConceptService;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.Namespace;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.*;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.WriterConfig;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.endeavourhealth.imapi.model.*;
import org.endeavourhealth.imapi.model.search.SearchRequest;
import org.endeavourhealth.imapi.model.search.ConceptSummary;
import org.endeavourhealth.imapi.model.valuset.ExportValueSet;
import org.endeavourhealth.imapi.model.valuset.ValueSetMember;
import org.endeavourhealth.imapi.model.valuset.ValueSetMembership;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.dataaccess.graph.PrefixedTupleQuery.prefixIri;
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.dataaccess.graph.PrefixedTupleQuery.prepare;

@Component
@Qualifier("ConceptServiceRDF4J")
public class ConceptServiceRDF4J implements IConceptService {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptServiceRDF4J.class);

    private Repository db;

    public ConceptServiceRDF4J() {
        db = new HTTPRepository("http://localhost:7200/", "InformationModel");
    }

    @Override
    public Concept getConcept(String iri) {
        try (RepositoryConnection conn = db.getConnection()) {
            Model model = getDefinition(conn, iri);
            if (model.isEmpty()) {
                LOG.error("Unable to load concept [{}]\n", iri);
                return null;
            } else
                return toConcept(getFullIri(iri, conn.getNamespaces().stream().collect(Collectors.toSet())), model);
        }
    }

    @Override
    public ConceptReference getConceptReference(String iri) {
        try (RepositoryConnection conn = db.getConnection()) {
            String sql = "SELECT ?n\n" +
                "WHERE { ?subj rdfs:label ?n }";

            PrefixedTupleQuery qry = prepare(conn, sql)
                .bind("subj", prefixIri(iri));

            try (PrefixedTupleQueryResult matches = qry.evaluate()) {
                if (matches.hasNext()) {
                    BindingSet bs = matches.next();
                    return new ConceptReference(iri, bs.getValue("n").stringValue());
                } else
                    return null;
            }
        }
    }

    @Override
    public List<ConceptReferenceNode> getImmediateChildren(String iri, Integer page, Integer size, boolean includeLegacy) {
        List<ConceptReferenceNode> result = new ArrayList<>();
        try (RepositoryConnection conn = db.getConnection()) {
            String qry = "SELECT ?s ?n ?e\n" +
                "WHERE {\n" +
                "   ?s sn:116680003 ?parent;" +
                "   rdfs:label ?n.\n" +
                "   BIND(EXISTS {?c sn:116680003 ?s . } AS ?e)\n" +
                "}\n" +
                "ORDER BY ?n";

            PrefixedTupleQuery query = prepare(conn, qry)
                .bind("parent", prefixIri(iri));

            try (PrefixedTupleQueryResult matches = query.evaluate()) {
                for (BindingSet bs : matches) {
                    result.add(new ConceptReferenceNode(
                            bs.getValue("s").stringValue(),
                            bs.getValue("n").stringValue())
                            .setHasChildren("true".equals(bs.getValue("e").stringValue()))
                    );
                }
            }
        }

        return result;
    }

    @Override
    public List<ConceptReferenceNode> getImmediateParents(String iri, Integer page, Integer size, boolean includeLegacy) {
        List<ConceptReferenceNode> result = new ArrayList<>();
        try (RepositoryConnection conn = db.getConnection()) {
            String qry = "SELECT ?s ?n ?e\n" +
                "WHERE {\n" +
                "   ?child sn:116680003 ?s." +
                "   ?s rdfs:label ?n.\n" +
                "   BIND(EXISTS {?s sn:116680003 ?p . } AS ?e)\n" +
                "}\n" +
                "ORDER BY ?n";

            PrefixedTupleQuery query = prepare(conn, qry)
                .bind("child", prefixIri(iri));

            try (PrefixedTupleQueryResult matches = query.evaluate()) {
                for (BindingSet bs : matches) {
                    result.add(new ConceptReferenceNode(
                        bs.getValue("s").stringValue(),
                        bs.getValue("n").stringValue())
                        .setHasChildren("true".equals(bs.getValue("e").stringValue()))
                    );
                }
            }
        }

        return result;
    }

    @Override
    public List<ConceptReferenceNode> getParentHierarchy(String iri) {
        List<ConceptReferenceNode> result = new ArrayList<>();
        try (RepositoryConnection conn = db.getConnection()) {
            String qry = "SELECT ?s ?n\n" +
                "WHERE {?child sn:116680003 ?s;" +
                "   rdfs:label ?n.\n" +
                "}";

            PrefixedTupleQuery query = prepare(conn, qry)
                .bind("child", prefixIri(iri));

            try (PrefixedTupleQueryResult matches = query.evaluate()) {
                for (BindingSet bs : matches) {
                    result.add(new ConceptReferenceNode(
                        bs.getValue("s").stringValue(),
                        bs.getValue("n").stringValue()
                    ));
                }
            }
        }

        return result;
    }

    @Override
    public List<ConceptReference> isWhichType(String iri, List<String> candidates) {
        List<ConceptReference> result = new ArrayList<>();
        try (RepositoryConnection conn = db.getConnection()) {
            String qry = "SELECT ?o ?n\n" +
                "WHERE { \n" +
                "    ?s sn:116680003 + ?o .\n" +
                "    ?o rdfs:label ?n ;\n" +
                "    FILTER (?o IN (";

            for (int i = 0; i < candidates.size(); i++) {
                if (i > 0) qry += ", ";
                qry += "?f" + i;
            }
            qry += "))\n}";

            PrefixedTupleQuery query = prepare(conn, qry)
                .bind("s", prefixIri(iri));

            for (int i = 0; i < candidates.size(); i++) {
                query.bind("f" + i, prefixIri(candidates.get(i)));
            }

            try (PrefixedTupleQueryResult matches = query.evaluate()) {
                for (BindingSet bs : matches) {
                    result.add(new ConceptReference(
                        bs.getValue("o").stringValue(),
                        bs.getValue("n").stringValue()
                    ));
                }
            }
        }
        return result;
    }

    @Override
    public List<ConceptSummary> usages(String iri) {
        return null;
    }

    @Override
    public List<ConceptSummary> advancedSearch(SearchRequest request) {
        List<ConceptSummary> result = new ArrayList<>();

        try (RepositoryConnection conn = db.getConnection()) {
            String sql = "SELECT * \n" +
                "WHERE { \n" +
                "\t?iri rdfs:label ?label\n" +
                "      OPTIONAL { ?iri :code ?code }\n" +
                "    filter (\n" +
                "        contains(?label, ?term) || \n" +
                "        contains(?iri, ?term) ||\n" +
                "        contains(?code, ?term)\n" +
                "    )\n" +
                "} limit 20 \n";

            PrefixedTupleQuery qry = prepare(conn, sql)
                .bind("term", literal(request.getTermFilter()));

            try (PrefixedTupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet row = rs.next();
                    result.add(new ConceptSummary()
                        .setIri(row.getValue("iri").stringValue())
                        .setName(row.getValue("label").stringValue())
                        .setCode(row.getValue("code").stringValue())
                    );
                }
            }
        }

        return result;
    }

    @Override
    public List<Concept> getAncestorDefinitions(String iri) {
        return null;
    }

    @Override
    public ExportValueSet getValueSetMembers(String iri, boolean expand) {
        ExportValueSet result = new ExportValueSet();

        try (RepositoryConnection conn = db.getConnection()) {
            String qry = expand
                ? "SELECT ?u ?n ?c ?cs ?csn WHERE { \n" +
                "    ?s rdfs:subClassOf ?o .\n" +
                "    ?o owl:intersectionOf ?i .\n" +
                "    ?i :hasMembers ?m .\n" +
                "    ?m owl:unionOf ?u .\n" +
                "    ?u rdfs:label ?n .\n" +
                "    ?u :code ?c .\n" +
                "    ?u :has_scheme ?cs .\n" +
                "    ?cs rdfs:label ?csn .\n" +
                "}"
                : "SELECT ?u ?n ?c ?cs ?csn WHERE { \n" +
                "\t?s rdfs:subClassOf ?o .\n" +
                "    ?o owl:intersectionOf ?i .\n" +
                "    ?i :hasMembers ?m .\n" +
                "    ?m owl:unionOf ?t .\n" +
                "    ?u (sn:116680003)* ?t .\n" +
                "    ?u rdfs:label ?n .\n" +
                "    ?u :code ?c .\n" +
                "    ?u :has_scheme ?cs .\n" +
                "    ?cs rdfs:label ?csn .\n" +
                "}";

            PrefixedTupleQuery query = prepare(conn, qry)
                .bind("s", prefixIri(iri));

            try (PrefixedTupleQueryResult matches = query.evaluate()) {
                for (BindingSet bs : matches) {
                    result.addIncluded(new ValueSetMember()
                        .setConcept(new ConceptReference(bs.getValue("u").stringValue(), bs.getValue("n").stringValue()))
                        .setCode(bs.getValue("c").stringValue())
                        .setScheme(new ConceptReference(bs.getValue("cs").stringValue(), bs.getValue("csn").stringValue())
                        ));
                }
            }
        }

        return result;
    }

    @Override
    public ValueSetMembership isValuesetMember(String valueSetIri, String memberIri) {
        return new ValueSetMembership();
    }

    @Override
    public List<ConceptReference> getCoreMappedFromLegacy(String legacyIri) {
        return null;
    }

    @Override
    public List<ConceptReference> getLegacyMappedToCore(String coreIri) {
        return null;
    }

    // ================================================ PRIVATE METHODS ================================================

    private IRI getFullIri(String prefixedIri, Collection<Namespace> namespaces) {
        // Check if already full Iri
        if (prefixedIri.startsWith("http://") || prefixedIri.startsWith("https://"))
            return iri(prefixedIri);

        String[] parts = prefixedIri.split(":");

        Optional<Namespace> namespace = namespaces.stream().filter(ns -> ns.getPrefix().equals(parts[0])).findFirst();

        if (!namespace.isPresent())
            throw new IllegalStateException("Unknown namespace prefix [" + parts[0] + "]");

        return iri(namespace.get().getName() + parts[1]);
    }

    private String getPrefixIri(String fullIri, Collection<Namespace> namespaces) {
        // Check if full iri
        if (!fullIri.startsWith("http://") && !fullIri.startsWith("https://"))
            return fullIri;

        Optional<Namespace> namespace = namespaces.stream().filter(ns -> fullIri.startsWith(ns.getName())).findFirst();

        if (!namespace.isPresent())
            throw new IllegalStateException("Unknown namespace for [" + fullIri + "]");

        return fullIri.replace(namespace.get().getName(), namespace.get().getPrefix() + ":");
    }

    private Model getDefinition(RepositoryConnection conn, String iri) {
        Model result = new TreeModel();

        Set<Namespace> namespaces = conn.getNamespaces().stream().collect(Collectors.toSet());
        namespaces.forEach(result::setNamespace);

        addResourceStatementsToModel(conn, getFullIri(iri, namespaces), result);

        return result;
    }

    private void addResourceStatementsToModel(RepositoryConnection conn, Resource resource, Model m) {
        RepositoryResult<Statement> statements = conn.getStatements(resource, null, null);
        for(Statement s : statements) {
            m.add(s);
            Value o = s.getObject();
            if (o.isBNode()) {
                addResourceStatementsToModel(conn, (Resource)s.getObject(), m);
            }
        }
    }

    private Concept toConcept(IRI iri, Model model) {
        Concept result;

        if (model.contains(iri, RDF.TYPE, IM.RECORD))
            result = new Concept(ConceptType.RECORD);
        else if (model.contains(iri, RDF.TYPE, IM.VALUESET))
            result = new Concept(ConceptType.VALUESET);
        else
            result = new Concept(ConceptType.CLASSONLY);

        result.setIri(getPrefixIri(iri.stringValue(), model.getNamespaces()));


        WriterConfig config = new WriterConfig();
        config.set(BasicWriterSettings.INLINE_BLANK_NODES, true);
        Rio.write(model, System.out, RDFFormat.TURTLE, config);


        Iterable<Statement> items = model.getStatements(iri, null, null);

        for(Statement s : items) {
            Value p = s.getPredicate();
            Value o = s.getObject();
            if (RDF.TYPE.equals(p)) result.setConceptType(fromIri(o.stringValue()));
            else if (RDFS.LABEL.equals(p)) result.setName(o.stringValue());
            else if (RDFS.COMMENT.equals(p)) result.setDescription(o.stringValue());
            else if (IM.CODE.equals(p)) result.setCode(o.stringValue());
            else if (IM.HAS_SCHEME.equals(p)) result.setScheme(getConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            else if (IM.STATUS.equals(p)) result.setStatus(ConceptStatus.byName(o.stringValue()));
            else if (IM.SYNONYM.equals(p)) result.addSynonym(getTermCode(model, (Resource)o));
            else if (RDFS.SUBCLASSOF.equals(p)) result.addSubClassOf(getExpression(model, (Resource)o));
            else if (SNOMED.IS_A.equals(p)) result.addIsa(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            else if (SHACL.PROPERTY.equals(p)) result.addProperty(getProperty(model, (Resource) o));
            else if (IM.HAS_MEMBERS.equals(p)) result.addMember(getExpression(model, (Resource)o));
            else throw new IllegalStateException("Unknown concept property [" + p.stringValue() + "]");
        }

        return result;
    }

    private ConceptType fromIri(String iri) {
        if (iri.equals(OWL.CLASS.stringValue())) return ConceptType.CLASSONLY;
        else if (iri.equals(RDFS.DATATYPE.stringValue())) return ConceptType.DATATYPE;
        else if (iri.equals(OWL.OBJECTPROPERTY.stringValue())) return ConceptType.OBJECTPROPERTY;
        else if (iri.equals(IM.RECORD.stringValue())) return ConceptType.RECORD;
        else if (iri.equals(IM.VALUESET.stringValue())) return ConceptType.VALUESET;
        else throw new IllegalStateException("Unknown concept type [" + iri + "]");
    }

    private ClassExpression getExpression(Model model, Resource s) {
        ClassExpression result = new ClassExpression();

        if (s.isIRI())
            return result.setClazz(getPrefixIri(s.stringValue(), model.getNamespaces()));
        else if (s.isBNode()) {
            Iterable<Statement> items = model.getStatements(s, null, null);

            for (Statement item : items) {
                Value p = item.getPredicate();
                Value o = item.getObject();
                if (OWL.INTERSECTIONOF.equals(p)) result.addIntersection(getExpression(model, (Resource) o));
                else throw new IllegalStateException("Unknown expression type [" + p.stringValue() + "]");
            }

            return result;
        } else
            throw new IllegalStateException("Attempted getExpression on non iri/bnode");
    }

    private PropertyValue getProperty(Model model, Resource r) {
        PropertyValue result = new PropertyValue();

        Iterable<Statement> items = model.getStatements(r, null, null);

        for (Statement s : items) {
            Value p = s.getPredicate();
            Value o = s.getObject();

            if (SHACL.CLASS.equals(p)) result.setProperty(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            else if (SHACL.MIN_COUNT.equals(p)) result.setMin(((Literal)o).intValue());
            else if (SHACL.MAX_COUNT.equals(p)) result.setMax(((Literal)o).intValue());
            else if (SHACL.PATH.equals(p)) result.setValueType(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            else throw new IllegalStateException("Unknown property type [" + p.toString() + "]");
        }

        return result;
    }

    private TermCode getTermCode(Model model, Resource r) {
        TermCode result = new TermCode();

        Iterable<Statement> items = model.getStatements(r, null, null);

        for (Statement s : items) {
            Value p = s.getPredicate();
            Value o = s.getObject();

            if (IM.CODE.equals(p)) result.setCode(o.stringValue());
            else if (RDFS.LABEL.equals(p)) result.setTerm(o.stringValue());
            else throw new IllegalStateException("Unknown term code property type [" + p.toString() + "]");
        }


        return result;
    }
}
