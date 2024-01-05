package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager.prepareSparql;

public class ProvRepository {
    public List<TTEntity> getProvHistory(String iri) {
        List<TTEntity> results = new ArrayList<>();

        StringJoiner sql = new StringJoiner(System.lineSeparator())
                .add("SELECT * WHERE {")
                .add("?prov <http://endhealth.info/im#provenanceTarget> ?entity ;")
                .add("      <http://endhealth.info/im#effectiveDate> ?effectiveDate ;")
                .add("      <http://endhealth.info/im#provenanceActivityType> ?activityType .")
                .add("Optional {?prov <http://endhealth.info/im#provenanceAgent> ?agent .")
                .add("         Optional {?agent rdfs:label ?agentName .}}")
                .add("Optional {?prov <http://endhealth.info/im#usedEntity> ?usedEntity .")
                .add("         Optional {?usedEntity rdfs:label ?usedEntityName .}}")
                .add("Optional {?activityType rdfs:label ?activityTypeName .}")
                .add("} order by desc(?effectiveDate)");

        try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
            TupleQuery qry = prepareSparql(conn, sql.toString());
            qry.setBinding("entity", iri(iri));
            try (TupleQueryResult rs = qry.evaluate()) {
                while (rs.hasNext()) {
                    BindingSet bs = rs.next();
                    TTEntity entity = new TTEntity(bs.getValue("prov").stringValue());
                    entity.set(TTIriRef.iri(IM.PROVENANCE_TARGET),iri);
                    entity.set(TTIriRef.iri(IM.EFFECTIVE_DATE),bs.getValue("effectiveDate").stringValue());
                    entity.set(TTIriRef.iri(IM.PROVENANCE_ACTIVITY_TYPE),new TTIriRef(bs.getValue("activityType").stringValue(), bs.getValue("activityTypeName").toString()));
                    if(bs.getValue("agent") != null) {
                        entity.set(TTIriRef.iri(IM.PROVENANCE_AGENT),new TTIriRef(bs.getValue("agent").stringValue(),bs.getValue("agentName").stringValue()));
                    }
                    if (bs.getValue("usedEntity") != null){
                        entity.set(TTIriRef.iri(IM.PROVENANCE_USED), new TTIriRef(bs.getValue("usedEntity").stringValue(),
                                bs.getValue("usedEntityName")!= null ? bs.getValue("usedEntityName").stringValue() : null));
                    }
                    results.add(entity);
                }
            }
        }
        return results;
    }
}
