package org.endeavourhealth.imapi.mapping.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.endeavourhealth.imapi.vocabulary.RDF;

import com.fasterxml.jackson.databind.JsonNode;

public class MappingInstructionBuilder {

	public static MappingInstructionWrapper buildMappingInstructionList(JsonNode map) {
		ArrayList<MappingInstruction> instructions = new ArrayList<MappingInstruction>();
		instructions.addAll(getSubjectMappingInstruction(map));
		instructions.addAll(getObjectMappingInstructions(map));
		return new MappingInstructionWrapper().setInstructions(instructions).setIterator(getIterator(map));
	}

	private static String getIterator(JsonNode map) {
		JsonNode iterator = findFirstNodeByObjectFieldValue(map, "predicate", "localName", "iterator");
		if (iterator == null) {
			return null;
		}
		return iterator.get("object").get("label").asText();
	}

	private static List<MappingInstruction> getObjectMappingInstructions(JsonNode map) {
		List<MappingInstruction> instructions = new ArrayList<>();
		List<JsonNode> predicateObjectMaps = findNodesByObjectFieldValue(map, "predicate", "localName",
				"predicateObjectMap");
		predicateObjectMaps.forEach(objectMap -> {
			String objectId = objectMap.get("object").get("id").asText();
			List<JsonNode> nodes = findNodesByObjectFieldValue(map, "subject", "id", objectId);
			JsonNode propertyNode = nodes.stream()
					.filter(node -> node.get("predicate").get("localName").asText().equals("predicate"))
					.collect(Collectors.toList()).get(0).get("object");
			String property = propertyNode.get("namespace").asText() + propertyNode.get("localName").asText();

			if (!property.equals("executes")) {
				objectId = nodes.stream().filter(node -> node.get("object").has("id")).collect(Collectors.toList())
						.get(0).get("object").get("id").asText();
				JsonNode node = findFirstNodeByObjectFieldValue(map, "subject", "id", objectId);
				String mappingType = node.get("predicate").get("localName").asText();
				if (node.get("object").has("label")) {
					String value = node.get("object").get("label").asText();
					instructions.add(new MappingInstruction(property, mappingType, value));

				} else if (node.get("predicate").get("localName").asText().equals("functionValue")) {
					instructions.add(new MappingInstruction(property, mappingType, getFunctionName(map, node)));
				}
			}

		});
		return instructions;

	}

	private static List<MappingInstruction> getSubjectMappingInstruction(JsonNode map) {
		List<MappingInstruction> subjectList = new ArrayList<MappingInstruction>();
		JsonNode subjectMapObjectId = findFirstNodeByObjectFieldValue(map, "predicate", "localName", "subjectMap")
				.get("object").get("id");
		JsonNode object = findFirstNodeByObjectFieldValue(map, "subject", "id", subjectMapObjectId.asText());
		String mappingType = object.get("predicate").get("localName").asText();
		String value = mappingType.equals("functionValue") ? getFunctionName(map, object)
				: object.get("object").get("label").asText();
		subjectList.add(new MappingInstruction("@id", mappingType, value));

//		TODO rr:class vs rr:termType
//		JsonNode classObject = findFirstNodeByObjectFieldValue(map, "predicate", "localName", "class");
//		if (classObject != null) {
//			MappingInstruction typeInstruction = new MappingInstruction().setProperty(RDF.TYPE.getIri());
//			String constantValue = classObject.get("object").get("namespace").asText()
//					+ classObject.get("object").get("localName").asText();
//			typeInstruction.setConstant(constantValue);
//			subjectList.add(typeInstruction);
//		}

		return subjectList;
	}

	private static String getFunctionName(JsonNode map, JsonNode object) {
		String objectId = object.get("object").get("id").asText();
		objectId = findFirstNodeByObjectFieldValue(map, "subject", "id", objectId).get("object").get("id").asText();
		List<JsonNode> nodes = findNodesByObjectFieldValue(map, "subject", "id", objectId).stream()
				.filter(node -> node.get("object").has("id")).collect(Collectors.toList());
		objectId = nodes.get(0).get("object").get("id").asText();
		return findFirstNodeByObjectFieldValue(map, "subject", "id", objectId).get("object").get("localName").asText();
	}

	private static List<JsonNode> findNodesByObjectFieldValue(JsonNode node, String object, String field,
			String value) {
		List<JsonNode> nodes = new ArrayList<>();
		Iterator<JsonNode> nodeIterator = node.elements();
		while (nodeIterator.hasNext()) {
			JsonNode element = nodeIterator.next();
			Iterator<Entry<String, JsonNode>> fieldsIterator = element.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> fieldValueMap = fieldsIterator.next();
				if (fieldValueMap.getKey().equals(object) && fieldValueMap.getValue().has(field)
						&& fieldValueMap.getValue().get(field).asText().equals(value)) {
					nodes.add(element);
				}
			}
		}

		return nodes;
	}

	private static JsonNode findFirstNodeByObjectFieldValue(JsonNode node, String object, String field, String value) {
		JsonNode returnValue = null;
		boolean found = false;
		Iterator<JsonNode> nodeIterator = node.elements();
		while (nodeIterator.hasNext() && !found) {
			JsonNode element = nodeIterator.next();
			Iterator<Entry<String, JsonNode>> fieldsIterator = element.fields();
			while (fieldsIterator.hasNext() && !found) {
				Map.Entry<String, JsonNode> fieldValueMap = fieldsIterator.next();
				if (fieldValueMap.getKey().equals(object) && fieldValueMap.getValue().has(field)
						&& fieldValueMap.getValue().get(field).asText().equals(value)) {
					returnValue = element;
					found = true;
				}
			}
		}

		return returnValue;
	}
}
