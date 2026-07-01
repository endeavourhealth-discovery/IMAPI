package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.endeavourhealth.imapi.json.TTNodeSerializer;
import org.endeavourhealth.imapi.json.TTNodeSerializerV2;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TTNodeJsonTest {

  @Test
  void serializationTest() throws JsonProcessingException {
    TTNodeJava node = TestHelper.getTestEntity();
    TestHelper.checkEntity(node);

    // Serialize
    ObjectMapper om = new ObjectMapper();
    String json = om
      .writerWithDefaultPrettyPrinter()
      .writeValueAsString(node);

    System.out.println(json);

    assertEquals(TestHelper.getTestEntityJson(), json);
  }

  @Test
  void serializationTestPrefix() throws JsonProcessingException {
    TTNodeJava node = TestHelper.getTestEntity();
    TestHelper.checkEntity(node);

    // Serialize
    ObjectMapper om = new ObjectMapper();
    String json = om
      .writerWithDefaultPrettyPrinter()
      .withAttribute(TTContextJava.OUTPUT_CONTEXT, true)
      .writeValueAsString(node);


    System.out.println(json);

    assertEquals(TestHelper.getTestEntityJsonPrefixWithContext(), json);
  }

  @Test
  void serializationTestSimpleProperties() throws JsonProcessingException {
    TTNodeJava node = TestHelper.getTestEntity();
    TestHelper.checkEntity(node);

    // Serialize
    ObjectMapper om = new ObjectMapper();
    String json = om
      .writerWithDefaultPrettyPrinter()
      .withAttribute(TTNodeSerializer.SIMPLE_PROPERTIES, true)
      .writeValueAsString(node);

    System.out.println(json);

    assertEquals(TestHelper.getTestEntityJsonSimple(), json);
  }

  @Test
  void serializationTestSimplePropertiesAndPrefix() throws JsonProcessingException {
    TTNodeJava node = TestHelper.getTestEntity();
    TestHelper.checkEntity(node);

    // Serialize
    ObjectMapper om = new ObjectMapper();
    String json = om
      .writerWithDefaultPrettyPrinter()
      .withAttribute(TTNodeSerializer.SIMPLE_PROPERTIES, true)
      .withAttribute(TTContextJava.OUTPUT_CONTEXT, true)
      .writeValueAsString(node);

    System.out.println(json);

    assertEquals(TestHelper.getTestEntityJsonSimplePropertiesAndPrefixWithContext(), json);
  }

  @Test
  void serializationTestV2() throws IOException {
    TTNodeJava node = TestHelper.getTestEntity();
    TestHelper.checkEntity(node);
    TTEntityJava entity = TestHelper.getTestEntity();
    TTNodeSerializerV2 ser = new TTNodeSerializerV2(TTNodeJava.class, entity.getContext(), List.of(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL),
      TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.
        HAS_STATUS),
      TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)));

    SimpleModule mod = new SimpleModule("SimpleModule", new Version(1, 0, 0, null, null, null));
    mod.addSerializer(TTEntityJava.class, ser);

    // Serialize
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(mod);
    String json = mapper.writerWithDefaultPrettyPrinter()
      .writeValueAsString(entity);


    System.out.println("result : " + json);

    assertEquals(TestHelper.getTestEntityJson(), json);
  }

  @Test
  void serializationTestSimplePropertiesV2() throws JsonProcessingException {
    TTNodeJava node = TestHelper.getTestEntity();
    TestHelper.checkEntity(node);

    TTEntityJava entity = TestHelper.getTestEntity();
    TTNodeSerializerV2 ser = new TTNodeSerializerV2(TTNodeJava.class, entity.getContext(), List.of(TTIriRefExtensionsKt.iri(new TTIriRef(), RdfVocab.TYPE), TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.LABEL),
      TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.COMMENT), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.CODE), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.HAS_SCHEME), TTIriRefExtensionsKt.iri(new TTIriRef(), ImVocab.
        HAS_STATUS),
      TTIriRefExtensionsKt.iri(new TTIriRef(), RdfsVocab.SUBCLASS_OF)));

    SimpleModule mod = new SimpleModule("SimpleModule", new Version(1, 0, 0, null, null, null));
    mod.addSerializer(TTEntityJava.class, ser);

    // Serialize
    ObjectMapper om = new ObjectMapper();
    String json = om
      .writerWithDefaultPrettyPrinter()
      .withAttribute(TTNodeSerializer.SIMPLE_PROPERTIES, true)
      .writeValueAsString(node);

    System.out.println(json);

    assertEquals(TestHelper.getTestEntityJsonSimple(), json);
  }

  @Test
  void deserializationTest() throws JsonProcessingException {
    // Deserialize
    ObjectMapper om = new ObjectMapper();
    TTNodeJava adverseReaction = om.readValue(TestHelper.getTestEntityJson(), TTNodeJava.class);

    TestHelper.checkEntity(adverseReaction);
  }
}
