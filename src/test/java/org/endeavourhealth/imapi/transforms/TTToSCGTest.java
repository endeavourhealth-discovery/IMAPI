package org.endeavourhealth.imapi.transforms;

import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TTToSCGTest {

  @Test
  void getSCG() throws DataFormatException {
    String scg = "=== 173422009: 405813007 = 75573002 , 272741003 = 51440002";
    SCGToTT cnv = new SCGToTT();
    TTEntityJava entity = new TTEntityJava();
    try (TTManager manager = new TTManager()) {
      entity.setContext(manager.createDefaultContext());
      cnv.setDefinition(entity, scg);
      TTToSCG rev = new TTToSCG();
      String scg2 = "=== " + rev.getSCG(entity, false);
      System.out.println(scg2);
      assertEquals(scg, scg2);
    }
  }

}