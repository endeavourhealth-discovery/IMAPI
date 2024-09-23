package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.Context;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;

public class EntityRepository {
  static final String PARENT_PREDICATES = "rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf|im:isSubsetOf";
  private static final Logger LOG = LoggerFactory.getLogger(EntityRepository.class);

  /**
   * returns and entity from a recursive SPARQL query with property paths based on the
   * required sub predicates
   *
   * @param iri            iri of the entity
   * @param mainPredicates set of predicates for the main body, null if all
   * @param subPredicates  set of predicates for recursive blank node/ sub node
   * @return the entity populated
   */
  private static TTEntity getEntityWithSubPredicates(String iri, Set<TTIriRef> mainPredicates, Set<TTIriRef> subPredicates) {
    RepositoryConnection conn = ConnectionManager.getIMConnection();
    StringBuilder mainPredVar;
    StringBuilder subPredVar = new StringBuilder("?p2");
    if (mainPredicates != null) {
      int i = 0;
      mainPredVar = new StringBuilder("(");
      for (TTIriRef pred : mainPredicates) {
        mainPredVar.append(i > 0 ? "," : "").append("<").append(pred.getIri()).append("> ");
        i++;
      }
      mainPredVar.append(")");
    }
    if (subPredicates != null) {
      int i = 0;
      subPredVar = new StringBuilder("(");
      for (TTIriRef pred : subPredicates) {
        subPredVar.append(i > 0 ? "|" : "").append("<").append(pred.getIri()).append("> ");
        i++;
      }
      subPredVar.append(")+ ");
    }

    String sql = """
      CONSTRUCT {
        ?entity ?p1 ?o1.
        ?o1 rdfs:label ?iriLabel1.
        ?s2 ?p2 ?o2.
        ?o2 ?p3 ?o3.
        ?o3 rdfs:label ?iriLabel3.
      }
      WHERE {
        ?entity ?p1 ?o1.
        OPTIONAL {
          ?o1 rdfs:label ?iriLabel1.
          FILTER(isIri(?o1))
        }
        OPTIONAL {
          ?o1 %s ?o2.
          ?s2 ?p2 ?o2.
          FILTER(isBLANK(?o1))
          OPTIONAL {?o2 ?p3 ?o3.
          FILTER(isBLANK(?o2))}
          OPTIONAL {
            ?o3 rdfs:label ?iriLabel3.
            FILTER (isIRI(?o3))
          }
        }
      }
      """.formatted(subPredVar.toString());

    GraphQuery qry = conn.prepareGraphQuery(addSparqlPrefixes(sql));
    qry.setBinding("entity",
      iri(Objects.requireNonNull(iri,
        "iri may not be null")));

    try (GraphQueryResult gs = qry.evaluate()) {
      Map<Value, TTValue> valueMap = new HashMap<>();
      TTEntity entity = new TTEntity().setIri(iri);
      int i = 0;
      for (org.eclipse.rdf4j.model.Statement st : gs) {
        i++;
        if (i % 100 == 0) {
          LOG.debug("{} {} {} {}", i, st.getSubject().stringValue(), st.getPredicate().stringValue(), st.getObject().stringValue());
        }
        populateEntity(st, entity, valueMap);
      }
      return entity;
    }
  }

  /**
   * populates an entity or sub nodes with statement
   *
   * @param st       statement to add to entity
   * @param entity   the entity to add the statement to
   * @param valueMap the map from statement to node that builds
   */
  private static void populateEntity(Statement st, TTEntity entity, Map<Value, TTValue> valueMap) {
    Resource subject = st.getSubject();
    TTIriRef predicate = TTIriRef.iri(st.getPredicate().stringValue());
    Value value = st.getObject();
    valueMap.put(st.getPredicate(), predicate);
    if (valueMap.get(subject) == null) {
      if (subject.stringValue().equals(entity.getIri()))
        valueMap.put(subject, entity);
      else if (subject.isIRI())
        valueMap.put(subject, TTIriRef.iri(subject.stringValue()));
      else
        valueMap.put(subject, new TTNode());
    }
    TTValue ttValue = valueMap.get(subject);
    if (ttValue.isIriRef()) {
      if (predicate.getIri().equals(RDFS.LABEL))
        ttValue.asIriRef().setName(value.stringValue());
    } else {
      processNode(value, valueMap, subject, st, predicate);
    }
  }

