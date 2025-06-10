package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.tripletree.*;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.XSD;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;

/**
 * DeSerializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument Deserializer
 */
@Slf4j
public class TTNodeDeserializer {

  private final TTContext context;

  /**
   * @param context the map of prefixes to namespaces in string form.
   */
  public TTNodeDeserializer(TTContext context) {
    this.context = context;
  }


  public void populatePrefixesFromJson(JsonNode document, List<TTPrefix> prefixes) {
    JsonNode contextNode = document.get("context");
    if (contextNode != null) {
      Iterator<Map.Entry<String, JsonNode>> fields = contextNode.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> field = fields.next();
        String key = field.getKey();
        JsonNode value = field.getValue();

        if (value.isTextual() && value.textValue().startsWith("http")) {
          prefixes.add(new TTPrefix(value.textValue(), key));
          context.add(value.asText(), key);
        }
      }
    }
  }

  public void populateTTNodeFromJson(TTNode result, JsonNode node) throws IOException {
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> field = iterator.next();
      String key = field.getKey();
      if (!"context".equals(key)) {
        JsonNode value = field.getValue();
        if ("iri".equals(key))
          result.setIri(expand(value.textValue()));
        else if (value.isArray()) {
          result.set(iri(expand(key)), getArrayNodeAsTripleTreeArray((ArrayNode) value));
        } else {
          result.set(iri(expand(key)), getJsonNodeAsValue(value));
        }
      }
    }
  }

  public TTArray getArrayNodeAsTripleTreeArray(ArrayNode arrayNode) throws IOException {
    TTArray result = new TTArray();

    Iterator<JsonNode> iterator = arrayNode.elements();
    while (iterator.hasNext()) {
      JsonNode value = iterator.next();
      result.add(getJsonNodeAsValue(value));
    }

    return result;
  }

  public TTArray getJsonNodeArrayAsValue(JsonNode node) throws IOException {
    return getArrayNodeAsTripleTreeArray((ArrayNode) node);
  }

  public TTValue getJsonNodeAsValue(JsonNode node) throws IOException {
    if (node.isValueNode())
      return TTLiteral.literal(node);
    else if (node.isObject()) {
      if (node.has(IM.IRI)) {
        if (node.has("name"))
          return iri(expand(node.get(IM.IRI).asText()), node.get("name").asText());
        else
          return iri(expand(node.get(IM.IRI).asText()));
      } else {
        if (node.has(IM.VALUE)) {
          return getJsonNodeAsLiteral(node);
        } else {
          TTNode result = new TTNode();
          populateTTNodeFromJson(result, node);
          return result;
        }
      }
    } else if (node.isArray()) {
      throw new IOException("Failed to deserialize node array");
    } else {
      log.warn("TTNode deserializer - Unhandled node type, reverting to String");
      return TTLiteral.literal(node.asText());
    }
  }

  public TTLiteral getJsonNodeAsLiteral(JsonNode node) throws IOException {
    if (!node.has(IM.TYPE))
      return TTLiteral.literal(node.get(IM.VALUE).textValue());

    TTIriRef type = iri(expand(node.get(IM.TYPE).asText()));
    return switch (type.getIri()) {
      case XSD.STRING -> TTLiteral.literal(node.get(IM.VALUE).textValue());
      case XSD.BOOLEAN -> TTLiteral.literal(Boolean.valueOf(node.get(IM.VALUE).asText()));
      case XSD.INTEGER -> TTLiteral.literal(Integer.valueOf(node.get(IM.VALUE).asText()));
      case XSD.PATTERN -> TTLiteral.literal(Pattern.compile(node.get(IM.VALUE).textValue()));
      case null, default -> throw new IOException("Unhandled literal type [" + type.getIri() + "]");
    };
  }

  public String expand(String iri) {
    return context.expand(iri);
  }

}
