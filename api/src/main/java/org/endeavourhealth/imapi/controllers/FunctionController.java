package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.FunctionService;
import org.endeavourhealth.imapi.logic.service.SearchService;
import org.endeavourhealth.imapi.model.sets.Query;
import org.endeavourhealth.imapi.model.sets.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/function")
@CrossOrigin(origins = "*")
@Tag(name="FunctionController")
@RequestScope
public class FunctionController {
	private static final Logger LOG = LoggerFactory.getLogger(FunctionController.class);


	@GetMapping( "/callFunction")
	@Operation(
		summary = "function",
		description = "Runs a function IM"
	)
	public ObjectNode callFunction(	@RequestParam(name = "iri") String iri,
																	 @RequestParam(name = "arguments") Map<String,String> arguments) throws Exception {
		LOG.debug("callFunction");
		return new FunctionService().callFunction(iri,arguments);
	}
}