  private static void processNode(Value value, Map<Value, TTValue> valueMap, Resource subject, Statement st, TTIriRef predicate) {
    TTNode node = valueMap.get(subject).asNode();
    if (value.isLiteral()) {
      node.set(TTIriRef.iri(st.getPredicate().stringValue()), TTLiteral.literal(value.stringValue(), ((Literal) value).getDatatype().stringValue()));
    } else if (value.isIRI()) {
      TTIriRef objectIri = null;
      if (valueMap.get(value) != null)
        objectIri = valueMap.get(value).asIriRef();
      if (objectIri == null)
        objectIri = TTIriRef.iri(value.stringValue());
      if (node.get(predicate) == null)
        node.set(predicate, objectIri);
      else
        node.addObject(predicate, objectIri);
      valueMap.putIfAbsent(value, objectIri);
    } else if (value.isBNode()) {
      TTNode ob = new TTNode();
      if (node.get(predicate) == null)
        node.set(predicate, ob);
      else
        node.addObject(predicate, ob);
      valueMap.put(value, ob);
    }
  }

  private static void hydrateCorePropertiesSetEntityDocumentProperties(EntityDocument entityDocument, TupleQueryResult qr) {
    BindingSet rs = qr.next();
    entityDocument.setName(rs.getValue("name").stringValue());
    entityDocument.addTermCode(entityDocument.getName(), null, null);

    if (rs.hasBinding("preferredName"))
      entityDocument.setPreferredName(rs.getValue("preferredName").stringValue());

    if (rs.hasBinding("code"))
      entityDocument.setCode(rs.getValue("code").stringValue());

    if (rs.hasBinding("scheme"))
      entityDocument.setScheme(new TTIriRef(rs.getValue("scheme").stringValue()));

    if (rs.hasBinding("status")) {
      TTIriRef status = TTIriRef.iri(rs.getValue("status").stringValue());
      if (rs.hasBinding("statusName"))
        status.setName(rs.getValue("statusName").stringValue());
      entityDocument.setStatus(status);
    }

    TTIriRef type = TTIriRef.iri(rs.getValue("type").stringValue());
    if (rs.hasBinding("typeName"))
      type.setName(rs.getValue("typeName").stringValue());
    entityDocument.addType(type);

    if (rs.hasBinding("extraType")) {
      TTIriRef extraType = TTIriRef.iri(rs.getValue("extraType").stringValue(), rs.getValue("extraTypeName").stringValue());
      entityDocument.addType(extraType);
      if (extraType.equals(TTIriRef.iri(IM.NAMESPACE + "DataModelEntity"))) {
        int usageTotal = 2000000;
        entityDocument.setUsageTotal(usageTotal);
      }
    }
    if (rs.hasBinding("usageTotal")) {
      entityDocument.setUsageTotal(((Literal) rs.getValue("usageTotal")).intValue());
    }
  }

