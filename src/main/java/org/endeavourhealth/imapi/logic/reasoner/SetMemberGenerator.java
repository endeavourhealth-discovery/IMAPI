package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

@Slf4j
public class SetMemberGenerator {
  private final EntityRepository entityRepository = new EntityRepository();
  private final SetRepository setRepo = new SetRepository();

  public void generateAllSetMembers(String graph) throws JsonProcessingException, QueryException {
    log.info("Getting value sets....");
    //First get the list of sets that dont have members already expanded
    Set<String> sets = setRepo.getSets(graph);
    //for each set get their definition
    for (String iri : sets) {
      if (entityRepository.hasPredicates(iri, Set.of(IM.ENTAILED_MEMBER, IM.DEFINITION), graph)) {
        generateMembers(iri, graph);
      }
    }
  }


  public void generateMembers(String iri, String graph) throws QueryException, JsonProcessingException {
    TTBundle setDefinition = entityRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION));
    if (setDefinition.getEntity().get(iri(IM.DEFINITION)) == null) {
      Set<Concept> members = setRepo.getExpansionFromEntailedMembers(iri, graph); //might be an instance member definition
      if (!members.isEmpty()) {
        log.info("Expanding " + iri);
        setRepo.updateMembers(iri, members, graph);
      }
    } else {
      log.info("Expanding " + iri);
      Set<Concept> members = setRepo.getMembersFromDefinition(setDefinition.getEntity().get(iri(IM.DEFINITION)).asLiteral()
        .objectValue(Query.class), graph);
      setRepo.updateMembers(iri, members, graph);
    }
  }
}



