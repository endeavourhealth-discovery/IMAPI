package org.endeavourhealth.imapi.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.endeavourhealth.imapi.mapping.validator.PredicateValidator;
import org.endeavourhealth.imapi.mapping.builder.EntityBuilder;
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

	@Autowired
	PredicateValidator predicateValidator;

	ObjectMapper mapper = new ObjectMapper();

	@PostMapping
	public TTDocument map(@RequestParam MultipartFile contentFile, @RequestParam MultipartFile mappingFile,
			@RequestParam String graph, @RequestParam boolean nested) throws Exception {
		JsonNode content = FileParser.parseFile(contentFile);
		MappingInstructionWrapper map = FileParser.parseMap(mappingFile);
		writeToFile("content", content, "Content files loaded.");
		writeToFile("map", map, "Map files loaded.");
		return mapFromJsonNodes(content, map, graph, nested);
	}

	@PostMapping("/newPredicates")
	public Set<String> validateMapDocument(@RequestParam MultipartFile mapDocument) throws Exception {
		MappingInstructionWrapper map = FileParser.parseMap(mapDocument);
		return predicateValidator.getNewPredicates(map);
	}

	@PostMapping("/references")
	public Set<String> getReferencesFromContentFile(@RequestParam MultipartFile contentFile) throws IOException {
		if ("text/csv".equals(contentFile.getContentType())) {
			return FileParser.getColumnNamesFromCsv(contentFile);
		}
		if ("json".equals(contentFile.getContentType())) {

		}
		return null;
	}

	public TTDocument mapFromJsonNodes(JsonNode content, MappingInstructionWrapper map, String graph, boolean nested)
			throws Exception {
		List<TTEntity> entities = EntityBuilder.buildEntityListFromJson(content, map, nested); // Step 1: map content to
																								// entities
		writeToFile("entities", entities, "Content mapped to " + entities.size() + " entities.");

		entities = EntityBuilder.groupEntities(entities); // Step 2: group entities with same IRI
		writeToFile("grouped", entities, "Grouped to " + entities.size() + " entities.");

		TTDocument ttdocument = new TTDocument().setEntities(entities).setGraph(TTIriRef.iri(graph))
				.setCrud(IM.REPLACE); // Step 3: populate ttdocument
		writeToFile("ttdocument", ttdocument, "TTDocument populated.");

		return ttdocument;
	}

	private void writeToFile(String filename, Object object, String message) throws IOException {
		System.out.println(
				LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)) + " : " + message);
		mapper.enable(SerializationFeature.INDENT_OUTPUT)
				.writeValue(new File("src/main/resources/" + filename + ".json"), object);
	}

}
