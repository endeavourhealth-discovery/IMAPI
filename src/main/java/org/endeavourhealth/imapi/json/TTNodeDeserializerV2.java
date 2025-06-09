package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Serializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument serializer
 */
public class TTNodeDeserializerV2 extends StdDeserializer<TTNode> {
  private TTContext context;

  public TTNodeDeserializerV2() {
    this(null);
  }

  public TTNodeDeserializerV2(Class<TTNode> t) {
    super(t);
  }

  /**
   * @param context the context object for the JSON-LD document
   */
  public TTNodeDeserializerV2(Class<TTNode> t, TTContext context) {
    super(t);
    this.context = context;
  }

  public TTNode deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);

    TTNode result = new TTNode();
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> field = iterator.next();
      String key = expand(field.getKey());
      if (!"context".equals(key)) {
        JsonNode value = field.getValue();
        if ("iri".equals(key))
          result.setIri(expand(value.textValue()));
        else if (value.isTextual())
          result.set(TTIriRef.iri(key), TTLiteral.literal(value.asText()));
        else if (value.isArray())
          result.set(TTIriRef.iri(key), ctx.readValue(value.traverse(jsonParser.getCodec()), TTArray.class));
        else if (value.isObject()) {
          if (value.has("iri"))
            result.set(TTIriRef.iri(key), TTIriRef.iri(value.get("iri").asText()));
          else
            result.set(TTIriRef.iri(key), ctx.readValue(value.traverse(jsonParser.getCodec()), TTNode.class));
        } else
          result.set(TTIriRef.iri(key), TTLiteral.literal(value.asText()));
      }
    }
    return result;
  }

  public String expand(String iri) {
    if (context == null)
      return iri;
    else
      return context.expand(iri);
  }
}
