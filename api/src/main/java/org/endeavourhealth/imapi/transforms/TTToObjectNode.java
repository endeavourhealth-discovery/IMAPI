package org.endeavourhealth.imapi.transforms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.model.tripletree.*;

import java.util.Map;

public class TTToObjectNode {
	public static ObjectNode getAsObjectNode(TTEntity entity) throws JsonProcessingException {
		ObjectNode objectNode = new ObjectMapper().createObjectNode();
		objectNode.put("@id",entity.getIri());
		processNode(entity, objectNode);
		return objectNode;
	}
	private static String getShort(String iri){
		return iri.substring(iri.lastIndexOf("#")+1);
	}

	private static void processNode(TTNode node,ObjectNode objectNode) throws JsonProcessingException {
		ObjectMapper om= new ObjectMapper();
		for (Map.Entry<TTIriRef, TTArray> entry:node.getPredicateMap().entrySet()){
			ObjectNode nodeValue=null;
				if (entry.getValue().isNode()) {
				ObjectNode subNode= om.createObjectNode();
				processNode(entry.getValue().asNode(),subNode);
				objectNode.set(getShort(entry.getKey().getIri()),subNode);
			}
			else if (entry.getValue().isIriRef()){
				ObjectNode iriNode= om.createObjectNode();
				iriNode.put("@id",entry.getValue().asIriRef().getIri());
				objectNode.set(getShort(entry.getKey().getIri()),iriNode);
			}
			else if (entry.getValue().isLiteral()) {
					if (entry.getValue().asLiteral().getValue().charAt(0) == '{') {
						nodeValue = om.readValue(entry.getValue().asLiteral().getValue(), ObjectNode.class);
						objectNode.set(getShort(entry.getKey().getIri()), nodeValue);
					} else
						objectNode.put(getShort(entry.getKey().getIri()), entry.getValue().asLiteral().getValue());
				}
			else {
					ArrayNode arrayNode = om.createArrayNode();
					objectNode.set(getShort(entry.getKey().getIri()),arrayNode);
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
	}

}
