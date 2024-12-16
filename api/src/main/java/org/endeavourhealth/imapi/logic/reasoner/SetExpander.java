package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityRepository;
import org.endeavourhealth.imapi.dataaccess.SetRepository;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.imq.QueryException;
import org.endeavourhealth.imapi.model.tripletree.TTBundle;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class SetExpander {
  private static final Logger LOG = LoggerFactory.getLogger(SetExpander.class);
  private final EntityRepository entityRepository = new EntityRepository();
  private final SetRepository setRepo = new SetRepository();

  public void expandAllSets() throws JsonProcessingException, QueryException {
    LOG.info("Getting value sets....");
    //First get the list of sets that dont have members already expanded
    Set<String> sets = setRepo.getSets();
    //for each set get their definition
    for (String iri : sets) {
      if (entityRepository.hasPredicates(iri, Set.of(IM.ENTAILED_MEMBER, IM.DEFINITION))) {
        expandSet(iri);
      }
    }
  }


  public void expandSet(String iri) throws QueryException, JsonProcessingException {
    TTBundle setDefinition = entityRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION));
    if (setDefinition.getEntity().get(iri(IM.DEFINITION)) == null) {
      Set<Concept> members = setRepo.getExpansionFromEntailedMembers(iri); //might be an instance member definition
      if (!members.isEmpty()) {
        LOG.info("Expanding "+ iri);
        setRepo.updateMembers(iri, members);
      }
    }
    else {
      LOG.info("Expanding "+ iri);
      Set<Concept> members = setRepo.getMembersFromDefinition(setDefinition.getEntity().get(iri(IM.DEFINITION)).asLiteral()
        .objectValue(Query.class));
      setRepo.updateMembers(iri, members);
    }
  }
}



