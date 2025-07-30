package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.codegen.IMDMBase;

import java.io.IOException;
import java.util.Map;

public class IMDMSerializer extends StdSerializer<IMDMBase<?>> {

  public IMDMSerializer() {
    this(null);
  }

  protected IMDMSerializer(Class<IMDMBase<?>> t) {
    super(t);
  }

  @Override
  public void serialize(IMDMBase value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    if ((value.getId() == null || value.getId().toString().isEmpty()) && (value.getProperties() == null || value.getProperties().isEmpty())) {
      return;
    }

    Map<String, Object> props = value.getProperties();

    gen.writeStartObject();

    gen.writeStringField("_type", value.getType());
    if (value.getId() != null)
      gen.writeStringField("_id", value.getId().toString());

    for (Map.Entry<String, Object> kvp : props.entrySet()) {
      gen.writeObjectField(kvp.getKey(), kvp.getValue());
    }

    gen.writeEndObject();
  }
}
