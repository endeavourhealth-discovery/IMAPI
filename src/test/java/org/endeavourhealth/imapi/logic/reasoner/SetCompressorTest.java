package org.endeavourhealth.imapi.logic.reasoner;

import org.junit.jupiter.api.Test;

public class SetCompressorTest {
  //@Test
  public void testSetCompressor() {
    new SetDefinitionCompressor().getSetDefinition("http://snomed.info/sct#999012891000230104");

  }
}
