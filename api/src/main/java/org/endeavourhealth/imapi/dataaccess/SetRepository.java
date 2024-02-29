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
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.SNOMED;
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
     * @param imQuery im query conforming to ecl language constraints
     * @param includeLegacy  to include legacy concepts linked by matchedTo to core concept
     * @return a Set of concepts with matchedFrom legacy concepts and list of im1 ids
     * @throws JsonProcessingException if json definitino invalid
     * @throws DataFormatException if query definition invalid
     */
    public Set<Concept> getSetExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter) throws QueryException {
        //add scheme filter
        return getSetExpansion(imQuery,includeLegacy,statusFilter,schemeFilter,null);
    }

    public Set<Concept> getSetExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, Page page) throws QueryException {
        Set<Concept> result = getExpansion(imQuery, includeLegacy, statusFilter, schemeFilter, page);
        imQuery.getMatch().get(0).setVariable("outerEntity");
        imQuery.match(m->m
          .setBool(Bool.or)
          .match(m1->m1
            .setVariable("entity")
            .property(p->p
              .setIri(IM.PREVIOUS_ENTITY_OF)
              .addIs(new Node().setNodeRef("outerEntity"))))
          .match(m1->m1
            .setVariable("entity")
            .property(p->p
              .setIri(IM.SUBSUMED_BY)
              .addIs(new Node().setNodeRef("outerEntity"))))
          .match(m1->m1
          .setVariable("entity")
          .property(p->p
            .setIri(IM.USUALLY_SUBSUMED_BY)
            .addIs(new Node().setNodeRef("outerEntity")))));
        Set<Concept> result2 = getExpansion(imQuery, includeLegacy, statusFilter, schemeFilter, page);
        result.addAll(result2);
        return result;
    }

    private Set<Concept> getExpansion(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter, Page page) throws QueryException {
    Return aReturn= new Return();
        imQuery.addReturn(aReturn);
        aReturn
            .property(s->s
                .setIri(RDFS.LABEL).as("term"))
            .property(s->s
                .setIri(IM.CODE).as("code"))
            .property(s->s
                .setIri(IM.HAS_SCHEME)
                .return_(n->n
                    .as("scheme")
                    .property(s2->s2
                        .setIri(RDFS.LABEL).as("schemeName"))))
            .property(s->s
                .setIri(IM.NAMESPACE+"usageTotal").as("usage"))
            .property(s->s
                .setIri(IM.IM1ID).as("im1Id"))
            .property(s->s
                .setIri(IM.HAS_STATUS)
                .return_(s2->s2
                    .as("status")
                    .property(p->p
                        .setIri(RDFS.LABEL).as("statusName"))))
            .property(s->s
                .setIri(RDF.TYPE)
                .return_(s2->s2
                    .as("entityType")
                    .property(p->p
                        .setIri(RDFS.LABEL).as("typeName"))))
          .property(s->s
            .setIri(IM.CODE_ID)
            .as("codeId"))
          .property(s->s
            .setIri(IM.ALTERNATIVE_CODE)
            .as("alternativeCode"));

        if (includeLegacy) {
            aReturn
                .property(p->p
                    .setIri(IM.MATCHED_TO)
                    .setInverse(true)
                    .return_(n->n
                        .as("legacy")
                        .property(s -> s
                            .setIri(RDFS.LABEL).as("legacyTerm"))
                        .property(s -> s
                            .setIri(IM.CODE).as("legacyCode"))
                        .property(s -> s
                            .setIri(IM.HAS_SCHEME)
                            .return_(n1->n1
                                .as("legacyScheme")
                                .property(p1->p1
                                    .setIri(RDFS.LABEL).as("legacySchemeName"))))
                        .property(s->s
                            .setIri(IM.NAMESPACE+"usageTotal").as("legacyUse"))
                      .property(s->s
                        .setIri(IM.CODE_ID)
                        .as("legacyCodeId"))
                        .property(s->s
                            .setIri(IM.IM1ID).as("legacyIm1Id"))));
        }
        QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
        if (null != page && null!= page.getPageNumber() && null!= page.getPageSize()) newRequest.setPage(page);
        String sql= new SparqlConverter(newRequest).getSelectSparql(statusFilter);
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            return getCoreLegacyCodesForSparql(qry, includeLegacy, schemeFilter);

        }
    }

    public int getSetExpansionTotalCount(Query imQuery, boolean includeLegacy, Set<TTIriRef> statusFilter, List<String> schemeFilter) throws QueryException {
        //add scheme filter
        QueryRequest newRequest = new QueryRequest().setQuery(imQuery);
        String sql= new SparqlConverter(newRequest).getCountSparql(statusFilter);
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            return getCountForSparql(qry, includeLegacy, schemeFilter);

        }
    }


    public Set<String> getSubsets(String setIri) {
        Set<String> result = new HashSet<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
          .add(IM_PREFIX)
          .add("SELECT ?subset WHERE {")
          .add("?subset ?issubset ?set .")
          .add("}");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql.toString());
            qry.setBinding("set", Values.iri(setIri));
            qry.setBinding("issubset", Values.iri(IM.IS_SUBSET_OF));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    String subsetIri = bs.getValue("subset").stringValue();
                    try {
                        Values.iri(subsetIri);
                        result.add(subsetIri);
                    } catch (IllegalArgumentException ignored) {
                        LOG.warn("Invalid subset iri [{}] for set [{}]", subsetIri, setIri);
                    }
                }
            }
        }

        return result;
    }


    private Set<Concept> getCoreLegacyCodesForSparql(TupleQuery qry, boolean includeLegacy, List<String> schemes) {
        Set<Concept> result = new HashSet<>();
        Set<String> coreSchemes= Set.of(SNOMED.NAMESPACE,IM.NAMESPACE);
        Map<String,Concept> conceptMap= new HashMap<>();
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
                    Value alternativeCode= bs.getValue("alternativeCode");
                    Value schemeName = bs.getValue("schemeName");
                    Value usage = bs.getValue("usage");
                    Value status = bs.getValue("status");
                    Value statusName = bs.getValue("statusName");
                    Value type = bs.getValue("entityType");
                    Value typeName = bs.getValue("typeName");
                    Value codeId=bs.getValue("codeId");
                    cl.setIri(concept);
                    if (name != null)
                        cl.setName(name.stringValue());
                    if (code != null) {
                        cl.setCode(code.stringValue());
                    }
                    if (alternativeCode!=null){
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
                    if (null!=codeId){
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
                    if (legacyScheme==null) {
                        if (!coreSchemes.contains(scheme.stringValue())) {
                            bindLegacyFromCore(bs, cl);
                        }
                    }
                    else if (schemes.size()==0){
                        bindResults(bs,cl);
                    }
                    else {
                            if(schemes.stream().anyMatch(s -> s.equals(legacyScheme))) {
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
        String legIri =cl.getIri();
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

    private Concept matchLegacy(Concept cl,String iri){
        if (cl.getMatchedFrom()!=null)
            for (Concept legacy:cl.getMatchedFrom())
                if (legacy.getIri().equals(iri))
                    return legacy;
        return null;
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

            if(schemes.size() != 0) {
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
            return getCoreLegacyCodesForSparql(qry, includeLegacy,List.of());
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
        Set<String> predicates= new HashSet<>();
        predicates.add(IM.DEFINITION);
        predicates.add(IM.IS_CONTAINED_IN);
        predicates.add(RDFS.LABEL);

        TTBundle entityPredicates = entityTripleRepository.getEntityPredicates(setIri, predicates);
        return entityPredicates.getEntity();
    }


}
