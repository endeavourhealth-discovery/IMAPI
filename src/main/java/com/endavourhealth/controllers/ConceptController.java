package com.endavourhealth.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.services.axioms.AxiomService;
import com.endavourhealth.services.axioms.models.Axiom;
import com.endavourhealth.services.concept.ConceptService;
import com.endavourhealth.services.concept.models.Concept;
import com.endavourhealth.services.members.MembersService;
import com.endavourhealth.services.members.models.Code;
import com.endavourhealth.services.properties.PropertiesService;
import com.endavourhealth.services.properties.models.Properties;

@RestController
@CrossOrigin
public class ConceptController {
	
	@Autowired
	ConceptService conceptService;
	
	@Autowired
	PropertiesService dataModelService;
	
	@Autowired
	AxiomService ontologyService;
	
	@Autowired
	MembersService valueSetService;
	
	@GetMapping(value = "/concept/search")
	public List<Concept> search(@RequestParam("term") String term) {
		return conceptService.search(term);
	}
	
	@GetMapping(value = "/concept/{iri}")
	public Concept getConcept(@PathVariable("iri") String iri) {
		Concept concept = conceptService.getConcept(iri);
//		conceptService.addParents(concept);
//		conceptService.addChildren(concept);
		return concept;
	}
	
	@GetMapping(value = "/properties/{iri}")
	public Properties getProperties(@PathVariable("iri") String iri) {
		return dataModelService.getProperties(iri);
	}
	
	@GetMapping(value = "/axioms/{iri}")
	public List<Axiom> getAxioms(@PathVariable("iri") String iri) {
		return ontologyService.getAxioms(iri);
	}
	
	@GetMapping(value = "/members/{iri}")
	public List<Code> getMembers(@PathVariable("iri") String iri) {
		return valueSetService.getMembers(iri);
	}

}
