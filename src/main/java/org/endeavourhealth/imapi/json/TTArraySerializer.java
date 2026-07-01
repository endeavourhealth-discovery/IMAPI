package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;

import java.io.IOException;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTArraySerializer extends StdSerializer<TTArrayJava> {
  public TTArraySerializer() {
    this(null);
  }

  public TTArraySerializer(Class<TTArrayJava> t) {
    super(t);
  }

  public void serialize(TTArrayJava array, JsonGenerator gen, SerializerProvider prov) throws IOException {
    if (array.isLiteral()) {
      prov.defaultSerializeValue(array.asLiteral(), gen);
    } else {
      gen.writeStartArray();

      for (TTValueJava v : array.iterator()) {
        prov.defaultSerializeValue(v, gen);
      }
      gen.writeEndArray();
    }
  }
}
