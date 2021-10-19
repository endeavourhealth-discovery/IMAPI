package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;

import org.endeavourhealth.imapi.vocabulary.XSD;
import org.junit.jupiter.api.Test;


import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TTLiteralTest {

    @Test
    void testTTLiteralSerialization_allNull() throws JsonProcessingException {
        TTLiteral first = literal(null, (TTIriRef)null);
        TTLiteral second = literal(null, (TTIriRef)null);

        assertEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_FirstNull() throws JsonProcessingException {
        TTLiteral first = literal(null, (TTIriRef)null);
        TTLiteral second = literal("TEST", XSD.STRING);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_SecondNull() throws JsonProcessingException {
        TTLiteral first = literal("TEST", XSD.STRING);
        TTLiteral second = literal(null, (TTIriRef)null);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_DiffVal() throws JsonProcessingException {
        TTLiteral first = literal("SAME", XSD.STRING);
        TTLiteral second = literal("DIFFERENT", XSD.STRING);

        assertNotEquals(first, second);
    }

    @Test
    void testTTLiteralSerialization_DiffType() throws JsonProcessingException {
        TTLiteral first = literal("SAME", XSD.STRING);
        TTLiteral second = literal("SAME", XSD.INTEGER);

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
        TTLiteral first = literal("SAME", XSD.STRING);
        TTLiteral second = literal("SAME", XSD.STRING);

        assertEquals(first, second);
    }
}
