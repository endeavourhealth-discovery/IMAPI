package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EntityRepository2 {
    private static final Logger LOG = LoggerFactory.getLogger(EntityRepository2.class);

    private Map<String, String> prefixMap;
    private StringJoiner spql;


    public Set<String> getSetDbids(String setIri, TTArray definition) {
        Set<String> result = new HashSet<>();

        addExpansionDbids(definition, result);

        addSetMemberDbids(setIri, result);

        return result;
    }

    /**
     * Gets the (definition based) expansion set for a concept set
     *
     * @param definition    definition of the set
     * @param includeLegacy flag whether to include legacy codes
     * @return A set of Core codes and their legacy codes
     */
    public List<CoreLegacyCode> getSetExpansion(TTArray definition, boolean includeLegacy) {
        List<CoreLegacyCode> result = new ArrayList<>();
        String sql = getExpansionAsSelect(definition, includeLegacy);
        List<String> lsa= new ArrayList<>();
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);

            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    CoreLegacyCode cl = new CoreLegacyCode();
                    String concept= bs.getValue("concept").stringValue();
                    Value name= bs.getValue("name");
                    Value code = bs.getValue("code");
                    Value scheme= bs.getValue("scheme");
                    Value schemeName= bs.getValue("schemeName");
                    cl.setIri(concept);
                    if (name!=null)
                        cl.setTerm(name.stringValue());
                    if (code!=null) {
                        cl.setCode(code.stringValue());
                        cl.setScheme(iri(scheme.stringValue(), schemeName.stringValue()));
                    }

                    if (includeLegacy) {
                        Value legIri= bs.getValue("legacy");
                        Value lc = bs.getValue("legacyCode");
                        Value lt = bs.getValue("legacyName");
                        Value ls = bs.getValue("legacyScheme");
                        Value lsn = bs.getValue("legacySchemeName");
                        if (legIri!=null)
                            cl.setLegacyIri(legIri.stringValue());
                        if (lc!=null)
                            cl.setLegacyCode(lc.stringValue());
                        if (lt!=null)
                            cl.setLegacyTerm(lt.stringValue());
                        if (ls!=null)
                            cl.setLegacyScheme(iri(ls.stringValue(),lsn.stringValue()));

                        }
                    result.add(cl);
                }

            }
        }
        return result;

    }

    private void addExpansionDbids(TTArray definition, Set<String> result) {
        if (definition != null) {
            String sql = getIm1ExpansionAsSelect(definition);
            try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
                TupleQuery qry = conn.prepareTupleQuery(sql);
                qry.setBinding("im1id", Values.iri(IM.IM1ID.getIri()));
                try (TupleQueryResult rs = qry.evaluate()) {
                    while (rs.hasNext()) {
                        BindingSet bs = rs.next();
                        result.add(((Literal)bs.getValue("id")).stringValue());
                        if (bs.getValue("legacyId") != null)
                            result.add(((Literal)bs.getValue("legacyId")).stringValue());
                    }
                }
            }
        }
    }


    private void addSetMemberDbids(String setIri, Set<String> result) {
        String sql = new StringJoiner(System.lineSeparator())
            .add("SELECT ?id")
            .add("WHERE {")
            .add("  ?set ?imHasMember ?concept.")
            .add("  ?concept ?imDbid ?id.")
            .add("}")
            .toString();

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            qry.setBinding("set", Values.iri(setIri));
            qry.setBinding("imHasMember", Values.iri(IM.HAS_MEMBER.getIri()));
            qry.setBinding("imDbid", Values.iri(IM.IM1ID.getIri()));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(((Literal)bs.getValue("id")).stringValue());
                }
            }
        }
    }

    /**
     * An alternative method of getting an entity definition assuming all predicates inclided
     * @param iri of the entity
     * @return a bundle including the entity and the predicate names
     */
    public TTBundle getBundle(String iri) {
        return getBundle(iri, null, false);
    }

    /**
     * An alternative method of getting an entity definition
     * @param iri of the entity
     * @param predicates List of predicates to `include`
     * @return bundle with entity and map of predicate names
     */
    public TTBundle getBundle(String iri, Set<String> predicates) {
        return getBundle(iri, predicates, false);
    }



    /**
     * An alternative method of getting an entity definition
     * @param iri of the entity
     * @param predicates List of predicates
     * @param excludePredicates Flag denoting if predicate list is inclusion or exclusion
     * @return
     */
    public TTBundle getBundle(String iri, Set<String> predicates,
                              boolean excludePredicates) {

        TTBundle bundle = new TTBundle()
          .setEntity(new TTEntity().setIri(iri))
          .setPredicates(new HashMap<>());

        StringJoiner sql = getBundleSparql(predicates, excludePredicates);

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            GraphQuery qry=conn.prepareGraphQuery(sql.toString());
            qry.setBinding("entity", Values.iri(iri));
            try (GraphQueryResult gs = qry.evaluate()) {
                Map<String, TTValue> valueMap = new HashMap<>();
                for (org.eclipse.rdf4j.model.Statement st : gs) {
                    processStatement(bundle, valueMap, iri, st);
                }
                TTManager.unwrapRDFfromJson(bundle.getEntity());
                Set<TTIriRef> iris = TTManager.getIrisFromNode(bundle.getEntity());
                getIriNames(conn,iris);
                setNames(bundle.getEntity(), iris);
                iris.forEach(bundle::addPredicate);
            } catch (IOException ignored) {
                //Do nothing
            }
            return bundle;
        }
    }

    private void setNames(TTValue subject, Set<TTIriRef> iris){
        HashMap<String, String> names = new HashMap<>();
        iris.forEach(i -> names.put(i.getIri(),i.getName()));
        if (subject.isIriRef())
            subject.asIriRef().setName(names.get(subject.asIriRef().getIri()));
        else if (subject.isNode()){
            if (subject.asNode().getPredicateMap()!=null){
                for (Map.Entry<TTIriRef,TTArray> entry:subject.asNode().getPredicateMap().entrySet()){
                    for (TTValue value:entry.getValue().getElements()){
                        if (value.isIriRef())
                            value.asIriRef().setName(names.get(value.asIriRef().getIri()));
                        else if (value.isNode())
                            setNames(value,iris);
                    }
                }
            }
        }
    }


    /**
     * Returns an entity iri and name from a code or a term code
     * @param code the code or description id or term code
     * @return iri and name of entity
     */
    public Set<TTIriRef> getCoreFromCode(String code,List<String> schemes){
        StringBuilder sql=
          new StringBuilder("PREFIX im: <http://endhealth.info/im#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "select ?concept ?label\n");
        for (String scheme:schemes){
            sql.append("from <").append(scheme).append(">\n");
        }
          sql.append("where {  {\n")
            .append(" ?concept im:code ?code.\n")
            .append("    filter (isIri(?concept))\n")
            .append(" ?concept rdfs:label ?label.}\n")
            .append("  UNION{?concept im:hasTermCode ?node.\n")
            .append("        ?node im:code ?code.\n")
            .append("          filter not exists { ?concept im:matchedTo ?core}\n")
            .append("        ?concept rdfs:label ?label}\n")
            .append("  UNION {?legacy im:hasTermCode ?node.\n")
            .append("         ?node im:code ?code.\n")
            .append("          ?legacy im:matchedTo ?concept.\n")
            .append("         ?concept rdfs:label ?label.}\n")
            .append("   UNION {?legacy im:codeId ?code.\n")
            .append("          ?legacy im:matchedTo ?concept.\n")
            .append("          ?concept rdfs:label ?label.}\n")
            .append("}");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("code", Values.literal(code));
            return getConceptRefsFromResult(qry);
        }

    }


    /**
     * Returns an entity iri and name from a code or a term code
     * @param codeId the code or description id or term code
     * @return iri and name of entity
     */
    public Set<TTIriRef> getCoreFromCodeId(String codeId,List<String> schemes){
        StringBuilder sql=
          new StringBuilder("PREFIX im: <http://endhealth.info/im#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "select ?concept ?label\n");
        for (String scheme:schemes){
            sql.append("from <").append(scheme).append(">\n");
        }
        sql.append("where {  ")
          .append(" ?legacy im:codeId ?codeId.\n")
          .append(" ?legacy im:matchedTo ?concept.")
          .append(" ?concept rdfs:label ?label.}\n");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("codeId", Values.literal(codeId));
            return getConceptRefsFromResult(qry);
        }

    }

    /**
     * Returns a core entity iri and name from a legacy term
     * @param term the code or description id or term code
     * @param scheme the legacy scheme of the term
     * @return iri and name of entity
     */
    public Set<TTIriRef> getCoreFromLegacyTerm(String term,String scheme){
        String sql="PREFIX im: <http://endhealth.info/im#>\n" +
          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "select ?concept ?label\n"+
          "where { graph ?scheme {\n" +
          "?legacy rdfs:label ?term.\n" +
          "?legacy im:matchedTo ?concept.}\n"+
          "{?concept rdfs:label ?label} }";
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            qry.setBinding("term", Values.literal(term));
            qry.setBinding("scheme", Values.iri(scheme));
            return getConceptRefsFromResult(qry);
        }
    }

    /**
     * Returns an entity iri and name from a term code code
     * @param code the code that is a term code
     * @param scheme the scheme of the term
     * @return set of iris and name of entity
     */

    public Set<TTIriRef> getReferenceFromTermCode(String code, String scheme) {
        String sql="PREFIX im: <http://endhealth.info/im#>\n" +
          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "select ?concept ?label\n"+
          "where { graph ?scheme {\n" +
          "?tc im:code ?code.\n" +
          "?concept im:hasTermCode ?tc.}\n"+
          "{?concept rdfs:label ?label} }";
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            qry.setBinding("code", Values.literal(code));
            qry.setBinding("scheme", Values.iri(scheme));
            return getConceptRefsFromResult(qry);
        }

    }



    /**
     * Returns A core entity iri and name from a core term
     * @param term the code or description id or term code
     * @return iri and name of entity
     */
    public TTIriRef getReferenceFromCoreTerm(String term){
        List<String> schemes = List.of(IM.NAMESPACE, SNOMED.NAMESPACE);
        String sql="PREFIX im: <http://endhealth.info/im#>\n" +
          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "select ?concept ?label\n";
        for (String scheme:schemes)
            sql=sql+"from <"+scheme+">\n";
         sql=sql+ "where { {\n" +
          "?concept rdfs:label ?term." +
           "filter(isIri(?concept))}\n"+
           "union { ?concept im:hasTermCode ?tc."+
           "?tc rdfs:label ?term.} }";
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            qry.setBinding("term", Values.literal(term));
            return getConceptRefFromResult(qry);
        }
    }

    public Map<String,Set<String>> getAllMatchedLegacy(){
        String sql="PREFIX im: <http://endhealth.info/im#>\n"+
      "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"+
      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"+
     "select ?legacy ?concept\n"+
      "where {?legacy im:matchedTo ?concept.}\n";
        Map<String,Set<String>> maps= new HashMap<>();
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            TTIriRef concept=null;
            try (TupleQueryResult gs = qry.evaluate()) {
                while (gs.hasNext()) {
                    BindingSet bs = gs.next();
                    String legacy= bs.getValue("legacy").stringValue();
                    maps.putIfAbsent(legacy, new HashSet<>());
                    maps.get(legacy).add(bs.getValue("concept").stringValue());
                    if (bs.getValue("label") != null)
                        concept.setName(bs.getValue("label").stringValue());

                }
            }
        }
        return maps;
    }


    public Set<TTIriRef> getMatchedCore(String legacy){
        String sql="PREFIX im: <http://endhealth.info/im#>\n" +
          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "select ?concept ?label\n" +
          "where {\n" +
          "    ?legacy im:matchedTo ?concept.\n" +
          "    ?concept rdfs:label ?label}\n" +
          "    ";
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            qry.setBinding("legacy", Values.iri(legacy));
            return getConceptRefsFromResult(qry);
        }
    }

    private TTIriRef getConceptRefFromResult(TupleQuery qry) {
        TTIriRef concept=null;
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
        Set<TTIriRef> results=null;
        try (TupleQueryResult gs = qry.evaluate()) {
            while (gs.hasNext()) {
                BindingSet bs = gs.next();
                if (results==null)
                    results= new HashSet<>();
                TTIriRef concept = TTIriRef.iri(bs.getValue("concept").stringValue());
                if (bs.getValue("label") != null)
                    concept.setName(bs.getValue("label").stringValue());
                results.add(concept);

            }
        }
        return results;
    }


    private StringJoiner getBundleSparql(Set<String> predicates,
                                         boolean excludePredicates) {
        int  depth= 5;
        StringJoiner sql = new StringJoiner("\n");
        sql.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        sql.add("CONSTRUCT {")
            .add("  ?entity ?1predicate ?1Level.")
            .add("  ?1Level rdfs:label ?1Name.");
        for (int i = 1; i < depth; i++) {
            sql.add("  ?" + i + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
                .add("  ?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName.")
                .add("  ?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name.");
        }
        sql.add("} WHERE {");

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
        }
        sql.add("  OPTIONAL {?1Level rdfs:label ?1Name.")
            .add("    FILTER (!isBlank(?1Level))}");
        for (int i = 1; i < depth; i++) {
            sql.add("  OPTIONAL {?" + (i) + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.")
                .add("    FILTER (isBlank(?" + i + "Level))")
                .add("  OPTIONAL {?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName}")
                .add("  OPTIONAL {?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name")
                .add("    FILTER (!isBlank(?" + (i + 1) + "Level))}");
        }
        sql.add("}}}}}");
        return sql;
    }

    private void processStatement(TTBundle bundle, Map<String,TTValue> tripleMap, String entityIri, Statement st) {
        TTEntity entity = bundle.getEntity();
        Resource s= st.getSubject();
        IRI p= st.getPredicate();
        Value o =  st.getObject();
        String subject= s.stringValue();
        String predicate= p.stringValue();
        String value = o.stringValue();
        Map<String,String> predNames= bundle.getPredicates();
        if (tripleMap.get(predicate)!=null) {
            if (tripleMap.get(predicate).asIriRef().getName() != null) {
                predNames.put(predicate, tripleMap.get(predicate).asIriRef().getName());
            }
            else
                predNames.put(predicate,predicate);
        } else {
            tripleMap.putIfAbsent(predicate, iri(predicate));
            predNames.put(predicate,predicate);
        }
        TTNode node;
        if (predicate.equals(RDFS.LABEL.getIri())) {
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
                tripleMap.get(subject).asNode().set(RDFS.LABEL, TTLiteral.literal(value, ((Literal)o).getDatatype().stringValue()));
            }
        }
        else {
            if (s.isIRI()) {
                node = entity;
            }
            else {
                tripleMap.putIfAbsent(subject,new TTNode());
                node= tripleMap.get(subject).asNode();
            }
            if (o.isBNode()){
                tripleMap.putIfAbsent(value,new TTNode());
                node.addObject(tripleMap.get(predicate).asIriRef(),tripleMap.get(value));
            }
            else if (o.isIRI()){
                tripleMap.putIfAbsent(value, iri(value));
                node.addObject(tripleMap.get(predicate).asIriRef(),tripleMap.get(value));
            }
            else {
                tripleMap.putIfAbsent(value,TTLiteral.literal(value, ((Literal)o).getDatatype().stringValue()));
                node.set(tripleMap.get(predicate).asIriRef(),tripleMap.get(value).asLiteral());
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
        initialiseBuilders();
        spql.add("CONSTRUCT {?concept rdfs:label ?name.")
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
        addNames(includeLegacy);
        spql.add("{SELECT distinct ?concept");
        whereClause(definition);
        spql.add("}");
        return insertPrefixes() + spql.toString();
    }


    /**
     * Returns a set expansion as a select query. Note that if legacy is included the result will be a denormalised list.
     *
     * @param definition    definition of set
     * @param includeLegacy whether to include simple legacy maps
     * @return String containing the sparql query
     */
    public String getExpansionAsSelect(TTArray definition, boolean includeLegacy) {
        initialiseBuilders();
        spql.add("SELECT ?concept ?name ?code ?scheme ?schemeName ");
        if (includeLegacy)
            spql.add("?legacy ?legacyName ?legacyCode ?legacyScheme ?legacySchemeName");
        spql.add("WHERE {");
        addNames(includeLegacy);
        spql.add("{SELECT distinct ?concept");
        whereClause(definition);
        spql.add("}");
        spql.add("}");
        return insertPrefixes() + spql.toString();
    }


    private String getIm1ExpansionAsSelect(TTArray definition) {
        initialiseBuilders();
        spql.add("SELECT ?concept ?id ?legacy ?legacyId")
            .add("WHERE {")
            .add("  ?concept ?im1id ?id.")
            .add("  OPTIONAL {")
            .add("      ?legacy im:matchedTo ?concept.")
            .add("      ?legacy ?im1id ?legacyId.")
            .add("  }")
            .add("  {")
            .add("      SELECT distinct ?concept");
        whereClause(definition);
        spql.add("  }");
        spql.add("}");

        return insertPrefixes() + spql.toString();
    }

    private void whereClause(TTArray definition) {
        spql.add("WHERE {");
        graphWherePattern(definition);
        spql.add("}");
    }

    private void graphWherePattern(TTArray definition) {
        if (definition.isIriRef()) {
            simpleSuperClass(definition.asIriRef());
        } else if (definition.asNode().get(SHACL.OR) != null) {
            orClause(definition.asNode().get(SHACL.OR));

        } else if (definition.asNode().get(SHACL.AND) != null) {
            boolean hasRoles = andClause(definition.asNode().get(SHACL.AND), true);
            if (hasRoles) {
                andClause(definition.asNode().get(SHACL.AND), false);
            }
        }
    }


    private void orClause(TTArray ors) {
        spql.add("{");
        StringBuilder values = new StringBuilder();
        for (TTValue superClass : ors.getElements()) {
            if (superClass.isIriRef())
                values.append(getShort(superClass.asIriRef().getIri())).append(" ");
        }
        if (!values.toString().equals("")) {
            spql.add("{ ?concept " + isa() + " ?superClass.");
            values = new StringBuilder("VALUES ?superClass {" + values + "}");
            spql.add(values.toString());

            spql.add("}");
        }

        for (TTValue complexClass : ors.getElements()) {
            if (complexClass.isNode()) {

                addUnion(complexClass.asNode());
            }
        }
        spql.add("}");
    }

    private void addNames(boolean includeLegacy) {
        spql.add("GRAPH ?scheme {?concept " + getShort(RDFS.LABEL.getIri(), "rdfs") + " ?name.\n" +
                "?concept " + getShort(IM.CODE.getIri(), "im") + " ?code");
        spql.add(" OPTIONAL {?scheme rdfs:label ?schemeName}}");
        if (includeLegacy) {
            spql.add("OPTIONAL {GRAPH ?legacyScheme {")
                    .add("?legacy im:matchedTo ?concept.")
                    .add("OPTIONAL {?legacy rdfs:label ?legacyName.}")
                    .add("?legacy im:code ?legacyCode.")
                    .add("OPTIONAL {?legacyScheme rdfs:label ?legacySchemeName}}}");
        }
    }

    private void simpleSuperClass(TTIriRef superClass) {
        spql.add("?concept " + isa() + " " + getShort(superClass.asIriRef().getIri()) + ".");

    }

    private void addUnion(TTNode union) {

        if (union.get(SHACL.AND) != null) {
            spql.add("UNION {");
            boolean hasRoles = andClause(union.get(SHACL.AND), true);
            spql.add("}");
            if (hasRoles) {
                spql.add("UNION {");
                andClause(union.get(SHACL.AND), false);
                spql.add("}");
            }
        } else {
            spql.add("UNION {");
            roles(union, true); //adds a set of roles from a group.
            spql.add("}");
            spql.add("UNION {");
            roles(union, false);
            spql.add("}");
        }
    }


    private void roles(TTNode node, boolean group) {
        int count = 1;
        for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
            count++;
            String obj = "?o_" + count;
            String pred = "?p_" + count;
            spql.add(obj + " " + isa() + " " + getShort(entry.getValue().asIriRef().getIri()) + ".");
            spql.add(pred + " " + isa() + " " + getShort(entry.getKey().getIri()) + ".");
            if (group) {
                spql.add("?roleGroup " + pred + " " + obj + ".");
                spql.add(" FILTER (isBlank(?roleGroup))");
                spql.add("?superMember " + getShort(IM.ROLE_GROUP.getIri(), "im") + " ?roleGroup.");
            } else {
                spql.add("?superMember " + pred + " " + obj + ".");
                spql.add("  FILTER (isIri(?superMember))");
            }
        }
        spql.add("?concept " + getShort(IM.IS_A.getIri(), "im") + " ?superMember.");

    }


    private String getShort(String iri) {
        if (iri.contains("#")) {
            String prefix = iri.substring(0, iri.lastIndexOf("#"));
            prefix = prefix.substring(prefix.lastIndexOf("/") + 1);
            prefixMap.put(iri.substring(0, iri.lastIndexOf("#") + 1), prefix);
            return prefix + ":" + iri.substring(iri.lastIndexOf("#") + 1);
        }
        return "<" + iri + ">";
    }

    private String getShort(String iri, String prefix) {
        prefixMap.put(iri.substring(0, iri.lastIndexOf("#") + 1), prefix);
        return prefix + ":" + iri.substring(iri.lastIndexOf("#") + 1);
    }

    private Boolean andClause(TTArray and, boolean group) {
        boolean hasRoles = false;
        for (TTValue inter : and.getElements()) {
            if (inter.isNode() && inter.asNode().get(SHACL.NOT) == null) {
                roles(inter.asNode(), group);
                hasRoles = true;
            }
        }
        for (TTValue inter : and.getElements()) {
            if (inter.isIriRef()) {
                simpleSuperClass(inter.asIriRef());
            }
        }
        for (TTValue inter : and.getElements()) {
            if (inter.isNode() && inter.asNode().get(SHACL.NOT) != null)
                notClause(inter.asNode().get(SHACL.NOT).asValue());
        }
        return hasRoles;
    }

    private String isa() {
        return getShort(IM.IS_A.getIri());
    }

    private void notClause(TTValue not) {
        spql.add("MINUS {");
        if (not.isIriRef())
            simpleSuperClass(not.asIriRef());
        else if (not.isNode()) {
            if (not.asNode().get(SHACL.OR) != null) {
                orClause(not.asNode().get(SHACL.OR));
            } else if (not.asNode().get(SHACL.AND) != null) {
                boolean hasRoles = andClause(not.asNode().get(SHACL.AND), true);
                if (hasRoles) {
                    andClause(not.asNode().get(SHACL.AND), false);
                }
            }
        }
        spql.add("}");
    }


    private void initialiseBuilders() {
        prefixMap = new HashMap<>();
        spql = new StringJoiner("\n");
    }

    private String insertPrefixes() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
            sb.append("PREFIX ")
                    .append(entry.getValue()).append(": <")
                    .append(entry.getKey()).append(">\n");
        }
        return sb.toString();
    }

    public boolean isSet(String iri) {
        StringJoiner sql = new StringJoiner("\n");
        sql.add("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>");
        sql.add("SELECT * WHERE {");
        sql.add("?s rdf:type ?o .");
        sql.add("}");
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", Values.iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                if (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    return bs.getValue("o").stringValue().equals(IM.CONCEPT_SET.getIri()) || bs.getValue("o").stringValue().equals(IM.VALUESET.getIri());
                }
            }
        }

        return false;
    }

    public List<String> getMemberIris(String iri) {
        List<String> result = new ArrayList<>();

        StringJoiner sql = new StringJoiner("\n");
        sql.add("PREFIX im: <http://endhealth.info/im#>");
        sql.add("PREFIX sh: <http://www.w3.org/ns/shacl#>");
        sql.add("SELECT ?o2 WHERE {");
        sql.add("?s im:definition ?o .");
        sql.add("?o (sh:or|sh:and) ?o2 .");
        sql.add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", Values.iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    String iriValue = bs.getValue("o2").stringValue();
                    try {
                        Values.iri(iriValue);
                        result.add(bs.getValue("o2").stringValue());
                    } catch (IllegalArgumentException ignored) {
                        //Do nothing
                    }
                }
            }
        }

        return result;
    }

   public static Map<String,String> getIriNames(RepositoryConnection conn,Set<TTIriRef> iris){
       Map<String,String> names= new HashMap<>();
        if (iris == null || iris.size() == 0)
            return names;

       String iriTokens = iris.stream().map(i -> "<"+ i.getIri()+">").collect(Collectors.joining(","));
       StringJoiner sql = new StringJoiner("\n");
       sql.add("SELECT ?iri ?label")
         .add("WHERE {")
         .add("?iri rdfs:label ?label")
         .add(" filter (?iri in (")
         .add(iriTokens+"))")
         .add("}");
       TupleQuery qry = conn.prepareTupleQuery(sql.toString());
       try (TupleQueryResult rs = qry.evaluate()){
           while (rs.hasNext()) {
               BindingSet bs = rs.next();
               TTIriRef iri= iri(bs.getValue("iri").stringValue());
               iris.stream().filter(i-> i.equals(iri))
                 .findFirst().ifPresent(i-> i.setName(bs.getValue("label").stringValue()));
           }
       }
       return names;
   }

    public Set<TTEntity> getLinkedShapes(String iri){
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

    private void processTripleLinkShape(Set<TTEntity> entities, Map<String, TTValue> valueMap, Map<String,TTNode> subjectMap,Statement st) {
        Resource subject = st.getSubject();
        TTIriRef predicate = iri(st.getPredicate().stringValue());
        Value object = st.getObject();
        TTNode node = subjectMap.get(subject.stringValue());
        if (node == null) {
            if (subject.isIRI()) {
                TTEntity entity = new TTEntity().setIri(subject.stringValue());
                subjectMap.put(subject.stringValue(), entity);
                entities.add(entity);
            }
            else
                subjectMap.put(subject.stringValue(), new TTNode());
        }
        node= subjectMap.get(subject.stringValue());
        if (object.isLiteral()) {
            Literal l = (Literal) object;
            node.set(predicate, TTLiteral.literal(l.stringValue(), l.getDatatype().stringValue()));
        }
        else if (object.isIRI()) {
            node.addObject(predicate, iri(object.stringValue()));
        }
        else {
            if (valueMap.get(object.stringValue()) == null) {
                if (subjectMap.get(object.stringValue()) != null)
                    valueMap.put(object.stringValue(), subjectMap.get(object.stringValue()));
                else {
                    valueMap.put(object.stringValue(), new TTNode());
                    subjectMap.put(object.stringValue(), valueMap.get(object.stringValue()).asNode());
                }
            }
            subjectMap.put(object.stringValue(), valueMap.get(object.stringValue()).asNode());
            node.addObject(predicate,valueMap.get(object.stringValue()).asNode());
        }
    }

    private String getLinkedShapeSql() {
        return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "PREFIX im: <http://endhealth.info/im#>\n" +
          "PREFIX sh: <http://www.w3.org/ns/shacl#>\n" +
          "Construct {\n" +
          "    ?s ?p ?o.\n" +
          "    ?sub ?p2 ?o2.\n" +
          "    ?o2 ?p3 ?o3.\n" +
          "}\n" +
          "where { ?s ?p ?o.\n" +
          "    filter (?s= ?shape)\n" +
          "    ?s (sh:property|sh:node)+ ?sub.\n" +
          "    ?sub ?p2 ?o2.\n" +
          "    Optional { ?o2 ?p3 ?o3\n" +
          "        filter (isBlank(?o2))}\n" +
          "}";

    }


    public List<TTIriRef> findUnassigned() {
        List<TTIriRef> result = new ArrayList<>();

        StringJoiner guery = new StringJoiner("\n");
        guery.add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>");
        guery.add("SELECT * WHERE {");
        guery.add("?s <http://endhealth.info/im#status> <http://endhealth.info/im#Unassigned> .");
        guery.add("?s rdfs:label ?name .");
        guery.add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(guery.toString());
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
                }
            }
        }

        return result;
    }
}



