package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Json LD serializer for use only with JsonLDMapper as the mapper sets the context objects from the objects passed in
 */
public class JsonLDSerializer extends StdSerializer<Object> {
  private ObjectMapper defaultMapper = new ObjectMapper();
  private Map<String, String> prefixIriMap;
  private Map<String, String> iriPrefixMap;


  protected JsonLDSerializer(Class<Object> t) {
    super(t);
  }

  public JsonLDSerializer setPrefixIriMap(Map<String, String> prefixIriMap) {
    this.prefixIriMap = prefixIriMap;
    if (prefixIriMap != null) {
      iriPrefixMap = new HashMap<>();
      for (Map.Entry<String, String> prefixIri : prefixIriMap.entrySet()) {
        iriPrefixMap.put(prefixIri.getValue(), prefixIri.getKey());
      }
    }
    return this;
  }

  public JsonLDSerializer setDefaultMapper(ObjectMapper defaultMapper) {
    this.defaultMapper = defaultMapper;
    return this;
  }

  @Override
  public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    ObjectNode node;
    if (!(value instanceof JsonNode)) {
      node = defaultMapper.valueToTree(value);
    } else
      node = (ObjectNode) value;
    JsonNode context = node.get("context");
    if (context != null) {
      Map<String, String> map = defaultMapper.convertValue(context, HashMap.class);
      setPrefixIriMap(map);
    }
    gen.writeStartObject();
    Iterator<String> fieldNames = node.fieldNames();
    while (fieldNames.hasNext()) {
      String fieldName = fieldNames.next();
      JsonNode fieldValue = node.get(fieldName);
      if (fieldName.equals("iri") || fieldName.equals("type") || fieldName.equals("set")) {
        gen.writeStringField(fieldName, prefix(fieldValue.asText()));
      } else {
        gen.writeFieldName(fieldName);
        serializeFieldValue(fieldValue, gen, serializers);
      }
    }
    gen.writeEndObject();
  }

  private void serializeFieldValue(JsonNode value, JsonGenerator gen,
                                   SerializerProvider serializers) throws IOException {
    if (value.isTextual())
      gen.writeString(value.asText());
    else if (value.isObject())
      serialize(value, gen, serializers);
    else if (value.isFloat())
      gen.writeNumber(value.asDouble());
    else if (value.isInt())
      gen.writeNumber(value.asInt());
    else if (value.isLong())
      gen.writeNumber(value.asLong());
    else if (value.isBoolean())
      gen.writeBoolean(value.asBoolean());
    else if (value.isArray()) {
      gen.writeStartArray();
      for (int i = 0; i < value.size(); i++) {
        serializeFieldValue(value.get(i), gen, serializers);
      }
      gen.writeEndArray();
    } else if (value.isNull())
      gen.writeNull();
    else
      throw new IOException("unsupported json node type : " + value.getClass());
  }

  public String prefix(String iri) {
    if (prefixIriMap == null)
      return iri;
    int end = Integer.max(iri.lastIndexOf("/"), iri.lastIndexOf("#"));
    String path = iri.substring(0, end + 1);
    String prefix = iriPrefixMap.get(path);
    if (prefix == null)
      return iri;
    else if (end < iri.length() - 1)
      return prefix + ":" + iri.substring(end + 1);
    else if (end == iri.length() - 1)
      return prefix + ":";
    else return iri;
  }

  public String expand(String prefixed) {
    if (prefixIriMap == null)
      return prefixed;
    int colonPos = prefixed.indexOf(":");
    if (colonPos == -1)
      return prefixed;
    String prefix = prefixed.substring(0, colonPos);
    String namespace = prefixIriMap.get(prefix);
    if (namespace != null) {
      return namespace + prefixed.substring(colonPos + 1);
    }
    for (Map.Entry<String, String> iriPrefix : iriPrefixMap.entrySet()) {
      if (iriPrefix.getValue().equals(prefix)) {
        prefixIriMap.put(namespace, prefix);
        return namespace + prefixed.substring(colonPos + 1);
      }
    }
    return prefixed;
  }

}
