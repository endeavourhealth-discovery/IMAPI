package org.endeavourhealth.imapi.logic.reasoner;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.dataaccess.databases.IMDB;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.GRAPH;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.RDF;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SetBinder {
  private final SetRepository setRepository = new SetRepository();

  public void bindSets(GRAPH insertGraph) {
    log.info("Getting value sets....");
    Set<String> sets = getSets();
    int count = 0;
    for (String iri : sets) {
      count++;
      if (count % 100 == 0) {
        log.info("{} sets bound", count);
      }
      bindSet(iri, insertGraph);
    }
  }

  private Set<String> getSets() {
    Set<String> setIris = new HashSet<>();
    try (IMDB conn = IMDB.getConnection()) {
      String sparql = """
        SELECT distinct ?iri
        WHERE {
          ?iri ?rdfType ?type.
          filter (?type in (?imConceptSet, ?imValueSet)).
        }
        """;
      TupleQuery qry = conn.prepareTupleSparql(sparql);
      qry.setBinding("rdfType", EnumUtils.asDbIri(RDF.TYPE));
      qry.setBinding("imConceptSet", EnumUtils.asDbIri(IM.CONCEPT_SET));
      qry.setBinding("imValueSet", EnumUtils.asDbIri(IM.VALUE_SET));
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          setIris.add(rs.next().getValue("iri").stringValue());
        }
      }
    }
    return setIris;
  }

  public void bindSet(String iri, GRAPH insertGraph) {
    Set<Concept> members = setRepository.getSomeMembers(iri, 100);
    if (!members.isEmpty()) {
      Set<String> memberIris = members.stream().map(Entity::getIri).collect(Collectors.toSet());
      Set<TTNode> dataModels = setRepository.getBindingsForConcept(memberIris);
      setRepository.bindConceptSetToDataModel(iri, dataModels, insertGraph);
    }
  }

}


