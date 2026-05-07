package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.vocabulary.GRAPH;

public class Resolvers {
  // @Test
  public void resolveDomains() {
    new DomainResolver().updateDomains(GRAPH.IM);
  }
}
