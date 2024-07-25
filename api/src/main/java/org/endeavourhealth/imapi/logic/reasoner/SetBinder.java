package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.dataaccess.helpers.ConnectionManager;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.iml.Entity;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.SHACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;


import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetBinder {
  private static final Logger LOG = LoggerFactory.getLogger(SetBinder.class);
  private final EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
  private final SetRepository setRepository = new SetRepository();

  public void bindSets() throws QueryException, JsonProcessingException {
    LOG.info("Getting value sets....");
    Set<String> sets = getSets();
    int count = 0;
    for (String iri : sets) {
      count++;
      if (count % 100 == 0) {
        LOG.info(count + " sets bound");
      }
      bindSet(iri);
    }
  }

  private Set<String> getSets() {
    Set<String> setIris = new HashSet<>();
    try (RepositoryConnection conn = ConnectionManager.getIMConnection()) {
      StringJoiner spq = new StringJoiner("\n");
      spq.add("SELECT distinct ?iri ")
        .add("WHERE { ?iri <" + RDF.TYPE + "> ?type.")
        .add("  filter (?type in (<" + IM.CONCEPT_SET + ">,<" + IM.VALUESET + ">)).}");
      TupleQuery qry = conn.prepareTupleQuery(spq.toString());
      try (TupleQueryResult rs = qry.evaluate()) {
        while (rs.hasNext()) {
          setIris.add(rs.next().getValue("iri").stringValue());
        }
      }
    }
    return setIris;
  }

  public Set<TTNode> bindSet(String iri) throws JsonProcessingException, QueryException {
    Set<Concept> members = setRepository.getSomeMembers(iri, 10);
    if (!members.isEmpty()) {
      Set<String> memberIris = members.stream().map(Entity::getIri).collect(Collectors.toSet());
      Set<TTNode> dataModels = setRepository.getBindingsForConcept(memberIris);
      setRepository.bindConceptSetToDataModel(iri, dataModels);
      return dataModels;
    }
    return null;
  }

}


