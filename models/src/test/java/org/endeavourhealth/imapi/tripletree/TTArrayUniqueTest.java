package org.endeavourhealth.imapi.tripletree;

import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.junit.jupiter.api.Test;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

class TTArrayUniqueTest {
    @Test
    void differentObjectDifferentValue_Iri() {
        TTArray actual = new TTArray();
        actual.add(iri("http://example.org#SAME"));
        actual.add(iri("http://example.org#DIFFERENT"));

        assertEquals(2, actual.size());
    }

    @Test
    void sameObjectSameValue_Iri() {
        TTIriRef testIri = iri("http://example.org#SAME");

        TTArray actual = new TTArray();

        actual.add(testIri);
        assertEquals(1, actual.size());
        actual.add(testIri);
        assertEquals(1, actual.size());
    }

    @Test
    void differentObjectSameValue_Iri() {
        TTArray actual = new TTArray();
        actual.add(iri("http://example.org#SAME"));
        actual.add(iri("http://example.org#SAME"));

        assertEquals(1, actual.size());
    }

    @Test
    void differentObjectDifferentValue_Literal() {
        TTArray actual = new TTArray();
        actual.add(literal("SAME"));
        actual.add(literal("DIFFERENT"));

        assertEquals(2, actual.size());
    }

    @Test
    void differentObjectSameValue_Literal() {
        TTArray actual = new TTArray();
        actual.add(literal("SAME"));
        actual.add(literal("SAME"));

        assertEquals(1, actual.size());
    }


    @Test
    void sameObjectSameValue_Literal() {
        TTLiteral lit = literal("SAME");

        TTArray actual = new TTArray();
        actual.add(lit);
        actual.add(lit);

        assertEquals(1, actual.size());
    }
}
