package org.endeavourhealth.imapi.logic.reasoner;

import org.endeavourhealth.imapi.vocabulary.Graph;

import java.util.List;

public class Resolvers {
  // @Test
  public void resolveDomains() {
    new DomainResolver().updateDomains(Graph.IM);
  }
}
