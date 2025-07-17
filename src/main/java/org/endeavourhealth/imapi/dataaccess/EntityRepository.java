package org.endeavourhealth.imapi.dataaccess;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.*;
import org.endeavourhealth.imapi.cache.TimedCache;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;
import org.endeavourhealth.imapi.vocabulary.Namespace;

import java.util.*;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.*;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArray;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asArrayList;

@Slf4j
public class EntityRepository {
  static final String PARENT_PREDICATES = "rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf|im:isSubsetOf";
  private static final TimedCache<String, String> iriNameCache = new TimedCache<>("IriNameCache", 30, 5, 100);
  private static final String EXECUTING = "Executing...";
  private static final String RETRIEVING = "Retrieving...";
  private int row = 0;

  private static void hydrateCorePropertiesSetEntityDocumentProperties(EntityDocument entityDocument, TupleQueryResult qr) {
    BindingSet rs = qr.next();
    entityDocument.setName(rs.getValue("name").stringValue());
    entityDocument.addTermCode(entityDocument.getName(), null, null, null);

    if (rs.hasBinding("preferredName")) entityDocument.setPreferredName(rs.getValue("preferredName").stringValue());

    if (rs.hasBinding("code")) entityDocument.setCode(rs.getValue("code").stringValue());

    if (rs.hasBinding("scheme")) entityDocument.setScheme(new TTIriRef(rs.getValue("scheme").stringValue()));

    if (rs.hasBinding("status")) {
      TTIriRef status = TTIriRef.iri(rs.getValue("status").stringValue());
      if (rs.hasBinding("statusName")) status.setName(rs.getValue("statusName").stringValue());
      entityDocument.setStatus(status);
    }

    TTIriRef type = TTIriRef.iri(rs.getValue("type").stringValue());
    if (rs.hasBinding("typeName")) type.setName(rs.getValue("typeName").stringValue());
    entityDocument.addType(type);

    if (rs.hasBinding("extraType")) {
      TTIriRef extraType = TTIriRef.iri(rs.getValue("extraType").stringValue(), rs.getValue("extraTypeName").stringValue());
      entityDocument.addType(extraType);
      if (extraType.equals(TTIriRef.iri(Namespace.IM + "DataModelEntity"))) {
        int usageTotal = 2000000;
        entityDocument.setUsageTotal(usageTotal);
      }
    }
    if (rs.hasBinding("usageTotal")) {
      entityDocument.setUsageTotal(((Literal) rs.getValue("usageTotal")).intValue());
    }
  }

