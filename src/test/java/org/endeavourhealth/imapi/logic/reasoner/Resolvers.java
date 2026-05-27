package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.interfacemanager.model.GRAPH;

public class Resolvers {
  // @Test
  public void resolveDomains() {
    new DomainResolver().updateDomains(GRAPH.IM);
  }
}
