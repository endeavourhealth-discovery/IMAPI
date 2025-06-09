package org.endeavourhealth.imapi.logic.reasoner;

import org.junit.jupiter.api.Test;

public class Resolvers {
  @Test
  public void resolveDomains(){
    new DomainResolver().updateDomains();
  }
}