  public static void getIriNames(IMDB conn, Set<TTIriRef> iris) {

    if (iris == null || iris.isEmpty()) return;

    Set<String> toFetch = new HashSet<>();

    iris.forEach(i -> {
      String name = iriNameCache.get(i.getIri());
      if (name != null) i.setName(name);
      else toFetch.add("<" + i.getIri() + ">");
    });

    if (toFetch.isEmpty()) {
      return;
    }

    String sql = """
      SELECT ?iri ?label ?description
      WHERE {
        ?iri rdfs:label ?label.
        Optional { ?iri rdfs:comment ?description }
        filter (?iri in (%s))
      }
      """.formatted(String.join(",", toFetch));

    TupleQuery qry = conn.prepareTupleSparql(sql);
    try (TupleQueryResult rs = qry.evaluate()) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        TTIriRef iri = TTIriRef.iri(bs.getValue("iri").stringValue());
        iris.stream().filter(i -> i.equals(iri)).findFirst().ifPresent(i -> {
          i.setName(bs.getValue("label").stringValue());
          iriNameCache.put(i.getIri(), i.getName());
          if (bs.getValue("description") != null) i.setDescription(bs.getValue("description").stringValue());
        });
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public TTIriRef getEntityReferenceByIri(String iri, Graph graph) {
    TTIriRef result = new TTIriRef();
    String sql = """
      SELECT ?sname
      WHERE {
        ?s rdfs:label ?sname
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
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

  public List<TTEntity> getEntitiesByType(String type, Integer offset, Integer limit, Graph graph, String... predicates) {
    Map<String, TTEntity> entities = new HashMap<>();
    String predicateList = Arrays.stream(predicates).map(p -> "<" + p + ">").collect(Collectors.joining(" "));
    String sql = """
      ?entity ?predicate ?object ?predicate2 ?object2
      select *
      where {
      values ?predicate {%s}
      ?entity rdf:type ?type.
      ?entity ?predicate ?object.
      optional {?object ?predicate2 ?object2.
      filter (isBlank(?object2)}
      }
      order by ?object
      offset %s limit %s
      """.formatted(predicateList, offset, limit);

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("type", iri(type));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("entity").stringValue();
          entities.putIfAbsent(iri, new TTEntity().setIri(iri));
          TTEntity entity = entities.get(iri);
          Value object = bs.getValue("folder");
          if (object.isIRI()) {
            entity.addObject(TTIriRef.iri(bs.getValue("predicate").stringValue()), TTIriRef.iri(object.stringValue()));
          } else if (object.isBNode()) {
            if (entity.get(TTIriRef.iri(bs.getValue("predicate").stringValue())) == null) {
              entity.set(TTIriRef.iri(bs.getValue("predicate").stringValue()), new TTNode());
            }
            Value object2 = bs.getValue("object2");
            if (object2.isIRI()) {
              entity.get(TTIriRef.iri(bs.getValue("predicate").stringValue()))
                .asNode().addObject(TTIriRef.iri(bs.getValue("predicate2").stringValue()), TTIriRef.iri(object2.stringValue()));
            } else entity.get(TTIriRef.iri(bs.getValue("predicate").stringValue()))
              .asNode().addObject(TTIriRef.iri(bs.getValue("predicate2").stringValue()), TTLiteral.literal(object2.stringValue()));
          } else {
            entity.addObject(TTIriRef.iri(bs.getValue("predicate").stringValue()), TTLiteral.literal(object.stringValue()));
          }
        }
      }
    }
    return new ArrayList<>(entities.values());
  }

  public Map<String, Set<TTIriRef>> getTypesByIris(Set<String> stringIris, Graph graph) {
    Map<String, Set<TTIriRef>> iriToTypesMap = new HashMap<>();
    StringJoiner iriLine = new StringJoiner(" ");
    for (String stringIri : stringIris) {
      iri(stringIri);
      iriLine.add("<" + stringIri + ">");
    }

    String sql = """
      SELECT ?s ?o ?oname
      WHERE {
        ?s rdf:type ?o .
        ?o rdfs:label ?oname .
        VALUES ?s { %s }
      }
      """.formatted(iriLine.toString());

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
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

  public List<SearchResultSummary> getEntitySummariesByIris(Set<String> stringIris, Graph graph) {
    List<SearchResultSummary> summaries = new ArrayList<>();
    StringJoiner iriLine = new StringJoiner(" ");
    for (String stringIri : stringIris) {
      iri(stringIri);
      iriLine.add("<" + stringIri + ">");
    }

    String sql = """
      SELECT ?s ?sname ?scode ?sstatus ?sstatusname ?sdescription ?sscheme ?sschemename
      WHERE {
        ?s rdfs:label ?sname .
        OPTIONAL { ?s im:code ?scode . }
        OPTIONAL {
          ?s im:status ?sstatus .
          ?sstatus rdfs:label ?sstatusname .
        }
        OPTIONAL {
          ?s im:scheme ?sscheme .
          ?sscheme rdfs:label ?sschemename
        }
        OPTIONAL { ?s rdfs:comment ?sdescription .}
        VALUES ?s { %s }
      }
      """.formatted(iriLine.toString());

    Map<String, Set<TTIriRef>> iriToTypesMap = getTypesByIris(stringIris, graph);

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
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
            .setScheme(new TTIriRef(bs.getValue("sscheme").stringValue(), (bs.getValue("sschemename") == null ? "" : bs.getValue("sschemename").stringValue())))
            .setType(types)
            .setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue()))
            .setDescription(bs.getValue("sdescription") == null ? "" : bs.getValue("sdescription").stringValue());
          summaries.add(summary);
        }
      }
    }
    return summaries;
  }

  public SearchResultSummary getEntitySummaryByIri(String iri, Graph graph) {
    SearchResultSummary result = new SearchResultSummary();
    result.setIri(iri);

    String sql = """
      SELECT ?sname ?type ?typeName ?scode ?sstatus ?sstatusname ?sdescription ?sscheme ?sschemename ?intervalUnit ?intervalUnitName ?qualifier ?qualifierName
      WHERE {
        ?s rdfs:label ?sname .
        OPTIONAL { ?s im:code ?scode . }
        OPTIONAL {
          ?s im:status ?sstatus .
          ?sstatus rdfs:label ?sstatusname .
        }
        OPTIONAL {
          ?s im:scheme ?sscheme .
          ?sscheme rdfs:label ?sschemename .
        }
        OPTIONAL {
          ?s rdf:type ?type .
          ?type rdfs:label ?typeName .
        }
        OPTIONAL { ?s rdfs:comment ?sdescription . }
        OPTIONAL {
          ?s im:intervalUnit ?intervals .
          ?intervalUnit rdfs:subClassOf ?intervals .
          ?intervalUnit rdfs:label ?intervalUnitName .
          filter (?intervalUnit!= ?intervals)
        }
        OPTIONAL {
          ?s im:datatypeQualifier ?qualifier .
          ?qualifier rdfs:label ?qualifierName .
        }
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (result.getName() == null) {
            result.setIri(iri).setName(bs.getValue("sname").stringValue())
              .setCode(bs.getValue("scode") == null ? "" : bs.getValue("scode").stringValue())
              .setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue()))
              .setDescription(bs.getValue("sdescription") == null ? "" : bs.getValue("sdescription").stringValue());
          }
          if (bs.hasBinding("type")) {
            result.addType(TTIriRef.iri(bs.getValue("type").stringValue())
              .setName(bs.getValue("typeName").stringValue()));
          }

          if (bs.hasBinding("sscheme")) {
            result.setScheme(new TTIriRef(bs.getValue("sscheme").stringValue(), (bs.getValue("sschemename") == null ? "" : bs.getValue("sschemename").stringValue())));
          }
          if (bs.hasBinding("intervalUnit")) {
            result.addIntervalUnit(TTIriRef.iri(bs.getValue("intervalUnit").stringValue())
              .setName(bs.getValue("intervalUnitName").stringValue()));
          }
          if (bs.hasBinding("qualifier")) {
            TTIriRef qualifier = TTIriRef.iri(bs.getValue("qualifier").stringValue())
              .setName(bs.getValue("qualifierName").stringValue());
            if (result.getQualifier() == null || (!result.getQualifier().contains(qualifier))) {
              result.addQualifier(qualifier);
            }
          }
        }
      }
    }
    return result;
  }

  public List<ParentDto> findParentHierarchies(String iri, Graph graph) {
    List<ParentDto> result = new ArrayList<>();

    String spql = """
      SELECT *
      WHERE {
        ?s (%s) ?o .
        ?o rdfs:label ?name .
      }
      """.formatted(PARENT_PREDICATES);

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(spql);
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

  public boolean iriExists(String iri, Graph graph) {
    boolean result = false;

    try (IMDB conn = IMDB.getConnection(graph)) {
      String sql = """
        SELECT *
        WHERE {
          ?s ?p ?o.
        }
        limit 1
        """;
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          result = true;
        }
      }
    }
    return result;
  }

  public List<String> getIM1SchemeOptions(Graph graph) {
    List<String> results = new ArrayList<>();

    String sql = """
      SELECT DISTINCT ?o
      WHERE {
        ?s ?im1Scheme ?o .
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("im1Scheme", Namespace.IM1.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          results.add(bs.getValue("o").stringValue());
        }
      }
    }
    return results;
  }

  public boolean predicatePathExists(String subject, TTIriRef predicate, String object, Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      String sprql = """
          ASK {
            ?s (?p)* ?o.
          }
        """;
      BooleanQuery qry = conn.prepareBooleanSparql(sprql);
      qry.setBinding("p", iri(predicate.getIri()));
      qry.setBinding("s", iri(subject));
      qry.setBinding("o", iri(object));
      return qry.evaluate();
    }
  }

  public EntityDocument getOSDocument(String iri, Graph graph) {
    EntityDocument result = new EntityDocument().setIri(iri);
    hydrateCoreProperties(result, graph);
    hydrateTerms(result, graph);
    hydrateIsAs(result, graph);
    hydrateSubsumptionCount(result, graph);
    return result;
  }

  private void hydrateCoreProperties(EntityDocument entityDocument, Graph graph) {
    String spql = """
      SELECT ?iri ?name ?preferredName ?status ?statusName ?code ?scheme ?schemeName ?type ?typeName ?usageTotal ?extraType ?extraTypeName
      WHERE {
        ?iri rdf:type ?type .
        Optional { ?iri rdfs:label ?name.}
        Optional { ?iri im:preferredName ?preferredName.}
        Optional {
          ?iri im:isA ?extraType.
          ?extraType rdfs:label ?extraTypeName.
          filter (?extraType in (im:dataModelProperty, im:DataModelEntity))
        }
        Optional {?type rdfs:label ?typeName}
        Optional {
          ?iri im:status ?status.
          Optional {?status rdfs:label ?statusName}
        }
        Optional {
          ?iri im:scheme ?scheme
          Optional {?scheme rdfs:label ?schemeName .}
        }
        Optional {?iri im:code ?code.}
        Optional {?iri im:usageTotal ?usageTotal.}
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery tupleQuery = conn.prepareTupleSparql(spql);
      tupleQuery.setBinding("iri", iri(entityDocument.getIri()));
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        if (qr.hasNext()) {
          hydrateCorePropertiesSetEntityDocumentProperties(entityDocument, qr);
        }
      }
    }
  }

  private void hydrateTerms(EntityDocument entityDocument, Graph graph) {
    String sparql = """
      SELECT ?termCode ?synonym ?termCodeStatus
      WHERE {
      ?iri im:hasTermCode ?tc.
        Optional {?tc im:code ?termCode}
        Optional  {?tc rdfs:label ?synonym}
        Optional  {?tc im:status ?termCodeStatus}
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery tupleQuery = conn.prepareTupleSparql(sparql);
      tupleQuery.setBinding("iri", literal(entityDocument.getIri()));
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          String termCode = null;
          String synonym = null;
          TTIriRef status = null;
          if (rs.hasBinding("synonym")) synonym = rs.getValue("synonym").stringValue().toLowerCase();
          if (rs.hasBinding("termCode")) termCode = rs.getValue("termCode").stringValue();
          if (rs.hasBinding("termCodeStatus")) status = TTIriRef.iri(rs.getValue("termCodeStatus").stringValue());

          if (synonym != null) {
            SearchTermCode tc = getTermCode(entityDocument, synonym);
            if (tc == null) {
              entityDocument.addTermCode(synonym, termCode, status, null);
            } else if (termCode != null) {
              tc.setCode(termCode);
            }
          } else if (termCode != null) {
            SearchTermCode tc = getTermCodeFromCode(entityDocument, termCode);
            if (tc == null) entityDocument.addTermCode(null, termCode, status, null);
          }

        }
      }
    }
  }

  private void hydrateSubsumptionCount(EntityDocument entityDocument, Graph graph) {
    String sparql = """
      SELECT (count(?subType) as ?subsumptions)
      WHERE {
        ?iri ^im:isA ?subType.
        ?subType im:status im:Active.
      }
      """;
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery tupleQuery = conn.prepareTupleSparql(sparql);
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
      if (tc.getTerm() != null && tc.getTerm().equals(term)) return tc;
    }
    return null;
  }

  private SearchTermCode getTermCodeFromCode(EntityDocument blob, String code) {
    for (SearchTermCode tc : blob.getTermCode()) {
      if (tc.getCode() != null && tc.getCode().equals(code)) return tc;
    }
    return null;
  }

  private void hydrateIsAs(EntityDocument entityDocument, Graph graph) {
    String spql = """
      SELECT ?superType
      WHERE {
        ?iri im:isA ?superType.
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery tupleQuery = conn.prepareTupleSparql(spql);
      tupleQuery.setBinding("iri", literal(entityDocument.getIri()));
      try (TupleQueryResult qr = tupleQuery.evaluate()) {
        while (qr.hasNext()) {
          BindingSet rs = qr.next();
          entityDocument.getIsA().add(TTIriRef.iri(rs.getValue("superType").stringValue()));
        }
      }
    }
  }

  public Set<String> getPredicates(String iri, Graph graph) {
    Set<String> predicates = new HashSet<>();
    try (IMDB conn = IMDB.getConnection(graph)) {
      String sparql = """
        SELECT DISTINCT ?p
        WHERE {
          ?s ?p ?o .
        }
        """;
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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

  public List<TTIriRef> findAncestorsByType(String childIri, VocabEnum relationshipIri, List<String> candidateAncestorIris, Graph graph) {
    List<TTIriRef> result = new ArrayList<>();
    String sql = """
      SELECT ?aname
      WHERE {
        ?c ?r ?a .
        ?a rdfs:label ?aname .
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("c", Values.iri(childIri));
      qry.setBinding("r", relationshipIri.asDbIri());

      for (String candidate : candidateAncestorIris) {
        qry.setBinding("a", Values.iri(candidate));
        try (TupleQueryResult rs = qry.evaluate()) {
          if (rs.hasNext()) {
            BindingSet bs = rs.next();
            result.add(new TTIriRef().setIri(candidate).setName(bs.getValue("aname").stringValue()));
          }
        }
      }
    }
    return result;
  }

  /**
   * An alternative method of getting an entity definition assuming all predicates inclided
   *
   * @param iri of the entity
   * @return a bundle including the entity and the predicate names
   */
  public TTBundle getBundle(String iri) {
    return getBundle(iri, null, false, null);
  }

  /**
   * An alternative method of getting an entity definition
   *
   * @param iri        of the entity
   * @param predicates List of predicates to `include`
   * @return bundle with entity and map of predicate names
   */
  public TTBundle getBundle(String iri, Set<String> predicates) {
    return getBundle(iri, predicates, false, null);
  }

  /**
   * An alternative method of getting an entity definition
   *
   * @param iri               of the entity
   * @param predicates        List of predicates
   * @param excludePredicates Flag denoting if predicate list is inclusion or exclusion
   * @return                  Bundle
   */
  public TTBundle getBundle(String iri, Set<String> predicates, boolean excludePredicates, Graph graph) {
    return getBundle(iri, predicates, excludePredicates, 5, graph);
  }

  public TTBundle getBundle(String iri, Set<String> predicates, boolean excludePredicates, int depth, Graph graph) {
    if (null == graph) graph = Graph.IM;
    TTBundle bundle = new TTBundle().setEntity(new TTEntity().setIri(iri)).setPredicates(new HashMap<>());
    if (null != predicates && predicates.contains(RDFS.LABEL.toString()) && !predicates.contains(RDFS.COMMENT.toString())) {
      Set<String> predicatesPlus = new HashSet<>(predicates);
      predicatesPlus.add(RDFS.COMMENT.toString());
      predicates = predicatesPlus;
    }

    StringJoiner sql = getBundleSparql(predicates, excludePredicates, depth);

    try (IMDB conn = IMDB.getConnection(graph)) {
      GraphQuery qry = conn.prepareGraphSparql(sql.toString());
      qry.setBinding("entity", Values.iri(iri));
      try (GraphQueryResult gs = qry.evaluate()) {
        Map<String, TTValue> valueMap = new HashMap<>();
        for (org.eclipse.rdf4j.model.Statement st : gs) {
          processStatement(bundle, valueMap, iri, st);
        }
        Set<TTIriRef> iris = TTManager.getIrisFromNode(bundle.getEntity());
        getIriNames(conn, iris);
        HashMap<String, String> iriToName = new HashMap<>();
        for (TTIriRef ttIriRef : iris) {
          iriToName.put(ttIriRef.getIri(), ttIriRef.getName());
        }
        setNames(bundle.getEntity(), iriToName);
        iris.forEach(bundle::addPredicate);
      }
      return bundle;
    }
  }

  private void setNames(TTValue subject, Map<String, String> iriToName) {
    if (subject.isIriRef() && (subject.asIriRef().getName() == null || subject.asIriRef().getName().isEmpty()))
      subject.asIriRef().setName(iriToName.get(subject.asIriRef().getIri()));
    else if (subject.isNode() && subject.asNode().getPredicateMap() != null) {
      for (Map.Entry<TTIriRef, TTArray> entry : subject.asNode().getPredicateMap().entrySet()) {
        for (TTValue value : entry.getValue().getElements()) {
          if (value.isIriRef() && (value.asIriRef().getName() == null || value.asIriRef().getName().isEmpty()))
            value.asIriRef().setName(iriToName.get(value.asIriRef().getIri()));
          else if (value.isNode()) setNames(value, iriToName);
        }
      }

    }
  }

  public void getNames(Set<TTIriRef> iris, Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      getIriNames(conn, iris);
    }
  }

  /**
   * creates ranges for properties without ranges where the super properties have them
   */
  public void inheritRanges(Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      inheritRanges(conn, graph);
    }
  }

  public void inheritRanges(IMDB conn, Graph graph) {
    String sql = """
      INSERT { ?property rdfs:range ?range }
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
      """;
    Update upd = conn.prepareInsertSparql(sql, graph);
    upd.execute();
  }

  /**
   * Returns an entity iri and name from a code or a term code
   *
   * @param code the code or description id or term code
   * @return iri and name of entity
   */
  public Set<Entity> getCoreFromCode(String code, List<Namespace> namespaces, Graph graph) {
    List<String> schemes = namespaces.stream().map(Namespace::toString).toList();

    String sql = """
      SELECT ?concept ?label ?type
      WHERE {
        values ?codeProperty {im:code im:codeId im:alternativeCode}
        %s
        {
          ?concept ?codeProperty ?code.
          filter (isIri(?concept))
          ?concept rdfs:label ?label.
          ?concept im:scheme ?scheme.
          filter (?scheme in(sn:,im:))
        }
        UNION {
          ?concept im:hasTermCode ?node.
          ?node im:code ?code.
          ?concept rdfs:label ?label.
           ?concept im:scheme ?scheme.
          filter (?scheme in(sn:,im:))
        }
        UNION {
          ?legacy im:hasTermCode ?node.
          ?node im:code ?code.
          ?legacy im:matchedTo ?concept.
          ?concept rdfs:label ?label.
          ?concept im:scheme ?schemes.
        }
        UNION {
          ?legacy ?codeProperty ?code.
          ?legacy im:matchedTo ?concept.
          ?concept rdfs:label ?label.
          ?concept im:scheme ?schemes .
        }
        ?concept rdf:type ?type.
      }
      """.formatted(SparqlHelper.valueList("scheme", schemes));
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("code", Values.literal(code));
      return getConceptRefsFromResult(qry);
    }
  }


  /**
   * Returns a core entity iri and name from a legacy term
   *
   * @param term   the code or description id or term code
   * @param namespace the legacy scheme of the term
   * @return iri and name of entity
   */
  public Set<Entity> getCoreFromLegacyTerm(String term, Namespace namespace, Graph graph) {
    String sql = """
      SELECT ?concept ?label ?type
      WHERE {
          ?legacy rdfs:label ?term.
          ?legacy im:matchedTo ?concept.
          ?legacy im:hasScheme ?scheme.
          ?concept rdfs:label ?label.
          ?concept rdf:type ?type.
      }
      """;
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("term", Values.literal(term));
      qry.setBinding("scheme", namespace.asDbIri());
      return getConceptRefsFromResult(qry);
    }
  }

  /**
   * Returns an entity iri and name from a term code
   *
   * @param code   the code that is a term code
   * @param namespace the scheme of the term
   * @return set of iris and name of entity
   */
  public Set<Entity> getReferenceFromTermCode(String code, Namespace namespace, Graph graph) {
    String sql = """
      SELECT ?concept ?label ?type
      WHERE {
          ?concept im:scheme ?scheme .
          ?concept im:hasTermCode ?tc.
          ?tc im:code ?code.
          ?concept rdfs:label ?label.
          ?concept rdf:type ?type.
      }
      """;
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("code", Values.literal(code));
      qry.setBinding("scheme", namespace.asDbIri());
      return getConceptRefsFromResult(qry);
    }
  }

  public Map<String, String> getCodesToIri(Namespace namespace, Graph graph) {
    String sql = """
      SELECT ?code ?scheme ?iri ?altCode
      WHERE {
        Values ?scheme {%s}
        ?iri im:code ?code.
        OPTIONAL {?iri im:alternativeCode ?altCode}
        ?iri im:scheme ?scheme
      }
      """.formatted("<" + namespace + ">");
    return getCodes(sql, graph);
  }

  public Map<String, String> getCodeToIri(Graph graph) {
    String sql = """
      SELECT ?code ?scheme ?iri ?altCode
      WHERE {
        ?iri im:code ?code.
        OPTIONAL {?iri im:alternativeCode ?altCode}
        ?iri im:scheme ?scheme
      }
      """;
    return getCodes(sql, graph);
  }

  private Map<String, String> getCodes(String sql, Graph graph) {
    Map<String, String> codeToIri = new HashMap<>();
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
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
  public TTIriRef getReferenceFromCoreTerm(String term, Graph graph) {
    List<String> schemes = asArrayList(Namespace.IM, Namespace.SNOMED);
    String sql = """
      select ?concept ?label
      where {
        %s
        {
          ?concept rdfs:label ?term;
                   im:scheme ?scheme.
          filter(isIri(?concept))
        }
        union {
          ?tc rdfs:label ?term.
          ?concept im:hasTermCode ?tc;
                   im:scheme ?scheme.
        }
      }
      """.formatted(SparqlHelper.valueList("s", schemes));
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("term", Values.literal(term));
      return getConceptRefFromResult(qry);
    }
  }

  public Map<String, Set<String>> getAllMatchedLegacy(Graph graph) {
    String sql = """
      SELECT ?legacy ?concept
      WHERE {
        ?legacy im:matchedTo ?concept.
      }
      """;
    Map<String, Set<String>> maps = new HashMap<>();
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      TTIriRef concept = new TTIriRef();
      try (TupleQueryResult gs = qry.evaluate()) {
        while (gs.hasNext()) {
          BindingSet bs = gs.next();
          String legacy = bs.getValue("legacy").stringValue();
          maps.putIfAbsent(legacy, new HashSet<>());
          maps.get(legacy).add(bs.getValue("concept").stringValue());
          if (bs.getValue("label") != null) concept.setName(bs.getValue("label").stringValue());

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
        if (bs.getValue("label") != null) concept.setName(bs.getValue("label").stringValue());

      }
    }
    return concept;
  }

  private Set<Entity> getConceptRefsFromResult(TupleQuery qry) {
    Set<Entity> results = null;
    Set<String> iris = new HashSet<>();
    try (TupleQueryResult gs = qry.evaluate()) {
      while (gs.hasNext()) {
        BindingSet bs = gs.next();
        if (results == null) results = new HashSet<>();
        String iri = bs.getValue("concept").stringValue();
        if (!iris.contains(iri)) {
          iris.add(iri);
          Entity concept = new Entity().setIri(iri);
          concept.addType(TTIriRef.iri(bs.getValue("type").stringValue()));
          if (bs.getValue("label") != null) concept.setName(bs.getValue("label").stringValue());
          results.add(concept);
        }

      }
    }
    return results;
  }

  private StringJoiner getBundleSparql(Set<String> predicates, boolean excludePredicates, int depth) {
    StringJoiner sql = new StringJoiner(System.lineSeparator());
    sql.add("CONSTRUCT {").add("  ?entity ?1predicate ?1Level.").add("  ?1Level rdfs:label ?1Name.");
    for (int i = 1; i < depth; i++) {
      sql.add("  ?" + i + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.").add("  ?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName.").add("  ?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name.");
    }
    sql.add("} WHERE { {");

    sql.add("  ?entity ?1predicate ?1Level.").add("  ?1predicate rdfs:label ?1pName.");
    if (predicates != null && !predicates.isEmpty()) {
      StringBuilder inPredicates = new StringBuilder();
      int i = 0;
      for (String pred : predicates) {
        inPredicates.append(i > 0 ? "," : "").append("<").append(pred).append("> ");
        i++;
      }
      if (excludePredicates) sql.add("   FILTER (?1predicate NOT IN (" + inPredicates + "))");
      else sql.add("   FILTER (?1predicate IN (" + inPredicates + "))");
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

    sql.add("  OPTIONAL {?1Level rdfs:label ?1Name.").add("    FILTER (!isBlank(?1Level))}");
    for (int i = 1; i < depth; i++) {
      sql.add("  OPTIONAL {?" + (i) + "Level ?" + (i + 1) + "predicate ?" + (i + 1) + "Level.").add("    FILTER (isBlank(?" + i + "Level))").add("  OPTIONAL {?" + (i + 1) + "predicate rdfs:label ?" + (i + 1) + "pName}").add("  OPTIONAL {?" + (i + 1) + "Level rdfs:label ?" + (i + 1) + "Name").add("    FILTER (!isBlank(?" + (i + 1) + "Level))}");
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
      } else predNames.put(predicate, predicate);
    } else {
      tripleMap.putIfAbsent(predicate, TTIriRef.iri(predicate));
      predNames.put(predicate, predicate);
    }
    TTNode node;
    if (predicate.equals(RDFS.LABEL.toString())) {
      if (s.isIRI()) {
        if (subject.equals(entityIri)) {
          entity.setName(value);
        } else {
          tripleMap.putIfAbsent(subject, TTIriRef.iri(subject));
          tripleMap.get(subject).asIriRef().setName(value);
          predNames.computeIfPresent(subject, (k, v) -> value);
        }
      } else {
        tripleMap.putIfAbsent(subject, new TTNode());
        tripleMap.get(subject).asNode().set(RDFS.LABEL.asIri(), TTLiteral.literal(value, ((Literal) o).getDatatype().stringValue()));
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
        tripleMap.putIfAbsent(value, TTIriRef.iri(value));
        node.addObject(tripleMap.get(predicate).asIriRef(), tripleMap.get(value));
      } else {
        tripleMap.putIfAbsent(value, TTLiteral.literal(value, ((Literal) o).getDatatype().stringValue()));
        node.set(tripleMap.get(predicate).asIriRef(), tripleMap.get(value).asLiteral());
      }
    }
  }

  public TTArray getEntityTypes(String iri, Graph graph) {
    TTArray result = new TTArray();

    String sql = """
      SELECT ?o ?oname
      WHERE {
        ?s rdf:type ?o .
        ?o rdfs:label ?oname .
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);

      qry.setBinding("s", Values.iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();

          result.add(new TTIriRef().setIri(bs.getValue("o").stringValue()).setName(bs.getValue("oname").stringValue())

          );
        }
      }
    }

    return result;
  }

  public TTBundle getEntityPredicates(String iri, Set<String> predicates) {
    return getBundle(iri, predicates, false, null);
  }

  public List<TTIriRef> getConceptUsages(String objectIri, Integer rowNumber, Integer pageSize, Graph graph) {
    List<TTIriRef> result = new ArrayList<>();

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT DISTINCT ?s ?name
      WHERE {
        { ?s ?p ?o . }
        UNION {
          ?s sh:property ?prop .
          ?prop sh:path ?propIri .
          FILTER(?propIri = ?o)
        }
        UNION {
        ?o im:usedIn ?s.
        }
        ?s rdfs:label ?name .
        ?s im:status ?status .
        FILTER (?p != rdfs:subclassOf && ?status != im:Inactive)
      }
      """);

    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT " + pageSize + " OFFSET " + rowNumber);
    }

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
      qry.setBinding("o", iri(objectIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new TTIriRef().setIri(bs.getValue("s").stringValue()).setName(bs.getValue("name").stringValue()));
        }
      }
    }
    return result;
  }

  public Integer getConceptUsagesCount(String objectIri, Graph graph) {
    String sql = """
      SELECT (COUNT(DISTINCT ?s) AS ?cnt)
      WHERE {
        { ?s ?p ?o . }
        UNION {
          ?s sh:property ?prop .
          ?prop sh:path ?propIri .
          FILTER(?propIri = ?o)
        }
        ?s im:status ?status .
        FILTER (?p != rdfs:subclassOf && ?status != im:Inactive && ?s != ?o)
      }
      """;


    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
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

  public List<EntityReferenceNode> getEntityReferenceNodes(Set<String> stringIris, List<String> schemeIris, boolean inactive,String parentContext, Graph graph) {
    for (String stringIri : stringIris) {
      iri(stringIri);
    }
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?s ?name ?typeIri ?typeName ?order ?contextOrder ?hasChildren ?hasGrandchildren
      WHERE {
        ?s rdfs:label ?name.
        ?s rdf:type ?typeIri.
        ?typeIri rdfs:label ?typeName.
        %s
        OPTIONAL { ?s sh:order ?order . }
        BIND(EXISTS{?child (%s) ?s} AS ?hasChildren)
        BIND(EXISTS{?grandChild (%s) ?child. ?child (%s) ?s} AS ?hasGrandchildren)
      """.formatted( valueList("s", stringIris), PARENT_PREDICATES, PARENT_PREDICATES, PARENT_PREDICATES));

    if (!inactive) {
      sql.add("  OPTIONAL { ?s im:status ?status FILTER (?status != im:Inactive) }");
    }
    if (parentContext != null) {
      sql.add("""
        OPTIONAL {?s im:contextOrder ?co .
        ?co im:context %s.
        ?co sh:order ?contextOrder.}
        """.formatted("<" + parentContext + ">"));
    }
    sql.add("}");


    Map<String, EntityReferenceNode> iriMap = new HashMap<>();
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("s").stringValue();
          EntityReferenceNode refNode = iriMap.get(iri);
          if (refNode == null) {
            refNode = new EntityReferenceNode(iri).setType(new TTArray());
            iriMap.put(iri, refNode);
          }
          refNode.getType().add(TTIriRef.iri(bs.getValue("typeIri").stringValue())
            .setName(bs.getValue("typeName").stringValue()));
          if (bs.hasBinding("contextOrder")) refNode.setOrderNumber((((Literal) bs.getValue("contextOrder")).intValue()));
          else if (bs.hasBinding("order")) refNode.setOrderNumber(((Literal) bs.getValue("order")).intValue());
          else refNode.setOrderNumber(Integer.MAX_VALUE);
          refNode.setHasChildren(((Literal) bs.getValue("hasChildren")).booleanValue()).setHasGrandChildren(((Literal) bs.getValue("hasGrandchildren")).booleanValue()).setName(bs.getValue("name").stringValue());

        }
      }
    }
    return new ArrayList<>(new ArrayList<>(iriMap.values()));
  }

  public List<EntityReferenceNode> getAsEntityReferenceNodes(List<String> iris, Graph graph) {
    List<EntityReferenceNode> result = new ArrayList<>();

    Map<String,Integer> orderMap = new HashMap<>();
    for (int i = 0; i < iris.size(); i++) {
      orderMap.put(iris.get(i), i);
    }
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT distinct ?entity ?name ?typeIri ?typeName ?hasChildren ?hasGrandchildren
      WHERE {
        %s
        ?entity rdfs:label ?name
        OPTIONAL { ?entity rdf:type ?typeIri .
          OPTIONAL { ?typeIri rdfs:label ?typeName . }
        }
        BIND(EXISTS{?child (%s) ?entity} AS ?hasChildren)
        BIND(EXISTS{?grandChild (%s) ?child. ?child (%s) ?entity} AS ?hasGrandchildren)
      }
      """.formatted(valueList("entity", iris), PARENT_PREDICATES, PARENT_PREDICATES, PARENT_PREDICATES));
    Map<String, EntityReferenceNode> entityMap = new HashMap<>();

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("entity").stringValue();
          EntityReferenceNode refNode = entityMap.get(iri);
          if (refNode == null) {
            refNode = new EntityReferenceNode(bs.getValue("entity").stringValue());
            result.add(refNode);
            entityMap.put(iri, refNode);
            refNode.setOrderNumber(orderMap.get(iri));
          }
          refNode.setName(bs.getValue("name").stringValue());
          if (bs.getValue("hasChildren") != null) {
            refNode.setHasChildren(((Literal) bs.getValue("hasChildren")).booleanValue());
          }
          if (bs.getValue("hasGrandchildren") != null) {
            refNode.setHasGrandChildren(((Literal) bs.getValue("hasGrandchildren")).booleanValue());
          }
          if (bs.getValue("typeIri") != null && bs.getValue("typeName") != null)
            refNode.addType(new TTIriRef(bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()));
        }
      }
    }
    //Sorted by initial list order?
    //result = result.stream()
     // .sorted(Comparator.comparing(EntityReferenceNode::isHasChildren).reversed())
     // .collect(Collectors.toList());
    return result;
  }


  public EntityReferenceNode getEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive, Graph graph) {
    TTArray types = new TTArray();
    EntityReferenceNode result = new EntityReferenceNode(iri).setType(types);

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?name ?typeIri ?typeName ?order ?hasChildren ?hasGrandchildren
      WHERE {
        %s
        ?s im:scheme ?scheme ;
           rdfs:label ?name .
        OPTIONAL { ?s sh:order ?order . }
        OPTIONAL { ?s rdf:type ?typeIri .
          OPTIONAL { ?typeIri rdfs:label ?typeName . }
        }
        BIND(EXISTS{?child (%s) ?s} AS ?hasChildren)
        BIND(EXISTS{?grandChild (%s) ?child. ?child (%s) ?s} AS ?hasGrandchildren)
      """.formatted(valueList("scheme", schemeIris), PARENT_PREDICATES, PARENT_PREDICATES, PARENT_PREDICATES));

    if (!inactive) {
      sql.add("  OPTIONAL { ?s im:status ?status FILTER (?status != im:Inactive) }");
    }

    sql.add("}");

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          BindingSet bs = rs.next();

          if (bs.hasBinding("order")) result.setOrderNumber(((Literal) bs.getValue("order")).intValue());
          else result.setOrderNumber(Integer.MAX_VALUE);

          result.setHasChildren(((Literal) bs.getValue("hasChildren")).booleanValue()).setHasGrandChildren(((Literal) bs.getValue("hasGrandchildren")).booleanValue()).setName(bs.getValue("name").stringValue());

          if (bs.getValue("typeIri") != null && bs.getValue("typeName") != null)
            types.add(new TTIriRef(bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()));

          while (rs.hasNext()) {
            bs = rs.next();
            types.add(new TTIriRef(bs.getValue("typeIri").stringValue(), bs.getValue("typeName").stringValue()));
          }
        } else throw new IllegalArgumentException("Iri does not exist: " + iri);
      }
    }

    return result;
  }

  public Pageable<TTIriRef> findImmediateChildrenPagedByIriWithTotalCount(String parentIri, List<String> schemeIris, Integer rowNumber,
                                                                          Integer pageSize, boolean inactive,
                                                                          List<String> entityTypes, Graph graph) {
    List<TTIriRef> children = new ArrayList<>();
    Pageable<TTIriRef> result = new Pageable<>();

    StringJoiner sqlCount = new StringJoiner(System.lineSeparator()).add("SELECT (COUNT(?c) as ?count)").add("WHERE {").add("  ?c (" + PARENT_PREDICATES + ") ?p .");
    if (!inactive) {
      sqlCount.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
    }
    sqlCount.add("}");

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("SELECT ?c ?cname ").add("WHERE {").add("  ?c ( " + PARENT_PREDICATES + ") ?p .");
    if (schemeIris != null && !schemeIris.isEmpty()) {
      sql.add("?c im:scheme ?scheme . ?c rdfs:label ?cname .").add(valueList("scheme", schemeIris));
    } else {
      sql.add("?c rdfs:label ?cname .");
    }
    if (!inactive) {
      sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
    }
    if (entityTypes != null && !entityTypes.isEmpty()) {
      entityTypes.add(IM.FOLDER.toString());
      sql.add("?c rdf:type ?typeIri .").add(valueList("typeIri", entityTypes));
    }
    sql.add("}");
    sql.add("ORDER BY ?cname");
    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
    }

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qryCount = conn.prepareTupleSparql(sqlCount.toString());
      qryCount.setBinding("p", iri(parentIri));
      try (TupleQueryResult rsCount = qryCount.evaluate()) {
        BindingSet bsCount = rsCount.next();
        result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
      }

      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
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


  public Pageable<TTIriRef> findPartialWithTotalCount(String parentIri, String predicateIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive, Graph graph) {
    List<TTIriRef> children = new ArrayList<>();
    Pageable<TTIriRef> result = new Pageable<>();

    StringJoiner sqlCount = new StringJoiner(System.lineSeparator()).add("SELECT (COUNT(?p) as ?count)").add("WHERE {").add("  ?c ?pr ?p .");
    if (!inactive) {
      sqlCount.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .");
    }
    sqlCount.add("}");

    StringJoiner sql = new StringJoiner(System.lineSeparator())
      .add("SELECT ?p ?pname ")
      .add("WHERE {").add("  ?c ?pr ?p .")
      .add("?p rdfs:label ?pname .");
    if (schemeIris != null && !schemeIris.isEmpty()) {
      sql.add(valueList("scheme", schemeIris)).add("  OPTIONAL { ?p im:scheme ?scheme . }");
    }
    if (!inactive) {
      sql.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .");
    }
    sql.add("}");
    sql.add("ORDER BY ?pname");
    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
    }

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qryCount = conn.prepareTupleSparql(sqlCount.toString());
      qryCount.setBinding("c", iri(parentIri));
      qryCount.setBinding("pr", iri(predicateIri));
      try (TupleQueryResult rsCount = qryCount.evaluate()) {
        BindingSet bsCount = rsCount.next();
        result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
      }

      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
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

  public List<TTIriRef> findImmediateChildrenByIri(String parentIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive, Graph graph) {
    List<TTIriRef> result = new ArrayList<>();

    StringJoiner sql = new StringJoiner(System.lineSeparator())
      .add("SELECT DISTINCT ?c ?cname")
      .add("WHERE { ?c (" + PARENT_PREDICATES + ") ?p .")
      .add(valueList("schemes", schemeIris))
      .add("?c im:scheme ?schemes .")
      .add("?c rdfs:label ?cname .");

    if (!inactive) {
      sql.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
    }

    sql.add("}");
    sql.add("ORDER BY ?cname");

    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
    }

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
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

  public List<TTIriRef> findImmediateParentsByIri(String childIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive, Graph graph) {
    List<TTIriRef> result = new ArrayList<>();

    StringJoiner sql = new StringJoiner(System.lineSeparator())
      .add("SELECT DISTINCT ?p ?pname")
      .add("WHERE {")
      .add("  ?c (" + PARENT_PREDICATES + ") ?p .")
      .add(valueList("schemes", schemeIris))
      .add("?c im:scheme ?schemes .")
      .add("?p rdfs:label ?pname .");

    if (!inactive) {
      sql.add("  OPTIONAL { ?p im:status  ?s}").add("  FILTER (?s != im:Inactive) .");
    }

    sql.add("}");

    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT " + pageSize).add("OFFSET " + rowNumber);
    }

    sql.add("ORDER BY ?pname");

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql.toString());
      qry.setBinding("c", iri(childIri));

      log.debug(EXECUTING);
      try (TupleQueryResult rs = qry.evaluate()) {
        log.debug(RETRIEVING);
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue()));
        }
        log.debug("Finished ({} rows)", result.size());
      }
    }
    return result;
  }

  private void addTriples(List<Tpl> triples, Resource subject, Integer parent, Graph graph) {
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?sname ?p ?pname ?o ?oname
      WHERE {
        ?s ?p ?o ;
        OPTIONAL { ?s rdfs:label ?sname }
        OPTIONAL { ?p rdfs:label ?pname }
        OPTIONAL { ?o rdfs:label ?oname }
      """);

    sql.add("}");
    setAndEvaluate(triples, subject, parent, sql, graph);
  }

  private void setAndEvaluate(List<Tpl> triples, Resource subject, Integer parent, StringJoiner sql, Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql.toString());

      qry.setBinding("s", subject);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Tpl tpl = new Tpl().setDbid(row++).setParent(parent).setPredicate(TTIriRef.iri(getString(bs, "p"), getString(bs, "pname")));

          triples.add(tpl);
          Value object = bs.getValue("o");
          if (object.isIRI()) {
            tpl.setObject(TTIriRef.iri(object.stringValue(), getString(bs, "oname")));
          } else if (object.isLiteral()) {
            tpl.setLiteral(object.stringValue()).setObject(TTIriRef.iri(((Literal) object).getDatatype().stringValue()));
          } else if (object.isBNode()) {
            addTriples(triples, (BNode) object, row - 1, graph);
          } else {
            throw new DALException("ARRAY!");
          }
        }
      }
    }
  }

  public TTIriRef findParentFolderRef(String iri, Graph graph) {
    String sql = """
      SELECT ?p ?pname
      WHERE {
        ?c (%s) ?p .
        ?p rdfs:label ?pname .
      }
      """.formatted(PARENT_PREDICATES);

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("c", iri(iri));

      log.debug(EXECUTING);
      try (TupleQueryResult rs = qry.evaluate()) {
        log.debug(RETRIEVING);
        if (rs.hasNext()) {
          BindingSet bs = rs.next();
          return new TTIriRef(bs.getValue("p").stringValue(), bs.getValue("pname").stringValue());
        }
      }
    }
    return null;
  }

  public boolean hasPredicates(String subjectIri, Set<String> predicateIris, Graph graph) {
    try (IMDB conn = IMDB.getConnection(graph)) {
      String sql = """
        ASK {
          %s
          ?subject ?predicates ?value.
        }
        """.formatted(valueList("predicates", predicateIris));
      BooleanQuery sparql = conn.prepareBooleanSparql(sql);
      sparql.setBinding("subject", iri(subjectIri));
      return sparql.evaluate();
    }
  }


  public Map<String, TTEntity> getEntitiesWithPredicates(Set<String> iris, Set<String> predicates, Graph graph) {
    Map<String, TTEntity> result = new HashMap<>();
    iris.remove(null);
    String sql = """
      select ?entity ?entityLabel ?predicate ?predicateLabel ?object ?objectLabel ?subPredicate ?subPredicateLabel ?subObject ?subObjectLabel
      where {
        %s
        ?entity rdfs:label ?entityLabel.
        optional {
          %s
          ?entity ?predicate ?object.
          ?predicate rdfs:label ?predicateLabel.
          optional {
            ?object rdfs:label ?objectLabel.
            ?object ?subPredicate ?subObject.
            ?subPredicate rdfs:label ?subPredicateLabel.
            optional {
              ?subObject rdfs:label ?subObjectLabel.
            }
          }
        }
      }
      """.formatted(valueList("entity", iris), valueList("predicate", predicates));
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        Map<String, TTNode> bnodeMap = new HashMap<>();
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("entity").stringValue();
          result.putIfAbsent(iri, new TTEntity());
          TTEntity entity = result.get(iri).setIri(iri);
          entity.setName(bs.getValue("entityLabel").stringValue());
          if (bs.getValue("predicate") != null) {
            String predicate = bs.getValue("predicate").stringValue();
            result.putIfAbsent(predicate, new TTEntity());
            result.get(predicate).setName(bs.getValue("predicateLabel").stringValue());
            Value object = bs.getValue("object");
            if (object.isIRI()) {
              TTIriRef ttIriRef = TTIriRef.iri(object.stringValue());
              if (null != bs.getValue("objectLabel")) ttIriRef.setName(bs.getValue("objectLabel").stringValue());
              entity.addObject(TTIriRef.iri(predicate), ttIriRef);
            } else if (object.isBNode()) {
              bnodeMap.putIfAbsent(object.stringValue(), new TTNode());
              TTNode blank = bnodeMap.get(object.stringValue());
              if (bs.getValue("subPredicate") != null) {
                String subPredicate = bs.getValue("subPredicate").stringValue();
                result.putIfAbsent(subPredicate, new TTEntity());
                result.get(subPredicate).setName(bs.getValue("subPredicateLabel").stringValue());
                Value subObject = bs.getValue("subObject");
                if (subObject.isIRI()) {
                  blank.addObject(TTIriRef.iri(subPredicate), TTIriRef.iri(subObject.stringValue()).setName(bs.getValue("subObjectLabel").stringValue()));
                } else blank.addObject(TTIriRef.iri(subPredicate), TTLiteral.literal(subObject.stringValue()));
              }
            } else
              entity.addObject(TTIriRef.iri(predicate), TTLiteral.literal(object.stringValue()));
          }
        }
      }
    }
    return result;
  }


  public List<TTIriRef> findEntitiesByType(EntityType typeIri, Graph graph) {
    String sparqlString =
      """
          select *
          FROM <""" + graph + ">" + """
          where {
              ?s rdf:type ?c .
              ?s rdfs:label ?name .
          }
        """;
    ArrayList<TTIriRef> iriRefs = new ArrayList<>();

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sparqlString);
      qry.setBinding("c", typeIri.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          iriRefs.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
        }
      }
    }
    return iriRefs;
  }

  public List<org.endeavourhealth.imapi.model.Namespace> findNamespaces(Graph graph) {
    List<org.endeavourhealth.imapi.model.Namespace> result = new ArrayList<>();

    String sql = """
       select *
       where {
         ?s im:isA ?namespace .
         ?s rdfs:label ?name .
       }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("namespace", IM.NAMESPACE.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("s").stringValue();
          org.endeavourhealth.imapi.model.Namespace namespace = new org.endeavourhealth.imapi.model.Namespace()
            .setIri(iri)
            .setName(bs.getValue("name").stringValue())
            .setPrefix(getPrefixFromIri(iri));
          result.add(namespace);
        }
      }
    }
    return result;
  }

  private String getPrefixFromIri(String iri) {
    int lastSlashIndex = iri.lastIndexOf('/');
    int hashIndex = iri.lastIndexOf('#');
    if (lastSlashIndex != -1 && hashIndex != -1 && hashIndex > lastSlashIndex) {
      return iri.substring(lastSlashIndex + 1, hashIndex);
    } else if (iri.equals(Namespace.FHIR.toString())) {
      return FHIR.PREFIX.toString();
    }
    return iri;
  }

  public Set<String> getByNamespace(Namespace namespace, Graph graph) {
    Set<String> results = new HashSet<>();
    String sparql = """
      SELECT DISTINCT ?s
      WHERE {
        ?s im:scheme ?scheme
      }
      """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("scheme", namespace.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          results.add(bs.getValue("s").stringValue());
        }
      }
    }
    return results;
  }

  public List<TTIriRef> findInvertedIsas(String iri, Graph graph) {
    String sparqlString =
      """
          select *
          where {
            ?s im:isA ?iri .
            ?s rdfs:label ?name .
          }
        """;
    ArrayList<TTIriRef> iriRefs = new ArrayList<>();

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sparqlString);
      qry.setBinding("iri", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          iriRefs.add(new TTIriRef(bs.getValue("s").stringValue(), bs.getValue("name").stringValue()));
        }
      }
    }
    return iriRefs;
  }

  public List<String> findOperatorOptions(String iri, Graph graph) {
    List<String> options = new ArrayList<>();

    String sparqlString =
      """
          select *
          where {
            ?s sh:in ?o .
          }
        """;

    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sparqlString);
      qry.setBinding("s", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          if (null != bs.getValue("o")) {
            String[] splits = bs.getValue("o").stringValue().split(" ");
            options = Arrays.stream(splits).toList();
          }
        }
      }
    }
    return options;
  }

  public List<TTEntity> getFolderChildren(String iri, Graph graph, String... predicates) {
    Map<String, TTEntity> entities = new HashMap<>();
    String predicateList = Arrays.stream(predicates).map(p -> "<" + p + ">").collect(Collectors.joining(" "));
    String sql = """
      select ?entity ?predicate ?order ?object
      where {
        values ?predicate {%s}
        ?entity im:isContainedIn ?containedIn.
        ?entity ?predicate ?object.
        optional {?entity sh:order ?order}
      }
      """.formatted(predicateList);
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("containedIn", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          entities.putIfAbsent(bs.getValue("entity").stringValue(), new TTEntity());
          TTEntity entity = entities.get(bs.getValue("entity").stringValue());
          entity.setIri(bs.getValue("entity").stringValue());
          Value object = bs.getValue("object");
          TTValue ttValue = object.isIRI() ? TTIriRef.iri(object.stringValue()) : TTLiteral.literal(object.stringValue());
          entity.addObject(TTIriRef.iri(bs.getValue("predicate").stringValue()), ttValue);
        }
      }
    }
    return entities.values().stream()
      .sorted(Comparator.comparingInt(entity ->
        entity.get(TTIriRef.iri(SHACL.ORDER)).asLiteral().intValue()
      ))
      .toList();
  }


  public List<TTEntity> getAllowableChildTypes(String iri, Graph graph) {
    String spq = """
      SELECT distinct  ?entity ?label ?path ?pathLabel
      WHERE {
           VALUES ?parent {%s}
          {
           ?parent rdf:type ?entity.
           ?entity rdfs:label ?label.
          ?entity sh:property ?property.
          ?property sh:path ?path.
              ?path rdfs:label ?pathLabel.
          ?property sh:node ?entity.
          }
          union {
              VALUES ?path {im:isContainedIn}
           ?parent im:contentType ?entity.
           ?entity rdfs:label ?label.
              ?entity sh:property ?property.
              ?path rdfs:label ?pathLabel.
          }
      }
      """.formatted("<"+iri+">");
    List<TTEntity> result = new ArrayList<>();
    Map<String, TTEntity> iriMap = new HashMap<>();
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(spq);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          TTEntity child = iriMap.get(bs.getValue("entity").stringValue());
          if (child == null) {
            child = new TTEntity()
              .setIri(bs.getValue("entity").stringValue())
              .setName(bs.getValue("label").stringValue());
            iriMap.put(bs.getValue("entity").stringValue(), child);
            result.add(child);
          }
          child.set(TTIriRef.iri(SHACL.PATH), TTIriRef
            .iri(bs.getValue("path").stringValue())
            .setName(bs.getValue("pathLabel").stringValue()));
        }
      }
    }
    return result;
  }

  public List<String> getChildIris(String iri, Graph graph) {
    String spq = """
      select ?child ?name ?contextOrder ?order
      where {
       values ?iri {%s}
        ?child im:isContainedIn|rdfs:subClassOf ?iri.
        ?child rdfs:label ?name.
        optional {?child sh:order ?order}
        optional {?child im:contextOrder ?co.
                  ?co im:context ?iri.
                  ?co sh:order ?contextOrder}
      
      }
      order by ?contextOrder ?order
   
      """.formatted(toIri(iri));
    List<String> result = new ArrayList<>();
    try (IMDB conn = IMDB.getConnection(graph)) {
      TupleQuery qry = conn.prepareTupleSparql(spq);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(bs.getValue("child").stringValue());
        }
      }
    }
    return result;
  }

  private Object toIri(String iri) {
    return "<" + iri + ">";
  }
}
