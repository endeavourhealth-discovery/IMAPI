package org.endeavourhealth.imapi.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.mapping.parser.FileParser;
import org.endeavourhealth.imapi.mapping.builder.EntityBuilder;
import org.endeavourhealth.imapi.mapping.builder.MappingInstructionBuilder;
import org.endeavourhealth.imapi.mapping.model.MappingInstructionWrapper;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController", description = "Mapping endpoint")
public class MappingController {

	ObjectMapper mapper = new ObjectMapper();
	
	@PostMapping
	public TTDocument map(@RequestParam MultipartFile contentFile, @RequestParam MultipartFile mappingFile,
			@RequestParam String graph, @RequestParam boolean nested) throws IOException {
		JsonNode content = FileParser.parseFile(contentFile);
		JsonNode map = FileParser.parseFile(mappingFile);
		writeToFile("content", content, "Content files loaded.");
		writeToFile("map", map, "Map files loaded.");
		return mapFromJsonNodes(content, map, graph, nested);
	}

	public TTDocument mapFromJsonNodes(JsonNode content, JsonNode map, String graph, boolean nested)
			throws IOException {
		MappingInstructionWrapper instructionWrapper = MappingInstructionBuilder.buildMappingInstructionList(map); // Step 1: simple mapping instructions generating
		writeToFile("instructions", instructionWrapper, "Mapping instructions converted.");
		
		List<TTEntity> entities = EntityBuilder.buildEntityListFromJson(content, instructionWrapper, nested); // Step 2: map content to entities
		writeToFile("entities", entities, "Content mapped to " + entities.size() + " entities.");
		
		entities = EntityBuilder.groupEntities(entities, graph); // Step 3: group entities with same IRI
		writeToFile("grouped", entities, "Grouped to " + entities.size() + " entities.");

		return new TTDocument().setEntities(entities).setGraph(TTIriRef.iri(graph)).setCrud(IM.REPLACE); // Step 4: populate ttdocument
	}

	private void writeToFile(String filename, Object object, String message) throws IOException {
		System.out.println(
				LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + " : " + message);
		mapper.enable(SerializationFeature.INDENT_OUTPUT)
				.writeValue(new File("src/main/resources/mapping/logs/" + filename + ".json"), object);
	}

}
