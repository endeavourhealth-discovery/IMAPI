package org.endeavourhealth.imapi.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * DeSerializes a TTNode to JSON-LD. Normally called by a specialised class such as TTEntity or TTDocument Deserializer
 */
@Slf4j
public class TTNodeDeserializer {

  private final TTContextJava context;

  /**
   * @param context the map of prefixes to namespaces in string form.
   */
  public TTNodeDeserializer(TTContextJava context) {
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

  public void populateTTNodeFromJson(TTNodeJava result, JsonNode node) throws IOException {
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> field = iterator.next();
      String key = field.getKey();
      if (!"context".equals(key)) {
        JsonNode value = field.getValue();
        if ("iri".equals(key))
          result.setIri(expand(value.textValue()));
        else if (value.isArray()) {
          result.set(TTIriRefExtensionsKt.iri(new TTIriRef(), expand(key)), getArrayNodeAsTripleTreeArray((ArrayNode) value));
        } else {
          result.set(TTIriRefExtensionsKt.iri(new TTIriRef(), expand(key)), getJsonNodeAsValue(value));
        }
      }
    }
  }

  public TTArrayJava getArrayNodeAsTripleTreeArray(ArrayNode arrayNode) throws IOException {
    TTArrayJava result = new TTArrayJava();

    Iterator<JsonNode> iterator = arrayNode.elements();
    while (iterator.hasNext()) {
      JsonNode value = iterator.next();
      result.add(getJsonNodeAsValue(value));
    }

    return result;
  }

  public TTArrayJava getJsonNodeArrayAsValue(JsonNode node) throws IOException {
    return getArrayNodeAsTripleTreeArray((ArrayNode) node);
  }

  public TTValueJava getJsonNodeAsValue(JsonNode node) throws IOException {
    if (node.isValueNode())
      return TTLiteralJava.literal(node);
    else if (node.isObject()) {
      if (node.has(ImVocab.IRI.toString())) {
        if (node.has("name"))
          return TTIriRefExtensionsKt.iri(new TTIriRef(), expand(node.get(ImVocab.IRI.toString()).asText()), node.get("name").asText());
        else
          return TTIriRefExtensionsKt.iri(new TTIriRef(), expand(node.get(ImVocab.IRI.toString()).asText()));
      } else {
        if (node.has(ImVocab.VALUE.toString())) {
          return getJsonNodeAsLiteral(node);
        } else {
          TTNodeJava result = new TTNodeJava();
          populateTTNodeFromJson(result, node);
          return result;
        }
      }
    } else if (node.isArray()) {
      throw new IOException("Failed to deserialize node array");
    } else {
      log.warn("TTNode deserializer - Unhandled node type, reverting to String");
      return TTLiteralJava.literal(node.asText());
    }
  }

  public TTLiteralJava getJsonNodeAsLiteral(JsonNode node) throws IOException {
    if (!node.has(ImVocab.TYPE.toString()))
      return TTLiteralJava.literal(node.get(ImVocab.VALUE.toString()).textValue());

    TTIriRef type = TTIriRefExtensionsKt.iri(new TTIriRef(), expand(node.get(ImVocab.TYPE.toString()).asText()));
    return switch (XsdVocab.fromValue(type.getIri())) {
      case XsdVocab.STRING -> TTLiteralJava.literal(node.get(ImVocab.VALUE.toString()).textValue());
      case XsdVocab.BOOLEAN -> TTLiteralJava.literal(Boolean.valueOf(node.get(ImVocab.VALUE.toString()).asText()));
      case XsdVocab.INTEGER -> TTLiteralJava.literal(Integer.valueOf(node.get(ImVocab.VALUE.toString()).asText()));
      case XsdVocab.PATTERN -> TTLiteralJava.literal(Pattern.compile(node.get(ImVocab.VALUE.toString()).textValue()));
      case null, default -> throw new IOException("Unhandled literal type [" + type.getIri() + "]");
    };
  }

  public String expand(String iri) {
    return context.expand(iri);
  }

}
