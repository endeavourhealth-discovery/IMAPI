package org.endeavourhealth.imapi.tripletree;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.*;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import static org.endeavourhealth.imapi.model.tripletree.TTLiteral.literal;

public class PrefixTest {
    @Test
    public void prefixSerializationEntityTest() throws JsonProcessingException {

    }

    @Test
    public void prefixSerializationDocumentTest() throws JsonProcessingException {
        TTDocument document = new TTDocument();
;
    }

    @Test
    public void prefixSerializationEntityInDocumentTest() throws JsonProcessingException {
    }


}
