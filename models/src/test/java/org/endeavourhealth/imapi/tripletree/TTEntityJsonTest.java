package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class TTEntityJsonTest {
    @Test
    void serializationTest() throws JsonProcessingException {
        TTEntity adverseReaction = TestHelper.getTestEntity();

        ObjectMapper om = new ObjectMapper();
        String actual = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        String expected = TestHelper.getTestEntityJson();

        assertEquals(expected, actual);
    }

    @Test
    void deserializationTest() throws JsonProcessingException {
        TTEntity adverseReaction = TestHelper.getTestEntity();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        // Deserialize
        adverseReaction = om.readValue(json, TTEntity.class);

        TestHelper.checkEntity(adverseReaction);
    }

    @Test
    void flipFlopTest() throws JsonProcessingException {
        TTEntity adverseReaction = TestHelper.getTestEntity();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        // Deserialize
        adverseReaction = om.readValue(json, TTEntity.class);

        // Reserialize
        String out = om.writerWithDefaultPrettyPrinter().writeValueAsString(adverseReaction);

        System.out.println("================= IN ==================");
        System.out.println(json);
        System.out.println("----------------- OUT -----------------");
        System.out.println(out);
        System.out.println("=======================================");

        // Compare
        String expected = Arrays.stream(json.split(System.lineSeparator())).sorted().collect(Collectors.joining(System.lineSeparator()));
        String actual = Arrays.stream(out.split(System.lineSeparator())).sorted().collect(Collectors.joining(System.lineSeparator()));

        assertEquals(expected, actual);
    }
}
