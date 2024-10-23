package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.endeavourhealth.imapi.cache.TimedCache;
import org.endeavourhealth.imapi.dataaccess.entity.Tpl;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.dataaccess.helpers.DALException;
import org.endeavourhealth.imapi.model.ConceptContextMap;
import org.endeavourhealth.imapi.model.Context;
import org.endeavourhealth.imapi.model.EntityReferenceNode;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.dto.ParentDto;
import org.endeavourhealth.imapi.model.dto.SimpleMap;
import org.endeavourhealth.imapi.model.search.EntityDocument;
import org.endeavourhealth.imapi.model.search.SearchResultSummary;
import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.transforms.TTManager;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.*;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList;

public class EntityRepository {
  static final String PARENT_PREDICATES = "rdfs:subClassOf|im:isContainedIn|im:isChildOf|rdfs:subPropertyOf|im:isSubsetOf";
  private static final Logger LOG = LoggerFactory.getLogger(EntityRepository.class);
  private static final String IM_PREFIX = "PREFIX im: <" + IM.NAMESPACE + ">";
  private static final String RDFS_PREFIX = "PREFIX rdfs: <" + RDFS.NAMESPACE + ">";
  private static final String RDF_PREFIX = "PREFIX rdf: <" + RDF.NAMESPACE + ">";
  private static final TimedCache<String, String> iriNameCache = new TimedCache<>("IriNameCache", 30, 5, 100);
  private static final List<org.endeavourhealth.imapi.model.Namespace> namespaceCache = new ArrayList<>();
  private static final String EXECUTING = "Executing...";
  private static final String RETRIEVING = "Retrieving...";
  private final Map<String, Integer> bnodes = new HashMap<>();
  private int row = 0;

