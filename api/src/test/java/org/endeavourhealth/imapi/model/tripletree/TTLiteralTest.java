package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.filer.TTFilerFactory;
import org.endeavourhealth.imapi.logic.service.EntityService;

import org.endeavourhealth.imapi.model.search.SearchTermCode;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;
import org.endeavourhealth.imapi.vocabulary.XS;
import org.junit.jupiter.api.Test;


import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TTLiteralTest {
    private final TTEntity testObject = (TTEntity) new TTEntity("http://endhealth.co.uk/im#objectTest")
        .setGraph(iri("http://endhealth.co.uk/im#Rich"))
        .set(TTIriRef.iri(RDFS.LABEL), "Test object")
        .set(TTIriRef.iri(RDFS.COMMENT), "This is an entity to test object serialization")
        .set(TTIriRef.iri(IM.QUERY), literal(new SearchTermCode().setTerm("Mickey Mouse").setCode("EM-EYE-CEE").setStatus(TTIriRef.iri(IM.ACTIVE))));

    private final String json = new StringJoiner(System.lineSeparator())
        .add("{")
        .add("  \"@id\" : \"http://endhealth.co.uk/im#objectTest\",")
        .add("  \"@graph\" : \"http://endhealth.co.uk/im#Rich\",")
        .add("  \"http://www.w3.org/2000/01/rdf-schema#label\" : \"Test object\",")
        .add("  \"http://www.w3.org/2000/01/rdf-schema#comment\" : \"This is an entity to test object serialization\",")
        .add("  \"http://endhealth.info/im#Query\" : \"{\\\"term\\\":\\\"Mickey Mouse\\\",\\\"code\\\":\\\"EM-EYE-CEE\\\",\\\"status\\\":{\\\"@id\\\":\\\"http://endhealth.info/im#Active\\\"}}\"")
        .add("}")
        .toString();

    TTLiteralTest() throws JsonProcessingException {
    }

    // @Test
    void saveTest() throws Exception {
        TTDocument doc = new TTDocument();
        doc.addEntity(testObject);
        doc.setCrud(TTIriRef.iri(IM.UPDATE_ALL));

        TTFilerFactory.getDocumentFiler().fileDocument(doc);
    }

    // @Test
    void loadTest() throws JsonProcessingException {
        TTBundle bundle = new EntityService().getFullEntity("http://endhealth.co.uk/im#objectTest");
        TTArray preds = bundle.getEntity().get(TTIriRef.iri(IM.QUERY));
        assertEquals(1, preds.size());

        TTValue val = preds.get(0);
        assertTrue(val.isLiteral());

        SearchTermCode tc = val.asLiteral().objectValue(SearchTermCode.class);

        assertEquals("Mickey Mouse", tc.getTerm());
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

        TTArray preds = entity.get(TTIriRef.iri(IM.QUERY));
        assertEquals(1, preds.size());

        TTValue val = preds.get(0);
        assertTrue(val.isLiteral());

        SearchTermCode tc = val.asLiteral().objectValue(SearchTermCode.class);

        assertEquals("Mickey Mouse", tc.getTerm());
        assertEquals("EM-EYE-CEE", tc.getCode());
    }
    @Test
    void testTTLiteralSerialization_allNull() throws JsonProcessingException {
        TTLiteral first = literal(null, (TTIriRef)null);
        TTLiteral second = literal(null, (TTIriRef)null);

        assertEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_FirstNull() throws JsonProcessingException {
        TTLiteral first = literal(null, (TTIriRef)null);
        TTLiteral second = literal("TEST", XS.STRING);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_SecondNull() throws JsonProcessingException {
        TTLiteral first = literal("TEST", XS.STRING);
        TTLiteral second = literal(null, (TTIriRef)null);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_DiffVal() throws JsonProcessingException {
        TTLiteral first = literal("SAME", XS.STRING);
        TTLiteral second = literal("DIFFERENT", XS.STRING);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_DiffType() throws JsonProcessingException {
        TTLiteral first = literal("SAME", XS.STRING);
        TTLiteral second = literal("SAME", XS.INTEGER);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_DiffVal_NullType() throws JsonProcessingException {
        TTLiteral first = literal("SAME", (TTIriRef) null);
        TTLiteral second = literal("DIFFERENT", (TTIriRef) null);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_SameVal_NullType() throws JsonProcessingException {
        TTLiteral first = literal("SAME", (TTIriRef) null);
        TTLiteral second = literal("SAME", (TTIriRef) null);

        assertEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_Same() throws JsonProcessingException {
        TTLiteral first = literal("SAME", XS.STRING);
        TTLiteral second = literal("SAME", XS.STRING);

        assertEquals(first, second);
    }
}
