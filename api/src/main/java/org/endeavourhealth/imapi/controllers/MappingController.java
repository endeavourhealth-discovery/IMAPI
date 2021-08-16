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
import org.endeavourhealth.imapi.mapping.builder.MappingInstructionBuilder;
import org.endeavourhealth.imapi.mapping.function.MappingFunction;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController", description = "Mapping endpoint")
public class MappingController {

	ObjectMapper mapper = new ObjectMapper();

	@PostMapping
	public Object main(@RequestParam MultipartFile file, @RequestParam MultipartFile maps, @RequestParam String graph)
			throws IOException {
		// Step 1: file loading
		JsonNode content = FileParser.parseFile(file);
		JsonNode jsonMap = FileParser.parseFile(maps);
		System.out.println("content: " + content);
		System.out.println("jsonMap: " + jsonMap);
		System.out.println();

		// Step 2: simple mapping instructions generating
		List<MappingInstruction> instructions = MappingInstructionBuilder.buildMappingInstructionList(jsonMap);
		String json = mapper.writeValueAsString(instructions);
		System.out.println("instructions: " + json);

		// Step 3: map content to entities
		List<TTEntity> entities = mapContentToEntities(content, instructions);

		// Step 4: populate ttdocument
		return new TTDocument().setEntities(entities);
	}

	private List<TTEntity> mapContentToEntities(JsonNode content, List<MappingInstruction> instructions) {
		ArrayList<TTEntity> entities = new ArrayList<TTEntity>();

		Iterator<JsonNode> elements = content.get("dataset").get(0).get("concept").elements();

		elements.forEachRemaining(element -> {
			try {
				addEntity(entities, element, instructions);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		return entities;
	}

	private void addEntity(List<TTEntity> entities, JsonNode element, List<MappingInstruction> instructions) throws Exception {

		if (!element.isArray()) {
			entities.add(buildEntity(element, instructions));
			if (element.has("concept")) {
				addEntity(entities, element.get("concept"), instructions);
			}
		} 

		element.elements().forEachRemaining(remaining -> {
			try {
				entities.add(buildEntity(remaining, instructions));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (remaining.has("concept")) {
				try {
					addEntity(entities, remaining.get("concept"), instructions);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private TTEntity buildEntity(JsonNode element, List<MappingInstruction> instructions) throws Exception {
		TTEntity entity = new TTEntity();

		for (MappingInstruction instruction : instructions) {
			switch (instruction.getValueTypeString()) {
			case "function":
				executeFunction(element, entity, instruction);
			case "reference":
				// TODO
			case "template":
				// TODO
			case "constant":
				// TODO
			}
		}

		return entity;
	}

	private void executeFunction(JsonNode element, TTEntity entity, MappingInstruction instruction) throws Exception {
		switch (instruction.getFunction()) {
		case "generateIri":
			entity.setIri(MappingFunction.generateIri(element));
			break;

		case "getType":
			entity.set(TTIriRef.iri(instruction.getProperty()), TTIriRef.iri(MappingFunction.getType(element)));
			break;
		}
	}

//	private String getValueFromReference() {
//		
//	}

}
