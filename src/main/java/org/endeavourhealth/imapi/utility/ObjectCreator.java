package org.endeavourhealth.imapi.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.endeavourhealth.interfacemanager.model.NAMESPACE;

import java.util.Map;
import java.util.regex.Pattern;

public class ObjectCreator {
  public static Map<String, String> prefixMap = Map.of(
    "im", NAMESPACE.IM.toString(),
    "rdf", NAMESPACE.RDF.toString(),
    "rdfs", NAMESPACE.RDFS.toString());
  static Pattern BARE_IRI =
    Pattern.compile("(:\\s*)([a-zA-Z_][\\w-]*:[\\w-]+)");
  static Pattern BARE_IDENTIFIER =
    Pattern.compile("(:\\s*)([a-zA-Z_][\\w-]*)(?=\\s*[,}\\]])");

  public static <T> T create(String json, Class<T> clazz) throws JsonProcessingException {
    json = preprocess(json);
    ObjectMapper mapper = JsonMapper.builder()
      .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
      .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
      .enable(JsonReadFeature.ALLOW_TRAILING_COMMA)
      .build();
    ObjectNode object = mapper.readValue(json, ObjectNode.class);
    replacePrefixes(object);
    return mapper.treeToValue(object, clazz);
  }

  private static ObjectNode replacePrefixes(ObjectNode object) {
    object.fields().forEachRemaining(entry -> {
      String key = entry.getKey();
      JsonNode value = entry.getValue();
      if (value.isTextual()) {
        String text = value.asText();
        int colonIndex = text.indexOf(":");
        if (colonIndex > 0) {
          String prefix = text.substring(0, text.indexOf(":"));
          if (prefixMap.containsKey(prefix)) {
            value = new TextNode(prefixMap.get(prefix) + text.substring(text.indexOf(":") + 1));
            entry.setValue(value);
          }
        }
      } else if (value.isArray()) {
        for (JsonNode node : value) {
          replacePrefixes((ObjectNode) node);
        }
      } else if (value.isObject()) replacePrefixes((ObjectNode) value);
    });
    return object;
  }


  public static String preprocess(String input) {
    input = BARE_IRI.matcher(input)
      .replaceAll("$1\"$2\"");
    input = BARE_IDENTIFIER.matcher(input)
      .replaceAll(match -> {
        String value = match.group(2);
        if (value.equals("true") || value.equals("false") || value.equals("null"))
          return match.group(0); // leave untouched
        return match.group(1) + "\"" + value + "\"";
      });
    return input;
  }
}
