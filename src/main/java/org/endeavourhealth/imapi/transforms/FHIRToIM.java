package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.fhir.CodeSystem;
import org.endeavourhealth.imapi.model.fhir.FHIRConcept;
import org.endeavourhealth.imapi.model.fhir.Include;
import org.endeavourhealth.imapi.model.fhir.ValueSet;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.interfacemanager.model.Match;
import org.endeavourhealth.interfacemanager.model.Node;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class FHIRToIM {

  public TTEntity convertValueSet(ValueSet valueSet, TTIriRef setType, String folder) throws JsonProcessingException {
    TTEntity set = new TTEntity()
      .addType(setType)
      .setIri(valueSet.getURL())
      .setScheme(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.FHIR))
      .setStatus(valueSet.getStatus().equals("active") ? TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ACTIVE) : TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DRAFT))
      .setName("FHIR " + valueSet.getName().replaceAll("([a-z])([A-Z])", "$1 $2"))
      .setDescription(valueSet.getDescription());
    set.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CONTAINED_IN), TTIriRefExtensionsKt.iri(new TTIriRef(), folder));
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
      set.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION), TTLiteral.literal(query));
    }


    return set;
  }

  public List<TTEntity> convertCodeSystem(CodeSystem codeSystem, String folder) {
    List<TTEntity> concepts = new ArrayList<>();
    String iri = codeSystem.getUrl();
    TTEntity parent = new TTEntity()
      .addType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT))
      .setCode(codeSystem.getID())
      .setIri(iri)
      .setScheme(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.FHIR))
      .setStatus(codeSystem.getStatus().equals("active") ? TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ACTIVE) : TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DRAFT))
      .setName(codeSystem.getTitle() + "( FHIR code system)")
      .setDescription(codeSystem.getDescription());
    parent.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.IS_CONTAINED_IN), TTIriRefExtensionsKt.iri(new TTIriRef(), folder));
    concepts.add(parent);
    for (FHIRConcept fhirConcept : codeSystem.getConcept()) {
      TTEntity concept = new TTEntity()
        .addType(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CONCEPT))
        .setName(fhirConcept.getDisplay() + " (" + parent.getName() + ")")
        .setDescription(fhirConcept.getDefinition())
        .setIri(parent.getIri() + "/" + fhirConcept.getCode())
        .setScheme(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.FHIR))
        .setCode(fhirConcept.getCode());
      concept.addObject(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF), TTIriRefExtensionsKt.iri(new TTIriRef(), parent.getIri()));
      concepts.add(concept);
    }

    return concepts;

  }
}
