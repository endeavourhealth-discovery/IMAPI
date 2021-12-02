package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TTNodeJsonTest {
    @Test
    void serializationTest() throws JsonProcessingException {
        TTNode node = TestHelper.getTestEntity();
        TestHelper.checkEntity(node);

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(node);

        System.out.println(json);

        assertEquals(TestHelper.getTestEntityJson(), json);
    }

    @Test
    void deserializationTest() throws JsonProcessingException {
        // Deserialize
        ObjectMapper om = new ObjectMapper();
        TTNode adverseReaction = om.readValue(TestHelper.getTestEntityJson(), TTNode.class);

        TestHelper.checkEntity(adverseReaction);
    }
}
