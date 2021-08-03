package org.endeavourhealth.imapi.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.mapping.parser.FileParser;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.hibernate.internal.build.AllowSysOut;

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
		getSubjectMappingInstruction(map);
		Iterator<JsonNode> elementsIterator = map.elements();
		while (elementsIterator.hasNext()) {
			JsonNode element = elementsIterator.next();
			Iterator<Entry<String, JsonNode>> fieldsIterator = element.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				JsonNode value = field.getValue();
				Iterator<Entry<String, JsonNode>> valueFieldsIterator = value.fields();
				while (valueFieldsIterator.hasNext()) {
					Map.Entry<String, JsonNode> valueField = valueFieldsIterator.next();
					if (valueField.getKey().equals("label")) {
						JsonNode subjectId = element.get("subject").get("id");
						if (subjectId != null) {
							JsonNode objectWithSubjectId = findNodeByObjectFieldValue(map, "object", "id",
									subjectId.asText());
							subjectId = objectWithSubjectId.get("subject").get("id");
							JsonNode subjectWithSubjectId = findNodeByObjectFieldValue(map, "subject", "id",
									subjectId.asText());
							instructions.add(new MappingInstruction(getPredicateValue(subjectWithSubjectId),
									valueField.getValue().asText(), null));
						}
					}
				}
			}
		}
		return instructions;
	}

	private MappingInstruction getSubjectMappingInstruction(JsonNode map) {
		JsonNode subjectMapObjectId = findNodeByObjectFieldValue(map, "predicate", "localName", "subjectMap")
				.get("object").get("id");
		JsonNode object = findNodeByObjectFieldValue(map, "subject", "id", subjectMapObjectId.asText());
		if (object.get("predicate").get("localName").asText().equals("functionValue")) {
			System.out.println("function");
		}
		System.out.println(object);
		return null;
	}

	private JsonNode findNodeByObjectFieldValue(JsonNode node, String object, String field, String value) {
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

	private String getPredicateValue(JsonNode node) {
		JsonNode object = node.get("object");
		return object.get("namespace").asText() + object.get("localName").asText();
	}
}
