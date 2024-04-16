package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EntityRepository2 {
    private static final Logger LOG = LoggerFactory.getLogger(EntityRepository2.class);

    private String IM_PREFIX = "PREFIX im: <" + IM.NAMESPACE + ">";
    private String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.NAMESPACE + ">";
    private String RDF_PREFIX = "PREFIX rdf: <" + RDF.NAMESPACE + ">";
    private String SH_PREFIX = "PREFIX sh: <" + SHACL.NAMESPACE + ">";
    private String SN_PREFIX = "PREFIX sn: <" + SNOMED.NAMESPACE + ">";


    /**
     * An alternative method of getting an entity definition assuming all predicates inclided
     *
     * @param iri of the entity
     * @return a bundle including the entity and the predicate names
     */
    public TTBundle getBundle(String iri) {
        return getBundle(iri, null, false);
    }

    /**
     * An alternative method of getting an entity definition
     *
     * @param iri        of the entity
     * @param predicates List of predicates to `include`
     * @return bundle with entity and map of predicate names
     */
    public TTBundle getBundle(String iri, Set<String> predicates) {
        return getBundle(iri, predicates, false);
    }


    /**
     * An alternative method of getting an entity definition
     *
     * @param iri               of the entity
     * @param predicates        List of predicates
     * @param excludePredicates Flag denoting if predicate list is inclusion or exclusion
     * @return
     */
    public TTBundle getBundle(
            String iri,
            Set<String> predicates,
            boolean excludePredicates
    ) {
        return getBundle(iri, predicates, excludePredicates, 5);
    }

    public TTBundle getBundle(
            String iri,
            Set<String> predicates,
            boolean excludePredicates,
            int depth
    ) {
        TTBundle bundle = new TTBundle()
                .setEntity(new TTEntity().setIri(iri))
                .setPredicates(new HashMap<>());

        StringJoiner sql = getBundleSparql(predicates, excludePredicates, depth);

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            GraphQuery qry = conn.prepareGraphQuery(sql.toString());
            qry.setBinding("entity", Values.iri(iri));
            try (GraphQueryResult gs = qry.evaluate()) {
                Map<String, TTValue> valueMap = new HashMap<>();
                for (org.eclipse.rdf4j.model.Statement st : gs) {
                    processStatement(bundle, valueMap, iri, st);
                }
                Set<TTIriRef> iris = TTManager.getIrisFromNode(bundle.getEntity());
                getIriNames(conn, iris);
                setNames(bundle.getEntity(), iris);
                iris.forEach(bundle::addPredicate);
            }
            return bundle;
        }
    }

    private void setNames(TTValue subject, Set<TTIriRef> iris) {
        HashMap<String, String> names = new HashMap<>();
        iris.forEach(i -> names.put(i.getIri(), i.getName()));
        if (subject.isIriRef())
            subject.asIriRef().setName(names.get(subject.asIriRef().getIri()));
        else if (subject.isNode() && subject.asNode().getPredicateMap() != null) {
            for (Map.Entry<TTIriRef, TTArray> entry : subject.asNode().getPredicateMap().entrySet()) {
                for (TTValue value : entry.getValue().getElements()) {
                    if (value.isIriRef())
                        value.asIriRef().setName(names.get(value.asIriRef().getIri()));
                    else if (value.isNode())
                        setNames(value, iris);
                }
            }

        }
    }

    public void getNames(Set<TTIriRef> iris) {
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            EntityRepository2.getIriNames(conn, iris);
        }
    }

    public Map<String,String> getNameMap(Set<TTIriRef> iris) {
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            Map<String,String> nameMap= new HashMap<>();
            getNames(iris);
            for (TTIriRef iri:iris){
                nameMap.put(iri.getIri(),iri.getName());
            }
            return nameMap;
        }
    }

    /**
     * creates ranges for properties without ranges where the super properties have them
     */
    public void inheritRanges(RepositoryConnection conn) {
        StringJoiner sql = new StringJoiner("\n")
          .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
            "insert {?property rdfs:range ?range}" +
            "where {\n" +
            "    {\n" +
            "   Select ?property ?range\n" +
            "   where {\n" +
            "    ?property rdf:type rdf:Property.\n" +
            "    #filter (?property= <http://snomed.info/sct#10362801000001104> )\n" +
            "    ?property (rdfs:subClassOf)* ?superclass.\n" +
            "    filter not exists { \n" +
            "        ?property (rdfs:subClassOf) [\n" +
            "            (rdfs:subClassOf)+ ?superclass]}\n" +
            "            filter not exists {?property rdfs:range ?anyrange}\n" +
            "    ?superclass rdfs:range ?range. \n" +
            "    ?range rdfs:label ?rlabel.\n" +
            "   }}}");
        if (conn != null) {
            Update upd = conn.prepareUpdate(sql.toString());
            upd.execute();
        }
        else {
            try (RepositoryConnection conn2= ConnectionManager.getIMConnection()) {
                Update upd = conn2.prepareUpdate(sql.toString());
                upd.execute();
            }
        }
    }


    /**
     * Returns an entity iri and name from a code or a term code
     *
     * @param code the code or description id or term code
     * @return iri and name of entity
     */
    public Set<TTIriRef> getCoreFromCode(String code, List<String> schemes) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(RDFS_PREFIX)
                .add("select ?concept ?label");
        for (String scheme : schemes) {
            sql.add("from <" + scheme + ">");
        }
        sql.add("where {  {")
                .add(" ?concept im:code ?code.")
                .add("    filter (isIri(?concept))")
                .add(" ?concept rdfs:label ?label.}")
                .add("  UNION{?concept im:hasTermCode ?node.")
                .add("        ?node im:code ?code.")
                .add("          filter not exists { ?concept im:matchedTo ?core}")
                .add("        ?concept rdfs:label ?label}")
                .add("  UNION {?legacy im:hasTermCode ?node.")
                .add("         ?node im:code ?code.")
                .add("          ?legacy im:matchedTo ?concept.")
                .add("         ?concept rdfs:label ?label.}")
                .add("   UNION {?legacy im:codeId ?code.")
                .add("          ?legacy im:matchedTo ?concept.")
                .add("          ?concept rdfs:label ?label.}")
                .add("}");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("code", Values.literal(code));
            return getConceptRefsFromResult(qry);
        }
    }

    /**
     * Returns an entity iri and name from a code or a term code
     *
     * @param codeId the code or description id or term code
     * @return iri and name of entity
     */
    public Set<TTIriRef> getCoreFromCodeId(String codeId, List<String> schemes) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(RDFS_PREFIX)
                .add("select ?concept ?label");
        for (String scheme : schemes) {
            sql.add("from <" + scheme + ">");
        }
        sql.add("where {  ")
                .add(" ?legacy im:codeId ?codeId.")
                .add(" ?legacy im:matchedTo ?concept.")
                .add(" ?concept rdfs:label ?label.}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("codeId", Values.literal(codeId));
            return getConceptRefsFromResult(qry);
        }
    }

    /**
     * Returns a core entity iri and name from a legacy term
     *
     * @param term   the code or description id or term code
     * @param scheme the legacy scheme of the term
     * @return iri and name of entity
     */
    public Set<TTIriRef> getCoreFromLegacyTerm(String term, String scheme) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(RDFS_PREFIX)
                .add("select ?concept ?label")
                .add("where { graph ?scheme {")
                .add("?legacy rdfs:label ?term.")
                .add("?legacy im:matchedTo ?concept.}")
                .add("{?concept rdfs:label ?label} }");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("term", Values.literal(term));
            qry.setBinding("scheme", Values.iri(scheme));
            return getConceptRefsFromResult(qry);
        }
    }

    /**
     * Returns an entity iri and name from a term code code
     *
     * @param code   the code that is a term code
     * @param scheme the scheme of the term
     * @return set of iris and name of entity
     */

    public Set<TTIriRef> getReferenceFromTermCode(String code, String scheme) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(RDFS_PREFIX)
                .add("select ?concept ?label")
                .add("where { graph ?scheme {")
                .add("?tc im:code ?code.")
                .add("?concept im:hasTermCode ?tc.}")
                .add("{?concept rdfs:label ?label} }");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("code", Values.literal(code));
            qry.setBinding("scheme", Values.iri(scheme));
            return getConceptRefsFromResult(qry);
        }
    }

    public Map<String,String> getCodeToIri() {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
          .add(IM_PREFIX)
          .add(RDFS_PREFIX)
          .add("select ?code ?scheme ?iri ?altCode")
          .add("where {")
          .add("?iri im:code ?code.")
          .add("OPTIONAL {?iri im:alternativeCode ?altCode}")
          .add("?iri im:scheme ?scheme }");
        Map<String,String> codeToIri= new HashMap<>();
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            try (TupleQueryResult gs = qry.evaluate()) {
                while (gs.hasNext()) {
                    BindingSet bs = gs.next();
                    String code=bs.getValue("code").stringValue();
                    String scheme= bs.getValue("scheme").stringValue();
                    String iri= bs.getValue("iri").stringValue();
                    codeToIri.put(scheme+code,iri);
                    if (bs.getValue("altCode")!=null) {
                        codeToIri.put(scheme+bs.getValue("altCode").stringValue(),iri);
                    }
                }
            }

        }
        return codeToIri;
    }

    /**
     * Returns A core entity iri and name from a core term
     *
     * @param term the code or description id or term code
     * @return iri and name of entity
     */
    public TTIriRef getReferenceFromCoreTerm(String term) {
        List<String> schemes = List.of(IM.NAMESPACE, SNOMED.NAMESPACE);
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(RDFS_PREFIX)
                .add("select ?concept ?label");
        for (String scheme : schemes) {
            sql.add("from <" + scheme + ">");
        }
        sql.add("where { {")
                .add("?concept rdfs:label ?term.")
                .add("filter(isIri(?concept))}")
                .add("union { ?concept im:hasTermCode ?tc.")
                .add("?tc rdfs:label ?term.} }");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("term", Values.literal(term));
            return getConceptRefFromResult(qry);
        }
    }

    public Map<String, Set<String>> getAllMatchedLegacy() {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(RDFS_PREFIX)
                .add(RDF_PREFIX)
                .add("select ?legacy ?concept")
                .add("where {?legacy im:matchedTo ?concept.}");
        Map<String, Set<String>> maps = new HashMap<>();
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            TTIriRef concept = new TTIriRef();
            try (TupleQueryResult gs = qry.evaluate()) {
                while (gs.hasNext()) {
                    BindingSet bs = gs.next();
                    String legacy = bs.getValue("legacy").stringValue();
                    maps.putIfAbsent(legacy, new HashSet<>());
                    maps.get(legacy).add(bs.getValue("concept").stringValue());
                    if (bs.getValue("label") != null)
                        concept.setName(bs.getValue("label").stringValue());

                }
            }
        }
        return maps;
    }



    private TTIriRef getConceptRefFromResult(TupleQuery qry) {
        TTIriRef concept = null;
        try (TupleQueryResult gs = qry.evaluate()) {
            while (gs.hasNext()) {
                BindingSet bs = gs.next();
                concept = TTIriRef.iri(bs.getValue("concept").stringValue());
                if (bs.getValue("label") != null)
                    concept.setName(bs.getValue("label").stringValue());

            }
        }
        return concept;
    }

    private Set<TTIriRef> getConceptRefsFromResult(TupleQuery qry) {
        Set<TTIriRef> results = null;
        try (TupleQueryResult gs = qry.evaluate()) {
            while (gs.hasNext()) {
                BindingSet bs = gs.next();
                if (results == null)
                    results = new HashSet<>();
                TTIriRef concept = TTIriRef.iri(bs.getValue("concept").stringValue());
                if (bs.getValue("label") != null)
                    concept.setName(bs.getValue("label").stringValue());
                results.add(concept);

            }
        }
        return results;
    }


    private StringJoiner getBundleSparql(
            Set<String> predicates,
            boolean excludePredicates,
            int depth
    ) {
        StringJoiner sql = new StringJoiner(System.lineSeparator());
        sql.add(RDFS_PREFIX);
        sql.add("CONSTRUCT {")
                .add("  ?entity ?1predicate ?1Level.")
                .add("  ?1Level rdfs:label ?1Name.");
        for (int i = 1; i < depth; i++) {
            sql.add("  ?" + i + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
                    .add("  ?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName.")
                    .add("  ?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name.");
        }
        sql.add("} WHERE { {");

        sql.add("  ?entity ?1predicate ?1Level.")
                .add("  ?1predicate rdfs:label ?1pName.");
        if (predicates != null && !predicates.isEmpty()) {
            StringBuilder inPredicates = new StringBuilder();
            int i = 0;
            for (String pred : predicates) {
                inPredicates.append(i > 0 ? "," : "").append("<").append(pred).append("> ");
                i++;
            }
            if (excludePredicates)
                sql.add("   FILTER (?1predicate NOT IN (" + inPredicates + "))");
            else
                sql.add("   FILTER (?1predicate IN (" + inPredicates + "))");
            sql.add("}");
            if (!excludePredicates) {
                sql.add("UNION {");
                sql.add("  ?1predicate owl:inverseOf ?1revPredicate .");
                sql.add("  ?1Level ?1revPredicate ?entity");
                sql.add("   FILTER (?1predicate IN (" + inPredicates + "))");
                sql.add("}");
            }

        } else sql.add("}");

        sql.add("  OPTIONAL {?1Level rdfs:label ?1Name.")
                .add("    FILTER (!isBlank(?1Level))}");
        for (int i = 1; i < depth; i++) {
            sql.add("  OPTIONAL {?" + (i) + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
                    .add("    FILTER (isBlank(?" + i + "Level))")
                    .add("  OPTIONAL {?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName}")
                    .add("  OPTIONAL {?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name")
                    .add("    FILTER (!isBlank(?" + (i + 1) + "Level))}");
        }
        sql.add(String.join("", Collections.nCopies(depth, "}")));
        return sql;
    }

    private void processStatement(TTBundle bundle, Map<String, TTValue> tripleMap, String entityIri, Statement st) throws RuntimeException {
        TTEntity entity = bundle.getEntity();
        Resource s = st.getSubject();
        IRI p = st.getPredicate();
        Value o = st.getObject();
        String subject = s.stringValue();
        String predicate = p.stringValue();
        String value = o.stringValue();
        Map<String, String> predNames = bundle.getPredicates();
        if (tripleMap.get(predicate) != null) {
            if (tripleMap.get(predicate).asIriRef().getName() != null) {
                predNames.put(predicate, tripleMap.get(predicate).asIriRef().getName());
            } else
                predNames.put(predicate, predicate);
        } else {
            tripleMap.putIfAbsent(predicate, iri(predicate));
            predNames.put(predicate, predicate);
        }
        TTNode node;
        if (predicate.equals(RDFS.LABEL)) {
            if (s.isIRI()) {
                if (subject.equals(entityIri)) {
                    entity.setName(value);
                } else {
                    tripleMap.putIfAbsent(subject, iri(subject));
                    tripleMap.get(subject).asIriRef().setName(value);
                    predNames.computeIfPresent(subject, (k, v) -> value);
                }
            } else {
                tripleMap.putIfAbsent(subject, new TTNode());
                tripleMap.get(subject).asNode().set(iri(RDFS.LABEL), TTLiteral.literal(value, ((Literal) o).getDatatype().stringValue()));
            }
        } else {
            if (s.isIRI()) {
                node = entity;
            } else {
                tripleMap.putIfAbsent(subject, new TTNode());
                node = tripleMap.get(subject).asNode();
            }
            if (o.isBNode()) {
                tripleMap.putIfAbsent(value, new TTNode());
                node.addObject(tripleMap.get(predicate).asIriRef(), tripleMap.get(value));
            } else if (o.isIRI()) {
                tripleMap.putIfAbsent(value, iri(value));
                node.addObject(tripleMap.get(predicate).asIriRef(), tripleMap.get(value));
            } else {
                tripleMap.putIfAbsent(value, TTLiteral.literal(value, ((Literal) o).getDatatype().stringValue()));
                node.set(tripleMap.get(predicate).asIriRef(), tripleMap.get(value).asLiteral());
            }
        }
    }


    /**
     * Generates sparql from a concept set entity
     *
     * @param definition    definition of set
     * @param includeLegacy whether or not legacy codes should be included
     * @return A string of SPARQL
     */
    public String getExpansionAsGraph(TTArray definition, boolean includeLegacy) {
        Map<String, String> prefixMap = new HashMap<>();
        StringJoiner spql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(RDFS_PREFIX)
                .add("CONSTRUCT {?concept rdfs:label ?name.")
                .add("?concept im:code ?code.")
                .add("?concept im:scheme ?legacyScheme")
                .add("?concept im:schemeName ?schemeName.");
        if (includeLegacy) {
            spql.add("?legacy rdfs:label ?legacyName.")
                    .add("?legacy im:code ?legacyCode.")
                    .add("?legacy im:scheme ?legacyScheme")
                    .add("?legacy im:legacySchemeName ?legacySchemeName.");
        }
        spql.add("}");
        spql.add("WHERE {");
        addNames(includeLegacy, spql, prefixMap);
        spql.add("{SELECT distinct ?concept");
        whereClause(definition, spql, prefixMap);
        spql.add("}");
        spql = insertPrefixes(spql, prefixMap);
        return spql.toString();
    }



    private void whereClause(TTArray definition, StringJoiner spql, Map<String, String> prefixMap) {
        spql.add("WHERE {");
        graphWherePattern(definition, spql, prefixMap);
        spql.add("}");
    }

    private void graphWherePattern(TTArray definition, StringJoiner spql, Map<String, String> prefixMap) {
        for (TTValue clause : definition.getElements()) {
            if (clause.isIriRef()) {
                simpleSuperClass(clause.asIriRef(), spql, prefixMap);
            } else {
                if (clause.asNode().get(iri(SHACL.OR)) != null) {
                    orClause(clause.asNode().get(iri(SHACL.OR)), spql, prefixMap);

                }
                if (clause.asNode().get(iri(SHACL.AND)) != null) {
                    Boolean hasRoles = andClause(clause.asNode().get(iri(SHACL.AND)), true, spql, prefixMap);
                    if (Boolean.TRUE.equals(hasRoles)) {
                        andClause(clause.asNode().get(iri(SHACL.AND)), false, spql, prefixMap);
                    }
                }
                if (clause.asNode().get(iri(SHACL.NOT)) != null) {
                    notClause(clause.asNode().get(iri(SHACL.NOT)), spql, prefixMap);
                }
            }
        }
    }

    private void orClause(TTArray ors, StringJoiner spql, Map<String, String> prefixMap) {
        spql.add("{");
        StringBuilder values = new StringBuilder();
        for (TTValue superClass : ors.getElements()) {
            if (superClass.isIriRef())
                values.append(getShort(superClass.asIriRef().getIri(), prefixMap)).append(" ");
        }
        if (!values.toString().equals("")) {
            spql.add("{ ?concept " + isa(prefixMap) + " ?superClass.");
            values = new StringBuilder("VALUES ?superClass {" + values + "}");
            spql.add(values.toString());
            spql.add("}");
        }
        for (TTValue complexClass : ors.getElements()) {
            if (complexClass.isNode()) {
                addUnion(complexClass.asNode(), spql, prefixMap);
            }
        }
        spql.add("}");
    }

    private void addNames(boolean includeLegacy, StringJoiner spql, Map<String, String> prefixMap) {
        spql.add("GRAPH ?scheme {?concept " + getShort(RDFS.LABEL, "rdfs", prefixMap) + " ?name.")
                .add("?concept im:code ?code")
                .add(" OPTIONAL {?concept im:im1Id ?im1Id}");
        spql.add(" OPTIONAL {?scheme rdfs:label ?schemeName}}");
        if (includeLegacy) {
            spql.add("OPTIONAL {GRAPH ?legacyScheme {")
                    .add("?legacy im:matchedTo ?concept.")
                    .add("OPTIONAL {?legacy rdfs:label ?legacyName.}")
                    .add("?legacy im:code ?legacyCode.")
                    .add("OPTIONAL {?legacy im:im1Id ?legacyIm1Id}")
                    .add("OPTIONAL {?legacyScheme rdfs:label ?legacySchemeName}}}");
        }
    }

    private void simpleSuperClass(TTIriRef superClass, StringJoiner spql, Map<String, String> prefixMap) {
        spql.add("?concept " + isa(prefixMap) + " " + getShort(superClass.asIriRef().getIri(), prefixMap) + ".");
    }

    private void addUnion(TTNode union, StringJoiner spql, Map<String, String> prefixMap) {
        if (union.get(iri(SHACL.AND)) != null) {
            spql.add("UNION {");
            Boolean hasRoles = andClause(union.get(iri(SHACL.AND)), true, spql, prefixMap);
            spql.add("}");
            if (Boolean.TRUE.equals(hasRoles)) {
                spql.add("UNION {");
                andClause(union.get(iri(SHACL.AND)), false, spql, prefixMap);
                spql.add("}");
            }
        } else {
            spql.add("UNION {");
            roles(union, true, spql, prefixMap); //adds a set of roles from a group.
            spql.add("}");
            spql.add("UNION {");
            roles(union, false, spql, prefixMap);
            spql.add("}");
        }
    }

    private void roles(TTNode node, boolean group, StringJoiner spql, Map<String, String> prefixMap) {
        int count = 1;
        for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
            count++;
            String obj = "?o_" + count;
            String pred = "?p_" + count;
            spql.add(obj + " " + isa(prefixMap) + " " + getShort(entry.getValue().asIriRef().getIri(), prefixMap) + ".");
            spql.add(pred + " " + isa(prefixMap) + " " + getShort(entry.getKey().getIri(), prefixMap) + ".");
            if (group) {
                spql.add("?roleGroup " + pred + " " + obj + ".");
                spql.add(" FILTER (isBlank(?roleGroup))");
                spql.add("?superMember " + getShort(IM.ROLE_GROUP, "im", prefixMap) + " ?roleGroup.");
            } else {
                spql.add("?superMember " + pred + " " + obj + ".");
                spql.add("  FILTER (isIri(?superMember))");
            }
        }
        spql.add("?concept " + getShort(IM.IS_A, "im", prefixMap) + " ?superMember.");
    }


    private String getShort(String iri, Map<String, String> prefixMap) {
        if (iri.contains("#")) {
            String prefix = iri.substring(0, iri.lastIndexOf("#"));
            prefix = prefix.substring(prefix.lastIndexOf("/") + 1);
            prefixMap.put(iri.substring(0, iri.lastIndexOf("#") + 1), prefix);
            return prefix + ":" + iri.substring(iri.lastIndexOf("#") + 1);
        }
        return "<" + iri + ">";
    }

    private String getShort(String iri, String prefix, Map<String, String> prefixMap) {
        if (iri.contains("#")) {
            prefixMap.put(iri.substring(0, iri.lastIndexOf("#") + 1), prefix);
            return prefix + ":" + iri.substring(iri.lastIndexOf("#") + 1);
        }
        else
            return "<"+ iri+">";
    }

    private Boolean andClause(TTArray and, boolean group, StringJoiner spql, Map<String, String> prefixMap) {
        boolean hasRoles = false;
        for (TTValue inter : and.getElements()) {
            if (inter.isNode() && inter.asNode().get(iri(SHACL.NOT)) == null) {
                roles(inter.asNode(), group, spql, prefixMap);
                hasRoles = true;
            }
        }
        for (TTValue inter : and.getElements()) {
            if (inter.isIriRef()) {
                simpleSuperClass(inter.asIriRef(), spql, prefixMap);
            }
        }
        for (TTValue inter : and.getElements()) {
            if (inter.isNode() && inter.asNode().get(iri(SHACL.NOT)) != null)
                notClause(inter.asNode().get(iri(SHACL.NOT)), spql, prefixMap);
        }
        return hasRoles;
    }

    private String isa(Map<String, String> prefixMap) {
        return getShort(IM.IS_A, prefixMap);
    }

    private void notClause(TTArray notClause, StringJoiner spql, Map<String, String> prefixMap) {
        spql.add("MINUS {");
        if (notClause.size() > 1) {
            spql.add("{ ?concept " + isa(prefixMap) + " ?notClass.");
            StringBuilder values = new StringBuilder();
            for (TTValue superClass : notClause.getElements()) {
                if (superClass.isIriRef())
                    values.append(getShort(superClass.asIriRef().getIri(), prefixMap)).append(" ");
            }
            spql.add("VALUES ?notClass {" + values + "}}");

        } else {
            for (TTValue not : notClause.getElements()) {
                if (not.isIriRef())
                    simpleSuperClass(not.asIriRef(), spql, prefixMap);
                else if (not.isNode()) {
                    if (not.asNode().get(iri(SHACL.OR)) != null) {
                        orClause(not.asNode().get(iri(SHACL.OR)), spql, prefixMap);
                    } else if (not.asNode().get(iri(SHACL.AND)) != null) {
                        Boolean hasRoles = andClause(not.asNode().get(iri(SHACL.AND)), true, spql, prefixMap);
                        if (Boolean.TRUE.equals(hasRoles)) {
                            andClause(not.asNode().get(iri(SHACL.AND)), false, spql, prefixMap);
                        }
                    }
                }
            }
        }
        spql.add("}");
    }

    private StringJoiner insertPrefixes(StringJoiner spql, Map<String, String> prefixMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
            sb.append("PREFIX ")
                    .append(entry.getValue())
                    .append(": <")
                    .append(entry.getKey())
                    .append(">\n");
        }
        String spqlString = spql.toString();
        return new StringJoiner(System.lineSeparator())
                .add(sb.toString())
                .add(spqlString);
    }

    public boolean isSet(String iri) {
        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(RDF_PREFIX)
                .add("SELECT * WHERE {")
                .add("?s rdf:type ?o .")
                .add("}");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", Values.iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return bs.getValue("o").stringValue().equals(IM.CONCEPT_SET) || bs.getValue("o").stringValue().equals(IM.VALUESET);
                }
            }
        }

        return false;
    }

    public List<String> getMemberIris(String iri) {
        List<String> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add(SH_PREFIX)
                .add("SELECT ?o2 WHERE {")
                .add("?s im:definition ?o .")
                .add("?o (sh:or|sh:and) ?o2 .")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", Values.iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    String iriValue = bs.getValue("o2").stringValue();
                    try {
                        Values.iri(iriValue);
                        result.add(iriValue);
                    } catch (IllegalArgumentException ignored) {
                        //Do nothing
                    }
                }
            }
        }

        return result;
    }


    public Set<TTIriRef> getIsSubsetOf(String subsetIri) {
        Set<TTIriRef> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add(IM_PREFIX)
                .add("SELECT ?set ?name WHERE {")
                .add("?subset ?issubset ?set .")
                .add("?set ?label ?name .")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("subset", Values.iri(subsetIri));
            qry.setBinding("issubset", Values.iri(IM.IS_SUBSET_OF));
            qry.setBinding("label", Values.iri(RDFS.LABEL));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    String setIri = bs.getValue("set").stringValue();
                    String setName = bs.getValue("name").stringValue();
                    try {
                        result.add(new TTIriRef(setIri, setName));
                    } catch (IllegalArgumentException ignored) {
                        LOG.warn("Invalid subset iri [{}] for set [{}]", subsetIri, setIri);
                    }
                }
            }
        }

        return result;
    }



    public static Map<String, String> getIriNames(RepositoryConnection conn, Set<TTIriRef> iris) {
        Map<String, String> names = new HashMap<>();
        if (iris == null || iris.isEmpty())
            return names;

        String iriTokens = iris.stream().map(i -> "<" + i.getIri() + ">").collect(Collectors.joining(","));
        StringJoiner sql = new StringJoiner(System.lineSeparator());
        sql.add("SELECT ?iri ?label ?description")
                .add("WHERE {")
                .add("?iri rdfs:label ?label.");
        sql.add("Optional { ?iri rdfs:comment ?description }");
        sql.add(" filter (?iri in (")
                .add(iriTokens + "))")
                .add("}");
        TupleQuery qry = conn.prepareTupleQuery(sql.toString());
        try (TupleQueryResult rs = qry.evaluate()) {
            while (rs.hasNext()) {
                BindingSet bs = rs.next();
                TTIriRef iri = iri(bs.getValue("iri").stringValue());
                iris.stream().filter(i -> i.equals(iri))
                        .findFirst().ifPresent(i -> {i.setName(bs.getValue("label").stringValue());
                                         if (bs.getValue("description")!=null) i.setDescription(bs.getValue("description").stringValue());});
            }
        }
        return names;
    }

    public Set<TTEntity> getLinkedShapes(String iri) {
        String sql = getLinkedShapeSql();
        Set<TTEntity> shapes = new HashSet<>();
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            GraphQuery qry = conn.prepareGraphQuery(sql);
            qry.setBinding("shape", Values.iri(iri));
            try (GraphQueryResult gs = qry.evaluate()) {
                Map<String, TTValue> valueMap = new HashMap<>();
                Map<String, TTNode> subjectMap = new HashMap<>();
                for (org.eclipse.rdf4j.model.Statement st : gs) {
                    processTripleLinkShape(shapes, valueMap, subjectMap, st);
                }
            }
            return shapes;
        }

    }

    private void processTripleLinkShape(Set<TTEntity> entities, Map<String, TTValue> valueMap, Map<String, TTNode> subjectMap, Statement st) {
        Resource subject = st.getSubject();
        TTIriRef predicate = iri(st.getPredicate().stringValue());
        Value object = st.getObject();
        TTNode node = subjectMap.get(subject.stringValue());
        if (node == null) {
            if (subject.isIRI()) {
                TTEntity entity = new TTEntity().setIri(subject.stringValue());
                subjectMap.put(subject.stringValue(), entity);
                entities.add(entity);
            } else
                subjectMap.put(subject.stringValue(), new TTNode());
        }
        node = subjectMap.get(subject.stringValue());
        if (object.isLiteral()) {
            Literal l = (Literal) object;
            node.set(predicate, TTLiteral.literal(l.stringValue(), l.getDatatype().stringValue()));
        } else if (object.isIRI()) {
            node.addObject(predicate, iri(object.stringValue()));
        } else {
            if (valueMap.get(object.stringValue()) == null) {
                if (subjectMap.get(object.stringValue()) != null)
                    valueMap.put(object.stringValue(), subjectMap.get(object.stringValue()));
                else {
                    valueMap.put(object.stringValue(), new TTNode());
                    subjectMap.put(object.stringValue(), valueMap.get(object.stringValue()).asNode());
                }
            }
            subjectMap.put(object.stringValue(), valueMap.get(object.stringValue()).asNode());
            node.addObject(predicate, valueMap.get(object.stringValue()).asNode());
        }
    }

    private String getLinkedShapeSql() {
        return new StringJoiner(System.lineSeparator())
                .add(RDF_PREFIX)
                .add(RDFS_PREFIX)
                .add(IM_PREFIX)
                .add(SH_PREFIX)
                .add("Construct {")
                .add("    ?s ?p ?o.")
                .add("    ?sub ?p2 ?o2.")
                .add("    ?o2 ?p3 ?o3.")
                .add("}")
                .add("where { ?s ?p ?o.")
                .add("    filter (?s= ?shape)")
                .add("    ?s (sh:property|sh:node)+ ?sub.")
                .add("    ?sub ?p2 ?o2.")
                .add("    Optional { ?o2 ?p3 ?o3")
                .add("        filter (isBlank(?o2))}")
                .add("}")
                .toString();
    }


    public List<TTIriRef> findUnassigned() {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner query = new StringJoiner(System.lineSeparator())
                .add(RDFS_PREFIX + "\n")
                .add(IM_PREFIX + "\n")
                .add("SELECT * WHERE {")
                .add("?s im:status im:Unassigned .")
                .add("?s rdfs:label ?name .")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(query.toString());
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
                }
            }
        }

        return result;
    }

    public List<TTIriRef> findUnclassified() {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner query = new StringJoiner(System.lineSeparator())
                .add(RDFS_PREFIX)
                .add(IM_PREFIX)
                .add(SN_PREFIX)
                .add("SELECT ?s ?name {")
                .add("GRAPH sn: {")
                .add("?s im:scheme sn: ;")
                .add("    rdfs:label ?name .")
                .add("}")
                .add("MINUS { ?s (sn:370124000|rdfs:subClassOf|im:isContainedIn|rdfs:subPropertyOf) ?o }")
                .add("} LIMIT 1000");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(query.toString());
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
                }
            }
        }

        return result;
    }

    public List<TTEntity> findUnmapped(List<String> status, List<String> scheme, List<String> type, Integer usage, Integer limit) {
        List<TTEntity> result = new ArrayList<>();

        StringJoiner query = new StringJoiner(System.lineSeparator())
                .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
                .add("PREFIX im: <http://endhealth.info/im#>")
                .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
                .add("PREFIX sn: <http://snomed.info/sct#>")
                .add("SELECT DISTINCT ?s ?name ?usage {")
                .add(" GRAPH ?g {")
                .add("  ?s im:scheme ?scheme ;")
                .add("   rdfs:label ?name ;")
                .add("   im:usageTotal ?usage ;")
                .add("   rdf:type ?type ;")
                .add("   im:code ?code ;")
                .add("   im:status ?status .")
                .add(" }")
                .add("?scheme rdfs:label ?schemeName .")
                .add("?type rdfs:label ?typeName .")
                .add("?status rdfs:label ?statusName .")
                .add(" FILTER NOT EXISTS {")
                .add("  ?s (sn:370124000|im:hasMap|im:matchedTo) ?o2")
                .add(" }");

        if (null != usage) {
            query.add(" FILTER (?usage > " + usage + ")");
        }

        if (null != status && !status.isEmpty()) {
            ArrayList<String> statusIris = new ArrayList<>();
            for (String statusIri : status) {
                statusIris.add("<" + statusIri + ">");
            }
            query.add(" FILTER (?status IN (" + String.join(", ", statusIris) + "))");
        }

        if (null != scheme && !scheme.isEmpty()) {
            ArrayList<String> schemeIris = new ArrayList<>();
            for (String schemeIri : scheme) {
                schemeIris.add("<" + schemeIri + ">");
            }
            query.add(" FILTER (?scheme IN (" + String.join(", ", schemeIris) + "))");
        }

        if (null != type && !type.isEmpty()) {
            ArrayList<String> typeIris = new ArrayList<>();
            for (String typeIri : type) {
                typeIris.add("<" + typeIri + ">");
            }
            query.add(" FILTER (?type IN (" + String.join(", ", typeIris) + "))");
        } else {
            query.add("FILTER (?g NOT IN (im:, sn:))");
        }

        query.add("}").add("ORDER BY DESC(?usage)");

        if (null != limit) {
            query.add("LIMIT " + limit);
        }

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(query.toString());
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTEntity()
                            .setIri(bs.getValue("s").stringValue())
                            .setName(bs.getValue("name").stringValue())
                            .set(iri(IM.USAGE_TOTAL), TTLiteral.literal(bs.getValue("usage").stringValue()))
                    );
                }
            }
        }
        return result;
    }

    public TTArray findFilteredInTask(String actionIri, String taskIri) {
        TTArray ttArray = new TTArray();

        StringJoiner query = new StringJoiner(System.lineSeparator())
                .add(RDFS_PREFIX)
                .add(IM_PREFIX)
                .add("SELECT * {")
                .add("?actionIri im:inTask ?task .")
                .add("?task rdfs:label ?taskName .")
                .add("FILTER (?task != ?taskIri)")
                .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(query.toString());
            qry.setBinding("actionIri", Values.iri(actionIri));
            qry.setBinding("taskIri", Values.iri(taskIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    ttArray.add(new TTIriRef(bs.getValue("task").stringValue(), bs.getValue("taskName").stringValue()));
                }
            }
        }
        return ttArray;
    }

    public List<TTEntity> getActions(String taskIri) {
        List<TTEntity> result = new ArrayList<>();

        StringJoiner query = new StringJoiner("\n");
        query.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
                .add("PREFIX im: <http://endhealth.info/im#>")
                .add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>")
                .add("SELECT DISTINCT ?s ?name ?usage {")
                .add(" GRAPH ?g {")
                .add("  ?s im:inTask ?taskIri ;")
                .add("   im:scheme ?scheme ;")
                .add("   rdfs:label ?name ;")
                .add("   im:usageTotal ?usage ;")
                .add("   rdf:type ?type ;")
                .add("   im:code ?code ;")
                .add("   im:status ?status .")
                .add(" }")
                .add(" ?scheme rdfs:label ?schemeName .")
                .add(" ?type rdfs:label ?typeName .")
                .add(" ?status rdfs:label ?statusName .")
                .add("}")
                .add("ORDER BY DESC(?usage)");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(query.toString());
            qry.setBinding("taskIri", Values.iri(taskIri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTEntity()
                            .setIri(bs.getValue("s").stringValue())
                            .setName(bs.getValue("name").stringValue())
                            .set(iri(IM.USAGE_TOTAL), TTLiteral.literal(bs.getValue("usage").stringValue()))
                    );
                }
            }
        }

        return result;
    }
}



