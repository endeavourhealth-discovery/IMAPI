package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.CoreLegacyCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class EntityRepositoryImpl2 {
    private static final Logger LOG = LoggerFactory.getLogger(EntityRepositoryImpl2.class);

    private Map<String, String> prefixMap;
    private StringJoiner spql;

    /**
     * Gets the expansion set for a concept set
     *
     * @param set           iri of the concept set
     * @param includeLegacy flag whether to include legacy codes
     * @return A set of Core codes and their legacy codes
     */
    public Set<CoreLegacyCode> getSetExpansion(TTEntity set, boolean includeLegacy) {
        String sql = getExpansionAsSelect(set, includeLegacy);
        Set<CoreLegacyCode> result = new HashSet<>();
        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    CoreLegacyCode cl = new CoreLegacyCode();
                    result.add(cl);
                    cl.setIri(bs.getValue("concept").stringValue())
                            .setTerm(bs.getValue("name").stringValue())
                            .setCode(bs.getValue("code").stringValue())
                            .setScheme(TTIriRef.iri(bs.getValue("scheme").stringValue()));
                    if (includeLegacy) {
                        Value lc = bs.getValue("legacyCode");
                        if (lc != null)
                            cl.setLegacyCode(lc.stringValue());
                        Value lt = bs.getValue("legacyName");
                        if (lt != null)
                            cl.setLegacyTerm(lt.stringValue());
                        Value ls = bs.getValue("legacySchemeName");
                        if (ls != null)
                            cl.setLegacyTerm(ls.stringValue());
                    }

                }
            }
        }
        return result;

    }
    /**
     * An alternative method of getting an entity definition assuming all predicates inclided
     * @param iri of the entity
     * @return a bundle including the entity and the predicate names
     */
    public TTBundle getBundle(String iri){
        return getBundle(iri, null, false);
    }

    /**
     * An alternative method of getting an entity definition
     * @param iri of the entity
     * @param predicates List of predicates to `include`
     * @return bundle with entity and map of predicate names
     */
    public TTBundle getBundle(String iri, Set<String> predicates){
        return getBundle(iri, predicates, false);
    }

    /**
     * An alternative method of getting an entity definition
     * @param iri of the entity
     * @param predicates List of predicates
     * @param excludePredicates Flag denoting if predicate list is inclusion or exclusion
     * @return
     */
    public TTBundle getBundle(String iri, Set<String> predicates, boolean excludePredicates){
        TTBundle bundle = new TTBundle().setEntity(new TTEntity());

        StringJoiner sql = getBundleSparql(predicates, excludePredicates);

        Map<String, String> predNames = bundle.getPredicates();

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            GraphQuery qry=conn.prepareGraphQuery(sql.toString());
            qry.setBinding("entity",iri(iri));
            try (GraphQueryResult gs = qry.evaluate()) {
                Map<Value, TTValue> valueMap = new HashMap<>();
                Resource entityIri = null;
                for (org.eclipse.rdf4j.model.Statement st :gs) {
                    entityIri = processStatement(bundle, predNames, valueMap, entityIri, st);
                }

                // Sanity check
                for(Map.Entry<String, String> p : predNames.entrySet()) {
                    if (p.getValue().isEmpty())
                        LOG.warn("Unnamed predicate {}", p.getKey());
                }

                return bundle;
            }
        }
    }

    private StringJoiner getBundleSparql(Set<String> predicates, boolean excludePredicates) {
        int depth = 5;

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
        sql.add("}");

        sql.add("WHERE {")
            .add("  ?entity ?1predicate ?1Level.")
            .add("  ?1predicate rdfs:label ?1pName.");
        if (predicates != null) {
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

    private Resource processStatement(TTBundle bundle, Map<String, String> predNames, Map<Value, TTValue> valueMap, Resource entityIri, Statement st) {
        TTEntity entity = bundle.getEntity();

        // Process predicates
        String p = st.getPredicate().stringValue();

        if (p.equals(RDFS.LABEL.getIri())) {
            if (predNames.containsKey(st.getSubject().stringValue()))
                predNames.put(p, st.getObject().stringValue());
        } else if (!predNames.containsKey(p)) {
            predNames.put(p, "");
        }

        // Add to entity
        Resource subject = st.getSubject();
        TTIriRef predicate= TTIriRef.iri(st.getPredicate().stringValue());
        valueMap.put(st.getPredicate(),predicate);
        if (entity.getIri() == null) {
            entity.setIri(subject.stringValue());
            valueMap.put(subject, entity);
            entityIri = subject;
            bundle.setEntity(entity);
        }
        if (subject.isIRI() && !subject.equals(entityIri)) {
            TTIriRef subjectIri = valueMap.get(subject).asIriRef();
            subjectIri.setName(st.getObject().stringValue());
        }
        else {
            TTNode node = valueMap.get(subject).asNode();
            Value value = st.getObject();
            if (value.isLiteral()) {
                Literal l = (Literal) value;
                node.set(TTIriRef.iri(st.getPredicate().stringValue()), literal(l.stringValue(), l.getDatatype().stringValue()));
            }
            else if (value.isIRI()) {
                TTIriRef objectIri=null;
                if (valueMap.get(value)!=null)
                    objectIri= valueMap.get(value).asIriRef();
                if (objectIri==null)
                    objectIri = TTIriRef.iri(value.stringValue());
                if (node.get(predicate)==null)
                    node.set(predicate, objectIri);
                else
                    node.addObject(predicate,objectIri);
                valueMap.putIfAbsent(value, objectIri);
            }
            else if (value.isBNode()) {
                TTNode ob = new TTNode();
                if (node.get(predicate)==null)
                    node.set(predicate, ob);
                else
                    node.addObject(predicate,ob);
                valueMap.put(value, ob);
            }
        }
        return entityIri;
    }


    /**
     * Generates sparql from a concept set entity
     *
     * @param setEntity     the TT entity that holds the set definition
     * @param includeLegacy whether or not legacy codes should be included
     * @return A string of SPARQL
     */
    public String getExpansionAsGraph(TTEntity setEntity, boolean includeLegacy) {
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
        whereClause(setEntity);
        spql.add("}");
        return insertPrefixes() + spql.toString();
    }


    /**
     * Returns a set expansion as a select query. Note that if legacy is included the result will be a denormalised list.
     *
     * @param setEntity     iri of set
     * @param includeLegacy whether to include simple legacy maps
     * @return String containing the sparql query
     */
    public String getExpansionAsSelect(TTEntity setEntity, boolean includeLegacy) {
        initialiseBuilders();
        spql.add("SELECT ?concept ?name ?code ?scheme ?schemeName ");
        if (includeLegacy)
            spql.add("?legacy ?legacyName ?legacyCode ?legacyScheme ?legacySchemeName");
        spql.add("WHERE {");
        addNames(includeLegacy);
        spql.add("{SELECT distinct ?concept");
        whereClause(setEntity);
        spql.add("}");
        spql.add("}");
        return insertPrefixes() + spql.toString();
    }

    private void whereClause(TTEntity setEntity) {
        spql.add("WHERE {");
        TTArray definition = setEntity.get(IM.DEFINITION);
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
            spql.add("{");
            spql.add("?concept " + isa() + " ?superClass.");
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
        //return ("("+ getShort(RDFS.SUBCLASSOF.getIri(),"rdfs")+"|"+ getShort(SNOMED.REPLACED_BY.getIri(),"sct")+")*");
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
        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", iri(iri));
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

        try (RepositoryConnection conn = ConnectionManager.getConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("s", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    String iriValue = bs.getValue("o2").stringValue();
                    try {
                        iri(iriValue);
                        result.add(bs.getValue("o2").stringValue());
                    } catch (IllegalArgumentException ignored) {

                    }
                }
            }
        }

        return result;
    }

   public Map<String,String> getIriNames(Set<TTIriRef> iris){
       String iriTokens = iris.stream().map(i -> "<"+ i.getIri()+">").collect(Collectors.joining(","));
       StringJoiner sql = new StringJoiner("\n");
       sql.add("SELECT ?iri ?label")
         .add("WHERE {")
         .add("?iri rdfs:label ?label")
         .add(" filter (?iri in (")
         .add(iriTokens+"))")
         .add("}");
       Map<String,String> names= new HashMap<>();
       try (RepositoryConnection conn = ConnectionManager.getConnection()) {
           TupleQuery qry = conn.prepareTupleQuery(sql.toString());
           TupleQueryResult rs = qry.evaluate();
           while (rs.hasNext()) {
                   BindingSet bs = rs.next();
                   names.put(bs.getValue("iri").stringValue(),
                     bs.getValue("label").stringValue());
               }
       }
       return names;
   }

   public Set<TTEntity> getShapePropertyRanges(String iri){
        String sql = getSPRSQL();
       Set<TTEntity> predicateSet = new HashSet<>();

       try (RepositoryConnection conn = ConnectionManager.getConnection()) {
           TupleQuery qry = conn.prepareTupleQuery(sql);
           qry.setBinding("s", iri(iri));
           try (TupleQueryResult rs = qry.evaluate()) {
               while (rs.hasNext()) {
                   BindingSet bs = rs.next();
                   TTEntity pred = new TTEntity();
                   pred.setIri(bs.getValue("predicate").stringValue());
                   if (bs.getValue("range")!=null) {
                       TTEntity range = new TTEntity();
                       pred.set(RDFS.RANGE, range);
                       range.setIri(bs.getValue("range").stringValue());
                       range.set(RDF.TYPE, TTIriRef.iri(bs.getValue("type").stringValue()));
                   }
                   predicateSet.add(pred);
               }
               return predicateSet;
           }
       }
   }

    private String getSPRSQL() {
        return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
          "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
          "PREFIX im: <http://endhealth.info/im#>\n" +
          "PREFIX sh: <http://www.w3.org/ns/shacl#>\n" +
          "\n" +
          "\n" +
          "Select ?predicate ?range ?type\n" +
          "\n" +
          "where { ?s rdf:type sh:NodeShape.\n" +
          "    ?s (sh:property|sh:node)+ ?sub.\n" +
          "    ?sub sh:property ?prop.\n" +
          "    ?sub rdf:type ?type.\n" +
          "    ?prop sh:path ?predicate.\n" +
          "    Optional { ?predicate rdfs:range ?range}\n" +
          " \n" +
          "}";
    }


}



