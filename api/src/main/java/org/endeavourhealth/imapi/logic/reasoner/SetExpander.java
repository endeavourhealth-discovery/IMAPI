package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.dataaccess.EntityTripleRepository;
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
  private final EntityTripleRepository entityTripleRepository = new EntityTripleRepository();
  private final SetRepository setRepo = new SetRepository();

  public void expandAllSets() throws JsonProcessingException, QueryException {
    LOG.info("Getting value sets....");
    //First get the list of sets that dont have members already expanded
    Set<String> sets = setRepo.getSets();
    //for each set get their definition
    for (String iri : sets) {
      LOG.info("Updating members of {}", iri);
      //get the definition
      TTBundle setDefinition = entityTripleRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION));
      //get the expansion.
      Set<Concept> members = setRepo.getSetExpansion(setDefinition.getEntity().get(iri(IM.DEFINITION)).asLiteral().objectValue(Query.class), false, null, List.of());
      setRepo.updateMembers(iri, members);

    }

  }


  public void expandSet(String iri) throws QueryException, JsonProcessingException {
    LOG.info("Updating members of {}", iri);
    TTBundle setDefinition = entityTripleRepository.getEntityPredicates(iri, Set.of(IM.DEFINITION));
    if (setDefinition.getEntity().get(iri(IM.DEFINITION)) == null)
      return;
    //get the expansion.

    Set<Concept> members = setRepo.getSetExpansion(setDefinition.getEntity().get(iri(IM.DEFINITION)).asLiteral()
      .objectValue(Query.class), false, null, List.of(), null);
    setRepo.updateMembers(iri, members);
  }
}



