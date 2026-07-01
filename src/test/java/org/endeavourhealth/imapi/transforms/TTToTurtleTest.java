package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTContextJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TTToTurtleTest {

  @Test
  void transformEntity() {
    TTEntityJava entity = new TTEntityJava();
    TTContextJava context = new TTContextJava();
    entity.setContext(context);
    context.add(NamespaceVocab.IM, "im");
    context.add(NamespaceVocab.SNOMED, "sn");
    context.add(NamespaceVocab.OWL, "owl");
    entity.setIri(NamespaceVocab.IM + "VaccineSet");
    entity.set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION), new TTArrayJava().add(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "39330711000001103")))
    ;
    TTNodeJava inter = new TTNodeJava();
    inter.set(TTIriRefExtensionsKt.iri(new TTIriRef(), OwlVocab.INTERSECTION_OF), new TTArrayJava()
      .add(TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "10363601000001109"))
      .add(new TTNodeJava().set(TTIriRefExtensionsKt.iri(new TTIriRef(),
          NamespaceVocab.SNOMED + "10362601000001103"),
        TTIriRefExtensionsKt.iri(new TTIriRef(), NamespaceVocab.SNOMED + "39330711000001103"))));
    entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.DEFINITION)).add(inter);
    TTToTurtle converter = new TTToTurtle();
    String turtle = converter.transformEntity(entity);
    System.out.println(turtle);
    assertNotNull(turtle);
  }
}
