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

import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class EntityRepository2 {
  private static final Logger LOG = LoggerFactory.getLogger(EntityRepository2.class);

  private static final String IM_PREFIX = "PREFIX im: <" + IM.NAMESPACE + ">";
  private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.NAMESPACE + ">";
  private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.NAMESPACE + ">";

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
          .findFirst().ifPresent(i -> {
            i.setName(bs.getValue("label").stringValue());
            if (bs.getValue("description") != null) i.setDescription(bs.getValue("description").stringValue());
          });
      }
    }
    return names;
  }

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

  public Map<String, String> getNameMap(Set<TTIriRef> iris) {
    Map<String, String> nameMap = new HashMap<>();
    getNames(iris);
    for (TTIriRef iri : iris) {
      nameMap.put(iri.getIri(), iri.getName());
    }
    return nameMap;
  }

  /**
   * creates ranges for properties without ranges where the super properties have them
   */
  public void inheritRanges(RepositoryConnection conn) {
    String sql = """
      INSERT {?property rdfs:range ?range}
      WHERE {
        SELECT ?property ?range
        WHERE {
          ?property rdf:type rdf:Property.
          {
            #filter (?property= <http://snomed.info/sct#10362801000001104> )
            ?property (rdfs:subClassOf)* ?superclass.
            filter not exists {
              ?property (rdfs:subClassOf) [
                (rdfs:subClassOf)+ ?superclass
              ]
            }
            filter not exists {?property rdfs:range ?anyrange}
            ?superclass rdfs:range ?range.
            ?range rdfs:label ?rlabel.
          }
        }
      }
      """;
    if (conn != null) {
      Update upd = conn.prepareUpdate(addSparqlPrefixes(sql));
      upd.execute();
    } else {
      try (RepositoryConnection conn2 = ConnectionManager.getIMConnection()) {
        Update upd = conn2.prepareUpdate(addSparqlPrefixes(sql));
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
      .add("SELECT ?concept ?label");
    for (String scheme : schemes) {
      sql.add("FROM <" + scheme + ">");
    }
    sql.add("""
      WHERE {
        {
          ?concept im:code ?code.
          filter (isIri(?concept))
          ?concept rdfs:label ?label.
        }
        UNION {
          ?concept im:hasTermCode ?node.
          ?node im:code ?code.
          filter not exists { ?concept im:matchedTo ?core}
          ?concept rdfs:label ?label
        }
        UNION {
          ?legacy im:hasTermCode ?node.
          ?node im:code ?code.
          ?legacy im:matchedTo ?concept.
          ?concept rdfs:label ?label.
        }
        UNION {
          ?legacy im:codeId ?code.
          ?legacy im:matchedTo ?concept.
          ?concept rdfs:label ?label.
        }
      }
      """);
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql.toString()));
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
      .add("SELECT ?concept ?label");
    for (String scheme : schemes) {
      sql.add("FROM <" + scheme + ">");
    }
    sql.add("""
      WHERE {
        ?legacy im:codeId ?codeId.
        ?legacy im:matchedTo ?concept.
        ?concept rdfs:label ?label.
      }
      """);

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql.toString()));
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
    String sql = """
      SELECT ?concept ?label
      WHERE {
        GRAPH ?scheme {
          ?legacy rdfs:label ?term.
          ?legacy im:matchedTo ?concept.
        }
        {
          ?concept rdfs:label ?label
        }
      }
      """;
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
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
    String sql = """
      SELECT ?concept ?label
      WHERE {
        GRAPH ?scheme {
          ?tc im:code ?code.
          ?concept im:hasTermCode ?tc.
        }
        {
          ?concept rdfs:label ?label
        }
      }
      """;
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
      qry.setBinding("code", Values.literal(code));
      qry.setBinding("scheme", Values.iri(scheme));
      return getConceptRefsFromResult(qry);
    }
  }

  public Map<String, String> getCodeToIri() {
    String sql = """
      SELECT ?code ?scheme ?iri ?altCode
      WHERE {
        ?iri im:code ?code.
        OPTIONAL {?iri im:alternativeCode ?altCode}
        ?iri im:scheme ?scheme
      }
      """;
    Map<String, String> codeToIri = new HashMap<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
      try (TupleQueryResult gs = qry.evaluate()) {
        while (gs.hasNext()) {
          BindingSet bs = gs.next();
          String code = bs.getValue("code").stringValue();
          String scheme = bs.getValue("scheme").stringValue();
          String iri = bs.getValue("iri").stringValue();
          codeToIri.put(scheme + code, iri);
          if (bs.getValue("altCode") != null) {
            codeToIri.put(scheme + bs.getValue("altCode").stringValue(), iri);
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
    String sql = """
      select ?concept ?label
      from <%s>
      from <%s>
      where {
        {
          ?concept rdfs:label ?term.
          filter(isIri(?concept))
        }
        union {
          ?concept im:hasTermCode ?tc.
          ?tc rdfs:label ?term.
        }
      }
      """.formatted(IM.NAMESPACE, SNOMED.NAMESPACE);
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
      qry.setBinding("term", Values.literal(term));
      return getConceptRefFromResult(qry);
    }
  }

  public Map<String, Set<String>> getAllMatchedLegacy() {
    String sql = """
      SELECT ?legacy ?concept
      WHERE {
        ?legacy im:matchedTo ?concept.
      }
      """;
    Map<String, Set<String>> maps = new HashMap<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
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
        sql.add("""
          UNION {
          ?1predicate owl:inverseOf ?1revPredicate .
          ?1Level ?1revPredicate ?entity
          FILTER (?1predicate IN (%s))
          }
          """.formatted(inPredicates));
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

  private void processStatement(TTBundle bundle, Map<String, TTValue> tripleMap, String entityIri, Statement st) {
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
    return addSparqlPrefixes(spql.toString());
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
    if (!values.toString().isEmpty()) {
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
    } else
      return "<" + iri + ">";
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

    String sql = """
      SELECT ?o2
      WHERE {
        ?s im:definition ?o .
        ?o (sh:or|sh:and) ?o2 .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
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

    String sql = """
      SELECT ?set ?name
      WHERE {
        ?subset ?issubset ?set .
        ?set ?label ?name .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
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

  public Set<TTEntity> getLinkedShapes(String iri) {
    String sql = getLinkedShapeSql();
    Set<TTEntity> shapes = new HashSet<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      GraphQuery qry = conn.prepareGraphQuery(addSparqlPrefixes(sql));
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
    return """
      CONSTRUCT {
        ?s ?p ?o.
        ?sub ?p2 ?o2.
        ?o2 ?p3 ?o3.
      }
      WHERE {
        ?s ?p ?o.
        FILTER (?s= ?shape)
        ?s (sh:property|sh:node)+ ?sub.
        ?sub ?p2 ?o2.
        OPTIONAL {
          ?o2 ?p3 ?o3
          FILTER (isBlank(?o2))
        }
      }
      """;
  }
}



