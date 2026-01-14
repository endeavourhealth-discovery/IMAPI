package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.model.imq.Node;
import org.endeavourhealth.imapi.model.imq.Return;
import org.endeavourhealth.imapi.model.imq.Where;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReturnFormatTests {
  @Test
  public void reformatReturn(){
    Match testMatch= new Match();
    testMatch
      .path(p->p
        .setIri(Namespace.IM+"observation")
        .setNode("obs")
        .path(p1->p1
          .setIri(Namespace.IM+"concept")
          .setNode("concept")));
    Where where= new Where();
    testMatch.setWhere(where);
    where
      .and(w->w
        .setNodeRef("obs")
        .setIri(Namespace.IM+"concept")
        .addIs(Node.iri(IM.CONCEPT.toString())))
        .and(w->w
          .setNodeRef("concept")
          .setIri(IM.HAS_STATUS.toString())
          .addIs(Node.iri(IM.ACTIVE.toString())));

    testMatch
      .return_(r->r.setIri(Namespace.IM+"dateOfBirth"))
      .return_(
      r->r.setNodeRef("obs")
        .setIri(Namespace.IM+"concept"))
      .return_(r->r.setNodeRef("concept").setIri(Namespace.IM+"code"))
      .return_(r->r.setNodeRef("concept").setIri(RDFS.LABEL.toString()));
      List<Return> nestedReturns= new IMQFormatter().getNestedReturns(testMatch);
      Return leafReturn= nestedReturns.get(1).getReturn().get(1).getReturn().getFirst();
      assertEquals(Namespace.IM+"code", leafReturn.getIri());
  }
}
