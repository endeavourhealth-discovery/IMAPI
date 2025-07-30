package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTArray;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import java.io.IOException;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTArraySerializer extends StdSerializer<TTArray> {
  public TTArraySerializer() {
    this(null);
  }

  public TTArraySerializer(Class<TTArray> t) {
    super(t);
  }

  public void serialize(TTArray array, JsonGenerator gen, SerializerProvider prov) throws IOException {
    if (array.isLiteral()) {
      prov.defaultSerializeValue(array.asLiteral(), gen);
    } else {
      gen.writeStartArray();

      for (TTValue v : array.iterator()) {
        prov.defaultSerializeValue(v, gen);
      }
      gen.writeEndArray();
    }
  }
}
