package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.fhir.CodeSystem;
import org.endeavourhealth.imapi.model.fhir.FHIRConcept;
import org.endeavourhealth.imapi.model.fhir.Include;
import org.endeavourhealth.imapi.model.fhir.ValueSet;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;
import org.endeavourhealth.interfacemanager.model.RDFS;

import java.util.ArrayList;
import java.util.List;

public class FHIRToIM {

  public TTEntity convertValueSet(ValueSet valueSet, TTIriRef setType, String folder) throws JsonProcessingException {
    TTEntity set = new TTEntity()
      .addType(setType)
      .setIri(valueSet.getURL())
      .setScheme(new TTIriRef(NAMESPACE.FHIR))
      .setStatus(valueSet.getStatus().equals("active") ? new TTIriRef(IM.ACTIVE) : new TTIriRef(IM.DRAFT))
      .setName("FHIR " + valueSet.getName().replaceAll("([a-z])([A-Z])", "$1 $2"))
      .setDescription(valueSet.getDescription());
    set.addObject(new TTIriRef(IM.IS_CONTAINED_IN), new TTIriRef(folder));
    if (valueSet.getCompose() != null && valueSet.getCompose().getInclude() != null) {
      Include[] include = valueSet.getCompose().getInclude();
      Query query = new Query();
      Match match = new Match();
      query.addOr(match);
      if (valueSet.getCompose().getInclude().length == 1) {
        String member = include[0].getSystem();
        match.addIs(new Node().setIri(member)
          .setDescendantsOrSelfOf(true));
      } else {
        for (Include value : include) {
          Match memberMatch = new Match();
          match.addOr(memberMatch);
          String member = value.getSystem().replace("fhir/", "fhir#");
          memberMatch.addIs(new Node().setIri(member).setDescendantsOrSelfOf(true));
        }
      }
      set.set(new TTIriRef(IM.DEFINITION), TTLiteral.literal(query));
    }


    return set;
  }

  public List<TTEntity> convertCodeSystem(CodeSystem codeSystem, String folder) {
    List<TTEntity> concepts = new ArrayList<>();
    String iri = codeSystem.getUrl();
    TTEntity parent = new TTEntity()
      .addType(new TTIriRef(IM.CONCEPT))
      .setCode(codeSystem.getID())
      .setIri(iri)
      .setScheme(new TTIriRef(NAMESPACE.FHIR))
      .setStatus(codeSystem.getStatus().equals("active") ? new TTIriRef(IM.ACTIVE) : new TTIriRef(IM.DRAFT))
      .setName(codeSystem.getTitle() + "( FHIR code system)")
      .setDescription(codeSystem.getDescription());
    parent.addObject(new TTIriRef(IM.IS_CONTAINED_IN), new TTIriRef(folder));
    concepts.add(parent);
    for (FHIRConcept fhirConcept : codeSystem.getConcept()) {
      TTEntity concept = new TTEntity()
        .addType(new TTIriRef(IM.CONCEPT))
        .setName(fhirConcept.getDisplay() + " (" + parent.getName() + ")")
        .setDescription(fhirConcept.getDefinition())
        .setIri(parent.getIri() + "/" + fhirConcept.getCode())
        .setScheme(new TTIriRef(NAMESPACE.FHIR))
        .setCode(fhirConcept.getCode());
      concept.addObject(new TTIriRef(RDFS.SUBCLASS_OF), new TTIriRef(parent.getIri()));
      concepts.add(concept);
    }

    return concepts;

  }
}
