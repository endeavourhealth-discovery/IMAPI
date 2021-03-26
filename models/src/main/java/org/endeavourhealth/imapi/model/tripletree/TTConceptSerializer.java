package org.endeavourhealth.imapi.model.tripletree;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

public class TTConceptSerializer extends StdSerializer<TTConcept> {
    private Map<String, String> contextMap = new HashMap<>();
    private TTNodeSerializer helper;

    public TTConceptSerializer() {
        this(null);
    }

    public TTConceptSerializer(Class<TTConcept> t) {
        super(t);
    }

    @Override
    public void serialize(TTConcept concept, JsonGenerator gen, SerializerProvider prov) throws IOException {

        helper= new TTNodeSerializer(contextMap);
        gen.writeStartObject();
        helper.serializeContexts(concept.getPrefixes(), gen);
        gen.writeStringField("@id",helper.prefix(concept.getIri()));
        helper.serializeNode(concept, gen);
        gen.writeEndObject();
    }



}
