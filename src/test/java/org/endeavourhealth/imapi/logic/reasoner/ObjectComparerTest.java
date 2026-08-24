package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.imq.Query;
import org.endeavourhealth.imapi.vocabulary.NAMESPACE;
import org.junit.jupiter.api.Test;


class ObjectComparerTest {

  @Test
  public void compare() throws JsonProcessingException {

    Query query1 = new Query()
      .setIri(NAMESPACE.IM + "123")
      .setName("match")
      .where(w1 -> w1.setIri(NAMESPACE.IM + "abc"));
    Query query2 = new Query()
      .setIri(NAMESPACE.IM + "124")
      .setName("match")
      .where(w1 -> w1.setIri(NAMESPACE.IM + "abc")
        .setNode("1234"));

    System.out.println(LogicComparer.compareMatches(query1, query2));


  }
}