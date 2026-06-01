package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.interfacemanager.model.IM;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;
import org.endeavourhealth.interfacemanager.model.OWL;
import org.junit.jupiter.api.Test;

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
    entity.set(new TTIriRef(IM.DEFINITION), new TTArray().add(new TTIriRef(NAMESPACE.SNOMED + "39330711000001103")));
    TTNode inter = new TTNode();
    inter.set(new TTIriRef(OWL.INTERSECTION_OF), new TTArray()
      .add(new TTIriRef(NAMESPACE.SNOMED + "10363601000001109"))
      .add(new TTNode().set(new TTIriRef(
          NAMESPACE.SNOMED + "10362601000001103"),
        new TTIriRef(NAMESPACE.SNOMED + "39330711000001103"))));
    entity.get(new TTIriRef(IM.DEFINITION)).add(inter);
    TTToTurtle converter = new TTToTurtle();
    String turtle = converter.transformEntity(entity);
    System.out.println(turtle);
    assertNotNull(turtle);
  }
}
