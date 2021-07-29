package org.endeavourhealth.imapi.controllers;

import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.endeavourhealth.imapi.mapping.parser.FileParser;

@RestController
@RequestMapping("api/mapping")
@CrossOrigin(origins = "*")
@Api(value = "MappingController", description = "Mapping endpoint")
public class MappingController {

	ObjectMapper mapper = new ObjectMapper();

	@PostMapping
	public Object main(@RequestParam MultipartFile file, @RequestParam MultipartFile maps, @RequestParam String graph)
			throws IOException {
//		return FileParser.parseFile(file);
		return FileParser.parseFile(maps);
	}
}
