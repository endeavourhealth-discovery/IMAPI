package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.fhir.CodeSystem;
import org.endeavourhealth.imapi.model.fhir.Include;
import org.endeavourhealth.imapi.model.fhir.ValueSet;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class FHIRToIM {

  public TTEntity convertValueSet(ValueSet valueSet, TTIriRef setType,String folder) throws JsonProcessingException {
    TTEntity set= new TTEntity()
      .addType(setType)
      .setIri(valueSet.getURL())
      .setStatus(valueSet.getStatus().equals("active")? iri(IM.ACTIVE): iri(IM.DRAFT))
      .setName(valueSet.getName().replaceAll("([a-z])([A-Z])", "$1 $2"))
      .setDescription(valueSet.getDescription());
    set.addObject(iri(IM.IS_CONTAINED_IN), iri(folder));
    if (valueSet.getCompose()!=null){
      if (valueSet.getCompose().getInclude()!=null){
        Include[] include= valueSet.getCompose().getInclude();
        Query query= new Query();
        Match match= new Match();
        query.addMatch(match);
        if (valueSet.getCompose().getInclude().length==1){
          match.addInstanceOf(new Node().setIri(include[0].getSystem())
            .setDescendantsOrSelfOf(true));
        }
        else {
          match.setBoolMatch(Bool.or);
          for (int i=0; i>include.length;i++){
            Match memberMatch= new Match();
            match.addMatch(memberMatch);
            memberMatch.addInstanceOf(new Node().setIri(include[i].getSystem()).setDescendantsOrSelfOf(true));
          }
        }
        set.set(iri(IM.DEFINITION), TTLiteral.literal(query));
      }
    }

    return set;
  }
  public List<TTEntity> convertCodeSystem(CodeSystem codeSystem,String folder){
    List<TTEntity> concepts= new ArrayList<>();
    TTEntity parent= new TTEntity()
      .addType(iri(IM.CONCEPT))
      .setIri(codeSystem.getURL())
      .setStatus(codeSystem.getStatus().equals("active") ?iri(IM.ACTIVE): iri(IM.DRAFT))
      .setName(codeSystem.getTitle())
      .setDescription(codeSystem.getDescription());
    parent.addObject(iri(IM.IS_CONTAINED_IN),iri(folder));
    concepts.add(parent);

    return concepts;

  }
}
