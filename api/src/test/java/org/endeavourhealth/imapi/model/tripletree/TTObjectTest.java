package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.logic.service.EntityService;
import org.endeavourhealth.imapi.model.TermCode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTObject.object;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TTObjectTest {

    private final TTEntity testObject = (TTEntity) new TTEntity("http://endhealth.co.uk/im#objectTest")
        .setGraph(iri("http://endhealth.co.uk/im#Rich"))
        .set(RDFS.LABEL, "Test object")
        .set(RDFS.COMMENT, "This is an entity to test object serialization")
        .set(IM.QUERY, object(new TermCode().setName("Mickey Mouse").setCode("EM-EYE-CEE")));

    private final String json = new StringJoiner(System.lineSeparator())
        .add("{")
        .add("  \"@id\" : \"http://endhealth.co.uk/im#objectTest\",")
        .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Test object\",")
        .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"This is an entity to test object serialization\",")
        .add("  \"http://endhealth.info/im#Query\" : [ {")
        .add("    \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" : \"org.endeavourhealth.imapi.model.TermCode\",")
        .add("    \"http://www.w3.org/ns/shacl#value\" : {")
        .add("      \"name\" : \"Mickey Mouse\",")
        .add("      \"code\" : \"EM-EYE-CEE\",")
        .add("      \"scheme\" : null,")
        .add("      \"entityTermCode\" : null")
        .add("    }")
        .add("  } ]")
        .add("}")
        .toString();

    // @Test
    void saveTest() throws Exception {
        TTDocument doc = new TTDocument();
        doc.addEntity(testObject);
        doc.setCrud(IM.UPDATE_ALL);

        TTFilerFactory.getDocumentFiler().fileDocument(doc);
    }

    // @Test
    void loadTest() {
        TTBundle bundle = new EntityService().getFullEntity("http://endhealth.co.uk/im#objectTest");
        TTArray preds = bundle.getEntity().get(IM.QUERY);
        assertEquals(1, preds.size());

        TTValue val = preds.get(0);
        assertTrue(val.isObject());

        TTObject obj = val.asObject();
        TermCode tc = (TermCode) obj.getObject();

        assertEquals("Mickey Mouse", tc.getName());
        assertEquals("EM-EYE-CEE", tc.getCode());
    }

    @Test
    void serializeTest() throws JsonProcessingException {
        String actual = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(testObject);
        assertEquals(json, actual);
    }

    @Test
    void deserializeTest() throws JsonProcessingException {
        TTEntity entity = new ObjectMapper().readValue(json, TTEntity.class);
        assertEquals(entity.getIri(), testObject.getIri());

        TTArray preds = entity.get(IM.QUERY);
        assertEquals(1, preds.size());

        TTValue val = preds.get(0);
        assertTrue(val.isObject());

        TTObject obj = val.asObject();
        TermCode tc = (TermCode) obj.getObject();

        assertEquals("Mickey Mouse", tc.getName());
        assertEquals("EM-EYE-CEE", tc.getCode());
    }
}