  public TTIriRef getEntityReferenceByIri(String iri) {
    TTIriRef result = new TTIriRef();
    String sql = """
      SELECT ?sname WHERE {
        ?s rdfs:label ?sname
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.setIri(iri).setName(bs.getValue("sname").stringValue());
        }
      }
    }
    return result;
  }

  private Set<TTIriRef> getTypesByIri(String iri) {
    Set<TTIriRef> result = new HashSet<>();

    String sql = """
      SELECT ?o ?oname WHERE {
        ?s rdf:type ?o .
        ?o rdfs:label ?oname .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new TTIriRef(bs.getValue("o").stringValue(), bs.getValue("oname").stringValue()));
        }
      }
    }
    return result;
  }

  public Map<String, Set<TTIriRef>> getTypesByIris(Set<String> stringIris) {
    Map<String, Set<TTIriRef>> iriToTypesMap = new HashMap<>();
    StringJoiner iriLine = new StringJoiner(" ");
    for (String stringIri : stringIris) {
      iri(stringIri);
      iriLine.add("<" + stringIri + ">");
    }

    String sql = """
      SELECT ?s ?o ?oname WHERE {
        ?s rdf:type ?o .
        ?o rdfs:label ?oname .
        VALUES ?s { %s }
      }
      """.formatted(iriLine.toString());

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("s").stringValue();
          if (!iriToTypesMap.containsKey(iri)) iriToTypesMap.put(iri, new HashSet<>());
          iriToTypesMap.get(iri).add(new TTIriRef(bs.getValue("o").stringValue(), bs.getValue("oname").stringValue()));
        }
      }
    }

    return iriToTypesMap;
  }

  public List<SearchResultSummary> getEntitySummariesByIris(Set<String> stringIris) {
    List<SearchResultSummary> summaries = new ArrayList<>();
    StringJoiner iriLine = new StringJoiner(" ");
    for (String stringIri : stringIris) {
      iri(stringIri);
      iriLine.add("<" + stringIri + ">");
    }

    String sql = """
      SELECT ?s ?sname ?scode ?sstatus ?sstatusname ?sdescription ?g ?gname WHERE {
      GRAPH ?g {
      ?s rdfs:label ?sname .
      }
      OPTIONAL { ?s im:code ?scode . }
      OPTIONAL { ?s im:status ?sstatus . ?sstatus rdfs:label ?sstatusname . }
      OPTIONAL { ?s rdfs:comment ?sdescription } .
      OPTIONAL { ?g rdfs:label ?gname } .
      VALUES ?s { %s }
      }
      """.formatted(iriLine.toString());

    Map<String, Set<TTIriRef>> iriToTypesMap = getTypesByIris(stringIris);

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("s").stringValue();
          Set<TTIriRef> types = iriToTypesMap.get(iri);
          SearchResultSummary summary = new SearchResultSummary();
          summary
            .setIri(iri)
            .setName(bs.getValue("sname").stringValue())
            .setCode(bs.getValue("scode") == null ? "" : bs.getValue("scode").stringValue())
            .setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())))
            .setEntityType(types)
            .setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue()))
            .setDescription(bs.getValue("sdescription") == null ? "" : bs.getValue("sdescription").stringValue());
          summaries.add(summary);
        }
      }
    }
    return summaries;
  }

  public SearchResultSummary getEntitySummaryByIri(String iri) {
    SearchResultSummary result = new SearchResultSummary();

    String sql = """
      SELECT ?sname ?scode ?sstatus ?sstatusname ?sdescription ?g ?gname ?sscheme ?sschemename
      WHERE {
        GRAPH ?g {
          ?s rdfs:label ?sname .
        }
        OPTIONAL { ?s im:code ?scode . }
        OPTIONAL { ?s im:status ?sstatus . ?sstatus rdfs:label ?sstatusname . }
        OPTIONAL { ?s im:scheme ?sscheme . ?sscheme rdfs:label ?sschemename . }
        OPTIONAL { ?s rdfs:comment ?sdescription } .
        OPTIONAL { ?g rdfs:label ?gname } .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          Set<TTIriRef> types = getTypesByIri(iri);
          BindingSet bs = rs.next();
          result
            .setIri(iri)
            .setName(bs.getValue("sname").stringValue())
            .setCode(bs.getValue("scode") == null ? "" : bs.getValue("scode").stringValue())
            .setEntityType(types)
            .setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue()))
            .setDescription(bs.getValue("sdescription") == null ? "" : bs.getValue("sdescription").stringValue());

          if (bs.hasBinding("sscheme")) {
            result.setScheme(new TTIriRef(bs.getValue("sscheme").stringValue(), (bs.getValue("sschemename") == null ? "" : bs.getValue("sschemename").stringValue())));
          } else {
            result.setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue())));
          }
        }
      }
    }
    return result;
  }

  public List<TTEntity> findEntitiesByName(String name) {
    List<TTEntity> result = new ArrayList<>();

    String spql = """
      SELECT *
      WHERE {
        ?s rdfs:label ?name ;
        rdf:type ?type .
        ?type rdfs:label ?typeName .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, spql);
      qry.setBinding("name", literal(name));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Optional<TTEntity> optionalEntity = result.stream()
            .filter(entity -> bs.getValue("s").stringValue().equals(entity.getIri()))
            .findAny();
          if (optionalEntity.isEmpty()) {
            TTEntity entity = new TTEntity()
              .setIri(bs.getValue("s").stringValue())
              .setName(bs.getValue("name").stringValue())
              .addType(new TTIriRef(bs.getValue("type").stringValue(), bs.getValue("typeName").stringValue()));
            result.add(entity);
          } else {
            optionalEntity.get().addType(new TTIriRef(bs.getValue("type").stringValue(), bs.getValue("typeName").stringValue()));
          }

        }
      }
    }

    return result;
  }

  public List<ParentDto> findParentHierarchies(String iri) {
    List<ParentDto> result = new ArrayList<>();

    String spql = """
      SELECT * {
        ?s (%s) ?o .
        ?o rdfs:label ?name .
      }
      """.formatted(PARENT_PREDICATES);

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, spql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new ParentDto(bs.getValue("o").stringValue(), bs.getValue("name").stringValue(), null));
        }
      }
    }

    return result;
  }

  public boolean iriExists(String iri) {
    boolean result = false;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, "SELECT * WHERE { ?s ?p ?o.} limit 1");
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          result = true;
        }
      }
    }
    return result;
  }

  public List<String> getIM1SchemeOptions() {
    List<String> results = new ArrayList<>();

    String sql = """
      SELECT DISTINCT ?o
      WHERE {
        ?s ?im1Scheme ?o .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      qry.setBinding("im1Scheme", iri(IM.IM_1_SCHEME));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          results.add(bs.getValue("o").stringValue());
        }
      }
    }
    return results;
  }

  public boolean predicatePathExists(String subject, TTIriRef predicate, String object) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      BooleanQuery qry = conn.prepareBooleanQuery("ASK { ?s (?p)* ?o.}");
      qry.setBinding("p", iri(predicate.getIri()));
      qry.setBinding("s", iri(subject));
      qry.setBinding("o", iri(object));
      return qry.evaluate();
    }
  }

  public EntityDocument getOSDocument(String iri) {
    EntityDocument result = new EntityDocument().setIri(iri);
    hydrateCoreProperties(result);
    hydrateTerms(result);
    hydrateIsAs(result);
    hydrateSubsumptionCount(result);
    return result;
  }

  private void hydrateCoreProperties(EntityDocument entityDocument) {
    String spql = """
      SELECT ?iri ?name ?preferredName ?status ?statusName ?code ?scheme ?schemeName ?type ?typeName ?usageTotal ?extraType ?extraTypeName
      WHERE {
        GRAPH ?scheme { ?iri rdf:type ?type }
        Optional { ?iri rdfs:label ?name.}
        Optional { ?iri im:preferredName ?preferredName.}
        Optional {?iri im:isA ?extraType.
          ?extraType rdfs:label ?extraTypeName.
          filter (?extraType in (im:dataModelProperty, im:DataModelEntity))
        }
        Optional {?type rdfs:label ?typeName}
        Optional {?iri im:status ?status.
          Optional {?status rdfs:label ?statusName}
        }
        Optional {?scheme rdfs:label ?schemeName }
        Optional {?iri im:code ?code.}
        Optional {?iri im:usageTotal ?usageTotal.}
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(addSparqlPrefixes(spql));
      tupleQuery.setBinding("iri", iri(entityDocument.getIri()));
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        if (qr.hasNext()) {
          hydrateCorePropertiesSetEntityDocumentProperties(entityDocument, qr);
        }
      }
    }
  }

  private void hydrateTerms(EntityDocument entityDocument) {
    String sparql = """
      SELECT ?termCode ?synonym ?termCodeStatus
      WHERE {
        ?iri im:hasTermCode ?tc.
        Optional {?tc im:code ?termCode}
        Optional  {?tc rdfs:label ?synonym}
        Optional  {?tc im:status ?termCodeStatus}
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(addSparqlPrefixes(sparql));
      tupleQuery.setBinding("iri", literal(entityDocument.getIri()));
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String termCode = null;
          String synonym = null;
          TTIriRef status = null;
          if (rs.hasBinding("synonym"))
            synonym = rs.getValue("synonym").stringValue().toLowerCase();
          if (rs.hasBinding("termCode"))
            termCode = rs.getValue("termCode").stringValue();
          if (rs.hasBinding("termCodeStatus"))
            status = TTIriRef.iri(rs.getValue("termCodeStatus").stringValue());

          if (synonym != null) {
            SearchTermCode tc = getTermCode(entityDocument, synonym);
            if (tc == null) {
              entityDocument.addTermCode(synonym, termCode, status);
              addKey(entityDocument, synonym);
            } else if (termCode != null) {
              tc.setCode(termCode);
            }
          } else if (termCode != null) {
            SearchTermCode tc = getTermCodeFromCode(entityDocument, termCode);
            if (tc == null)
              entityDocument.addTermCode(null, termCode, status);
          }

        }
      }
    }
  }

  private void hydrateSubsumptionCount(EntityDocument entityDocument) {
    String sparql = """
      SELECT (count(?subType) as ?subsumptions)
      WHERE {
        ?iri ^im:isA ?subType.
        ?subType im:status im:Active.
      }
      """;
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(addSparqlPrefixes(sparql));
      tupleQuery.setBinding("iri", literal(entityDocument.getIri()));

      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          entityDocument.setSubsumptionCount(Integer.parseInt(rs.getValue("subsumptions").stringValue()));
        }
      }
    }
  }

  private SearchTermCode getTermCode(EntityDocument blob, String term) {
    for (SearchTermCode tc : blob.getTermCode()) {
      if (tc.getTerm() != null && tc.getTerm().equals(term))
        return tc;
    }
    return null;
  }

  private void addKey(EntityDocument blob, String term) {
    term = term.replaceAll("[ '()\\-_./,]", "").toLowerCase();
    if (term.length() > 30)
      term = term.substring(0, 30);
  }

  private SearchTermCode getTermCodeFromCode(EntityDocument blob, String code) {
    for (SearchTermCode tc : blob.getTermCode()) {
      if (tc.getCode() != null && tc.getCode().equals(code))
        return tc;
    }
    return null;
  }

  private void hydrateIsAs(EntityDocument entityDocument) {
    String spql = """
      SELECT ?superType
      WHERE {
        ?iri im:isA ?superType.
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery tupleQuery = conn.prepareTupleQuery(addSparqlPrefixes(spql));
      tupleQuery.setBinding("iri", literal(entityDocument.getIri()));
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          entityDocument.getIsA().add(TTIriRef.iri(rs.getValue("superType").stringValue()));
        }
      }
    }
  }

  public List<TTIriRef> getProperties() {
    List<TTIriRef> result = new ArrayList<>();

    String spql = """
      SELECT ?s ?name
      WHERE {
        ?s rdf:type rdf:PropertyRef ;
        rdfs:label ?name .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, spql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
        }
      }
    }

    return result;
  }

  public Set<String> getDistillation(String iris) {
    Set<String> isas = new HashSet<>();

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sql = """
        SELECT ?child
        WHERE {
        VALUES ?child { %s }
        VALUES ?parent { %s }
        ?child ?isA ?parent .
        FILTER (?child != ?parent)}
        """.formatted(iris, iris);
      TupleQuery qry = conn.prepareTupleQuery(sql);
      qry.setBinding("isA", iri(IM.IS_A));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          isas.add(bs.getValue("child").stringValue());

        }
      }
      return isas;
    }
  }

  public Set<String> getPredicates(String iri) {
    Set<String> predicates = new HashSet<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sparql = """
        SELECT DISTINCT ?p
        WHERE {
          ?s ?p ?o .
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(sparql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          predicates.add(bs.getValue("p").stringValue());
        }
      }
    }
    return predicates;
  }

  public List<TTIriRef> findDataModelsFromProperty(String propIri) {
    List<TTIriRef> dmList = new ArrayList<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sparql = """
        SELECT ?dm ?dmName
        WHERE {
          ?dm sh:property ?prop .
          ?dm rdfs:label ?dmName .
          ?prop sh:path ?propIri .
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sparql));
      qry.setBinding("propIri", iri(propIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          dmList.add(new TTIriRef(bs.getValue("dm").stringValue(), bs.getValue("dmName").stringValue()));
        }
      }
    }
    return dmList;

  }

  public List<ConceptContextMap> getConceptContextMaps(String iri) {
    List<ConceptContextMap> result = new ArrayList<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sparql = """
        SELECT ?nodeName ?sourceVal ?sourceRegex ?propertyName ?publisherName ?systemName ?schema ?table ?field
        WHERE {
          ?map ?imConcept ?concept .
          ?node ?imHasMap ?map ;
          ?imTargetProperty ?property ;
          ?rdfsLabel ?nodeName .
          ?property ?rdfsLabel ?propertyName .
          ?context ?imContextNode ?node ;
          ?imSourcePublisher ?publisher .
          ?publisher rdfs:label ?publisherName .
          ?map ?imSourceValue ?sourceVal .
          OPTIONAL {
            ?context ?imSourceSystem ?system .
            ?system ?rdfsLabel ?systemName
          }
          OPTIONAL { ?context ?imSourceSchema ?schema }
          OPTIONAL { ?context ?imSourceTable ?table }
          OPTIONAL { ?context ?imSourceField ?field }
          OPTIONAL { ?context ?imSourceConcept ?concept }
        }
        ORDER BY ?nodeName ?sourceVal ?publisherName
        """;
      TupleQuery qry = conn.prepareTupleQuery(sparql);
      qry.setBinding("concept", iri(iri));
      qry.setBinding("imConcept", iri(IM.CONCEPT));
      qry.setBinding("imHasMap", iri(IM.HAS_MAP));
      qry.setBinding("rdfsLabel", iri(RDFS.LABEL));
      qry.setBinding("imContextNode", iri(IM.CONTEXT_NODE));
      qry.setBinding("imTargetProperty", iri(IM.TARGET_PROPERTY));
      qry.setBinding("imSourcePublisher", iri(IM.SOURCE_PUBLISHER));
      qry.setBinding("imSourceSystem", iri(IM.SOURCE_SYSTEM));
      qry.setBinding("imSourceSchema", iri(IM.SOURCE_SCHEMA));
      qry.setBinding("imSourceTable", iri(IM.SOURCE_TABLE));
      qry.setBinding("imSourceField", iri(IM.SOURCE_FIELD));
      qry.setBinding("imSourceValue", iri(IM.SOURCE_VALUE));
      qry.setBinding("imSourceRegex", iri(IM.SOURCE_REGEX));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (result.stream().noneMatch(r -> r.getNode().equals(bs.getValue("nodeName").stringValue()) || r.getValue().equals(bs.getValue("sourceVal").stringValue()) || (bs.getValue("sourceRegex") != null && r.getRegex().equals(bs.getValue("sourceRegex").stringValue())))) {
            ConceptContextMap conceptContextMap = new ConceptContextMap();
            conceptContextMap.setId(UUID.randomUUID().toString());
            conceptContextMap.setNode(bs.getValue("nodeName").stringValue());
            conceptContextMap.setValue(bs.getValue("sourceVal").stringValue());
            conceptContextMap.setProperty(bs.getValue("propertyName").stringValue());
            if (bs.getValue("sourceRegex") != null)
              conceptContextMap.setRegex(bs.getValue("sourceRegex").stringValue());
            Context context = new Context();
            context.setPublisher(bs.getValue("publisherName").stringValue());
            context.setSystem(bs.getValue("systemName").stringValue());
            context.setSchema(bs.getValue("schema").stringValue());
            context.setTable(bs.getValue("table").stringValue());
            context.setField(bs.getValue("field").stringValue());
            List<Context> contexts = new ArrayList<>();
            contexts.add(context);
            conceptContextMap.setContext(contexts);
            result.add(conceptContextMap);
          }
        }
      }
    }
    return result;
  }

  public String checkPropertyType(String propIri) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String query = """
        SELECT ?objectProperty ?dataProperty
        WHERE {"
          bind(exists{?propIri ?isA ?objProp} as ?objectProperty)
          bind(exists{?propIri ?isA ?dataProp} as ?dataProperty)
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(query);
      qry.setBinding("propIri", iri(propIri));
      qry.setBinding("isA", iri(IM.IS_A));
      qry.setBinding("objProp", iri(IM.DATAMODEL_OBJECTPROPERTY));
      qry.setBinding("dataProp", iri(IM.DATAMODEL_DATAPROPERTY));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (bs.hasBinding("objectProperty")) return IM.DATAMODEL_OBJECTPROPERTY;
          else if (bs.hasBinding("dataProperty")) return IM.DATAMODEL_DATAPROPERTY;
        }
      }
    }
    return null;
  }
}
