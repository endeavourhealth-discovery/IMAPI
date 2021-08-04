package org.endeavourhealth.imapi.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.mapping.parser.FileParser;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController", description = "Mapping endpoint")
public class MappingController {

	ObjectMapper mapper = new ObjectMapper();

	@PostMapping
	public Object main(@RequestParam MultipartFile file, @RequestParam MultipartFile maps, @RequestParam String graph)
			throws IOException {
		JsonNode content = FileParser.parseFile(file);
		JsonNode jsonMap = FileParser.parseFile(maps);
		return getMappingInstructions(jsonMap);
	}

	public TTDocument map(JsonNode content, JsonNode map) {
//		ArrayList<MappingInstruction> instructions = getMappingInstructions(map);
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = content.fields();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
		}
		return new TTDocument();
	}

	public ArrayList<MappingInstruction> getMappingInstructions(JsonNode map) {
		ArrayList<MappingInstruction> instructions = new ArrayList<MappingInstruction>();
		instructions.add(getSubjectMappingInstruction(map));
		instructions.addAll(getObjectMappingInstructions(map));
		return instructions;
	}

	private List<MappingInstruction> getObjectMappingInstructions(JsonNode map) {
		List<MappingInstruction> instructions = new ArrayList<>();
		List<JsonNode> predicateObjectMaps = findNodesByObjectFieldValue(map, "predicate", "localName",
				"predicateObjectMap");
		predicateObjectMaps.forEach(objectMap -> {
			String objectId = objectMap.get("object").get("id").asText();
			List<JsonNode> nodes = findNodesByObjectFieldValue(map, "subject", "id", objectId);
			String property = nodes.stream()
					.filter(node -> node.get("predicate").get("localName").asText().equals("predicate"))
					.collect(Collectors.toList()).get(0).get("predicate").get("localName").asText();
			objectId = nodes.stream().filter(node -> node.get("object").has("id")).collect(Collectors.toList()).get(0)
					.get("object").get("id").asText();
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

//					case "functionValue":
//						instructions.add(new MappingInstruction().setProperty(property).setTemplate(value));
//						break;

				}
			}

		});
		return instructions;

	}

	private MappingInstruction getSubjectMappingInstruction(JsonNode map) {
		MappingInstruction instruction = new MappingInstruction();
		JsonNode subjectMapObjectId = findFirstNodeByObjectFieldValue(map, "predicate", "localName", "subjectMap")
				.get("object").get("id");
		JsonNode object = findFirstNodeByObjectFieldValue(map, "subject", "id", subjectMapObjectId.asText());
		if (object.get("predicate").get("localName").asText().equals("functionValue")) {
			String functionName = getFunctionName(map, object);
			instruction = new MappingInstruction().setProperty("@id").setFunction(functionName);
		}
		return instruction;
	}

	private String getFunctionName(JsonNode map, JsonNode object) {
		String objectId = object.get("object").get("id").asText();
		objectId = findFirstNodeByObjectFieldValue(map, "subject", "id", objectId).get("object").get("id").asText();
		List<JsonNode> nodes = findNodesByObjectFieldValue(map, "subject", "id", objectId).stream()
				.filter(node -> node.get("object").has("id")).collect(Collectors.toList());
		objectId = nodes.get(0).get("object").get("id").asText();
		return findFirstNodeByObjectFieldValue(map, "subject", "id", objectId).get("object").get("localName").asText();
	}

	private List<JsonNode> findNodesByObjectFieldValue(JsonNode node, String object, String field, String value) {
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

	private JsonNode findFirstNodeByObjectFieldValue(JsonNode node, String object, String field, String value) {
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
