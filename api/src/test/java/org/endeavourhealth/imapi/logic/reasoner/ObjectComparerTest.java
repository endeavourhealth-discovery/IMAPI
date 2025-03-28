package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.imq.Bool;
import org.endeavourhealth.imapi.model.imq.Match;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.junit.jupiter.api.Test;


class ObjectComparerTest {

@Test
public void compare() throws IllegalAccessException, JsonProcessingException {

  Match match1 = new Match()
    .setIri(IM.NAMESPACE + "123")
    .setName("match")
    .where(w->w.setBool(Bool.and)
      .where(w1->w1.setIri(IM.NAMESPACE+"abc")
        .relativeTo(r->r.setNodeRef("ssss").setIri(IM.NAMESPACE+"x"))));
  Match match2 = new Match()
    .setIri(IM.NAMESPACE + "124")
    .setName("match")
    .where(w->w.setBool(Bool.and)
      .where(w1->w1.setIri(IM.NAMESPACE+"abc")
        .setValueVariable("1234")
        .relativeTo(r->r.setNodeRef("ttt").setIri(IM.NAMESPACE+"y"))));

  String content1= new ObjectMapper().writeValueAsString(match1);
  System.out.println(LogicComparer.compareMatches(match1,match2));


}



}