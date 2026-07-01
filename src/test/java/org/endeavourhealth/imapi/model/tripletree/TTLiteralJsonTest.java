package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteralJava.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TTLiteralJsonTest {
  private static final String LIT_STR_JSON = "\"Test name\"";
  private static final String LIT_INT_JSON = "10";
  private static final String LIT_BOOL_JSON = "true";
  private static final String LIT_PAT_JSON = "{\"value\":\".*\",\"type\":\"http://www.w3.org/2001/XMLSchema#pattern\"}";

  private static final String NAME = "Test name";
  private static final Pattern PATTERN = Pattern.compile(".*");

  private ObjectMapper om;

  @BeforeEach
  public void init() {
    this.om = new ObjectMapper();
  }

  @Test
  void testTTLiteralSerialization() throws JsonProcessingException {
    TTLiteralJava lit = literal(NAME);
    String actual = om.writeValueAsString(lit);

    assertEquals(LIT_STR_JSON, actual);
  }

  @Test
  void testTTLiteralDeserialization() throws JsonProcessingException {
    TTLiteralJava lit = om.readValue(LIT_STR_JSON, TTLiteralJava.class);

    assertEquals(NAME, lit.getValue());
    assertNull(lit.getType());
  }

  @Test
  void testTTLiteralIntSerialization() throws JsonProcessingException {
    TTLiteralJava lit = literal(10);
    String actual = om.writeValueAsString(lit);

    assertEquals(LIT_INT_JSON, actual);
  }

  @Test
  void testTTLiteralIntDeserialization() throws JsonProcessingException {
    TTLiteralJava lit = om.readValue(LIT_INT_JSON, TTLiteralJava.class);

    assertEquals("10", lit.getValue());
    assertEquals(TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.INTEGER), lit.getType());
  }

  @Test
  void testTTLiteralBoolSerialization() throws JsonProcessingException {
    TTLiteralJava lit = literal(true);
    String actual = om.writeValueAsString(lit);

    assertEquals(LIT_BOOL_JSON, actual);
  }

  @Test
  void testTTLiteralBoolDeserialization() throws JsonProcessingException {
    TTLiteralJava lit = om.readValue(LIT_BOOL_JSON, TTLiteralJava.class);

    assertEquals("true", lit.getValue());
    assertEquals(TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.BOOLEAN), lit.getType());
  }

  @Test
  void testTTLiteralPatSerialization() throws JsonProcessingException {
    TTLiteralJava lit = literal(PATTERN);
    String actual = om.writeValueAsString(lit);

    assertEquals(LIT_PAT_JSON, actual);
  }

  @Test
  void testTTLiteralPatDeserialization() throws JsonProcessingException {
    TTLiteralJava lit = om.readValue(LIT_PAT_JSON, TTLiteralJava.class);

    assertEquals(".*", lit.getValue());
    assertEquals(TTIriRefExtensionsKt.iri(new TTIriRef(), XsdVocab.PATTERN), lit.getType());
  }

}
