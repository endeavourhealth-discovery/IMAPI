package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTContext;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;

public class TTLiteralSerializer extends StdSerializer<TTLiteral> {
  private transient TTNodeSerializer helper;

  public TTLiteralSerializer() {
    this(null);
  }

  public TTLiteralSerializer(Class<TTLiteral> t) {
    super(t);
  }

  public TTLiteralSerializer(Class<TTLiteral> t, TTNodeSerializer helper) {
    super(t);
    this.helper = helper;
  }

  @Override
  public void serialize(TTLiteral literal, JsonGenerator gen, SerializerProvider prov) throws IOException {
    Boolean usePrefixes = (Boolean) prov.getAttribute(TTContext.OUTPUT_CONTEXT);
    usePrefixes = (usePrefixes != null && usePrefixes && helper != null);

    if (literal.getType() != null) {
      switch (XSD.from(literal.getType().getIri())) {
        case XSD.STRING -> gen.writeString(literal.getValue());
        case XSD.BOOLEAN -> gen.writeBoolean(literal.booleanValue());
        case XSD.INTEGER -> gen.writeNumber(literal.intValue());
        case XSD.LONG -> gen.writeNumber(literal.longValue());
        case XSD.PATTERN -> {
          gen.writeStartObject();
          gen.writeStringField("value", literal.getValue());
          gen.writeStringField("type", usePrefixes
            ? helper.prefix(literal.getType().getIri())
            : literal.getType().getIri()
          );
          gen.writeEndObject();
        }
        case null, default -> throw new IOException("Unhandled literal type [" + literal.getType().getIri() + "]");
      }

    } else
      // No type, assume string
      gen.writeString(literal.getValue());
  }
}
