package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTNode;

import org.endeavourhealth.imapi.model.tripletree.json.TTNodeSerializer;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;


import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TTArrayJsonTest {
    @Test
    void serializationTest() throws JsonProcessingException {
        TTArray node = getTestArray();

        // Serialize
        ObjectMapper om = new ObjectMapper();
        String json = om.writerWithDefaultPrettyPrinter()
                .writeValueAsString(node);

        System.out.println(json);

        assertEquals(getJson(), json);
    }

    @Test
    void deserializationTest() throws JsonProcessingException {
        // Deserialize
        ObjectMapper om = new ObjectMapper();
        TTArray array = om.readValue(getJson(), TTArray.class);

        checkArray(array);
    }

    private TTArray getTestArray() {
        return new TTArray()
            .add(iri("http://snomed.info/sct#371186005", "Amputation of toe (procedure)"))
            .add(iri("http://snomed.info/sct#732214009", "Amputation of left lower limb"))
            .add(new TTNode()
                .set(iri("http://snomed.info/sct#260686004", "Method"), iri("http://snomed.info/sct#129309007", "Amputation - action"))
                .set(iri("http://snomed.info/sct#405813007", "Procedure site - Direct"), iri("http://snomed.info/sct#732939008", "Part of toe of left foot"))
            );
    }

    private String getJson() {
        StringJoiner json = new StringJoiner(System.lineSeparator())
            .add("[ {")
            .add("  \"name\" : \"Amputation of toe (procedure)\",")
            .add("  \"@id\" : \"http://snomed.info/sct#371186005\"")
            .add("}, {")
            .add("  \"name\" : \"Amputation of left lower limb\",")
            .add("  \"@id\" : \"http://snomed.info/sct#732214009\"")
            .add("}, {")
            .add("  \"http://snomed.info/sct#260686004\" : [ {")
            .add("    \"name\" : \"Amputation - action\",")
            .add("    \"@id\" : \"http://snomed.info/sct#129309007\"")
            .add("  } ],")
            .add("  \"http://snomed.info/sct#405813007\" : [ {")
            .add("    \"name\" : \"Part of toe of left foot\",")
            .add("    \"@id\" : \"http://snomed.info/sct#732939008\"")
            .add("  } ]")
            .add("} ]");

        return json.toString();
    }

    private void checkArray(TTArray array) {
        assertEquals(3, array.size());

        assertTrue(array.get(0).isIriRef());
        assertEquals(iri("http://snomed.info/sct#371186005", "Amputation of toe (procedure)"), array.get(0));

        assertTrue(array.get(1).isIriRef());
        assertEquals(iri("http://snomed.info/sct#732214009", "Amputation of left lower limb"), array.get(1));

        assertTrue(array.get(2).isNode());
        TTNode node = array.get(2).asNode();

        assertTrue(node.has(iri("http://snomed.info/sct#260686004", "Method")));
        assertTrue(node.get(iri("http://snomed.info/sct#260686004", "Method")).isIriRef());
        assertEquals(iri("http://snomed.info/sct#129309007", "Amputation - action"), node.get(iri("http://snomed.info/sct#260686004", "Method")).asIriRef());

        assertTrue(node.has(iri("http://snomed.info/sct#405813007", "Procedure site - Direct")));
        assertTrue(node.get(iri("http://snomed.info/sct#405813007", "Procedure site - Direct")).isIriRef());
        assertEquals(iri("http://snomed.info/sct#732939008", "Part of toe of left foot"), node.get(iri("http://snomed.info/sct#405813007", "Procedure site - Direct")).asIriRef());
    }
}
