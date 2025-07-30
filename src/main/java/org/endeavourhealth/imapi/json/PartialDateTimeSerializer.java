package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.codegen.PartialDateTime;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PartialDateTimeSerializer extends StdSerializer<PartialDateTime> {

  public PartialDateTimeSerializer() {
    this(null);
  }

  protected PartialDateTimeSerializer(Class<PartialDateTime> t) {
    super(t);
  }

  @Override
  public void serialize(PartialDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    if (value.getDateTime() == null) {
      return;
    }

    String isoDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").format(value.getDateTime());

    gen.writeStartObject();

    gen.writeStringField("dateTime", isoDateTime);
    gen.writeStringField("precision", value.getPrecision().toString());

    gen.writeEndObject();
  }
}
