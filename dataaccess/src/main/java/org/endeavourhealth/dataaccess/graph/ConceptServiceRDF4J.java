package org.endeavourhealth.dataaccess.graph;

import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
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
import org.endeavourhealth.dataaccess.graph.tripletree.*;
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
import java.util.zip.DataFormatException;

import static org.eclipse.rdf4j.model.util.Values.*;
import static org.endeavourhealth.dataaccess.graph.PrefixedTupleQuery.prefixIri;
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.dataaccess.graph.PrefixedTupleQuery.prepare;

@Component
@Qualifier("ConceptServiceRDF4J")
public class ConceptServiceRDF4J implements IConceptService {
    private static final Logger LOG = LoggerFactory.getLogger(ConceptServiceRDF4J.class);

    private Repository db;
    private RepositoryType repositoryType;

    public ConceptServiceRDF4J() {
        String repositoryUrl = System.getenv("GRAPH_REPOSITORY_URL");
        String repositoryId = System.getenv("GRAPH_REPOSITORY_ID");
        repositoryType = RepositoryType.valueOf(System.getenv("GRAPH_REPOSITORY_TYPE"));

        db = new HTTPRepository(repositoryUrl, repositoryId);
    }

    protected ConceptServiceRDF4J(Repository repo) {
        db = repo;
    }

