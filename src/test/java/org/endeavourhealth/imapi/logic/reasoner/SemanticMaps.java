package org.endeavourhealth.imapi.logic.reasoner;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.filer.TTFilerException;

import org.endeavourhealth.library.model.imq.QueryException;
import org.endeavourhealth.library.vocabulary.GRAPH;
import org.junit.jupiter.api.Test;

public class SemanticMaps {
  @Test
  public void updateSemanticMaps() throws QueryException, TTFilerException, JsonProcessingException, QueryException {
    new SemanticMapGenerator().generateAllSemanticMaps(GRAPH.IM);
  }
}
