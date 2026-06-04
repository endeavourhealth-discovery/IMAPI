package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TTToTurtleTest {

  @Test
  void transformEntity() {
    TTEntity entity = new TTEntity();
    TTContext context = new TTContext();
    entity.setContext(context);
    context.add(NamespaceVocab. IM, "im");
    context.add(NamespaceVocab. SNOMED, "sn");
    context.add(NamespaceVocab. OWL, "owl");
    entity.setIri(NamespaceVocab. IM + "VaccineSet");
    entity.set(new TTIriRefExtended(ImVocab.DEFINITION), new TTArray().add(new TTIriRefExtended(NamespaceVocab. SNOMED + "39330711000001103")))
    ;
    TTNode inter = new TTNode();
    inter.set(new TTIriRefExtended(OwlVocab.INTERSECTION_OF), new TTArray()
      .add(new TTIriRefExtended(NamespaceVocab. SNOMED + "10363601000001109"))
      .add(new TTNode().set(new TTIriRefExtended(
      NamespaceVocab. SNOMED + "10362601000001103"),
    new TTIriRefExtended(NamespaceVocab. SNOMED + "39330711000001103"))));
    entity.get(new TTIriRefExtended(ImVocab.DEFINITION)).add(inter);
    TTToTurtle converter = new TTToTurtle();
    String turtle = converter.transformEntity(entity);
    System.out.println(turtle);
    assertNotNull(turtle);
  }
}