    @Override
    public Concept getConcept(String iri) {
        try (RepositoryConnection conn = db.getConnection()) {
            Model model = getDefinition(conn, iri);
            if (model.isEmpty()) {
            //   LOG.error("Unable to load concept [{}]\n", iri);
                return null;
            } else
                return toConcept(getFullIri(iri, conn.getNamespaces().stream().collect(Collectors.toSet())), model);
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
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
                getLuceneSparql(MatchType.FUZZY) +
                "    ?lucMatch rdfs:label ?label\n" +
                "      OPTIONAL { ?lucMatch :code ?code }\n" +
                "} limit 20 \n";

            PrefixedTupleQuery qry = prepare(conn, sql)
                .bind("lucTerm", literal(request.getTermFilter()));

            try (PrefixedTupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet row = rs.next();
                    result.add(new ConceptSummary()
                        .setIri(row.getValue("lucMatch").stringValue())
                        .setName(row.getValue("label").stringValue())
                        .setCode(row.getValue("code") != null ? row.getValue("code").stringValue() : null)
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

    protected Model getDefinition(RepositoryConnection conn, String iri) {
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

    private Concept toConcept(IRI iri, Model model) throws DataFormatException {
        Concept result;

        if (model.contains(iri, RDF.TYPE, IM.RECORD))
            result = new Concept(ConceptType.RECORD);
        else if (model.contains(iri, RDF.TYPE, IM.VALUESET))
            result = new Concept(ConceptType.VALUESET);
        else
            result = new Concept(ConceptType.CLASSONLY);

        result.setIri(getPrefixIri(iri.stringValue(), model.getNamespaces()));

        /*
        WriterConfig config = new WriterConfig();
        config.set(BasicWriterSettings.INLINE_BLANK_NODES, true);
        Rio.write(model, System.out, RDFFormat.TURTLE, config);

         */

        Iterable<Statement> items = model.getStatements(iri, null, null);
        for(Statement s : items) {
            Value p = s.getPredicate();
            Value o = s.getObject();
            if (RDF.TYPE.equals(p)) setConceptTypes(result,o.stringValue());
            else if (RDFS.LABEL.equals(p)) result.setName(o.stringValue());
            else if (RDFS.COMMENT.equals(p)) result.setDescription(o.stringValue());
            else if (IM.CODE.equals(p)) result.setCode(o.stringValue());
            else if (IM.HAS_SCHEME.equals(p)) result.setScheme(getConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            else if (IM.STATUS.equals(p)) result.setStatus(ConceptStatus.byName(o.stringValue()));
            else if (IM.SYNONYM.equals(p)) result.addSynonym(getTermCode(model, (Resource)o));
            else if (RDFS.SUBCLASSOF.equals(p)) result.addSubClassOf(getExpression(model, (Resource)o));
            else if (SNOMED.IS_A.equals(p)) result.addIsa(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            else if (SHACL.PROPERTY.equals(p)) result.addProperty(getPropertyValue(model, (Resource) o));
            else if (IM.HAS_MEMBERS.equals(p)) result.addMember(getExpression(model, (Resource)o));
            else if (OWL.EQUIVALENTCLASS.equals(p)){
                getEquivalentTo(result,model, (Resource) o);
            } else if (IM.ROLE_GROUP.equals(p)){
                result.addRoleGroup(getConceptRoleGroup(model,(Resource) o));
            }
            else if (OWL.ANNOTATION.equals(p)) result.addAnnotation(getAnnotation(model,(Resource)o));
            else if (o.isLiteral())
                System.err.println("Literal");

        }

        return result;
    }

    private Annotation getAnnotation(Model model, Resource s) {
        Annotation annotation= new Annotation();
        Iterable<Statement> items = model.getStatements(s, null, null);
        for (Statement item : items) {
            Value p = item.getPredicate();
            Value o = item.getObject();
            annotation.setProperty(new ConceptReference(getPrefixIri(p.stringValue(),model.getNamespaces())));
            annotation.setValue(o.stringValue());
        }
        return annotation;
    }

    private ConceptRoleGroup getConceptRoleGroup(Model model, Resource s) throws DataFormatException {
        ConceptRoleGroup result= new ConceptRoleGroup();
        Iterable<Statement> items = model.getStatements(s, null, null);
        for (Statement item : items) {
            Value p = item.getPredicate();
            Value o = item.getObject();
            ConceptRole role= new ConceptRole();
            result.addRole(role);
            role.setProperty(new ConceptReference(getPrefixIri(p.stringValue(),model.getNamespaces())));
            if (o.isIRI())
                role.setValueType(new ConceptReference(getPrefixIri(o.stringValue(),model.getNamespaces())));
            else
               if ((o.isLiteral())) {
                   role.setValueData(o.stringValue());
               } else
                throw new DataFormatException("nested role groups not supported ");


        }
        return result;

    }

    private void setConceptTypes(Concept concept,String iri) {
        if (iri.equals(OWL.CLASS.stringValue())) concept.setConceptType(ConceptType.CLASSONLY);
        else if (iri.equals(RDFS.DATATYPE.stringValue())) concept.setConceptType(ConceptType.DATATYPE);
        else if (iri.equals(OWL.OBJECTPROPERTY.stringValue())) concept.setConceptType(ConceptType.OBJECTPROPERTY);
        else if (iri.equals(IM.RECORD.stringValue())) concept.setConceptType(ConceptType.RECORD);
        else if (iri.equals(OWL.DATATYPEPROPERTY.stringValue())) concept.setConceptType(ConceptType.DATAPROPERTY);
        else if (iri.equals(OWL.ANNOTATIONPROPERTY.stringValue())) concept.setConceptType(ConceptType.ANNOTATION);
        else if (iri.equals(IM.LEGACY.stringValue())) concept.setConceptType(ConceptType.LEGACY);
        else if (iri.equals(IM.VALUESET.stringValue())) concept.setConceptType(ConceptType.VALUESET);
        else if (iri.equals((OWL.FUNCTIONALPROPERTY.stringValue()))) concept.setIsFunctional(new Axiom());
        else if (iri.equals((OWL.REFLEXIVEPROPERTY.stringValue()))) concept.setIsReflexive(new Axiom());
        else if (iri.equals((OWL.SYMMETRICPROPERTY.stringValue()))) concept.setIsSymmetric(new Axiom());
        else if (iri.equals((OWL.TRANSITIVEPROPERTY.stringValue()))) concept.setIsTransitive(new Axiom());

        else throw new IllegalStateException("Unknown concept type [" + iri + "]");
    }

    private void getEquivalentTo(Concept concept,Model model, Resource s) throws DataFormatException {
        //Maybe a class expression or a data type
        Iterable<Statement> items = model.getStatements(s, null, null);

        for (Statement item : items) {
            Value p = item.getPredicate();
            Value o = item.getObject();
            if (OWL.WITHRESTRICTIONS.equals(p) || OWL.ONDATATYPE.equals(p) || RDFS.DATATYPE.equals(o)) {
                concept.setDataTypeDefinition(getDataTypeDefinition(model, s));
                return;
            } else {
                concept.addEquivalentTo(getExpression(model,s));
            }
        }
    }

    private DataTypeDefinition getDataTypeDefinition(Model model, Resource s) throws DataFormatException {
        DataTypeDefinition dtd= new DataTypeDefinition();
        Iterable<Statement> items = model.getStatements(s, null, null);

        for (Statement item : items) {
            Value p = item.getPredicate();
            Value o = item.getObject();
            if (OWL.ONDATATYPE.equals(p))
                dtd.setDataType(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            if (OWL.WITHRESTRICTIONS.equals(p)){
                return getRestrictions(model,dtd,(Resource)o);
            }
        }
        return dtd;
    }

    private DataTypeDefinition getRestrictions(Model model, DataTypeDefinition dtd, Resource s) throws DataFormatException {
        Iterable<Statement> items = model.getStatements(s, null, null);

        for (Statement item : items) {
            Value p = item.getPredicate();
            Value o = item.getObject();
            if (IM.PATTERN.equals(p)) {
                dtd.setPattern(o.stringValue());
                return dtd;
            }
        }
        throw new DataFormatException("restrictions not yet supported");

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
                if (OWL.INTERSECTIONOF.equals(p))
                    result.addIntersection(getExpression(model, (Resource) o));
                else if (OWL.UNIONOF.equals(p))
                    result.addUnion(getExpression(model, (Resource) o));
                else if (OWL.ONPROPERTY.equals(p) || OWL.SOMEVALUESFROM.equals(p) || OWL.MINCARDINALITY.equals(p)
                    || OWL.MAXCARDINALITY.equals(p)) {
                    result.setPropertyValue(getPropertyValue(model, s));
                    return result;
                } else if (OWL.RESTRICTION.equals(o)) {
                    result.setPropertyValue(getPropertyValue(model, s));
                    return result;
                } else if (OWL.COMPLEMENTOF.equals(p)) {
                    result.setComplementOf(getExpression(model, (Resource) o));
                }
                else if (!RDF.TYPE.equals(p))
                 throw new IllegalStateException("Unknown expression type [" + p.stringValue() + "]");
            }

            return result;
        } else
            throw new IllegalStateException("Attempted getExpression on non iri/bnode");
    }

    private List<ConceptReference> getOneOfs(Model model, Resource o) {
        return null;
    }

    private PropertyValue getPropertyValue(Model model, Resource s) {
        PropertyValue result = new PropertyValue();

        Iterable<Statement> items = model.getStatements(s, null, null);

        for (Statement item : items) {
            Value p = item.getPredicate();
            Value o = item.getObject();
            if (OWL.ONPROPERTY.equals(p))
                result.setProperty(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
            if (OWL.CLASS.equals(p))
                if (o.isIRI())
                    result.setValueType(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
                else
                    result.setExpression(getExpression(model,(Resource) o));
            if (OWL.MINCARDINALITY.equals(p))
                result.setMin(Integer.parseInt(o.stringValue()));
            if (OWL.MAXCARDINALITY.equals(p))
                result.setMax(Integer.parseInt(o.stringValue()));
            if (OWL.SOMEVALUESFROM.equals(p))
                if (o.isIRI())
                    result.setValueType(new ConceptReference(getPrefixIri(o.stringValue(), model.getNamespaces())));
                else
                    result.setExpression(getExpression(model,(Resource) o));

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

    /***
     * Returns a lucene search query to be injected into Sparql based on repository type.
     * Term should be bound to "?lucTerm"
     * Adds the following to the SPARQL query:-
     * ?lucMatch - Matching subject (iri)
     * ?lucScore - Match score
     *
     * @param matchType Type of string matching to perform
     * @return SPARQL segment string
     */
    private String getLuceneSparql(MatchType matchType) {
        switch (this.repositoryType) {
            case GRAPHDB:
                return getLuceneSparqlGraphDb(matchType);
            default:
                throw new IllegalStateException("Unsupported lucene implementation");
        }
    }

    private String getLuceneSparqlGraphDb(MatchType matchType) {
        switch (matchType) {
            case EXACT:
                return "\t?lucMatch luc:myTestIndex ?lucTerm ;\n\tluc:score ?lucScore .\n";
            case STARTS:
                return "\tBIND(CONCAT(?lucTerm, \"*\") AS ?lucTermStarts) .\n\t?lucMatch luc:myTestIndex ?lucTermStarts ;\n\tluc:score ?lucScore .\n";
            case ENDS:
                return "\tBIND(CONCAT(\"*\", ?lucTerm) AS ?lucTermStarts) .\n\t?lucMatch luc:myTestIndex ?lucTermStarts ;\n\tluc:score ?lucScore .\n";
            case CONTAINS:
                return "\tBIND(CONCAT(\"*\", ?lucTerm, \"*\") AS ?lucTermStarts) .\n\t?lucMatch luc:myTestIndex ?lucTermStarts ;\n\tluc:score ?lucScore .\n";
            case FUZZY:
                return "\tBIND(CONCAT(?lucTerm, \"~\") AS ?lucTermStarts) .\n\t?lucMatch luc:myTestIndex ?lucTermStarts ;\n\tluc:score ?lucScore .\n";
            default:
                throw new IllegalStateException("Unsupported lucene match type");
        }
    }
    /**
     * Returns the fully expanded list of value set members from a value set definition
     * @param iri  a string representation in full iri form e.g. http//.....#VSET_category_operations
     * @return a Set of concept references
     */

    public Set<String> getValueSetExpansion(String iri) throws DataFormatException {
        Concept valueSet= getConcept(iri);
        Set<String> members= new HashSet<>();
        String queryText= getValueSetQueryText(valueSet);
        try (RepositoryConnection conn = db.getConnection()) {
            TupleQuery query = conn.prepareTupleQuery(queryText);
            TupleQueryResult result= query.evaluate();
            while (result.hasNext()){
                BindingSet set= result.next();
                members.add(set.getValue("concept").stringValue());
            }
        }catch (QueryEvaluationException e) {
            e.printStackTrace();

        }
        return members;
    }

    private String getValueSetQueryText(Concept valueSet) throws DataFormatException {
        int memberCount = 0;

        //Now build the query based on the value set concept
        StringBuilder query = new StringBuilder("");
        for (ClassExpression exp : valueSet.getMember()) {
            memberCount++;
            if (memberCount > 1)
                query.append("UNION {\n");
            else query.append("{\n");
            if (exp.getClazz() != null) {
                setSimpleEntailment(exp, query);
                query.append("}\n");
            } else if (exp.getIntersection() != null) {
                setRefinedEntailment(exp, query);
                query.append("}\n");
            }
        }
        StringBuilder queryText= new StringBuilder("");
        db.getConnection().getNamespaces().stream().forEach(ns-> queryText.append("PREFIX ")
        .append(ns.getPrefix()).append(": <").append(ns.getName()).append(">\n"));

        queryText.append("SELECT ?concept\n");
        queryText.append("WHERE {\n");
        queryText.append(query.toString());
        queryText.append("}");
        return queryText.toString();
    }

    private void setSimpleEntailment(ClassExpression exp,StringBuilder query) {
        query.append("  ?concept :isA ")
            .append(exp.getClazz().getIri())
            .append("\n");
    }

    private void setRefinedEntailment(ClassExpression exp,StringBuilder query) throws DataFormatException {
        Map<String, String> group = new HashMap<>();
        List<Map<String, String>> groups = new ArrayList<>();
        groups.add(group);

        for (ClassExpression inter : exp.getIntersection()) {
            if (inter.getClazz() != null) {
                query.append("   ?concept <").append(IM.IS_A).append("> ")
                    .append(inter.getClazz().getIri())
                    .append(". \n");
            } else if (inter.getPropertyValue() != null) {
                group.put(inter.getPropertyValue().getProperty().getIri(),
                    inter.getPropertyValue().getValueType().getIri());
            } else if (inter.getComplementOf()!=null){
                ClassExpression negation= inter.getComplementOf();
                if (negation.getClazz()!=null)
                    query.append("FILTER NOT EXISTS { ?concept <").append(IM.IS_A).append("> ")
                        .append(negation.getClazz().getIri()).append("}\n");
                else
                    throw new DataFormatException("Complex negation not supported in this version");
            }
        }
        if (!groups.isEmpty()){
            int groupNumber = -1;
            int attNumber = -1;
            for (Map<String,String> roleGroup:groups){
                if (roleGroup.size()>0) {
                    groupNumber++;
                    query.append("   ?concept <").append(IM.ROLE_GROUP).append("> ")
                        .append("?group_").append(groupNumber).append(".\n");
                    for (String att:roleGroup.keySet()){
                        attNumber++;
                        query.append("?group_").append(groupNumber).append(" ");
                        query.append("?att_").append(attNumber).append(" ");
                        query.append("?val_").append(attNumber).append(". \n");
                        query.append("?att_").append(attNumber).append(" <")
                            .append(IM.IS_A).append("> ").append(att).append(". \n");
                        query.append("?val_").append(attNumber).append(" <")
                            .append(IM.IS_A).append("> ")
                            .append(roleGroup.get(att)).append(". \n");
                    }
                }
            }
        }
    }

    // TODO: OWL.SOMEVALUESFROM ??? - Single object vs Single element array?
    private List<IRI> arrayPredicates = Arrays.asList(OWL.EQUIVALENTCLASS, OWL.INTERSECTIONOF);

    public TTConcept getTTConcept(String conceptIri) {
        try (RepositoryConnection conn = db.getConnection()) {
            IRI iri = getFullIri(conceptIri, conn.getNamespaces().stream().collect(Collectors.toSet()));

            TTConcept result = new TTConcept(iri.stringValue());

            Set<Namespace> namespaces = conn.getNamespaces().stream().collect(Collectors.toSet());
            namespaces.forEach(ns -> result.addPrefix(ns.getName(), ns.getPrefix()));

            Iterable<Statement> items = conn.getStatements(iri, null, null);
            for (Statement item : items) {
                IRI p = item.getPredicate();
                Value v = item.getObject();

                setPredicateValueOnNode(conn, result, p, v);
            }
            return result;
        }
    }

    private void populateTTNode(RepositoryConnection conn, TTNode result, Resource resource) {
        Iterable<Statement> items = conn.getStatements(resource, null, null);

        for(Statement item : items) {
            IRI p = item.getPredicate();
            Value v = item.getObject();

            setPredicateValueOnNode(conn, result, p, v);
        }
    }

    private void setPredicateValueOnNode(RepositoryConnection conn, TTNode result, IRI p, Value v) {
        TTNode target = result;

        if(arrayPredicates.contains(p)) {
            addToArray(conn, result, p, v);
        } else {
            setNodeValue(conn, p, v, target);
        }
    }

    private void setNodeValue(RepositoryConnection conn, IRI p, Value v, TTNode target) {
        if (v.isIRI())
            target.set(p, (IRI) v);
        else if (v.isLiteral())
            target.set(p, (Literal) v);
        else if (v.isBNode()) {
            TTNode bnode = new TTNode();
            populateTTNode(conn, bnode, (BNode) v);
            target.set(p, bnode);
        } else {
            System.err.println("Unhandled node value type");
        }
    }

    private void addToArray(RepositoryConnection conn, TTNode result, IRI p, Value v) {
        TTArray array = result.getAsArray(p);
        if (array == null) {
            array = new TTArray();
            result.set(p, array);
        }
        if (v.isIRI())
            array.add((IRI) v);
        else if (v.isLiteral())
            array.add((Literal) v);
        else if (v.isBNode()) {
            TTNode bnode = new TTNode();
            array.add(bnode);
            populateTTNode(conn, bnode, (BNode) v);
        } else
            System.err.println("Unhandled array member");
    }

    public void saveTTConcept(TTConcept concept) {
        // TODO: Upsert!!!

        Model m = new TreeModel();

        for(TTPrefix prefix : concept.getPrefixes())
            m.setNamespace(prefix.getPrefix(), prefix.getIri());

        IRI iri = iri(concept.getIri());

        addTTNodeToModel(m, iri, concept);

        try (RepositoryConnection conn = db.getConnection()) {
            for(TTPrefix prefix : concept.getPrefixes())
                conn.setNamespace(prefix.getPrefix(), prefix.getIri());
            conn.add(m);
        }
    }

    private void addTTNodeToModel(Model m, Resource r, TTNode n) {
        for (Map.Entry<IRI, TTValue> entry : n.getPredicateMap().entrySet()) {
            addTTValueToModel(m, r, entry.getKey(), entry.getValue());
        }
    }

    private void addTTValueToModel(Model m, Resource r, IRI p, TTValue v) {
        if (v.isIriRef()) {
            m.add(r, p, iri(v.asIriRef().getIri()));
        } else if (v.isLiteral()) {
            m.add(r, p, v.asLiteral().getValue());
        } else if (v.isNode()) {
            BNode bNode = bnode();
            m.add(r, p, bNode);
            addTTNodeToModel(m, bNode, v.asNode());
        } else if (v.isList()) {
            for (TTValue item : v.asArray().getElements()) {
                addTTValueToModel(m, r, p, item);
            }
        } else {
            System.err.println("Unhandled TTValue type");
        }
    }
}
