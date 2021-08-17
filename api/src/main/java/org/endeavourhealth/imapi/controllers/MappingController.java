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
import org.endeavourhealth.imapi.mapping.builder.EntityBuilder;
import org.endeavourhealth.imapi.mapping.builder.MappingInstructionBuilder;
import org.endeavourhealth.imapi.mapping.function.MappingFunction;
import org.endeavourhealth.imapi.mapping.model.MappingInstruction;
import org.endeavourhealth.imapi.model.tripletree.TTDocument;
import org.endeavourhealth.imapi.model.tripletree.TTEntity;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.model.tripletree.TTLiteral;
import org.endeavourhealth.imapi.model.tripletree.TTValue;
import org.endeavourhealth.imapi.vocabulary.IM;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController", description = "Mapping endpoint")
public class MappingController {

	ObjectMapper mapper = new ObjectMapper();

	@PostMapping
	public Object main(@RequestParam MultipartFile file, @RequestParam MultipartFile maps, @RequestParam String graph, @RequestParam String iterator, @RequestParam String nestedProp)
			throws IOException {
		// Step 1: file loading
		JsonNode content = FileParser.parseFile(file);
		JsonNode jsonMap = FileParser.parseFile(maps);
		System.out.println("content: " + content);
		System.out.println("jsonMap: " + jsonMap);

		// Step 2: simple mapping instructions generating
		List<MappingInstruction> instructions = MappingInstructionBuilder.buildMappingInstructionList(jsonMap);
		System.out.println("instructions: " + mapper.writeValueAsString(instructions));

		// Step 3: map content to entities
		List<TTEntity> entities = EntityBuilder.buildEntityListFromJson(content, instructions, iterator, nestedProp);
		System.out.println("entities: " + mapper.writeValueAsString(entities));

		// Step 4: populate ttdocument
		return new TTDocument().setEntities(entities).setGraph(TTIriRef.iri(graph)).setCrud(IM.REPLACE);
	}

}
