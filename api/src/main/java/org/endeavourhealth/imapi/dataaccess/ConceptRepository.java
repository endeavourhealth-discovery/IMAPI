package org.endeavourhealth.imapi.dataaccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.filer.TTFilerException;
import org.endeavourhealth.imapi.filer.rdf4j.TTTransactionFiler;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.transforms.SnomedConcept;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM_FUNCTION;

import static org.eclipse.rdf4j.model.util.Values.iri;

public class ConceptRepository {
  public ObjectNode createConcept(String namespace) throws QueryException, TTFilerException, JsonProcessingException {
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sql = """
        select ?increment where {
          ?snomedGenerator ?hasIncrementalFrom ?increment
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(sql);
      qry.setBinding("snomedGenerator", iri(IM_FUNCTION.SNOMED_CONCEPT_GENERATOR));
      qry.setBinding("hasIncrementalFrom", iri(IM.HAS_INCREMENTAL_FROM));
      try (TupleQueryResult rs = qry.evaluate()) {
        if (rs.hasNext()) {
          Integer from = Integer.parseInt(rs.next().getValue("increment").stringValue());
          updateIncrement(from);
          String concept = SnomedConcept.createConcept(from, false);
          try (CachedObjectMapper om = new CachedObjectMapper()) {
            ObjectNode iri = om.createObjectNode();
            iri.put("@id", namespace + concept);
            return om.createObjectNode().set("iri", iri);
          }
        }
      }
    }
    return null;
  }

  private void updateIncrement(Integer from) throws QueryException, TTFilerException, JsonProcessingException {
    TTDocument document = new TTDocument()
      .setCrud(TTIriRef.iri(IM.UPDATE_PREDICATES))
      .setGraph(TTIriRef.iri(GRAPH.DISCOVERY))
      .addEntity(new TTEntity()
        .setCrud(TTIriRef.iri(IM.UPDATE_PREDICATES))
        .setIri(IM_FUNCTION.SNOMED_CONCEPT_GENERATOR)
        .set(TTIriRef.iri(IM.HAS_INCREMENTAL_FROM), TTLiteral.literal(from + 1)));
    try (TTTransactionFiler filer = new TTTransactionFiler()) {
      filer.fileDocument(document);
    }
  }
}
