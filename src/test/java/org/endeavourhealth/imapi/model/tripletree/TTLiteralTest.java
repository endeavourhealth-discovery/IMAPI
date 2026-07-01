package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.utility.EnumUtils;
import org.endeavourhealth.interfacemanager.model.SearchTermCode;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteralJava.literal;
import static org.junit.jupiter.api.Assertions.*;

class TTLiteralTest {
  final EntityService entityService = new EntityService();
  private final TTEntityJava testObject = (TTEntityJava) new TTEntityJava("http://endhealth.info/im#objectTest")
    .set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL), "Test object")
    .set(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT), "This is an entity to test object serialization")
    .set(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.QUERY), literal(new SearchTermCode().setTerm("Mickey Mouse").setCode("EM-EYE-CEE").setStatus(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.ACTIVE))));
  private final String json = new StringJoiner(System.lineSeparator())
    .add("{")
    .add("  \"iri\" : \"http://endhealth.info/im#objectTest\",")
    .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Test object\",")
    .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"This is an entity to test object serialization\",")
    .add("  \"http://endhealth.info/im#Query\" : \"{\\\"term\\\":\\\"Mickey Mouse\\\",\\\"code\\\":\\\"EM-EYE-CEE\\\",\\\"status\\\":{\\\"name\\\":\\\"Active\\\",\\\"iri\\\":\\\"http://endhealth.info/im#Active\\\"}}\"")
    .add("}")
    .toString();

  TTLiteralTest() throws JsonProcessingException {
  }

  // @Test
  void saveTest() throws Exception {
    TTDocumentJava doc = new TTDocumentJava();
    doc.addEntity(testObject);
    doc.setCrud(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.REPLACE_ALL_PREDICATES));

    TTFilerFactory.getDocumentFiler(GraphVocab.IM).fileDocument(doc);
  }

  // @Test
  void loadTest() throws JsonProcessingException {
    TTBundle bundle = entityService.getBundle("http://endhealth.info/im#objectTest", null);
    TTArrayJava preds = bundle.getEntity().get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.QUERY));
    assertEquals(1, preds.size());

    TTValueJava val = preds.get(0);
    assertTrue(val.isLiteral());

    SearchTermCode tc = val.asLiteral().objectValue(SearchTermCode.class);

    assertEquals("Mickey Mouse", tc.getTerm());
    assertEquals("EM-EYE-CEE", tc.getCode());
  }

  @Test
  void serializeTest() throws JsonProcessingException {
    ObjectMapper om = new ObjectMapper()
      .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    String actual = om
      .writerWithDefaultPrettyPrinter()
      .writeValueAsString(testObject);

    assertEquals(json, actual);
  }

  @Test
  void deserializeTest() throws JsonProcessingException {
    TTEntityJava entity = new ObjectMapper().readValue(json, TTEntityJava.class);
    assertEquals(entity.getIri(), testObject.getIri());

    TTArrayJava preds = entity.get(TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.QUERY));
    assertEquals(1, preds.size());

    TTValueJava val = preds.get(0);
    assertTrue(val.isLiteral());

    SearchTermCode tc = val.asLiteral().objectValue(SearchTermCode.class);

    assertEquals("Mickey Mouse", tc.getTerm());
    assertEquals("EM-EYE-CEE", tc.getCode());
  }

  @Test
  void testTTLiteralSerialization_allNull() throws JsonProcessingException {
    TTLiteralJava first = literal(null, (TTIriRef) null);
    TTLiteralJava second = literal(null, (TTIriRef) null);

    assertEquals(first, second);
  }

  @Test
  void testTTLiteralSerialization_FirstNull() throws JsonProcessingException {
    TTLiteralJava first = literal(null, (TTIriRef) null);
    TTLiteralJava second = literal("TEST", EnumUtils.asIri(XsdVocab.STRING));

    assertNotEquals(first, second);
  }

  @Test
  void testTTLiteralSerialization_SecondNull() throws JsonProcessingException {
    TTLiteralJava first = literal("TEST", EnumUtils.asIri(XsdVocab.STRING));
    TTLiteralJava second = literal(null, (TTIriRef) null);

    assertNotEquals(first, second);
  }

  @Test
  void testTTLiteralSerialization_DiffVal() throws JsonProcessingException {
    TTLiteralJava first = literal("SAME", EnumUtils.asIri(XsdVocab.STRING));
    TTLiteralJava second = literal("DIFFERENT", EnumUtils.asIri(XsdVocab.STRING));

    assertNotEquals(first, second);
  }

  @Test
  void testTTLiteralSerialization_DiffType() throws JsonProcessingException {
    TTLiteralJava first = literal("SAME", EnumUtils.asIri(XsdVocab.STRING));
    TTLiteralJava second = literal("SAME", EnumUtils.asIri(XsdVocab.INTEGER));

    assertNotEquals(first, second);
  }

  @Test
  void testTTLiteralSerialization_DiffVal_NullType() throws JsonProcessingException {
    TTLiteralJava first = literal("SAME", (TTIriRef) null);
    TTLiteralJava second = literal("DIFFERENT", (TTIriRef) null);

    assertNotEquals(first, second);
  }

  @Test
  void testTTLiteralSerialization_SameVal_NullType() throws JsonProcessingException {
    TTLiteralJava first = literal("SAME", (TTIriRef) null);
    TTLiteralJava second = literal("SAME", (TTIriRef) null);

    assertEquals(first, second);
  }

  @Test
  void testTTLiteralSerialization_Same() throws JsonProcessingException {
    TTLiteralJava first = literal("SAME", EnumUtils.asIri(XsdVocab.STRING));
    TTLiteralJava second = literal("SAME", EnumUtils.asIri(XsdVocab.STRING));

    assertEquals(first, second);
  }
}
