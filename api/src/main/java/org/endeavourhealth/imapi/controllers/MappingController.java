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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.mapping.parser.FileParser;
import org.endeavourhealth.imapi.mapping.builder.EntityBuilder;
import org.endeavourhealth.imapi.mapping.builder.MappingInstructionBuilder;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
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
	public Object main(@RequestParam MultipartFile file, @RequestParam MultipartFile maps, @RequestParam String graph,
			@RequestParam boolean nested) throws IOException {
		System.out.println();
		System.out.println(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + " : "
				+ "Mapping process started.");

		// Step 1: file loading
		JsonNode content = FileParser.parseFile(file);
		JsonNode jsonMap = FileParser.parseFile(maps);
		writeToFile("content", content, "Content files loaded.");
		writeToFile("maps", jsonMap, "Map files loaded.");

		// Step 2: simple mapping instructions generating
		MappingInstructionWrapper instructionWrapper = MappingInstructionBuilder.buildMappingInstructionList(jsonMap);
		writeToFile("instructions", instructionWrapper, "Mapping instructions converted.");

		// Step 3: map content to entities
		List<TTEntity> entities = EntityBuilder.buildEntityListFromJson(content, instructionWrapper, nested);
		writeToFile("entities", entities, "Content mapped to " + entities.size() + " entities.");

		// Step 4: group entities with same IRI
		entities = EntityBuilder.groupEntities(entities, graph);
		writeToFile("grouped", entities, "Grouped to " + entities.size() + " entities.");

		// Step 5: populate ttdocument
		return new TTDocument().setEntities(entities).setGraph(TTIriRef.iri(graph)).setCrud(IM.REPLACE);
	}

	private void writeToFile(String filename, Object object, String message)
			throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println(
				LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + " : " + message);
		mapper.enable(SerializationFeature.INDENT_OUTPUT)
				.writeValue(new File("src/main/resources/mapping/logs/" + filename + ".json"), object);
	}

}
