package org.endeavourhealth.imapi.mapping.builder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.endeavourhealth.imapi.mapping.function.MappingFunction;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class EntityBuilder {

	public static List<TTEntity> buildEntityListFromJson(JsonNode content, MappingInstructionWrapper instructionWrapper,
			boolean nested) {
		ArrayList<TTEntity> entities = new ArrayList<TTEntity>();

//		// $.dataset[*].concept[*]
//		// /dataset/*/concept

		Iterator<JsonNode> elements = instructionWrapper.getIterator() != null
				? elements = getElementsFromIteratorJsonPath(content, instructionWrapper.getIterator())
				: content.elements();

		elements.forEachRemaining(element -> {
			try {
				if (nested) {
					addEntity(entities, element, instructionWrapper.getInstructions(),
							instructionWrapper.getNestedPropName(), null);
				} else {
					addEntity(entities, element, instructionWrapper.getInstructions());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return entities;
	}

	public static List<TTEntity> groupEntities(List<TTEntity> entities, String graph) {
		HashMap<String, TTEntity> groupedMap = new HashMap<String, TTEntity>();

		for (TTEntity ungrouped : entities) {
			if (groupedMap.containsKey(ungrouped.getIri())) {
				TTEntity entity = groupedMap.get(ungrouped.getIri());
				Map<TTIriRef, TTValue> map = ungrouped.getPredicateMap();
				for (Entry<TTIriRef, TTValue> entry : map.entrySet()) {
					if (RDFS.COMMENT.equals(entry.getKey())) {
						entity.setDescription(entity.has(RDFS.COMMENT) ? getHtmlComment(entity, entry.getValue())
								: "<p>" + entry.getValue() + "</p>");
					} else if (entity.has(entry.getKey()) && isToBeAdded(entity, entry)) {
						entity.addObject(entry.getKey(), entry.getValue());
					} else {
						entity.set(entry.getKey(), entry.getValue());
					}

				}
			} else {
				groupedMap.put(ungrouped.getIri(), ungrouped.set(RDFS.SUBCLASSOF, TTIriRef.iri(graph)));
			}
		}

		return new ArrayList<TTEntity>(groupedMap.values());
	}

	private static Iterator<JsonNode> getElementsFromIteratorJsonPath(JsonNode content, String iterator) {
		Configuration JACKSON_CONFIGURATION = Configuration.builder().mappingProvider(new JacksonMappingProvider())
				.jsonProvider(new JacksonJsonNodeJsonProvider()).build();
		JsonNode nodeFromJsonPathJsonNode = JsonPath.using(JACKSON_CONFIGURATION).parse(content).read(iterator);
		return nodeFromJsonPathJsonNode.elements();
	}

	private static String getHtmlComment(TTEntity entity, TTValue value) {
		String startingTag = "<p>";
		String endingTag = "</p>";
		if (!entity.getDescription().startsWith(startingTag)) {
			return startingTag + entity.getDescription() + endingTag + startingTag + value.asLiteral().getValue()
					+ endingTag;
		}
		return entity.getDescription() + startingTag + value.asLiteral().getValue() + endingTag;
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

	private static void addEntity(List<TTEntity> entities, JsonNode element, List<MappingInstruction> instructions)
			throws Exception {
		try {
			entities.add(buildEntity(element, instructions, null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void addEntity(List<TTEntity> entities, JsonNode element, List<MappingInstruction> instructions,
			String nestedProp, JsonNode parent) throws Exception {

		entities.add(buildEntity(element, instructions, parent));

		if (element.has(nestedProp)) {
			element.get(nestedProp).forEach(nested -> {
				try {
					addEntity(entities, nested, instructions, nestedProp, element);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
	}

	private static TTEntity buildEntity(JsonNode element, List<MappingInstruction> instructions, JsonNode parent)
			throws Exception {
		TTEntity entity = new TTEntity();

		for (MappingInstruction instruction : instructions) {
			switch (instruction.getValueTypeString()) {
			case "function":
				executeFunction(element, entity, instruction, parent);
				break;
			case "reference":
				setFromReference(element, entity, instruction);
				break;
			case "template":
				setFromTemplate(element, entity, instruction);
				break;
			case "constant":
				setConstant(element, entity, instruction);
				break;
			}
		}

		return entity;
	}

	private static void setFromTemplate(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		String[] parts = instruction.getTemplate().split("\\{");
		if (parts.length == 2) {
			String second = element.at(instruction.getPathFromReference(parts[1].substring(0, parts[1].length() - 1)))
					.asText();

			if (instruction.getProperty().equals("@id")) {
				entity.setIri(parts[0] + second);
			} else {
				entity.set(TTIriRef.iri(instruction.getProperty()), new TTLiteral(parts[0] + second));
			}
		}
	}

	private static void setFromReference(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		if (instruction.getProperty().equals("@id")) {
			entity.setIri(element.at(instruction.getPathFromReference(null)).asText());
		} else {
			entity.set(TTIriRef.iri(instruction.getProperty()),
					new TTLiteral(element.at(instruction.getPathFromReference(null)).asText()));
		}

	}

	private static void setConstant(JsonNode element, TTEntity entity, MappingInstruction instruction) {
		if (instruction.getProperty().equals("@id")) {
			entity.setIri(instruction.getConstant());
		} else {
			entity.set(TTIriRef.iri(instruction.getProperty()), TTIriRef.iri(instruction.getConstant()));
		}

	}

	private static void executeFunction(JsonNode element, TTEntity entity, MappingInstruction instruction,
			JsonNode parent) throws Exception {
		Class<?> classObj = MappingFunction.class;
		Method function = classObj.getDeclaredMethod(instruction.getFunction(), TTEntity.class, JsonNode.class,
				JsonNode.class);
		TTIriRef iriRef = (TTIriRef) function.invoke(classObj, entity, element, parent);

		if (instruction.getProperty().equals("@id")) {
			entity.setIri(iriRef.getIri());
		} else {
			entity.set(TTIriRef.iri(instruction.getProperty()), iriRef);
		}
	}

}
