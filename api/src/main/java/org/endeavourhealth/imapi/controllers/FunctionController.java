package org.endeavourhealth.imapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.FunctionService;
import org.endeavourhealth.imapi.model.iml.FunctionRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;


import jakarta.servlet.http.HttpServletRequest;

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
	public JsonNode callFunction(HttpServletRequest request, @RequestBody FunctionRequest function) throws Exception {
		LOG.debug("callFunction");
		return new FunctionService().callFunction(request, function.getFunctionIri(),function.getArguments());
	}

	@PostMapping("/public/callAskFunction")
	@Operation(
		summary = "askFunction",
		description = "Runs a function passing in the iri of the function and a list of arguments including a parameter searchIri, and returns a boolean result."
	)
	public Boolean callAskFunction(HttpServletRequest request, @RequestBody FunctionRequest functionRequest) {
		LOG.debug("callAskFunction");
		return new FunctionService().callAskFunction(request, functionRequest.getFunctionIri(),functionRequest.getArguments());
	}
}