  private static void hydrateCorePropertiesSetEntityDocumentProperties(EntityDocument entityDocument, TupleQueryResult qr) {
    BindingSet rs = qr.next();
    entityDocument.setName(rs.getValue("name").stringValue());
    entityDocument.addTermCode(entityDocument.getName(), null, null);

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
          summary.setIri(iri).setName(bs.getValue("sname").stringValue()).setCode(bs.getValue("scode") == null ? "" : bs.getValue("scode").stringValue()).setScheme(new TTIriRef(bs.getValue("g").stringValue(), (bs.getValue("gname") == null ? "" : bs.getValue("gname").stringValue()))).setEntityType(types).setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue())).setDescription(bs.getValue("sdescription") == null ? "" : bs.getValue("sdescription").stringValue());
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
          result.setIri(iri).setName(bs.getValue("sname").stringValue()).setCode(bs.getValue("scode") == null ? "" : bs.getValue("scode").stringValue()).setEntityType(types).setStatus(new TTIriRef(bs.getValue("sstatus") == null ? "" : bs.getValue("sstatus").stringValue(), bs.getValue("sstatusname") == null ? "" : bs.getValue("sstatusname").stringValue())).setDescription(bs.getValue("sdescription") == null ? "" : bs.getValue("sdescription").stringValue());

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
          if (rs.hasBinding("synonym")) synonym = rs.getValue("synonym").stringValue().toLowerCase();
          if (rs.hasBinding("termCode")) termCode = rs.getValue("termCode").stringValue();
          if (rs.hasBinding("termCodeStatus")) status = TTIriRef.iri(rs.getValue("termCodeStatus").stringValue());

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
            if (tc == null) entityDocument.addTermCode(null, termCode, status);
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
      if (tc.getTerm() != null && tc.getTerm().equals(term)) return tc;
    }
    return null;
  }

  private void addKey(EntityDocument blob, String term) {
    term = term.replaceAll("[ '()\\-_./,]", "").toLowerCase();
    if (term.length() > 30) term = term.substring(0, 30);
  }

  private SearchTermCode getTermCodeFromCode(EntityDocument blob, String code) {
    for (SearchTermCode tc : blob.getTermCode()) {
      if (tc.getCode() != null && tc.getCode().equals(code)) return tc;
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

  public List<TTIriRef> findAncestorsByType(String childIri, String relationshipIri, List<String> candidateAncestorIris) {
    List<TTIriRef> result = new ArrayList<>();
    String sql = """
      SELECT ?aname
      WHERE {
        ?c ?r ?a .
        ?a rdfs:label ?aname .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      qry.setBinding("c", Values.iri(childIri));
      qry.setBinding("r", Values.iri(relationshipIri));

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

  public static void getIriNames(RepositoryConnection conn, Set<TTIriRef> iris) {

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

    TupleQuery qry = conn.prepareTupleQuery(sql);
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
      e.printStackTrace();
    }
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
  public TTBundle getBundle(String iri, Set<String> predicates, boolean excludePredicates) {
    return getBundle(iri, predicates, excludePredicates, 5);
  }

  public TTBundle getBundle(String iri, Set<String> predicates, boolean excludePredicates, int depth) {
    TTBundle bundle = new TTBundle().setEntity(new TTEntity().setIri(iri)).setPredicates(new HashMap<>());
    if (null != predicates && predicates.contains(RDFS.LABEL) && !predicates.contains(RDFS.COMMENT)) {
      Set<String> predicatesPlus = new HashSet<>();
      predicatesPlus.addAll(predicates);
      predicatesPlus.add(RDFS.COMMENT);
      predicates = predicatesPlus;
    }

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

  public void getNames(Set<TTIriRef> iris) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      getIriNames(conn, iris);
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
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("SELECT ?concept ?label");
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
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("SELECT ?concept ?label");
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

  private Set<TTIriRef> getConceptRefsFromResult(TupleQuery qry) {
    Set<TTIriRef> results = null;
    try (TupleQueryResult gs = qry.evaluate()) {
      while (gs.hasNext()) {
        BindingSet bs = gs.next();
        if (results == null) results = new HashSet<>();
        TTIriRef concept = TTIriRef.iri(bs.getValue("concept").stringValue());
        if (bs.getValue("label") != null) concept.setName(bs.getValue("label").stringValue());
        results.add(concept);

      }
    }
    return results;
  }

  private StringJoiner getBundleSparql(Set<String> predicates, boolean excludePredicates, int depth) {
    StringJoiner sql = new StringJoiner(System.lineSeparator());
    sql.add(RDFS_PREFIX);
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
    if (predicate.equals(RDFS.LABEL)) {
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
        tripleMap.get(subject).asNode().set(TTIriRef.iri(RDFS.LABEL), TTLiteral.literal(value, ((Literal) o).getDatatype().stringValue()));
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

  private void simpleSuperClass(TTIriRef superClass, StringJoiner spql, Map<String, String> prefixMap) {
    spql.add("?concept " + isa(prefixMap) + " " + getShort(superClass.asIriRef().getIri(), prefixMap) + ".");
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
    } else return "<" + iri + ">";
  }

  private String isa(Map<String, String> prefixMap) {
    return getShort(IM.IS_A, prefixMap);
  }

  public TTArray getEntityTypes(String iri) {
    TTArray result = new TTArray();

    String sql = """
      SELECT ?o ?oname
      WHERE {
        ?s rdf:type ?o .
        ?o rdfs:label ?oname .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);

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
    return getBundle(iri, predicates, false);
  }

  public List<TTIriRef> getConceptUsages(String objectIri, Integer rowNumber, Integer pageSize) {
    List<TTIriRef> result = new ArrayList<>();

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      PREFIX shacl: <http://www.w3.org/ns/shacl#>
      SELECT DISTINCT ?s ?name
      WHERE {
        { ?s ?p ?o . }
        UNION {
          ?s shacl:property ?prop .
          ?prop shacl:path ?propIri .
          FILTER(?propIri = ?o)
        }
        ?s rdfs:label ?name .
        ?s im:status ?status .
        FILTER (?p != rdfs:subclassOf && ?status != im:Inactive)
      }
      """);

    if (rowNumber != null && pageSize != null) {
      sql.add("LIMIT " + pageSize + " OFFSET " + rowNumber);
    }

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql.toString());
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

  public Integer getConceptUsagesCount(String objectIri) {
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


    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
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
    Map<String, Set<TTIriRef>> iriToTypesMap = getTypesByIris(stringIris);

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?s ?name ?typeIri ?typeName ?order ?hasChildren ?hasGrandchildren
      WHERE {
        GRAPH ?g { ?s rdfs:label ?name } .
        VALUES ?s { %s }
        OPTIONAL { ?s sh:order ?order . }
        BIND(EXISTS{?child (%s) ?s} AS ?hasChildren)
        BIND(EXISTS{?grandChild (%s) ?child. ?child (%s) ?s} AS ?hasGrandchildren)
      """.formatted(iriLine, PARENT_PREDICATES, PARENT_PREDICATES, PARENT_PREDICATES));

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
          for (TTIriRef type : types) {
            result.getType().add(type);
          }
          if (bs.hasBinding("order")) result.setOrderNumber(((Literal) bs.getValue("order")).intValue());
          else result.setOrderNumber(Integer.MAX_VALUE);

          result.setHasChildren(((Literal) bs.getValue("hasChildren")).booleanValue()).setHasGrandChildren(((Literal) bs.getValue("hasGrandchildren")).booleanValue()).setName(bs.getValue("name").stringValue());
          nodes.add(result);
        }
      }
    }

    return nodes;
  }

  public EntityReferenceNode getEntityReferenceNode(String iri, List<String> schemeIris, boolean inactive) {
    TTArray types = new TTArray();
    EntityReferenceNode result = new EntityReferenceNode(iri).setType(types);

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?name ?typeIri ?typeName ?order ?hasChildren ?hasGrandchildren
      WHERE {
        GRAPH ?g { ?s rdfs:label ?name } .
        OPTIONAL { ?s sh:order ?order . }
        OPTIONAL { ?s rdf:type ?typeIri .
          OPTIONAL { ?typeIri rdfs:label ?typeName . }
        }
        BIND(EXISTS{?child (%s) ?s} AS ?hasChildren)
        BIND(EXISTS{?grandChild (%s) ?child. ?child (%s) ?s} AS ?hasGrandchildren)
      """.formatted(PARENT_PREDICATES, PARENT_PREDICATES, PARENT_PREDICATES));

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

  public Pageable<TTIriRef> findImmediateChildrenPagedByIriWithTotalCount(String parentIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
    List<TTIriRef> children = new ArrayList<>();
    Pageable<TTIriRef> result = new Pageable<>();

    StringJoiner sqlCount = new StringJoiner(System.lineSeparator()).add("SELECT (COUNT(?c) as ?count)").add("WHERE {").add("  ?c (" + PARENT_PREDICATES + ") ?p .");
    if (!inactive) {
      sqlCount.add("  OPTIONAL { ?c im:status ?s}").add("  FILTER (?s != im:Inactive) .");
    }
    sqlCount.add("}");

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("SELECT ?c ?cname ").add("WHERE {").add("  ?c ( " + PARENT_PREDICATES + ") ?p .");
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

  public Pageable<TTIriRef> findPartialWithTotalCount(String parentIri, String predicateIri, List<String> schemeIris, Integer rowNumber, Integer pageSize, boolean inactive) {
    List<TTIriRef> children = new ArrayList<>();
    Pageable<TTIriRef> result = new Pageable<>();

    StringJoiner sqlCount = new StringJoiner(System.lineSeparator()).add("SELECT (COUNT(?p) as ?count)").add("WHERE {").add("  ?c ?pr ?p .");
    if (!inactive) {
      sqlCount.add("  OPTIONAL { ?p im:status ?s}").add("  FILTER (?s != im:Inactive) .");
    }
    sqlCount.add("}");

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("SELECT ?p ?pname ").add("WHERE {").add("  ?c ?pr ?p .").add("?p rdfs:label ?pname .");
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

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("SELECT DISTINCT ?c ?cname {").add("  ?c (" + PARENT_PREDICATES + ") ?p .").add("GRAPH ?g { ?c rdfs:label ?cname } .");

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

    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("SELECT DISTINCT ?p ?pname").add("WHERE {").add("  ?c (" + PARENT_PREDICATES + ") ?p .").add("GRAPH ?g { ?p rdfs:label ?pname } .");

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

  public List<org.endeavourhealth.imapi.model.Namespace> findNamespaces() {
    synchronized (namespaceCache) {
      if (namespaceCache.isEmpty()) {
        Map<String, org.endeavourhealth.imapi.model.Namespace> result = new HashMap<>();
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
          RepositoryResult<Namespace> namespaces = conn.getNamespaces();
          while (namespaces.hasNext()) {
            org.eclipse.rdf4j.model.Namespace ns = namespaces.next();
            result.put(ns.getName(), new org.endeavourhealth.imapi.model.Namespace(ns.getName(), ns.getPrefix(), ns.getName()));
          }
        }

        // Get/add schemes
        String sql = """
          SELECT *
          WHERE {
            GRAPH ?g {
              ?g rdfs:label ?name
            }
          }
          """;

        int i = 1;
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
          TupleQuery qry = conn.prepareTupleQuery(sql);
          try (TupleQueryResult rs = qry.evaluate()) {
            while (rs.hasNext()) {
              BindingSet bs = rs.next();
              org.endeavourhealth.imapi.model.Namespace ns = result.get(bs.getValue("g").stringValue());
              if (ns == null) {
                result.put(bs.getValue("g").stringValue(), new org.endeavourhealth.imapi.model.Namespace(bs.getValue("g").stringValue(), "PFX" + (i++), bs.getValue("name").stringValue()));
              } else {
                ns.setName(bs.getValue("name").stringValue());
              }
            }
          }
        }
        namespaceCache.addAll(result.values());
      }
      namespaceCache.sort(Comparator.comparing(org.endeavourhealth.imapi.model.Namespace::getName));
      return namespaceCache;
    }
  }

  private void addTriples(List<Tpl> triples, Resource subject, Integer parent, Set<String> predicates) {
    StringJoiner sql = new StringJoiner(System.lineSeparator()).add("""
      SELECT ?sname ?p ?pname ?o ?oname
      WHERE {
        ?s ?p ?o ;
        OPTIONAL { ?s rdfs:label ?sname }
        OPTIONAL { ?p rdfs:label ?pname }
        OPTIONAL { ?o rdfs:label ?oname }
      """);

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
          Tpl tpl = new Tpl().setDbid(row++).setParent(parent).setPredicate(TTIriRef.iri(getString(bs, "p"), getString(bs, "pname")));

          triples.add(tpl);
          Value object = bs.getValue("o");
          if (object.isIRI()) {
            tpl.setObject(TTIriRef.iri(object.stringValue(), getString(bs, "oname")));
          } else if (object.isLiteral()) {
            tpl.setLiteral(object.stringValue()).setObject(TTIriRef.iri(((Literal) object).getDatatype().stringValue()));
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

  public TTIriRef findParentFolderRef(String iri) {
    String sql = """
      SELECT ?p ?pname
      WHERE {
        ?c (%s) ?p .
        ?p rdfs:label ?pname .
      }
      """.formatted(PARENT_PREDICATES);

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
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

  public Boolean hasPredicates(String subjectIri, Set<String> predicateIris) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      StringJoiner stringQuery = new StringJoiner(System.lineSeparator()).add("ASK {");
      for (String predicateIri : predicateIris) {
        stringQuery.add("?subjectIri <" + iri(predicateIri) + "> ?o .");
      }
      stringQuery.add("}");

      BooleanQuery sparql = conn.prepareBooleanQuery(String.valueOf(stringQuery));
      sparql.setBinding("subjectIri", iri(subjectIri));
      return sparql.evaluate();
    }
  }

  public Map<String, Set<String>> findBNFs(List<String> codes) {
    Map<String, Set<String>> bnfMap = new HashMap<>();
    StringJoiner iriLine = new StringJoiner(" ");
    for (String code : codes) {
      iri(SNOMED.NAMESPACE + code);
      iriLine.add("<" + SNOMED.NAMESPACE + code + ">");
    }

    String sql = """
      PREFIX bnf: <http://bnf.info/bnf#>
      SELECT ?code ?name WHERE {
          ?bnf rdfs:label ?name .
          ?bnf im:scheme bnf: .
          ?bnf im:roleGroup ?o .
          ?o im:hasMemberParent ?s .
          ?s im:code ?code .
          VALUES ?s { %s }
      }
      """.formatted(iriLine.toString());

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String code = bs.getValue("code").stringValue();
          if (!bnfMap.containsKey(code)) bnfMap.put(code, new HashSet<>());
          bnfMap.get(code).add(bs.getValue("name").stringValue());
        }
      }
    }
    return bnfMap;
  }
}
