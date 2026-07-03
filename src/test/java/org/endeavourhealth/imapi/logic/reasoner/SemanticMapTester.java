package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.vocabulary.GRAPH;
import org.junit.jupiter.api.Test;

public class SemanticMapTester {
  //@Test
  public void updateSemanticMaps() throws Exception {
    new SemanticMapGenerator().generateAllSemanticMaps(GRAPH.IM);
  }
}
