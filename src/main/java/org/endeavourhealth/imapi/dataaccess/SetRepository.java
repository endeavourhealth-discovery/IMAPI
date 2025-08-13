package org.endeavourhealth.imapi.dataaccess;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.Pageable;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Page;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.imq.Return;
import org.endeavourhealth.imapi.model.requests.QueryRequest;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.endeavourhealth.imapi.dataaccess.helpers.SparqlHelper.valueList;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@Slf4j
public class SetRepository {
  public static final String EXPANDED_ENTITY = "expandedEntity";
  public static final String ENTITY = "entity";
  public static final String IM_1_ID = "im1Id";
  public static final String ENTITY_TYPE = "entityType";
  public static final String TYPE_NAME = "typeName";
  public static final String LEGACY_SCHEME = "legacyScheme";
  public static final String LEGACY_STATUS = "legacyStatus";
  public static final String LEGACY_STATUS_NAME = "legacyStatusName";

  public static final String CONCEPT = "concept";

  /**
   * Returns an expanded set members match an iml set definition. If already expanded then returns members
   * otherwise expands and retuens members
   *
   * @param imQuery       im query conforming to ecl language constraints
   * @param includeLegacy to include legacy concepts linked by matchedTo to core concept
   * @return a Set of concepts with matchedFrom legacy concepts and list of im1 ids
   * @throws QueryException if json definitino invalid
   */
  public Set<Concept> getSetExpansionFromQuery(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, List<Graph> graphs) throws QueryException {
    //add scheme filter
    return getSetExpansionFromQuery(imQuery, includeLegacy, statusFilter, schemeFilter, null, graphs);
  }

