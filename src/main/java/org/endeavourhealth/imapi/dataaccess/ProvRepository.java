package org.endeavourhealth.imapi.dataaccess;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.databases.ProvDB;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;

import java.util.ArrayList;
import java.util.List;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class ProvRepository {
  public List<TTEntity> getProvHistory(String iri) {
    List<TTEntity> results = new ArrayList<>();

    String sql = """
      SELECT *
      WHERE {
        ?prov im:provenanceTarget ?entity ;
        im:effectiveDate ?effectiveDate ;
        im:provenanceActivityType ?activityType .
        Optional {
          ?prov im:provenanceAgent ?agent .
          Optional {?agent rdfs:label ?agentName .}
        }
        Optional {
          ?prov im:usedEntity ?usedEntity .
          Optional {?usedEntity rdfs:label ?usedEntityName .}
        }
        Optional {?activityType rdfs:label ?activityTypeName .}
      } order by desc(?effectiveDate)
      """;

    try (ProvDB conn = ProvDB.getConnection()) {
      TupleQuery qry = conn.prepareTupleSparql(sql);
      qry.setBinding("entity", iri(iri));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          BindingSet bs = rs.next();
          TTEntity entity = new TTEntity(bs.getValue("prov").stringValue());
          entity.set(new TTIriRefExtended(ImVocab.PROVENANCE_TARGET), iri);
          entity.set(new TTIriRefExtended(ImVocab.EFFECTIVE_DATE), bs.getValue("effectiveDate").stringValue());
          entity.set(new TTIriRefExtended(ImVocab.PROVENANCE_ACTIVITY_TYPE),
            new TTIriRefExtended(bs.getValue("activityType").stringValue(), bs.getValue("activityTypeName").toString()));
          if (bs.getValue("agent") != null) {
            entity.set(new TTIriRefExtended(ImVocab.PROVENANCE_AGENT),
              new TTIriRefExtended(bs.getValue("agent").stringValue(), bs.getValue("agentName").stringValue()));
          }
          if (bs.getValue("usedEntity") != null) {
            entity.set(new TTIriRefExtended(ImVocab.PROVENANCE_USED), new TTIriRefExtended(bs.getValue("usedEntity").stringValue(),
              bs.getValue("usedEntityName") != null ? bs.getValue("usedEntityName").stringValue() : null));
          }
          results.add(entity);
        }
      }
    }
    return results;
  }
}
