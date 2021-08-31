package org.endeavourhealth.imapi.mapping.builder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.endeavourhealth.imapi.mapping.function.MappingFunction;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import static org.endeavourhealth.imapi.model.tripletree.TTIriRef.iri;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTNode;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;
import org.endeavourhealth.imapi.vocabulary.RDFS;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class EntityBuilder {

	public static List<TTEntity> buildEntityListFromJson(JsonNode content, MappingInstructionWrapper instructionWrapper,
			boolean nested) throws Exception {
		ArrayList<TTEntity> entities = new ArrayList<TTEntity>();

		Iterator<JsonNode> elements = instructionWrapper.getIterator() != null
				? elements = getElementsFromIteratorJsonPath(content, instructionWrapper.getIterator())
				: content.elements();

		while (elements.hasNext()) {
			JsonNode element = elements.next();
			if (nested) {
				addEntity(entities, element, instructionWrapper.getInstructions(),
						instructionWrapper.getNestedPropName(), null);
			} else {
				entities.add(buildEntity(element, instructionWrapper.getInstructions(), null));
			}
		}

		return entities;
	}

	public static List<TTEntity> groupEntities(List<TTEntity> entities) {
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
				groupedMap.put(ungrouped.getIri(), ungrouped);
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

	private static void addEntity(List<TTEntity> entities, JsonNode element, Map<String, List<MappingInstruction>> map,
			String nestedProp, JsonNode parent) throws Exception {

		entities.add(buildEntity(element, map, parent));

		if (element.has(nestedProp)) {
			Iterator<JsonNode> subElements = element.get(nestedProp).elements();

			while (subElements.hasNext()) {
				JsonNode nested = subElements.next();
				addEntity(entities, nested, map, nestedProp, element);
			}
		}
	}

	private static TTEntity buildEntity(JsonNode element, Map<String, List<MappingInstruction>> map, JsonNode parent)
			throws Exception {
		TTEntity entity = new TTEntity();
		String firstKey = map.keySet().stream().findFirst().get();

		for (MappingInstruction instruction : map.get(firstKey)) {
			if ("http://www.w3.org/ns/r2rml#parentTriplesMap".equals(instruction.getValueType())) {
				List<MappingInstruction> instructions = map.get(instruction.getValue());
				if (isBNode(instructions)) {
					TTNode node = new TTNode();
					for (MappingInstruction instr : instructions) {
						setPredicateFromInstruction(element, node, instr, parent);
					}
					entity.set(TTIriRef.iri(instruction.getProperty()), node);
				} else {
					TTEntity node = new TTEntity();
					for (MappingInstruction instr : instructions) {
						setPredicateFromInstruction(element, node, instr, parent);
					}
					entity.set(TTIriRef.iri(instruction.getProperty()), node);
				}

			} else {
				setPredicateFromInstruction(element, entity, instruction, parent);
			}
		}

		return entity;
	}

	private static boolean isBNode(List<MappingInstruction> instructions) {
		return instructions.stream()
				.anyMatch(instruction -> instruction.getValue().equals("http://www.w3.org/ns/r2rml#BlankNode"));
	}

	private static void setPredicateFromInstruction(JsonNode element, TTValue entity, MappingInstruction instruction,
			JsonNode parent) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		String value = getTTValue(element, instruction, parent);

		if (IM.IRI.equals(instruction.getProperty())) {
			((TTEntity) entity).setIri(value);
		} else if (!"http://www.w3.org/ns/r2rml#BlankNode".equals(instruction.getValue())) {
			entity.asNode().set(iri(instruction.getProperty()), new TTLiteral(value));
		}

	}

	private static String getTTValue(JsonNode element, MappingInstruction instruction, JsonNode parent)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		String value = "";
		switch (instruction.getValueType()) {
		case "http://semweb.mmlab.be/ns/rml#reference":
			value = element.at(instruction.getPathFromReference(null)).asText();
			break;
		case "http://www.w3.org/ns/r2rml#template":
			String[] parts = instruction.getValue().split("\\{");
			if (parts.length == 2) {
				String second = element
						.at(instruction.getPathFromReference(parts[1].substring(0, parts[1].length() - 1))).asText();
				value = parts[0] + second;
			}
			break;
		default:
			if (isFunction(instruction)) {
				String functionName = instruction.getValue().split("#")[1];
				Class<?> classObj = MappingFunction.class;
				Method function = classObj.getDeclaredMethod(functionName, JsonNode.class, JsonNode.class);
				value = (String) function.invoke(classObj, element, parent);
			} else {
				value = instruction.getValue();
			}
			break;
		}

		return value;
	}

	private static boolean isFunction(MappingInstruction instruction) {
		String value = instruction.getValue().split("#")[1];
		if (value == null) {
			return false;
		}
		Class<?> classObj = MappingFunction.class;
		List<Method> functions = Arrays.asList(classObj.getDeclaredMethods());
		return functions.stream().anyMatch(function -> value.equals(function.getName()));
	}

}
