package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.vocabulary.Namespace;
import org.junit.jupiter.api.Test;


class ObjectComparerTest {

  @Test
  public void compare() throws JsonProcessingException {

    Match match1 = new Match()
      .setIri(Namespace.IM + "123")
      .setName("match")
      .where(w1 -> w1.setIri(Namespace.IM + "abc")
        .relativeTo(r -> r.setNodeRef("ssss").setIri(Namespace.IM + "x")));
    Match match2 = new Match()
      .setIri(Namespace.IM + "124")
      .setName("match")
      .where(w1 -> w1.setIri(Namespace.IM + "abc")
        .setValueVariable("1234")
        .relativeTo(r -> r.setNodeRef("ttt").setIri(Namespace.IM + "y")));

    System.out.println(LogicComparer.compareMatches(match1, match2));


  }
}