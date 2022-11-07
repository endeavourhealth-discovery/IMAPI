package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;

import java.io.IOException;

public class TTEntitySerializer extends StdSerializer<TTEntity> {

    public TTEntitySerializer() {
        this(null);
    }

    public TTEntitySerializer(Class<TTEntity> t) {
        super(t);
    }

    @Override
    public void serialize(TTEntity entity, JsonGenerator gen, SerializerProvider prov) throws IOException {
        Boolean usePrefixes = (Boolean) prov.getAttribute(TTContext.OUTPUT_CONTEXT);
        usePrefixes = (usePrefixes != null && usePrefixes);

        TTContextHelper helper = new TTContextHelper(entity.getContext(), usePrefixes);

        gen.writeStartObject();
        helper.serializeContexts(entity.getPrefixes(), gen);
        gen.writeStringField("@id", helper.prefix(entity.getIri()));
        TTNodeSerializer nodeSerializer = new TTNodeSerializer(entity.getContext(), usePrefixes);
        nodeSerializer.serializeNode(entity, gen,prov);
        gen.writeEndObject();
    }
}
