package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TTDocumentTest {

  @Test
  void deserializeTest() throws JsonProcessingException {
    TTDocument first = getTestDocument();

    // Serialize
    ObjectMapper om = new ObjectMapper();
    String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(first);

    // Deserialize
    TTDocument second = om.readValue(json, TTDocument.class);

    checkDocument(first, second);
  }

  private void checkDocument(TTDocument first, TTDocument second) {
    assertEquals(first.getEntities().getFirst().getIri(),
      second.getEntities().getFirst().getIri());
  }


  private TTDocument getTestDocument() {
    TTDocument result = new TTDocument();
    result.addPrefix(new TTPrefix("http://endhealth.info/im#", "im"));
    result.addEntity(TestHelper.getTestEntity());
    return result;
  }

}
