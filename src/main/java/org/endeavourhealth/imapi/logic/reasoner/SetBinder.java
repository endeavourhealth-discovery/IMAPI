package org.endeavourhealth.imapi.logic.reasoner;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.eclipse.rdf4j.model.util.Values.iri;

@Slf4j
public class SetBinder {
  private final SetRepository setRepository = new SetRepository();

  public void bindSets() {
    log.info("Getting value sets....");
    Set<String> sets = getSets();
    int count = 0;
    for (String iri : sets) {
      count++;
      if (count % 100 == 0) {
        log.info("{} sets bound", count);
      }
      bindSet(iri);
    }
  }

  private Set<String> getSets() {
    Set<String> setIris = new HashSet<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      String sparql = """
        SELECT distinct ?iri
        WHERE {
          ?iri ?rdfType ?type.
          filter (?type in (?imConceptSet, ?imValueSet)).
        }
        """;
      TupleQuery qry = conn.prepareTupleQuery(sparql);
      qry.setBinding("rdfType", iri(RDF.TYPE));
      qry.setBinding("imConceptSet", iri(IM.CONCEPT_SET));
      qry.setBinding("imValueSet", iri(IM.VALUESET));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          setIris.add(rs.next().getValue("iri").stringValue());
        }
      }
    }
    return setIris;
  }

  public Set<TTNode> bindSet(String iri) {
    Set<Concept> members = setRepository.getSomeMembers(iri, 100);
    if (!members.isEmpty()) {
      Set<String> memberIris = members.stream().map(Entity::getIri).collect(Collectors.toSet());
      Set<TTNode> dataModels = setRepository.getBindingsForConcept(memberIris);
      setRepository.bindConceptSetToDataModel(iri, dataModels);
      return dataModels;
    }
    return Collections.emptySet();
  }

}


