package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.endeavourhealth.imapi.model.imq.Query;

public class LogicComparer {

  public static boolean compareMatches(Query from, Query to) throws JsonProcessingException {
    String fromString = serializeMatchLogic(from);
    String toString = serializeMatchLogic(to);
    return fromString.equals(toString);
  }

  public static String serializeMatchLogic(Query query) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.addMixIn(Query.class, MatchMixin.class);


    // Define a filter to ignore the specified fields at runtime
    FilterProvider filters = new SimpleFilterProvider()
      .addFilter("matchLogicFilter", SimpleBeanPropertyFilter.serializeAllExcept("iri"));
    return mapper.writer(filters).writeValueAsString(query);
  }

  @JsonFilter("matchLogicFilter")
  abstract static class MatchMixin {
  }

}
