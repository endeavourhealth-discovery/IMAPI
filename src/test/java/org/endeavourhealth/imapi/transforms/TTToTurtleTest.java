package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.library.vocabulary.IM;
import org.endeavourhealth.library.vocabulary.NAMESPACE;
import org.endeavourhealth.library.vocabulary.OWL;
import org.endeavourhealth.library.model.tripletree.*;
import org.endeavourhealth.library.transforms.TTToTurtle;
import org.junit.jupiter.api.Test;

import static org.endeavourhealth.library.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TTToTurtleTest {

  @Test
  void transformEntity() {
    TTEntity entity = new TTEntity();
    TTContext context = new TTContext();
    entity.setContext(context);
    context.add(NAMESPACE.IM, "im");
    context.add(NAMESPACE.SNOMED, "sn");
    context.add(NAMESPACE.OWL, "owl");
    entity.setIri(NAMESPACE.IM + "VaccineSet");
    entity.set(iri(IM.DEFINITION), new TTArray().add(TTIriRef.iri(NAMESPACE.SNOMED + "39330711000001103")));
    TTNode inter = new TTNode();
    inter.set(iri(OWL.INTERSECTION_OF), new TTArray()
      .add(iri(NAMESPACE.SNOMED + "10363601000001109"))
      .add(new TTNode().set(TTIriRef.iri(
          NAMESPACE.SNOMED + "10362601000001103"),
        iri(NAMESPACE.SNOMED + "39330711000001103"))));
    entity.get(iri(IM.DEFINITION)).add(inter);
    TTToTurtle converter = new TTToTurtle();
    String turtle = converter.transformEntity(entity);
    System.out.println(turtle);
    assertNotNull(turtle);
  }
}
