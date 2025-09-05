package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.vocabulary.Graph;

public class Resolvers {
  // @Test
  public void resolveDomains() {
    new DomainResolver().updateDomains(Graph.IM);
  }
}
