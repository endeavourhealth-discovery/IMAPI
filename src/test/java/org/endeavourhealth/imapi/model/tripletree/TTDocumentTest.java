package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TTDocumentTest {

  @Test
  void deserializeTest() throws JsonProcessingException {
    TTDocumentJava first = getTestDocument();

    // Serialize
    ObjectMapper om = new ObjectMapper();
    String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(first);

    // Deserialize
    TTDocumentJava second = om.readValue(json, TTDocumentJava.class);

    checkDocument(first, second);
  }

  private void checkDocument(TTDocumentJava first, TTDocumentJava second) {
    assertEquals(first.getEntities().getFirst().getIri(),
      second.getEntities().getFirst().getIri());
  }


  private TTDocumentJava getTestDocument() {
    TTDocumentJava result = new TTDocumentJava();
    result.addPrefix(new TTPrefix("http://endhealth.info/im#", "im"));
    result.addEntity(TestHelper.getTestEntity());
    return result;
  }

}
