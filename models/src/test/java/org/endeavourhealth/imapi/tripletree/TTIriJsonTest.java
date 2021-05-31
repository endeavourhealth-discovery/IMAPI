package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.XSD;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TTIriJsonTest {
    private static final String IRI_JSON = "{\"name\":\"Test name\",\"@id\":\"http://endhealth.info/im#Test\"}";

    private static final String IRI = "http://endhealth.info/im#Test";
    private static final String NAME = "Test name";

    private ObjectMapper om;

    @Before
    public void init() {
        this.om = new ObjectMapper();
    }

    @Test
    public void testTTIriSerialization() throws JsonProcessingException {
        TTIriRef ref = iri(IRI, NAME);
        String actual = om.writeValueAsString(ref);

        assertEquals(IRI_JSON, actual);
    }

    @Test
    public void testTTIriDeserialization() throws JsonProcessingException {
        TTIriRef ref = om.readValue(IRI_JSON, TTIriRef.class);

        assertEquals(IRI, ref.getIri());
        assertEquals(NAME, ref.getName());
    }
}
