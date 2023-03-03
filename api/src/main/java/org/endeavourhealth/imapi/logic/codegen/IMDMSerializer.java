package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

public class IMDMSerializer extends StdSerializer<IMDMBase> {

    public IMDMSerializer() {
        this(null);
    }

    protected IMDMSerializer(Class<IMDMBase> t) {
        super(t);
    }

    @Override
    public void serialize(IMDMBase value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if ((value._id == null || value._id.isEmpty()) && (value.properties == null || value.properties.isEmpty())) {
            return;
        }

        Map<String, Object> props = value.properties;

        gen.writeStartObject();

        gen.writeStringField("_type", value._type);
        if (value._id != null)
            gen.writeStringField("_id", value._id);

        for (Map.Entry<String, Object> kvp : props.entrySet()) {
            gen.writeObjectField(kvp.getKey(), kvp.getValue());
        }

        gen.writeEndObject();
    }
}
