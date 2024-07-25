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
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetRepository {
  private static final Logger LOG = LoggerFactory.getLogger(SetRepository.class);
  private EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
  private String IM_PREFIX = "PREFIX im: <" + IM.NAMESPACE + ">";

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
    replaced.setVariable("activeEntity");
    replaced.getMatch().get(0).setVariable("activeEntity");
    if (replaced.getMatch().get(0).getMatch() != null) {
      for (Match match : replaced.getMatch().get(0).getMatch()) {
        match.setVariable("activeEntity");
      }
    }
    Return aReturn = setReturn(imQuery, includeLegacy);
    aReturn.setNodeRef("entity");
    replaced.addReturn(aReturn);
    replaced.match(m -> m
      .setBoolMatch(Bool.or)
      .match(m1 -> m1
        .setVariable("entity")
        .where(p -> p
          .setIri(IM.PREVIOUS_ENTITY_OF)
          .addIs(new Node().setNodeRef("activeEntity"))))
      .match(m1 -> m1
        .setVariable("entity")
        .where(p -> p
          .setIri(IM.SUBSUMED_BY)
          .addIs(new Node().setNodeRef("activeEntity"))))
      .match(m1 -> m1
        .setVariable("entity")
        .where(p -> p
          .setIri(IM.USUALLY_SUBSUMED_BY)
          .addIs(new Node().setNodeRef("activeEntity")))));
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
        .setIri(IM.IM1ID).as("im1Id"))
      .property(s -> s
        .setIri(IM.HAS_STATUS)
        .return_(s2 -> s2
          .as("status")
          .property(p -> p
            .setIri(RDFS.LABEL).as("statusName"))))
      .property(s -> s
        .setIri(RDF.TYPE)
        .return_(s2 -> s2
          .as("entityType")
          .property(p -> p
            .setIri(RDFS.LABEL).as("typeName"))))
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
                .as("legacyScheme")
                .property(p1 -> p1
                  .setIri(RDFS.LABEL).as("legacySchemeName"))))
            .property(s -> s
              .setIri(IM.NAMESPACE + "usageTotal").as("legacyUse"))
            .property(s -> s
              .setIri(IM.CODE_ID)
              .as("legacyCodeId"))
            .property(s -> s
              .setIri(IM.IM1ID).as("legacyIm1Id"))));
    }
    return aReturn;
  }


  public int getSetExpansionTotalCount(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter) throws QueryException {
    //add scheme filter
    QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
    String sql = new SparqlConverter(newRequest).getCountSparql(statusFilter);
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(sql);
      return getCountForSparql(qry, includeLegacy, schemeFilter);

    }
  }

  public Set<TTIriRef> getSubsetIrisWithNames(String iri) {
    Set<TTIriRef> result = new HashSet<>();

    StringJoiner sql = new StringJoiner(System.lineSeparator())
      .add(IM_PREFIX)
      .add("SELECT ?subset ?name WHERE {")
      .add("?subset ?issubset ?set .")
      .add("?subset ?label ?name .")
      .add("}");

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(sql.toString());
      qry.setBinding("set", Values.iri(iri));
      qry.setBinding("issubset", Values.iri(IM.IS_SUBSET_OF));
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
        String concept = bs.getValue("entity").stringValue();
        Concept cl = conceptMap.get(concept);
        Value scheme = bs.getValue("scheme");
        if (cl == null) {
          cl = new Concept();
          conceptMap.put(concept, cl);
          result.add(cl);
          Value name = bs.getValue("term");
          Value code = bs.getValue("code");
          Value alternativeCode = bs.getValue("alternativeCode");
          Value schemeName = bs.getValue("schemeName");
          Value usage = bs.getValue("usage");
          Value status = bs.getValue("status");
          Value statusName = bs.getValue("statusName");
          Value type = bs.getValue("entityType");
          Value typeName = bs.getValue("typeName");
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
        } else {
          Value type = bs.getValue("entityType");
          Value typeName = bs.getValue("typeName");
          if (null != type) {
            cl.addType(iri(type.stringValue(), typeName.stringValue()));
          }
        }
        Value im1Id = bs.getValue("im1Id");
        if (im1Id != null)
          cl.addIm1Id(im1Id.stringValue());
        if (includeLegacy) {
          String legacyScheme = bs.getValue("legacyScheme") != null ? bs.getValue("legacyScheme").stringValue() : null;
          if (legacyScheme == null) {
            if (!coreSchemes.contains(scheme.stringValue())) {
              bindLegacyFromCore(bs, cl);
            }
          } else if (schemes.size() == 0) {
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

  private int getCountForSparql(TupleQuery qry, boolean includeLegacy, List<String> schemes) {
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
      Value lid = bs.getValue("im1Id");
      if (lid != null)
        legacy.addIm1Id(lid.stringValue());
    }
  }


  public void bindConceptSetToDataModel(String iri, Set<TTNode> dataModels) {

    StringJoiner deleteBinding = new StringJoiner("\n")
      .add("DELETE { <" + iri + "> <" + IM.BINDING + "> ?datamodel}")
      .add("WHERE { <" + iri + "> <" + IM.BINDING + "> ?datamodel}");

    StringJoiner newBinding = new StringJoiner("\n").add("INSERT DATA { graph <" + GRAPH.DISCOVERY + "> {");
    int blank = 0;
    for (TTNode dataModel : dataModels) {
      blank++;
      newBinding.add("<" + iri + "> <" + IM.BINDING + "> _:b" + blank + ".")
        .add("_:b" + blank + " <" + SHACL.PATH + "> <" + dataModel.get(iri(SHACL.PATH)).asIriRef().getIri() + ">.")
        .add("_:b" + blank + " <" + SHACL.NODE + "> <" + dataModel.get(iri(SHACL.NODE)).asIriRef().getIri() + ">.");
    }
    newBinding.add("}}");

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      conn.begin();
      org.eclipse.rdf4j.query.Update upd = conn.prepareUpdate(deleteBinding.toString());
      upd.execute();
      upd = conn.prepareUpdate(newBinding.toString());
      upd.execute();
      conn.commit();
    }

  }

  public Set<String> getSets() {
    Set<String> setIris = new HashSet<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      StringJoiner spq = new StringJoiner("\n");
      spq.add("SELECT distinct ?iri ")
        .add("WHERE { ?iri <" + RDF.TYPE + "> ?type.")
        .add("  filter (?type in (<" + IM.VALUESET + ">,<" + IM.CONCEPT_SET + ">))")
        .add("?iri <" + IM.DEFINITION + "> ?d.}");
      TupleQuery qry = conn.prepareTupleQuery(spq.toString());
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
      String spq = "DELETE { <" + iri + "> <" + IM.HAS_MEMBER + "> ?x.}" +
        "\nWHERE { <" + iri + "> <" + IM.HAS_MEMBER + "> ?x.}";
      org.eclipse.rdf4j.query.Update upd = conn.prepareUpdate(spq);
      upd.execute();
      spq = "SELECT ?g where { graph ?g {<" + iri + "> <" + RDF.TYPE + "> ?type }}";
      TupleQuery qry = conn.prepareTupleQuery(spq);
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
          sj.add("<" + iri + "> <" + IM.HAS_MEMBER + "> <" + member.getIri() + ">.");
        }
        sendUp(sj, conn);
      }
    }
  }


  private void sendUp(StringJoiner sj, RepositoryConnection conn) {
    sj.add("}}");
    org.eclipse.rdf4j.query.Update upd = conn.prepareUpdate(sj.toString());
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
        Value ls = bs.getValue("legacyScheme");
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
    StringJoiner spql = new StringJoiner(System.lineSeparator())
      .add("PREFIX im: <" + IM.NAMESPACE + ">")
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("select * where { ")
      .add("    ?setIri im:hasMember ?entity .")
      .add("}")
      .add("limit " + limit);
    Set<Concept> result = new HashSet<>();

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(spql.toString());
      qry.setBinding("setIri", Values.iri(setIri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          Concept concept = new Concept();
          concept.setIri(bs.getValue("entity").stringValue());
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
    StringJoiner spql = new StringJoiner(System.lineSeparator())
      .add("PREFIX im: <" + IM.NAMESPACE + ">")
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("select distinct ?dataModel ?path")
      .add("where { ")
      .add("    ?memberIri ^im:hasMember ?valueSet.")
      .add("     filter (?memberIri in(" + iriList + "))")
      .add("  {")
      .add("    ?valueSet ^<" + SHACL.CLASS + "> ?property.")
      .add("     ?property <" + SHACL.PATH + "> ?path.")
      .add("     ?property ^<" + SHACL.PROPERTY + "> ?dataModel.}")
      .add(" union {")
      .add("    ?valueSet ^<" + IM.NAMESPACE + "concept" + "> ?dataModel.}}")
      .add(" group by ?dataModel ?path");
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(spql.toString());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          TTNode dataModel = new TTNode();
          dataModel.set(iri(SHACL.NODE), iri(bs.getValue("dataModel").stringValue()));
          if (bs.getValue("path") != null)
            dataModel.set(iri(SHACL.PATH), iri(bs.getValue("path").stringValue()));
          else
            dataModel.set(iri(SHACL.PATH), iri(IM.NAMESPACE + "concept"));
          result.add(dataModel);
        }
      }

    }
    return result;
  }


  public Set<Concept> getSetMembers(String setIri, boolean includeLegacy, List<String> schemes) {
    StringJoiner spql = new StringJoiner(System.lineSeparator())
      .add("PREFIX im: <" + IM.NAMESPACE + ">")
      .add("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>")
      .add("select * where { ")
      .add("    ?setIri im:hasMember ?entity .")
      .add("    ?entity rdfs:label ?term;")
      .add("       im:code ?code;")
      .add("       im:scheme ?scheme.")
      .add("    ?scheme rdfs:label ?schemeName .")
      .add("    OPTIONAL { ?entity im:status ?status . ?status rdfs:label ?statusName . }")
      .add("    OPTIONAL { ?entity im:im1Id ?im1Id . }")
      .add("    OPTIONAL { ?entity im:usageTotal ?use . }")
      .add("    OPTIONAL { ?entity im:codeId ?codeId . }")
      .add("    OPTIONAL { ?entity im:alternativeCode ?alternativeCode.}");

    if (includeLegacy) {
      spql.add("    OPTIONAL {")
        .add("        ?legacy im:matchedTo ?entity;")
        .add("                rdfs:label ?legacyTerm;")
        .add("                im:code ?legacyCode;")
        .add("                im:scheme ?legacyScheme.");

      if (schemes.size() != 0) {
        String schemeIris = String.join(",", getIris(schemes));
        spql.add(" FILTER (?legacyScheme IN (" + schemeIris + "))");
      }

      spql.add("        ?legacyScheme rdfs:label ?legacySchemeName .")
        .add("        OPTIONAL { ?legacy im:im1Id ?legacyIm1Id }")
        .add("        OPTIONAL { ?legacy im:usageTotal ?legacyUse }")
        .add("        OPTIONAL { ?legacy im:codeId ?codeId}")
        .add("        OPTIONAL { ?legacy im:codeId ?legacyCodeId}")
        .add("    }");
    }

    spql.add("}  ");

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = conn.prepareTupleQuery(spql.toString());
      qry.setBinding("setIri", Values.iri(setIri));
      return getCoreLegacyCodesForSparql(qry, includeLegacy, List.of());
    }
  }

  private List<String> getIris(List<String> schemes) {
    return schemes.stream().map(iri -> "<" + iri + ">").collect(Collectors.toList());
  }


  public Set<TTEntity> getAllConceptSets(TTIriRef type) {
    Set<TTEntity> result = new HashSet<>();
    StringJoiner sql = new StringJoiner(System.lineSeparator())
      .add("SELECT ?s")
      .add("WHERE {")
      .add("  ?s rdf:type ?o")
      .add("}");

    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      TupleQuery qry = prepareSparql(conn, sql.toString());
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

    TTBundle entityPredicates = entityTripleRepository.getEntityPredicates(setIri, predicates);
    return entityPredicates.getEntity();
  }


}
