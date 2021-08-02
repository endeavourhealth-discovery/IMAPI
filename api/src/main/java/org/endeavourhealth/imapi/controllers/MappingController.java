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
		ArrayList<MappingInstruction> instructions = getMappingInstructions(map);
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = content.fields();
		while (fieldsIterator.hasNext()) {
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
		}
		return new TTDocument();
	}

	public ArrayList<MappingInstruction> getMappingInstructions(JsonNode map) {
		ArrayList<MappingInstruction> instructions = new ArrayList();
		Iterator<JsonNode> elementsIterator = map.elements();
		while (elementsIterator.hasNext()) {
			JsonNode element = elementsIterator.next();
			Iterator<Entry<String, JsonNode>> fieldsIterator = element.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				System.out.println();
				System.out.println(field.getKey());
				JsonNode value = field.getValue();
				Iterator<Entry<String, JsonNode>> valueFieldsIterator = value.fields();
				while (valueFieldsIterator.hasNext()) {
					Map.Entry<String, JsonNode> valueField = valueFieldsIterator.next();
					if(valueField.getKey().equals("label"))
					System.out.println(valueField.getKey() + ": " + valueField.getValue());
				}
			}
		}

		return instructions;
	}
}
