package org.endeavourhealth.imapi.mapping.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTValue;

import com.fasterxml.jackson.databind.JsonNode;

public class MappingInstructionBuilder {

	public static List<MappingInstruction> buildMappingInstructionList(JsonNode map) {
		ArrayList<MappingInstruction> instructions = new ArrayList<MappingInstruction>();
		instructions.add(getSubjectMappingInstruction(map));
		instructions.addAll(getObjectMappingInstructions(map));
		return instructions;
	}

	public static List<TTEntity> groupEntities(List<TTEntity> entities) {
		List<TTEntity> groupedList = new ArrayList<TTEntity>();
		Set<String> uniqueIris = new HashSet<String>();
		entities.forEach(entity -> {
			uniqueIris.add(entity.getIri());
		});

		uniqueIris.forEach(iri -> {
			List<TTEntity> filtered = entities.stream().filter(entity -> entity.getIri().equals(iri))
					.collect(Collectors.toList());
			TTEntity entity = filtered.get(0);

			filtered.forEach(ungrouped -> {
				Map<TTIriRef, TTValue> map = ungrouped.getPredicateMap();
				for (Entry<TTIriRef, TTValue> entry : map.entrySet()) {
					if (entity.has(entry.getKey())) {
						if (isToBeAdded(entity, entry)) {
							entity.addObject(entry.getKey(), entry.getValue());
						}
					} else {
						entity.set(entry.getKey(), entry.getValue());
					}

				}
			});
			groupedList.add(entity);
		});

		return groupedList;
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
					switch (mappingType) {
					case "constant":
						instructions.add(new MappingInstruction().setProperty(property).setConstant(value));
						break;
					case "reference":
						instructions.add(new MappingInstruction().setProperty(property).setReference(value));
						break;
					case "template":
						instructions.add(new MappingInstruction().setProperty(property).setTemplate(value));
						break;
					}
				}

				if (node.get("predicate").get("localName").asText().equals("functionValue")) {
					instructions.add(
							new MappingInstruction().setProperty(property).setFunction(getFunctionName(map, node)));
				}
			}

		});
		return instructions;

	}

	private static MappingInstruction getSubjectMappingInstruction(JsonNode map) {
		MappingInstruction instruction = new MappingInstruction().setProperty("@id");
		JsonNode subjectMapObjectId = findFirstNodeByObjectFieldValue(map, "predicate", "localName", "subjectMap")
				.get("object").get("id");
		JsonNode object = findFirstNodeByObjectFieldValue(map, "subject", "id", subjectMapObjectId.asText());
		String mappingType = object.get("predicate").get("localName").asText();

		switch (mappingType) {
		case "functionValue":
			instruction.setFunction(getFunctionName(map, object));
			break;
		case "reference":
			instruction.setReference(object.get("object").get("label").asText());
			break;
		case "template":
			instruction.setTemplate(object.get("object").get("label").asText());
			break;
		case "constant":
			instruction.setConstant(object.get("object").get("label").asText());
			break;

		}

		return instruction;
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

	private static boolean isToBeAdded(TTEntity entity, Entry<TTIriRef, TTValue> entry) {

		if (entity.get(entry.getKey()).isIriRef()) {
			String key = entity.get(entry.getKey()).asIriRef().getIri();
			String value = entry.getValue().asIriRef().getIri();
			boolean isAlreadyThere = key.equals(value);
			return !isAlreadyThere;
		}
		if (entity.get(entry.getKey()).isLiteral()) {
			String key = entity.get(entry.getKey()).asLiteral().getValue();
			String value = entry.getValue().asLiteral().getValue();
			boolean isAlreadyThere = key.equals(value);
			return !isAlreadyThere;
		}
		if (entity.get(entry.getKey()).isList()) {
			boolean isAlreadyThere = entity.get(entry.getKey()).asArray().getElements().stream()
					.anyMatch(element -> (element.isLiteral()
							&& element.asLiteral().getValue().equals(entry.getValue().asLiteral().getValue()))
							|| (element.isIriRef()
									&& element.asIriRef().getIri().equals(entry.getValue().asIriRef().getIri())));
			return !isAlreadyThere;
		}

		return true;
	}

}
