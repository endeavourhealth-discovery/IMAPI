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
import org.endeavourhealth.imapi.model.tripletree.TTIriRefExtended;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;

import java.util.ArrayList;
import java.util.List;

public class FHIRToIM {

  public TTEntity convertValueSet(ValueSet valueSet, TTIriRefExtended setType, String folder) throws JsonProcessingException {
    TTEntity set = new TTEntity()
      .addType(setType)
      .setIri(valueSet.getURL())
      .setScheme(new TTIriRefExtended(NamespaceVocab.FHIR))
      .setStatus(valueSet.getStatus().equals("active") ? new TTIriRefExtended(ImVocab.ACTIVE) : new TTIriRefExtended(ImVocab.DRAFT))
      .setName("FHIR " + valueSet.getName().replaceAll("([a-z])([A-Z])", "$1 $2"))
      .setDescription(valueSet.getDescription());
    set.addObject(new TTIriRefExtended(ImVocab.IS_CONTAINED_IN), new TTIriRefExtended(folder));
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
      set.set(new TTIriRefExtended(ImVocab.DEFINITION), TTLiteral.literal(query));
    }


    return set;
  }

  public List<TTEntity> convertCodeSystem(CodeSystem codeSystem, String folder) {
    List<TTEntity> concepts = new ArrayList<>();
    String iri = codeSystem.getUrl();
    TTEntity parent = new TTEntity()
      .addType(new TTIriRefExtended(ImVocab.CONCEPT))
      .setCode(codeSystem.getID())
      .setIri(iri)
      .setScheme(new TTIriRefExtended(NamespaceVocab.FHIR))
      .setStatus(codeSystem.getStatus().equals("active") ? new TTIriRefExtended(ImVocab.ACTIVE) : new TTIriRefExtended(ImVocab.DRAFT))
      .setName(codeSystem.getTitle() + "( FHIR code system)")
      .setDescription(codeSystem.getDescription());
    parent.addObject(new TTIriRefExtended(ImVocab.IS_CONTAINED_IN), new TTIriRefExtended(folder));
    concepts.add(parent);
    for (FHIRConcept fhirConcept : codeSystem.getConcept()) {
      TTEntity concept = new TTEntity()
        .addType(new TTIriRefExtended(ImVocab.CONCEPT))
        .setName(fhirConcept.getDisplay() + " (" + parent.getName() + ")")
        .setDescription(fhirConcept.getDefinition())
        .setIri(parent.getIri() + "/" + fhirConcept.getCode())
        .setScheme(new TTIriRefExtended(NamespaceVocab.FHIR))
        .setCode(fhirConcept.getCode());
      concept.addObject(new TTIriRefExtended(RdfsVocab.SUBCLASS_OF), new TTIriRefExtended(parent.getIri()));
      concepts.add(concept);
    }

    return concepts;

  }
}
