package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.*;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;
import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.addSparqlPrefixes;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetRepository {
  public static final String ACTIVE_ENTITY = "activeEntity";
  public static final String ENTITY = "entity";
  public static final String IM_1_ID = "im1Id";
  public static final String ENTITY_TYPE = "entityType";
  public static final String TYPE_NAME = "typeName";
  public static final String LEGACY_SCHEME = "legacyScheme";
  public static final String CONCEPT = "concept";
  private static final Logger LOG = LoggerFactory.getLogger(SetRepository.class);
  private EntityRepository entityRepository = new EntityRepository();

  /**
   * Returns an expanded set members match an iml set definition. If already expanded then returns members
   * otherwise expands and retuens members
   *
   * @param imQuery       im query conforming to ecl language constraints
   * @param includeLegacy to include legacy concepts linked by matchedTo to core concept
   * @return a Set of concepts with matchedFrom legacy concepts and list of im1 ids
   * @throws JsonProcessingException if json definitino invalid
   * @throws DataFormatException     if query definition invalid
   */
  public Set<Concept> getSetExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter) throws QueryException {
    //add scheme filter
    return getSetExpansion(imQuery, includeLegacy, statusFilter, schemeFilter, null);
  }

  public Set<Concept> getSetExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, Page page) throws QueryException {
    Set<Concept> result = getActiveExpansion(imQuery, includeLegacy, statusFilter, schemeFilter, page);
    Set<Concept> result2 = getReplacedExpansion(imQuery, includeLegacy, statusFilter, schemeFilter, page);
    result.addAll(result2);
    return result;
  }

  private Set<Concept> getReplacedExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, Page page) throws QueryException {
    Query replaced = new Query();
    replaced.addMatch(imQuery);
    replaced.setVariable(ACTIVE_ENTITY);
    replaced.getMatch().get(0).setVariable(ACTIVE_ENTITY);
    if (replaced.getMatch().get(0).getMatch() != null) {
      for (Match match : replaced.getMatch().get(0).getMatch()) {
        match.setVariable(ACTIVE_ENTITY);
      }
    }
    Return aReturn = setReturn(imQuery, includeLegacy);
    aReturn.setNodeRef(ENTITY);
    replaced.addReturn(aReturn);
    replaced.match(m -> m
      .setBoolMatch(Bool.or)
      .match(m1 -> m1
        .setVariable(ENTITY)
        .where(p -> p
          .setIri(IM.PREVIOUS_ENTITY_OF)
          .addIs(new Node().setNodeRef(ACTIVE_ENTITY))))
      .match(m1 -> m1
        .setVariable(ENTITY)
        .where(p -> p
          .setIri(IM.SUBSUMED_BY)
          .addIs(new Node().setNodeRef(ACTIVE_ENTITY))))
      .match(m1 -> m1
        .setVariable(ENTITY)
        .where(p -> p
          .setIri(IM.USUALLY_SUBSUMED_BY)
          .addIs(new Node().setNodeRef(ACTIVE_ENTITY)))));
    return getExpansion(replaced, includeLegacy, statusFilter, schemeFilter, page);

  }

  private Set<Concept> getActiveExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, Page page) throws QueryException {
    Return aReturn = setReturn(imQuery, includeLegacy);
    imQuery.addReturn(aReturn);
    return getExpansion(imQuery, includeLegacy, statusFilter, schemeFilter, page);
  }

  private Set<Concept> getExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, Page page) throws QueryException {
    QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
    if (null != page && null != page.getPageNumber() && null != page.getPageSize()) newRequest.setPage(page);
    String sql = new SparqlConverter(newRequest).getSelectSparql(statusFilter);
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(sql);
      return getCoreLegacyCodesForSparql(qry, includeLegacy, schemeFilter);

    }
  }


  private Return setReturn(Query imQuery, boolean includeLegacy) {
    Return aReturn = new Return();
    imQuery.addReturn(aReturn);
    aReturn
      .property(s -> s
        .setIri(RDFS.LABEL).as("term"))
      .property(s -> s
        .setIri(IM.CODE).as("code"))
      .property(s -> s
        .setIri(IM.HAS_SCHEME)
        .return_(n -> n
          .as("scheme")
          .property(s2 -> s2
            .setIri(RDFS.LABEL).as("schemeName"))))
      .property(s -> s
        .setIri(IM.NAMESPACE + "usageTotal").as("usage"))
      .property(s -> s
        .setIri(IM.IM_1_ID).as(IM_1_ID))
      .property(s -> s
        .setIri(IM.HAS_STATUS)
        .return_(s2 -> s2
          .as("status")
          .property(p -> p
            .setIri(RDFS.LABEL).as("statusName"))))
      .property(s -> s
        .setIri(RDF.TYPE)
        .return_(s2 -> s2
          .as(ENTITY_TYPE)
          .property(p -> p
            .setIri(RDFS.LABEL).as(TYPE_NAME))))
      .property(s -> s
        .setIri(IM.CODE_ID)
        .as("codeId"))
      .property(s -> s
        .setIri(IM.ALTERNATIVE_CODE)
        .as("alternativeCode"));

    if (includeLegacy) {
      aReturn
        .property(p -> p
          .setIri(IM.MATCHED_TO)
          .setInverse(true)
          .return_(n -> n
            .as("legacy")
            .property(s -> s
              .setIri(RDFS.LABEL).as("legacyTerm"))
            .property(s -> s
              .setIri(IM.CODE).as("legacyCode"))
            .property(s -> s
              .setIri(IM.HAS_SCHEME)
              .return_(n1 -> n1
                .as(LEGACY_SCHEME)
                .property(p1 -> p1
                  .setIri(RDFS.LABEL).as("legacySchemeName"))))
            .property(s -> s
              .setIri(IM.NAMESPACE + "usageTotal").as("legacyUse"))
            .property(s -> s
              .setIri(IM.CODE_ID)
              .as("legacyCodeId"))
            .property(s -> s
              .setIri(IM.IM_1_ID).as("legacyIm1Id"))));
    }
    return aReturn;
  }


  public int getSetExpansionTotalCount(Query imQuery, Set<TTIriRef> statusFilter) throws QueryException {
    //add scheme filter
    QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
    String sql = new SparqlConverter(newRequest).getCountSparql(statusFilter);
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(sql);
      return getCountForSparql(qry);

    }
  }

  public Set<TTIriRef> getSubsetIrisWithNames(String iri) {
    Set<TTIriRef> result = new HashSet<>();

    String sql = """
      SELECT ?subset ?name
      WHERE {
        ?subset ?isSubset ?set .
        ?subset ?label ?name .
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sql));
      qry.setBinding("set", Values.iri(iri));
      qry.setBinding("isSubset", Values.iri(IM.IS_SUBSET_OF));
      qry.setBinding("label", Values.iri(RDFS.LABEL));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String subsetIri = bs.getValue("subset").stringValue();
          String subsetName = bs.getValue("name").stringValue();
          try {
            TTIriRef subset = new TTIriRef(subsetIri, subsetName);
            result.add(subset);
          } catch (IllegalArgumentException ignored) {
            LOG.warn("Invalid subset iri [{}] for set [{}]", subsetIri, iri);
          }
        }
      }
    }

    return result;
  }

  private Set<Concept> getCoreLegacyCodesForSparql(TupleQuery qry, boolean includeLegacy, List<String> schemes) {
    Set<Concept> result = new HashSet<>();
    Set<String> coreSchemes = Set.of(SNOMED.NAMESPACE, IM.NAMESPACE);
    Map<String, Concept> conceptMap = new HashMap<>();
    try (TupleQueryResult rs = qry.evaluate()) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        String concept = bs.getValue(ENTITY).stringValue();
        Concept cl = conceptMap.get(concept);
        Value scheme = bs.getValue("scheme");
        if (cl == null) {
          cl = buildConcept(concept, conceptMap, result, bs, scheme);
        } else {
          Value type = bs.getValue(ENTITY_TYPE);
          Value typeName = bs.getValue(TYPE_NAME);
          if (null != type) {
            cl.addType(iri(type.stringValue(), typeName.stringValue()));
          }
        }
        Value im1Id = bs.getValue(IM_1_ID);
        if (im1Id != null)
          cl.addIm1Id(im1Id.stringValue());
        if (includeLegacy) {
          String legacyScheme = bs.getValue(LEGACY_SCHEME) != null ? bs.getValue(LEGACY_SCHEME).stringValue() : null;
          if (legacyScheme == null) {
            if (scheme != null && !coreSchemes.contains(scheme.stringValue())) {
              bindLegacyFromCore(bs, cl);
            }
          } else if (schemes.isEmpty()) {
            bindResults(bs, cl);
          } else {
            if (schemes.stream().anyMatch(s -> s.equals(legacyScheme))) {
              bindResults(bs, cl);
            }
          }
        }
      }
    }
    return result.stream().sorted(Comparator.comparing(Concept::getName)).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  private Concept buildConcept(String concept, Map<String, Concept> conceptMap, Set<Concept> result, BindingSet bs, Value scheme) {
    Concept cl = new Concept();
    conceptMap.put(concept, cl);
    result.add(cl);
    Value name = bs.getValue("term");
    Value code = bs.getValue("code");
    Value alternativeCode = bs.getValue("alternativeCode");
    Value schemeName = bs.getValue("schemeName");
    Value usage = bs.getValue("usage");
    Value status = bs.getValue("status");
    Value statusName = bs.getValue("statusName");
    Value type = bs.getValue(ENTITY_TYPE);
    Value typeName = bs.getValue(TYPE_NAME);
    Value codeId = bs.getValue("codeId");
    cl.setIri(concept);
    if (name != null)
      cl.setName(name.stringValue());
    if (code != null) {
      cl.setCode(code.stringValue());
    }
    if (alternativeCode != null) {
      cl.setAlternativeCode(alternativeCode.stringValue());
    }
    if (null != scheme) {
      cl.setScheme(iri(scheme.stringValue(), schemeName.stringValue()));
    }
    if (null != status) {
      cl.setStatus(iri(status.stringValue(), statusName.stringValue()));
    }
    if (null != type) {
      cl.addType(iri(type.stringValue(), typeName.stringValue()));
    }
    if (null != codeId) {
      cl.setCodeId(codeId.stringValue());
    }
    cl.setUsage(usage == null ? null : ((Literal) usage).intValue());
    return cl;
  }

  private int getCountForSparql(TupleQuery qry) {
    try (TupleQueryResult rs = qry.evaluate()) {
      if (rs.hasNext()) {
        BindingSet bs = rs.next();
        return ((Literal) bs.getValue("cnt")).intValue();
      } else {
        return 0;
      }
    }
  }


  private void bindLegacyFromCore(BindingSet bs, Concept cl) {
    String legIri = cl.getIri();
    if (legIri != null) {
      Concept legacy = matchLegacy(cl, legIri);
      if (legacy == null) {
        legacy = new Concept();
        cl.addMatchedFrom(legacy);
        legacy.setIri(legIri);
        legacy.setCode(cl.getCode());
        legacy.setName(cl.getName());
        legacy.setScheme(cl.getScheme());
        legacy.setCodeId(cl.getCodeId());
        legacy.setUsage(cl.getUsage());
      }
      Value lid = bs.getValue(IM_1_ID);
      if (lid != null)
        legacy.addIm1Id(lid.stringValue());
    }
  }


  public void bindConceptSetToDataModel(String iri, Set<TTNode> dataModels) {

    String deleteBinding = """
      DELETE { ?concept im:binding ?datamodel}
      WHERE { ?concept im:binding ?datamodel}
      """;

    StringJoiner newBinding = new StringJoiner("\n").add("INSERT DATA { graph <" + GRAPH.DISCOVERY + "> {");
    int blankCount = 0;
    for (TTNode dataModel : dataModels) {
      blankCount++;
      String pathIri = dataModel.get(iri(SHACL.PATH)).asIriRef().getIri();
      String nodeIri = dataModel.get(iri(SHACL.NODE)).asIriRef().getIri();
      newBinding.add("""
        <%s> im:binding _:b%s .
        _:b%s sh:path <%s> .
        _:b%s sh:node <%s> .
        """.formatted(iri, blankCount, blankCount, pathIri, blankCount, nodeIri));
    }
    newBinding.add("}}");

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      conn.begin();
      org.eclipse.rdf4j.query.Update upd = conn.prepareUpdate(addSparqlPrefixes(deleteBinding));
      upd.setBinding(CONCEPT, Values.iri(iri));
      upd.execute();
      upd = conn.prepareUpdate(addSparqlPrefixes(newBinding.toString()));
      upd.execute();
      conn.commit();
    }

  }

  public Set<String> getSets() {
    Set<String> setIris = new HashSet<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String spq = """
        SELECT distinct ?iri
        WHERE {
          ?iri rdf:type ?type.
          FILTER (?type in (im:ValueSet, im:ConceptSet))
          ?iri im:definition ?d.
        }
        """;

      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(spq));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          setIris.add(rs.next().getValue("iri").stringValue());
        }
      }
    }
    return setIris;
  }

  public void updateMembers(String iri, Set<Concept> members) {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String spq = """
        DELETE { ?concept im:hasMember ?x.}
        WHERE { ?concept im:hasMember ?x.}
        """;
      org.eclipse.rdf4j.query.Update upd = conn.prepareUpdate(addSparqlPrefixes(spq));
      upd.setBinding(CONCEPT, Values.iri(iri));
      upd.execute();
      spq = """
        SELECT ?g
        WHERE {
          graph ?g {?concept rdf:type ?type }
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(spq);
      qry.setBinding(CONCEPT, Values.iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        BindingSet bs = rs.next();
        String graph = bs.getValue("g").stringValue();
        StringJoiner sj = new StringJoiner("\n");
        sj.add("INSERT DATA { graph <" + graph + "> {");
        int batch = 0;
        for (Concept member : members) {
          batch++;
          if (batch == 1000) {
            sendUp(sj, conn);
            sj = new StringJoiner("\n");
            sj.add("INSERT DATA { graph <" + graph + "> {");
            batch = 0;
          }
          sj.add("<" + iri + "> im:hasMember <" + member.getIri() + ">.");
        }
        sendUp(sj, conn);
      }
    }
  }


  private void sendUp(StringJoiner sj, RepositoryConnection conn) {
    sj.add("}}");
    org.eclipse.rdf4j.query.Update upd = conn.prepareUpdate(addSparqlPrefixes(sj.toString()));
    conn.begin();
    upd.execute();
    conn.commit();
  }


  private void bindResults(BindingSet bs, Concept cl) {
    Value legIri = bs.getValue("legacy");
    if (legIri != null) {
      Concept legacy = matchLegacy(cl, legIri.stringValue());
      if (legacy == null) {
        legacy = new Concept();
        cl.addMatchedFrom(legacy);
        legacy.setIri(legIri.stringValue());
        Value lc = bs.getValue("legacyCode");
        Value lt = bs.getValue("legacyTerm");
        Value ls = bs.getValue(LEGACY_SCHEME);
        Value lsn = bs.getValue("legacySchemeName");
        Value luse = bs.getValue("legacyUse");
        Value codeId = bs.getValue("legacyCodeId");
        if (lc != null)
          legacy.setCode(lc.stringValue());
        if (lt != null)
          legacy.setName(lt.stringValue());
        if (ls != null) {
          if (lsn == null)
            legacy.setScheme(iri(ls.stringValue()));
          else
            legacy.setScheme(iri(ls.stringValue(), lsn.stringValue()));
        }
        if (codeId != null) {
          legacy.setCodeId(codeId.stringValue());
        }
        legacy.setUsage(luse == null ? null : ((Literal) luse).intValue());
      }
      Value lid = bs.getValue("legacyIm1Id");
      if (lid != null)
        legacy.addIm1Id(lid.stringValue());
    }
  }

  private Concept matchLegacy(Concept cl, String iri) {
    if (cl.getMatchedFrom() != null)
      for (Concept legacy : cl.getMatchedFrom())
        if (legacy.getIri().equals(iri))
          return legacy;
    return null;
  }

  public Set<Concept> getSomeMembers(String setIri, Integer limit) {
    String sparql = """
      SELECT *
      WHERE {
        ?setIri im:hasMember ?entity .
      }
      LIMIT %d
      """.formatted(limit);
    Set<Concept> result = new HashSet<>();

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(sparql));
      qry.setBinding("setIri", Values.iri(setIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Concept concept = new Concept();
          concept.setIri(bs.getValue(ENTITY).stringValue());
          result.add(concept);

        }
      }
    }
    return result;
  }

  public Set<TTNode> getBindingsForConcept(Set<String> members) {
    Set<TTNode> result = new HashSet<>();
    Set<String> sparqlIris = members.stream().map(m -> "<" + m + ">").collect(Collectors.toSet());
    String iriList = String.join(",", sparqlIris);
    String spql = """
      SELECT distinct ?dataModel ?path
      WHERE {
        ?memberIri ^im:hasMember ?valueSet.
        filter (?memberIri in(%s))
        {
          ?valueSet ^sh:class ?property.
          ?property sh:path ?path.
          ?property ^sh:property ?dataModel.
        }
        UNION {
          ?valueSet ^im:concept ?dataModel.
        }
      }
      GROUP BY ?dataModel ?path
      """.formatted(iriList);
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(spql));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          TTNode dataModel = new TTNode();
          dataModel.set(iri(SHACL.NODE), iri(bs.getValue("dataModel").stringValue()));
          if (bs.getValue("path") != null)
            dataModel.set(iri(SHACL.PATH), iri(bs.getValue("path").stringValue()));
          else
            dataModel.set(iri(SHACL.PATH), iri(IM.NAMESPACE + CONCEPT));
          result.add(dataModel);
        }
      }

    }
    return result;
  }


  public Set<Concept> getSetMembers(String setIri, boolean includeLegacy, List<String> schemes) {
    StringJoiner spql = new StringJoiner(System.lineSeparator())
      .add("""
        SELECT *
        WHERE {
          ?setIri im:hasMember ?entity .
          ?entity rdfs:label ?term;
          im:code ?code;
          im:scheme ?scheme.
          ?scheme rdfs:label ?schemeName .
          OPTIONAL { ?entity im:status ?status . ?status rdfs:label ?statusName . }
          OPTIONAL { ?entity im:im1Id ?im1Id . }
          OPTIONAL { ?entity im:usageTotal ?use . }
          OPTIONAL { ?entity im:codeId ?codeId . }
          OPTIONAL { ?entity im:alternativeCode ?alternativeCode.}
        """);

    if (includeLegacy) {
      spql.add("""
        OPTIONAL {
          ?legacy im:matchedTo ?entity;
          rdfs:label ?legacyTerm;
          im:code ?legacyCode;
          im:scheme ?legacyScheme.
        """);

      if (!schemes.isEmpty()) {
        String schemeIris = String.join(",", getIris(schemes));
        spql.add(" FILTER (?legacyScheme IN (" + schemeIris + "))");
      }

      spql.add("""
          ?legacyScheme rdfs:label ?legacySchemeName .
          OPTIONAL { ?legacy im:im1Id ?legacyIm1Id }
          OPTIONAL { ?legacy im:usageTotal ?legacyUse }
          OPTIONAL { ?legacy im:codeId ?codeId}
          OPTIONAL { ?legacy im:codeId ?legacyCodeId}
        }
        """);
    }

    spql.add("}  ");

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(addSparqlPrefixes(spql.toString()));
      qry.setBinding("setIri", Values.iri(setIri));
      return getCoreLegacyCodesForSparql(qry, includeLegacy, List.of());
    }
  }

  private List<String> getIris(List<String> schemes) {
    return schemes.stream().map(iri -> "<" + iri + ">").toList();
  }


  public Set<TTEntity> getAllConceptSets(TTIriRef type) {
    Set<TTEntity> result = new HashSet<>();
    String sql = """
      SELECT ?s
      WHERE {
        ?s rdf:type ?o
      }
      """;

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql);
      qry.setBinding("o", Values.iri(type.getIri()));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String iri = bs.getValue("s").stringValue();
          TTEntity set = getSetDefinition(iri);
          if (set.get(iri(IM.DEFINITION)) != null)
            result.add(set);
        }
      }
    }
    return result;
  }

  public TTEntity getSetDefinition(String setIri) {
    Set<String> predicates = new HashSet<>();
    predicates.add(IM.DEFINITION);
    predicates.add(IM.IS_CONTAINED_IN);
    predicates.add(RDFS.LABEL);

    TTBundle entityPredicates = entityRepository.getEntityPredicates(setIri, predicates);
    return entityPredicates.getEntity();
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
      qry.setBinding("isA", Values.iri(IM.IS_A));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          isas.add(bs.getValue("child").stringValue());

        }
      }
      return isas;
    }
  }

}
