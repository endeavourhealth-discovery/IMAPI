package com.endavourhealth.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@CrossOrigin
public class ConfigController {
	
	@GetMapping(value = "/ontologies")
	public List<String> getOntologies() {
		return null;
	}
	
	@GetMapping(value = "/modules/{ontologyIdentifier}")
	public List<String> getModules(@PathVariable("ontologyIdentifier") String ontologyIdentifier) {
		return null;
	}

}
