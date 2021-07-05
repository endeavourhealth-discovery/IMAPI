package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.XSD;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TTLiteralJsonTest {
    private static final String LIT_STR_JSON = "\"Test name\"";
    private static final String LIT_INT_JSON = "10";
    private static final String LIT_BOOL_JSON = "true";
    private static final String LIT_PAT_JSON = "{\"@value\":\".*\",\"@type\":\"http://www.w3.org/2001/XMLSchema#pattern\"}";

    private static final String NAME = "Test name";
    private static final Pattern PATTERN = Pattern.compile(".*");

    private ObjectMapper om;

    @Before
    public void init() {
        this.om = new ObjectMapper();
    }

    @Test
    public void testTTLiteralSerialization() throws JsonProcessingException {
        TTLiteral lit = literal(NAME);
        String actual = om.writeValueAsString(lit);

        assertEquals(LIT_STR_JSON, actual);
    }

    @Test
    public void testTTLiteralDeserialization() throws JsonProcessingException {
        TTLiteral lit = om.readValue(LIT_STR_JSON, TTLiteral.class);

        assertEquals(NAME, lit.getValue());
        assertNull(lit.getType());
    }

    @Test
    public void testTTLiteralIntSerialization() throws JsonProcessingException {
        TTLiteral lit = literal(10);
        String actual = om.writeValueAsString(lit);

        assertEquals(LIT_INT_JSON, actual);
    }

    @Test
    public void testTTLiteralIntDeserialization() throws JsonProcessingException {
        TTLiteral lit = om.readValue(LIT_INT_JSON, TTLiteral.class);

        assertEquals("10", lit.getValue());
        assertEquals(XSD.INTEGER, lit.getType());
    }

    @Test
    public void testTTLiteralBoolSerialization() throws JsonProcessingException {
        TTLiteral lit = literal(true);
        String actual = om.writeValueAsString(lit);

        assertEquals(LIT_BOOL_JSON, actual);
    }

    @Test
    public void testTTLiteralBoolDeserialization() throws JsonProcessingException {
        TTLiteral lit = om.readValue(LIT_BOOL_JSON, TTLiteral.class);

        assertEquals("true", lit.getValue());
        assertEquals(XSD.BOOLEAN, lit.getType());
    }

    @Test
    public void testTTLiteralPatSerialization() throws JsonProcessingException {
        TTLiteral lit = literal(PATTERN);
        String actual = om.writeValueAsString(lit);

        assertEquals(LIT_PAT_JSON, actual);
    }

    @Test
    public void testTTLiteralPatDeserialization() throws JsonProcessingException {
        TTLiteral lit = om.readValue(LIT_PAT_JSON, TTLiteral.class);

        assertEquals(".*", lit.getValue());
        assertEquals(XSD.PATTERN, lit.getType());
    }

}