  public Set<Concept> getMembersFromDefinition(Query imQuery, List<Graph> graphs) throws QueryException {
    Set<Concept> result = new HashSet<>();
    QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
    String sql = new SparqlConverter(newRequest).getSelectSparql(null);
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          result.add(new Concept().setIri(bs.getValue(ENTITY).stringValue()));
        }
      }
    }
    return result;
  }

  public Set<Concept> getSetExpansionFromQuery(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, Page page, List<Graph> graphs) throws QueryException {
    imQuery = getFullExpansionDefinition(imQuery, includeLegacy);
    QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
    if (null != page && null != page.getPageNumber() && null != page.getPageSize()) newRequest.setPage(page);
    String sql = new SparqlConverter(newRequest).getSelectSparql(statusFilter);
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      return getCoreLegacyCodesForSparql(qry, includeLegacy, schemeFilter, imQuery.getVariable());
    }
  }

  private Query getFullExpansionDefinition(Query imQuery, boolean includeLegacy) {
    Query fullQuery = new Query();
    fullQuery.setSubquery(imQuery);
    setReturn(fullQuery, includeLegacy);
    fullQuery.setVariable(EXPANDED_ENTITY);
    fullQuery
      .or(m -> m
        .where(p -> p
          .setIri(IM.PREVIOUS_ENTITY_OF)
          .addIs(new Node().setNodeRef(ENTITY))))
      .or(m1 -> m1
        .where(p -> p
          .setIri(IM.SUBSUMED_BY)
          .addIs(new Node().setNodeRef(ENTITY))));
    return fullQuery;

  }


  private void setReturn(Query imQuery, boolean includeLegacy) {
    Return aReturn = new Return();
    imQuery.setReturn(aReturn);
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
        .setIri(IM.USAGE_TOTAL).as("usage"))
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
      aReturn.property(p -> p
        .setIri(IM.MATCHED_TO)
        .setInverse(true)
        .return_(n -> n.as("legacy")
          .property(s -> s.setIri(RDFS.LABEL).as("legacyTerm"))
          .property(s -> s.setIri(IM.CODE).as("legacyCode"))
          .property(s -> s.setIri(IM.HAS_SCHEME).return_(n1 -> n1.as(LEGACY_SCHEME).property(p1 -> p1.setIri(RDFS.LABEL).as("legacySchemeName"))))
          .property(s -> s.setIri(IM.USAGE_TOTAL).as("legacyUse"))
          .property(s -> s.setIri(IM.CODE_ID).as("legacyCodeId"))
          .property(s -> s.setIri(IM.IM_1_ID).as("legacyIm1Id"))
        )
      );
      aReturn.property(p -> p
        .setIri(IM.LOCAL_SUBCLASS_OF)
        .setInverse(true)
        .return_(n -> n.as("legacy")
          .property(s -> s.setIri(RDFS.LABEL).as("legacyTerm"))
          .property(s -> s.setIri(IM.CODE).as("legacyCode"))
          .property(s -> s.setIri(IM.HAS_SCHEME).return_(n1 -> n1.as(LEGACY_SCHEME).property(p1 -> p1.setIri(RDFS.LABEL).as("legacySchemeName"))))
          .property(s -> s.setIri(IM.USAGE_TOTAL).as("legacyUse"))
          .property(s -> s.setIri(IM.CODE_ID).as("legacyCodeId"))
          .property(s -> s.setIri(IM.IM_1_ID).as("legacyIm1Id"))
        )
      );
    }
  }


  public int getSetExpansionTotalCount(Query imQuery, Set<TTIriRef> statusFilter, List<Graph> graphs) throws QueryException {
    //add scheme filter
    QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
    String sql = new SparqlConverter(newRequest).getCountSparql(statusFilter);
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      return getCountForSparql(qry);

    }
  }

  public Set<TTIriRef> getSubsetIrisWithNames(String iri, List<Graph> graphs) {
    Set<TTIriRef> result = new HashSet<>();

    String sql = """
      SELECT ?subset ?name
      WHERE {
        ?subset ?isSubset ?set .
        ?subset ?label ?name .
      }
      """;

    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("set", Values.iri(iri));
      qry.setBinding("isSubset", IM.IS_SUBSET_OF.asDbIri());
      qry.setBinding("label", RDFS.LABEL.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String subsetIri = bs.getValue("subset").stringValue();
          String subsetName = bs.getValue("name").stringValue();
          try {
            TTIriRef subset = new TTIriRef(subsetIri, subsetName);
            result.add(subset);
          } catch (IllegalArgumentException ignored) {
            log.warn("Invalid subset iri [{}] for set [{}]", subsetIri, iri);
          }
        }
      }
    }

    return result;
  }

  private Set<Concept> getCoreLegacyCodesForSparql(TupleQuery qry, boolean includeLegacy, List<String> schemes, String entityVariable) {
    Set<Concept> result = new HashSet<>();
    Set<String> coreSchemes = asHashSet(Namespace.SNOMED, Namespace.IM);
    Map<String, Concept> conceptMap = new HashMap<>();
    try (TupleQueryResult rs = qry.evaluate()) {
      while (rs.hasNext()) {
        BindingSet bs = rs.next();
        String concept = bs.getValue(entityVariable).stringValue();
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
        if (im1Id != null) cl.setIm1Id(im1Id.stringValue());
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
    if (name != null) cl.setName(name.stringValue());
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
    cl.setUsage(usage == null ? 0 : ((Literal) usage).intValue());
    return cl;
  }

  private int getCountForSparql(TupleQuery qry) {
    try (TupleQueryResult rs = qry.evaluate()) {
      if (rs.hasNext()) {
        BindingSet bs = rs.next();
        return ((Literal) bs.getValue("count")).intValue();
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
      if (lid != null) legacy.setIm1Id(lid.stringValue());
    }
  }


  public void bindConceptSetToDataModel(String iri, Set<TTNode> dataModels, List<Graph> userGraphs, Graph insertGraph) {

    String deleteBinding = """
      DELETE { ?concept im:binding ?datamodel}
      WHERE {
        ?concept im:binding ?datamodel
      }
      """;

    StringJoiner newBinding = new StringJoiner("\n").add("INSERT DATA {");
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
    newBinding.add("}");

    try (IMDB conn = IMDB.getConnection(userGraphs)) {
      conn.begin();
      org.eclipse.rdf4j.query.Update upd = conn.prepareDeleteSparql(deleteBinding);
      upd.setBinding(CONCEPT, Values.iri(iri));
      upd.execute();
      upd = conn.prepareInsertSparql(newBinding.toString(), insertGraph);
      upd.execute();
      conn.commit();
    }

  }

  public Set<String> getSets(List<Graph> graphs) {
    Set<String> setIris = new HashSet<>();
    try (IMDB conn = IMDB.getConnection(graphs)) {
      String spq = """
        SELECT distinct ?iri
        WHERE {
          ?iri rdf:type ?type.
          FILTER (?type in (im:ValueSet, im:ConceptSet))
        }
        """;

      TupleQuery qry = conn.prepareTupleSparql(spq);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          setIris.add(rs.next().getValue("iri").stringValue());
        }
      }
    }
    return setIris;
  }

  public void updateMembers(String iri, Set<Concept> members, Graph graph) {
    try (IMDB conn = IMDB.getConnection(List.of(graph))) {
      String spq = """
        DELETE { ?concept im:hasMember ?x.}
        WHERE {
          ?concept im:hasMember ?x.
        }
        """;
      org.eclipse.rdf4j.query.Update upd = conn.prepareDeleteSparql(spq);
      upd.setBinding(CONCEPT, Values.iri(iri));
      upd.execute();
      StringJoiner sj = new StringJoiner("\n");
      sj.add("INSERT DATA {");
      int batch = 0;
      for (Concept member : members) {
        batch++;
        if (batch == 1000) {
          sj.add("}");
          sendUp(sj, conn, graph);
          sj = new StringJoiner("\n");
          sj.add("INSERT DATA {");
          batch = 0;
        }
        sj.add("<" + iri + "> im:hasMember <" + member.getIri() + ">.");
      }
      sj.add("}");
      sendUp(sj, conn, graph);
    }
  }


  private void sendUp(StringJoiner sj, IMDB conn, Graph graph) {
    org.eclipse.rdf4j.query.Update upd = conn.prepareInsertSparql(sj.toString(), graph);
    upd.setBinding("g", graph.asDbIri());
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
        String legacyStatus = bs.getValue(LEGACY_STATUS) != null ? bs.getValue(LEGACY_STATUS).stringValue() : null;
        String legacyStatusName = bs.getValue(LEGACY_STATUS_NAME) != null ? bs.getValue(LEGACY_STATUS_NAME).stringValue() : null;
        if (null != legacyStatus && null != legacyStatusName)
          legacy.setStatus(new TTIriRef(legacyStatus, legacyStatusName));
        if (lc != null) legacy.setCode(lc.stringValue());
        if (lt != null) legacy.setName(lt.stringValue());
        if (ls != null) {
          if (lsn == null) legacy.setScheme(iri(ls.stringValue()));
          else legacy.setScheme(iri(ls.stringValue(), lsn.stringValue()));
        }
        if (codeId != null) {
          legacy.setCodeId(codeId.stringValue());
        }
        legacy.setUsage(luse == null ? 0 : ((Literal) luse).intValue());
      }
      Value lid = bs.getValue("legacyIm1Id");
      if (lid != null) legacy.setIm1Id(lid.stringValue());
    }
  }

  private Concept matchLegacy(Concept cl, String iri) {
    if (cl.getMatchedFrom() != null) for (Concept legacy : cl.getMatchedFrom())
      if (legacy.getIri().equals(iri)) return legacy;
    return null;
  }

  public Set<Concept> getSomeMembers(String setIri, Integer limit, List<Graph> graphs) {
    String sparql = """
      SELECT *
      WHERE {
        ?setIri im:hasMember ?entity .
      }
      LIMIT %d
      """.formatted(limit);
    Set<Concept> result = new HashSet<>();

    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sparql);
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

  public Set<TTNode> getBindingsForConcept(Set<String> members, List<Graph> graphs) {
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
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(spql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          TTNode dataModel = new TTNode();
          dataModel.set(iri(SHACL.NODE), iri(bs.getValue("dataModel").stringValue()));
          if (bs.getValue("path") != null) dataModel.set(iri(SHACL.PATH), iri(bs.getValue("path").stringValue()));
          else dataModel.set(iri(SHACL.PATH), iri(IM.CONCEPT_PROPERTY));
          result.add(dataModel);
        }
      }

    }
    return result;
  }


  public Set<Concept> getExpansionFromIri(String setIri, boolean includeLegacy, List<String> schemes,
                                          List<String> subsumptions, List<Graph> graphs) {

    StringJoiner spql = new StringJoiner(System.lineSeparator())
      .add("""
        SELECT distinct ?entity ?term ?code ?scheme ?schemeName ?status ?statusName ?im1Id ?use ?codeId
        ?alternativeCode ?legacy ?legacyTerm ?legacyCode ?legacyScheme ?legacySchemeName
        ?legacyIm1Id ?legacyUse ?legacyCodeId
        WHERE {
            Values ?setIri{%s}
            {
                    ?setIri im:hasMember ?entity .
            }
        """.formatted("<" + setIri + ">"));
    if (subsumptions != null) {
      spql.add("""
        union {
          ?setIri im:hasMember ?member.
          ?entity ?subsumption ?member.
          Values ?subsumption {%s}
        }
        """.formatted(subsumptions.stream().map(s -> "<" + s + ">").collect(Collectors.joining(" "))));
    }
    spql.add("""
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
          ?legacy im:matchedTo ?entity.
          ?legacy rdfs:label ?legacyTerm.
          ?legacy im:code ?legacyCode.
          ?legacy im:scheme ?legacyScheme.
        """);

      if (!schemes.isEmpty()) {
        String schemeIris = String.join(",", getIris(schemes));
        spql.add(" FILTER (?legacyScheme IN (" + schemeIris + "))");
      }

      spql.add("""
          ?legacyScheme rdfs:label ?legacySchemeName .
          OPTIONAL { ?legacy im:im1Id ?legacyIm1Id }
          OPTIONAL { ?legacy im:status ?legacyStatus . ?legacyStatus rdfs:label ?legacyStatusName . }
          OPTIONAL { ?legacy im:usageTotal ?legacyUse }
          OPTIONAL { ?legacy im:codeId ?codeId}
          OPTIONAL { ?legacy im:codeId ?legacyCodeId}
        }
        """);
    }

    spql.add("}  ");

    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(spql.toString());
      qry.setBinding("setIri", Values.iri(setIri));
      return getCoreLegacyCodesForSparql(qry, includeLegacy, List.of(), "entity");
    }
  }

  private List<String> getIris(List<String> schemes) {
    return schemes.stream().map(iri -> "<" + iri + ">").toList();
  }


  public Set<String> getDistillation(String iris, List<Graph> graphs) {
    Set<String> isas = new HashSet<>();

    try (IMDB conn = IMDB.getConnection(graphs)) {
      String sql = """
        SELECT ?child
        WHERE {
        VALUES ?child { %s }
        VALUES ?parent { %s }
        ?child ?isA ?parent .
        FILTER (?child != ?parent)}
        """.formatted(iris, iris);
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("isA", IM.IS_A.asDbIri());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          isas.add(bs.getValue("child").stringValue());

        }
      }
      return isas;
    }
  }


  public Pageable<Node> getMembers(String iri, boolean entailed, Integer pageNumber, Integer pageSize, List<Graph> graphs) {

    if (entailed) {
      Pageable<Node> result = getMemberWithPredicate(iri, IM.ENTAILED_MEMBER.toString(), pageNumber, pageSize, graphs);
      if (result.getTotalCount() > 0) return result;
    }
    return getMemberWithPredicate(iri, IM.HAS_MEMBER.toString(), pageNumber, pageSize, graphs);
  }

  private Pageable<Node> getMemberWithPredicate(String iri, String predicate, Integer pageNumber, Integer pageSize, List<Graph> graphs) {
    Pageable<Node> result = new Pageable<>();
    result.setTotalCount(0);
    String sql = """
      Select (count(distinct ?instance) as ?count)
      where {
      ?s ?p ?instance
      }
      """;
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("s", Values.iri(iri));
      qry.setBinding("p", Values.iri(predicate));
      try (TupleQueryResult rsCount = qry.evaluate()) {
        BindingSet bsCount = rsCount.next();
        result.setTotalCount(((Literal) bsCount.getValue("count")).intValue());
      }
    }
    String offset = Integer.toString((pageNumber - 1) * pageSize);
    if (result.getTotalCount() == 0) return result;
    if (predicate.equals(IM.ENTAILED_MEMBER.toString())) {
      sql = """
        Select ?member ?entailment ?name  ?exclude
        where {
        ?s im:entailedMember ?instance.
        ?instance im:instanceOf ?member.
        ?member rdfs:label ?name.
        optional {?instance im:exclude ?exclude.}
        optional {?instance im:entailment ?entailment}
        }
        order by ?exclude
        limit %s
        offset %s
        """.formatted(pageSize, offset);
    } else {
      sql = """
        Select ?member ?name
        where {
        ?s im:hasMember ?member.
        ?member rdfs:label ?name.
        }
        limit %s
        offset %s
        """.formatted(pageSize, offset);
    }
    List<Node> resultSet = new ArrayList<>();
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("s", Values.iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Node node = new Node();
          resultSet.add(node);
          node.setIri(bs.getValue("member").stringValue()).setName(bs.getValue("name").stringValue());
          if (bs.getValue("entailment") != null) {
            String entailment = bs.getValue("entailment").stringValue();
            switch (IM.from(entailment)) {
              case IM.DESCENDANTS_OR_SELF_OF -> node.setDescendantsOrSelfOf(true);
              case IM.DESCENDANTS_OF -> node.setDescendantsOf(true);
              case IM.ANCESTORS_OF -> node.setAncestorsOf(true);
            }
            if (bs.getValue("exclude") != null) {
              node.setExclude(true);
            }
          }
        }
        result.setResult(resultSet);
      }
    }
    return result;
  }

  public Set<Concept> getExpansionFromEntailedMembers(String setIri, List<Graph> graphs) {
    String sql = """
      select distinct ?member
      where {
        Values ?set { <%s> }
        ?set im:entailedMember ?entailed.
        {
          ?entailed im:instanceOf ?member.
          filter not exists {?entailed im:entailment ?entailment}
        }
        union {
          ?entailed im:instanceOf ?parent.
          ?entailed im:entailment im:DescendantsOrSelfOf.
          ?member im:isA ?parent.
        }
        union {
          ?entailed im:instanceOf ?parent.
          ?entailed im:entailment im:DescendantsOf.
          ?member im:isA ?parent.
          filter (?member!=?parent)
        }
        union {
          ?entailed im:instanceOf ?child.
          ?entailed im:entailment im:AncestorsOf.
          ?child im:isA ?member.
        }
        filter not exists {
          ?member im:isA ?parent2.
          ?parent2 ^im:instanceOf ?entailment2.
          ?entailment2 im:entailment im:DescendantsOrSelfOf.
          ?entailment2 im:exclude true.
        }
        filter not exists {
          ?member im:isA ?parent2.
          filter (?member!=?parent2)
          ?parent2 ^im:instanceOf ?entailment2.
          ?entailment2 im:entailment im:DescendantsOf.
          ?entailment2 im:exclude true.
        }
        filter not exists {
          ?parent2 im:isA ?member.
          ?parent2 ^im:instanceOf ?entailment2.
          ?entailment2 im:entailment im:AncestorsOf.
          ?entailment2 im:exclude true.
        }
      }
      """.formatted(setIri);
    Set<Concept> expansion = new HashSet<>();
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          expansion.add(new Concept().setIri(bs.getValue("member").stringValue()));
        }
      }
    }
    return expansion;
  }


  public boolean isValidPropertyForDomains(String propertyIri, Set<String> entityIris, List<Graph> graphs) {
    String spq = """
      ASK
      WHERE {?property im:isA ?superProperty.
      %s
      %s
      ?superProperty rdfs:domain ?domain.
      ?concept im:isA ?domain.
      }
      """.formatted(valueList("property", List.of(propertyIri)), valueList("concept", entityIris));
    try (IMDB conn = IMDB.getConnection(graphs)) {
      return conn.prepareBooleanSparql(spq).evaluate();
    }
  }

  public boolean isValidRangeForProperty(String propertyIri, String valueIri, List<Graph> graphs) {
    List<String> values = List.of(propertyIri, valueIri);
    String spq = """
      ASK
      WHERE {?property im:isA ?superProperty.
      %s
      ?superProperty rdfs:range ?range.
      %s
      ?concept im:isA ?range.
      }
      
      """.formatted(valueList("property", values), valueList("concept", values));
    try (IMDB conn = IMDB.getConnection(graphs)) {
      return conn.prepareBooleanSparql(spq).evaluate();
    }
  }


  public Map<String, Boolean> getValidConcepts(Set<String> conceptIris, List<Graph> graphs) {
    Map<String, Boolean> result = new HashMap<>();
    String spq = """
      SELECT ?concept ?type
      WHERE {
        %s
        optional {?concept rdf:type ?type.}
      }
      """.formatted(valueList("concept", conceptIris));
    try (IMDB conn = IMDB.getConnection(graphs)) {
      TupleQuery qry = conn.prepareTupleSparql(spq);
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          String conceptIri = bs.getValue("concept").stringValue();
          if (bs.getValue("type") != null) {
            result.put(conceptIri, true);
          } else result.put(conceptIri, false);
        }
      }
    }
    return result;
  }
}

