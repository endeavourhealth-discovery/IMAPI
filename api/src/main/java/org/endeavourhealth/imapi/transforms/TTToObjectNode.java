package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.logic.CachedObjectMapper;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.Map;

public class TTToObjectNode {
    public static ObjectNode getAsObjectNode(TTEntity entity) throws JsonProcessingException {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            ObjectNode objectNode = om.createObjectNode();
            objectNode.put("@id", entity.getIri());
            processNode(entity, objectNode);
            return objectNode;
        }
    }

    private static String getShort(String iri) {
        return iri.substring(iri.lastIndexOf("#") + 1);
    }

    private static void processNode(TTNode node, ObjectNode objectNode) throws JsonProcessingException {
        try (CachedObjectMapper om = new CachedObjectMapper()) {
            for (Map.Entry<TTIriRef, TTArray> entry : node.getPredicateMap().entrySet()) {
                processNodePredicates(objectNode, om, entry);
            }
        }
    }

    private static void processNodePredicates(ObjectNode objectNode, CachedObjectMapper om, Map.Entry<TTIriRef, TTArray> entry) throws JsonProcessingException {
        ObjectNode nodeValue = null;
        if (entry.getValue().isNode()) {
            processNodePredicateNode(objectNode, om, entry);
        } else if (entry.getValue().isIriRef()) {
            processNodePredicateIriRef(objectNode, om, entry);
        } else if (entry.getValue().isLiteral()) {
            processNodePredicateLiteral(objectNode, om, entry);
        } else {
            processNodePredicateArray(objectNode, om, entry);
        }
    }

    private static void processNodePredicateNode(ObjectNode objectNode, CachedObjectMapper om, Map.Entry<TTIriRef, TTArray> entry) throws JsonProcessingException {
        ObjectNode subNode = om.createObjectNode();
        processNode(entry.getValue().asNode(), subNode);
        objectNode.set(getShort(entry.getKey().getIri()), subNode);
    }

    private static void processNodePredicateIriRef(ObjectNode objectNode, CachedObjectMapper om, Map.Entry<TTIriRef, TTArray> entry) {
        ObjectNode iriNode = om.createObjectNode();
        iriNode.put("@id", entry.getValue().asIriRef().getIri());
        objectNode.set(getShort(entry.getKey().getIri()), iriNode);
    }

    private static void processNodePredicateLiteral(ObjectNode objectNode, CachedObjectMapper om, Map.Entry<TTIriRef, TTArray> entry) throws JsonProcessingException {
        ObjectNode nodeValue;
        if (entry.getValue().asLiteral().getValue().charAt(0) == '{') {
            nodeValue = om.readValue(entry.getValue().asLiteral().getValue(), ObjectNode.class);
            objectNode.set(getShort(entry.getKey().getIri()), nodeValue);
        } else
            objectNode.put(getShort(entry.getKey().getIri()), entry.getValue().asLiteral().getValue());
    }

    private static void processNodePredicateArray(ObjectNode objectNode, CachedObjectMapper om, Map.Entry<TTIriRef, TTArray> entry) throws JsonProcessingException {
        ObjectNode nodeValue;
        ArrayNode arrayNode = om.createArrayNode();
        objectNode.set(getShort(entry.getKey().getIri()), arrayNode);
        for (TTValue element : entry.getValue().getElements()) {
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
                iriNode.put("@id", element.asIriRef().getIri());
                arrayNode.add(iriNode);
            }
        }
    }
}
