package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.FunctionService;
import org.endeavourhealth.imapi.model.function.FunctionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("api/function")
@CrossOrigin(origins = "*")
@Tag(name="FunctionController")
@RequestScope
public class FunctionController {
	private static final Logger LOG = LoggerFactory.getLogger(FunctionController.class);


	@PostMapping( "/public/callFunction")
	@Operation(
		summary = "function",
		description = "Runs a function IM passing in the iri of the function and a list (map) parameter name/ value arguments"
	)
	public JsonNode callFunction(
		@RequestBody FunctionRequest function) throws Exception {
		LOG.debug("callFunction");
		return new FunctionService().callFunction(function.getFunctionIri(),function.getArguments());
	}
}

