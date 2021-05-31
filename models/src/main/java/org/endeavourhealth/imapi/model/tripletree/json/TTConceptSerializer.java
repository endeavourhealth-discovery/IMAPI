package org.endeavourhealth.imapi.model.tripletree.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTConcept;
import org.endeavourhealth.imapi.model.tripletree.TTContext;

import java.io.IOException;

public class TTConceptSerializer extends StdSerializer<TTConcept> {
    private TTContextHelper helper;
    private TTNodeSerializer nodeSerializer;

    public TTConceptSerializer() {
        this(null);
    }

    public TTConceptSerializer(Class<TTConcept> t) {
        super(t);
    }

    @Override
    public void serialize(TTConcept concept, JsonGenerator gen, SerializerProvider prov) throws IOException {
        Boolean usePrefixes = (Boolean) prov.getAttribute(TTContext.OUTPUT_CONTEXT);
        usePrefixes = (usePrefixes != null && usePrefixes);

        helper = new TTContextHelper(concept.getContext(), usePrefixes);

        gen.writeStartObject();
        helper.serializeContexts(concept.getPrefixes(), gen);
        gen.writeStringField("@id", helper.prefix(concept.getIri()));
        nodeSerializer = new TTNodeSerializer(concept.getContext(), usePrefixes);
        nodeSerializer.serializeNode(concept, gen);
        gen.writeEndObject();
    }



}
