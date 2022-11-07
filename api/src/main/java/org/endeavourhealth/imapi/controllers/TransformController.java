package org.endeavourhealth.imapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.endeavourhealth.imapi.logic.service.TransformService;
import org.endeavourhealth.imapi.model.maps.TransformRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

@RestController
@RequestMapping("api/transform")
@CrossOrigin(origins = "*")
@Tag(name="TransformController")
@RequestScope
public class TransformController {
	private static final Logger LOG = LoggerFactory.getLogger(TransformController.class);
	private final TransformService transformService = new TransformService();


	@PostMapping("/public/run")
	@Operation(
		summary = "Run transform",
		description = "Runs a transform from a set of typed sources to a set of target obbjects defined by a transform map"
	)
	public Set<Object> run(@RequestBody TransformRequest transformRequest) throws Exception {
		LOG.debug("run transform");
		return transformService.runTransform(transformRequest);
	}
}

