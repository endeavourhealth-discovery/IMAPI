package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.interfacemanager.model.TTIriRef;

import java.io.IOException;
import java.util.Iterator;

import static org.endeavourhealth.imapi.model.tripletree.TTLiteralJava.literal;

public class TTArrayDeserializer extends StdDeserializer<TTArrayJava> {
  public TTArrayDeserializer() {
    this(null);
  }

  public TTArrayDeserializer(Class<TTArrayJava> t) {
    super(t);
  }

  @Override
  public TTArrayJava deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
    ArrayNode array = jsonParser.getCodec().readTree(jsonParser);

    return getNodeAsArray(jsonParser, ctx, array);
  }

  private TTArrayJava getNodeAsArray(JsonParser jsonParser, DeserializationContext ctx, ArrayNode array) throws IOException {

    TTArrayJava result = new TTArrayJava();

    Iterator<JsonNode> elements = array.elements();
    while (elements.hasNext()) {
      JsonNode node = elements.next();
      if (node.isTextual())
        result.add(literal(node.asText()));
      else if (node.isArray())
        throw new IOException("Cant deserialize array of arrays");
      else if (node.isObject()) {
        if (node.has("iri"))
          result.add(TTIriRefExtensionsKt.iri(new TTIriRef(), node.get("iri").textValue()));
        else
          result.add(ctx.readValue(node.traverse(jsonParser.getCodec()), TTNodeJava.class));
      } else
        result.add(literal(node.asText()));
    }

    return result;
  }
}
