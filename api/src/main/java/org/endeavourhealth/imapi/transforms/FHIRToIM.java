package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.fhir.CodeSystem;
import org.endeavourhealth.imapi.model.fhir.FHIRConcept;
import org.endeavourhealth.imapi.model.fhir.Include;
import org.endeavourhealth.imapi.model.fhir.ValueSet;
import org.endeavourhealth.imapi.model.iml.Concept;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.FHIR;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class FHIRToIM {

  public TTEntity convertValueSet(ValueSet valueSet, TTIriRef setType,String folder) throws JsonProcessingException {
    TTEntity set= new TTEntity()
      .addType(setType)
      .setIri(valueSet.getURL())
      .setStatus(valueSet.getStatus().equals("active")? iri(IM.ACTIVE): iri(IM.DRAFT))
      .setName("FHIR "+ valueSet.getName().replaceAll("([a-z])([A-Z])", "$1 $2"))
      .setDescription(valueSet.getDescription());
    set.addObject(iri(IM.IS_CONTAINED_IN), iri(folder));
    if (valueSet.getCompose()!=null){
      if (valueSet.getCompose().getInclude()!=null){
        Include[] include= valueSet.getCompose().getInclude();
        Query query= new Query();
        Match match= new Match();
        query.addMatch(match);
        if (valueSet.getCompose().getInclude().length==1){
          String member= include[0].getSystem();
          match.addInstanceOf(new Node().setIri(member)
            .setDescendantsOrSelfOf(true));
        }
        else {
          match.setBoolMatch(Bool.or);
          for (int i=0; i>include.length;i++){
            Match memberMatch= new Match();
            match.addMatch(memberMatch);
            String member= include[i].getSystem().replaceAll("fhir/","fhir#");
            memberMatch.addInstanceOf(new Node().setIri(member).setDescendantsOrSelfOf(true));
          }
        }
        set.set(iri(IM.DEFINITION), TTLiteral.literal(query));
      }
    }

    return set;
  }
  public List<TTEntity> convertCodeSystem(CodeSystem codeSystem,String folder){
    List<TTEntity> concepts= new ArrayList<>();
    String iri= codeSystem.getUrl();
    Object rawQuery= new Object();
    TTEntity parent= new TTEntity()
      .addType(iri(IM.CONCEPT))
      .setCode(codeSystem.getID())
      .setIri(iri)
      .setStatus(iri(FHIR.GRAPH_FHIR))
      .setStatus(codeSystem.getStatus().equals("active") ?iri(IM.ACTIVE): iri(IM.DRAFT))
      .setName(codeSystem.getTitle()+ "( FHIR code system)")
      .setDescription(codeSystem.getDescription());
    parent.addObject(iri(IM.IS_CONTAINED_IN),iri(folder));
    concepts.add(parent);
    for (FHIRConcept fhirConcept:codeSystem.getConcept()){
      TTEntity concept= new TTEntity()
        .addType(iri(IM.CONCEPT))
        .setName(fhirConcept.getDisplay()+" ("+ parent.getName()+")")
        .setDescription(fhirConcept.getDefinition())
        .setIri(parent.getIri()+"/"+ fhirConcept.getCode())
        .setScheme(iri(FHIR.GRAPH_FHIR))
        .setCode(fhirConcept.getCode());
      concept.addObject(iri(RDFS.SUBCLASS_OF),iri(parent.getIri()));
      concepts.add(concept);
    }

    return concepts;

  }
}
