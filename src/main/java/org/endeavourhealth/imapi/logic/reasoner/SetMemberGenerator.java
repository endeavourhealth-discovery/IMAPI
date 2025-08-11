package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.vocabulary.Graph;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.List;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.vocabulary.VocabUtils.asHashSet;

@Slf4j
public class SetMemberGenerator {
  private final EntityRepository entityRepository = new EntityRepository();
  private final SetRepository setRepo = new SetRepository();

  public void generateAllSetMembers(List<Graph> userGraphs, Graph insertGraph) throws JsonProcessingException, QueryException {
    log.info("Getting value sets....");
    //First get the list of sets that dont have members already expanded
    Set<String> sets = setRepo.getSets(userGraphs);
    //for each set get their definition
    for (String iri : sets) {
      if (entityRepository.hasPredicates(iri, asHashSet(IM.ENTAILED_MEMBER, IM.DEFINITION), userGraphs)) {
        generateMembers(iri, userGraphs, insertGraph);
      }
    }
  }


  public void generateMembers(String iri, List<Graph> userGraphs, Graph insertGraph) throws QueryException, JsonProcessingException {
    TTBundle setDefinition = entityRepository.getEntityPredicates(iri, asHashSet(IM.DEFINITION), userGraphs);
    if (setDefinition.getEntity().get(iri(IM.DEFINITION)) == null) {
      Set<Concept> members = setRepo.getExpansionFromEntailedMembers(iri, userGraphs); //might be an instance member definition
      if (!members.isEmpty()) {
        log.info("Expanding members " + iri);
        setRepo.updateMembers(iri, members, insertGraph);
      }
    } else {
      log.info("Expanding from definition " + iri);
      Set<Concept> members = setRepo.getMembersFromDefinition(setDefinition.getEntity().get(iri(IM.DEFINITION)).asLiteral()
        .objectValue(Query.class), userGraphs);
      setRepo.updateMembers(iri, members, insertGraph);
    }
  }
}



