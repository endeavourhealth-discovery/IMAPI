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
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryRequest;
import org.endeavourhealth.imapi.model.imq.Select;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
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
    public Set<Concept> getSetExpansion(Query imQuery, boolean includeLegacy,Set<TTIriRef> statusFilter) throws JsonProcessingException, DataFormatException {
        imQuery
          .select(s->s
              .setIri(RDFS.LABEL.getIri()).setVariable("term"))
          .select(s->s
              .setIri(IM.CODE.getIri()).setVariable("code"))
          .select(s->s

              .setIri(IM.HAS_SCHEME.getIri()).setVariable("scheme")
            .select(s2->s2
                .setIri(RDFS.LABEL.getIri()).setVariable("schemeName")))
          .select(s->s
              .setIri(IM.WEIGHTING.getIri()).setVariable("usage"))
            .select(s->s
                .setIri(IM.IM1ID.getIri()).setVariable("im1Id"))
            .select(s->s
                    .setIri(IM.HAS_STATUS.getIri()).setVariable("status")
                .select(s2->s2
                        .setIri(RDFS.LABEL.getIri()).setVariable("statusName")))
            .select(s->s
                    .setIri(RDF.TYPE.getIri()).setVariable("type")
                .select(s2->s2
                        .setIri(RDFS.LABEL.getIri()).setVariable("typeName")));

        if (includeLegacy) {
            Select legacy= new Select();
              legacy
                  .setIri(IM.MATCHED_TO.getIri())
                .setInverse(true)
                .setVariable("legacy")
                .select(s -> s
                    .setIri(RDFS.LABEL.getIri()).setVariable("legacyTerm"))
                .select(s -> s
                    .setIri(IM.CODE.getIri()).setVariable("legacyCode"))
                .select(s -> s
                    .setIri(IM.HAS_SCHEME.getIri()).setVariable("legacyScheme")
                  .select(s1->s1
                      .setIri(RDFS.LABEL.getIri()).setVariable("legacySchemeName")))
                .select(s->s
                    .setIri(IM.NAMESPACE+"usageTotal").setVariable("legacyUse"))
                .select(s->s

                    .setIri(IM.IM1ID.getIri()).setVariable("legacyIm1Id"));
              imQuery.addSelect(legacy);
        }
        String sql= new SparqlConverter(new QueryRequest().setQuery(imQuery)).getSelectSparql(statusFilter);
        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(sql);
            return getCoreLegacyCodesForSparql(qry, includeLegacy);

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
            qry.setBinding("issubset", Values.iri(IM.IS_SUBSET_OF.getIri()));
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


    private Set<Concept> getCoreLegacyCodesForSparql(TupleQuery qry, boolean includeLegacy) {
        Set<Concept> result = new HashSet<>();
        Map<String,Concept> conceptMap= new HashMap<>();
        try (TupleQueryResult rs = qry.evaluate()) {
            while (rs.hasNext()) {
                BindingSet bs = rs.next();
                String concept =bs.getValue("entity").stringValue();
                Concept cl= conceptMap.get(concept);
                if (cl==null) {
                    cl = new Concept();
                    conceptMap.put(concept, cl);
                    result.add(cl);
                    Value name = bs.getValue("term");
                    Value code = bs.getValue("code");
                    Value scheme = bs.getValue("scheme");
                    Value schemeName = bs.getValue("schemeName");
                    Value usage = bs.getValue("usage");
                    Value status = bs.getValue("status");
                    Value statusName = bs.getValue("statusName");
                    Value type = bs.getValue("type");
                    Value typeName = bs.getValue("typeName");
                    cl.setIri(concept);
                    if (name != null)
                        cl.setName(name.stringValue());
                    if (code != null) {
                        cl.setCode(code.stringValue());
                        cl.setScheme(iri(scheme.stringValue(), schemeName.stringValue()));
                    }
                    if (null != status) {
                        cl.setStatus(iri(status.stringValue(),statusName.stringValue()));
                    }
                    if (null != type) {
                        cl.addType(iri(type.stringValue(),typeName.stringValue()));
                    }
                    cl.setUsage(usage == null ? null : ((Literal) usage).intValue());
                } else {
                    Value type = bs.getValue("type");
                    Value typeName = bs.getValue("typeName");
                    if (null != type) {
                        cl.addType(iri(type.stringValue(),typeName.stringValue()));
                    }
                }
                Value im1Id = bs.getValue("im1Id");
                if (im1Id != null)
                        cl.addIm1Id(im1Id.stringValue());
                if (includeLegacy) {
                        Value legIri = bs.getValue("legacy");
                        if (legIri != null) {
                            Concept legacy= matchLegacy(cl,legIri.stringValue());
                            if (legacy==null) {
                                legacy = new Concept();
                                cl.addMatchedFrom(legacy);
                                legacy.setIri(legIri.stringValue());
                                Value lc = bs.getValue("legacyCode");
                                Value lt = bs.getValue("legacyTerm");
                                Value ls = bs.getValue("legacyScheme");
                                Value lsn = bs.getValue("legacySchemeName");
                                Value luse = bs.getValue("legacyUse");
                                if (lc != null)
                                    legacy.setCode(lc.stringValue());
                                if (lt != null)
                                    legacy.setName(lt.stringValue());
                                if (ls != null) {
                                    legacy.setScheme(iri(ls.stringValue(), lsn.stringValue()));
                                }
                                legacy.setUsage(luse == null ? null : ((Literal) luse).intValue());
                            }
                            Value lid = bs.getValue("legacyIm1Id");
                            if (lid != null)
                                    legacy.addIm1Id(lid.stringValue());
                            }
                        }
                    }
                }
        return result;
    }

    private Concept matchLegacy(Concept cl,String iri){
        if (cl.getMatchedFrom()!=null)
            for (Concept legacy:cl.getMatchedFrom())
                if (legacy.getIri().equals(iri))
                    return legacy;
        return null;
    }


    public Set<Concept> getSetMembers(String setIri, boolean includeLegacy) {
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
          .add("    OPTIONAL { ?entity im:usageTotal ?use . }");

        if (includeLegacy) {
            spql.add("    OPTIONAL {")
              .add("        ?legacy im:matchedTo ?entity;")
              .add("                rdfs:label ?legacyTerm;")
              .add("                im:code ?legacyCode;")
              .add("                im:scheme ?legacyScheme.")
              .add("        ?legacyScheme rdfs:label ?legacySchemeName .")
              .add("        OPTIONAL { ?legacy im:im1Id ?legacyIm1Id }")
              .add("        OPTIONAL { ?legacy im:usageTotal ?legacyUse }")
              .add("    }");
        }

        spql.add("}  ");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = conn.prepareTupleQuery(spql.toString());
            qry.setBinding("setIri", Values.iri(setIri));
            return getCoreLegacyCodesForSparql(qry, includeLegacy);
        }
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
                    if (set.get(IM.DEFINITION) != null)
                        result.add(set);
                }
            }
        }
        return result;
    }

    public TTEntity getSetDefinition(String setIri) {
        Set<String> predicates= new HashSet<>();
        predicates.add(IM.DEFINITION.getIri());
        predicates.add(IM.IS_CONTAINED_IN.getIri());
        predicates.add(RDFS.LABEL.getIri());

        TTBundle entityPredicates = entityTripleRepository.getEntityPredicates(setIri, predicates);
        return entityPredicates.getEntity();
    }


}
