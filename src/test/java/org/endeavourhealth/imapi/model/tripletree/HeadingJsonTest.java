package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HeadingJsonTest {
  private static final String IRI_JSON = "{\"name\":\"Test name\",\"iri\":\"http://endhealth.info/im#Test\"}";

  private static final String IRI = "http://endhealth.info/im#Test";
  private static final String NAME = "Test name";

  private ObjectMapper om;

  @BeforeEach
  public void init() {
    this.om = new ObjectMapper();
  }

  @Test
  void testTTIriSerialization() throws JsonProcessingException {
    TTIriRef ref = iri(IRI, NAME);
    String actual = om.writeValueAsString(ref);

    assertEquals(IRI_JSON, actual);
  }

  @Test
  void testTTIriDeserialization() throws JsonProcessingException {
    TTIriRef ref = om.readValue(IRI_JSON, TTIriRef.class);

    assertEquals(IRI, ref.getIri());
    assertEquals(NAME, ref.getName());
  }
}
