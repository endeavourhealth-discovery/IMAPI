package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.fhir.CodeSystem;
import org.endeavourhealth.imapi.model.fhir.FHIRConcept;
import org.endeavourhealth.imapi.model.fhir.Include;
import org.endeavourhealth.imapi.model.fhir.ValueSet;
import org.endeavourhealth.interfacemanager.model.Query;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTLiteralJava;
import org.endeavourhealth.interfacemanager.model.Match;
import org.endeavourhealth.interfacemanager.model.Node;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.util.ArrayList;
import java.util.List;

public class FHIRToIM {

  public TTEntityJava convertValueSet(ValueSet valueSet, TTIriRef setType, String folder) throws JsonProcessingException {
    TTEntityJava set = new TTEntityJava()
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
      set.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION), TTLiteralJava.literal(query));
    }


    return set;
  }

  public List<TTEntityJava> convertCodeSystem(CodeSystem codeSystem, String folder) {
    List<TTEntityJava> concepts = new ArrayList<>();
    String iri = codeSystem.getUrl();
    TTEntityJava parent = new TTEntityJava()
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
      TTEntityJava concept = new TTEntityJava()
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
