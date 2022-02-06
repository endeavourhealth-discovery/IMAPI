package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializerV2;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDF;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TTNodeJsonTest {

    @Test
    void serializationTest() throws JsonProcessingException {
        TTNode node = TestHelper.getTestEntity();
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
        TTNode node = TestHelper.getTestEntity();
        TestHelper.checkEntity(node);

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om
                .writerWithDefaultPrettyPrinter()
                .withAttribute(TTContext.OUTPUT_CONTEXT,true)
                .writeValueAsString(node);


        System.out.println(json);

        assertEquals(TestHelper.getTestEntityJsonPrefixWithContext(), json);
    }

    @Test
    void serializationTestSimpleProperties() throws JsonProcessingException {
        TTNode node = TestHelper.getTestEntity();
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
        TTNode node = TestHelper.getTestEntity();
        TestHelper.checkEntity(node);

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om
                .writerWithDefaultPrettyPrinter()
                .withAttribute(TTNodeSerializer.SIMPLE_PROPERTIES, true)
                .withAttribute(TTContext.OUTPUT_CONTEXT,true)
                .writeValueAsString(node);

        System.out.println(json);

        assertEquals(TestHelper.getTestEntityJsonSimplePropertiesAndPrefixWithContext(), json);
    }

    @Test
    void serializationTestV2() throws IOException {
        TTNode node = TestHelper.getTestEntity();
        TestHelper.checkEntity(node);
        TTEntity entity = TestHelper.getTestEntity();
        TTNodeSerializerV2 ser = new TTNodeSerializerV2(TTNode.class, entity.getContext(), List.of(RDF.TYPE, RDFS.LABEL,
                RDFS.COMMENT, IM.CODE,IM.HAS_SCHEME,IM.HAS_STATUS,
                RDFS.SUBCLASSOF));

        SimpleModule mod = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
        mod.addSerializer(TTEntity.class, ser);

        // Serialize
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(mod);
        String json = mapper .writerWithDefaultPrettyPrinter()
                .writeValueAsString(entity);


        System.out.println("result : " +json);

        assertEquals(TestHelper.getTestEntityJson(), json);
    }

    @Test
    void serializationTestSimplePropertiesV2() throws JsonProcessingException {
        TTNode node = TestHelper.getTestEntity();
        TestHelper.checkEntity(node);

        TTEntity entity = TestHelper.getTestEntity();
        TTNodeSerializerV2 ser = new TTNodeSerializerV2(TTNode.class, entity.getContext(), List.of(RDF.TYPE, RDFS.LABEL,
                RDFS.COMMENT, IM.CODE,IM.HAS_SCHEME,IM.HAS_STATUS,
                RDFS.SUBCLASSOF));

        SimpleModule mod = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
        mod.addSerializer(TTEntity.class, ser);

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
        TTNode adverseReaction = om.readValue(TestHelper.getTestEntityJson(), TTNode.class);

        TestHelper.checkEntity(adverseReaction);
    }
}
