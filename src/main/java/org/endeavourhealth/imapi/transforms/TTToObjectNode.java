package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.TTArrayJava;
import org.endeavourhealth.imapi.model.tripletree.TTEntityJava;
import org.endeavourhealth.imapi.model.tripletree.TTNodeJava;
import org.endeavourhealth.imapi.model.tripletree.TTValueJava;

import java.util.Map;

public class TTToObjectNode {
  public static ObjectNode getAsObjectNode(TTEntityJava entity) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      ObjectNode objectNode = om.createObjectNode();
      objectNode.put("iri", entity.getIri());
      processNode(entity, objectNode);
      return objectNode;
    }
  }

  private static String getShort(String iri) {
    return iri.substring(iri.lastIndexOf("#") + 1);
  }

  private static void processNode(TTNodeJava node, ObjectNode objectNode) throws JsonProcessingException {
    try (CachedObjectMapper om = new CachedObjectMapper()) {
      for (Map.Entry<TTIriRef, TTArrayJava> entry : node.getPredicateMap().entrySet()) {
        ObjectNode nodeValue;
        if (entry.getValue().isNode()) {
          ObjectNode subNode = om.createObjectNode();
          processNode(entry.getValue().asNode(), subNode);
          objectNode.set(getShort(entry.getKey().getIri()), subNode);
        } else if (entry.getValue().isIriRef()) {
          ObjectNode iriNode = om.createObjectNode();
          iriNode.put("iri", entry.getValue().asIriRef().getIri());
          objectNode.set(getShort(entry.getKey().getIri()), iriNode);
        } else if (entry.getValue().isLiteral()) {
          if (entry.getValue().asLiteral().getValue().charAt(0) == '{') {
            nodeValue = om.readValue(entry.getValue().asLiteral().getValue(), ObjectNode.class);
            objectNode.set(getShort(entry.getKey().getIri()), nodeValue);
          } else
            objectNode.put(getShort(entry.getKey().getIri()), entry.getValue().asLiteral().getValue());
        } else {
          ArrayNode arrayNode = om.createArrayNode();
          objectNode.set(getShort(entry.getKey().getIri()), arrayNode);
          for (TTValueJava element : entry.getValue().getElements()) {
            if (element.isLiteral()) {
              if (element.asLiteral().getValue().charAt(0) == '{') {
                nodeValue = om.readValue(element.asLiteral().getValue(), ObjectNode.class);
                arrayNode.add(nodeValue);
              } else
                arrayNode.add(element.asLiteral().getValue());
            } else if (element.isNode()) {
              ObjectNode subNode = om.createObjectNode();
              processNode(element.asNode(), subNode);
              arrayNode.add(subNode);
            } else if (element.isIriRef()) {
              ObjectNode iriNode = om.createObjectNode();
              iriNode.put("iri", element.asIriRef().getIri());
              arrayNode.add(iriNode);
            }
          }
        }
      }
    }
  }
}
