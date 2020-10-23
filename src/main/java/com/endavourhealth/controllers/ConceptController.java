package com.endavourhealth.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.services.axioms.AxiomService;
import com.endavourhealth.services.axioms.models.Axiom;
import com.endavourhealth.services.concept.ConceptService;
import com.endavourhealth.services.concept.models.Concept;
import com.endavourhealth.services.concept.models.CreateConcept;
import com.endavourhealth.services.concept.models.Query;
import com.endavourhealth.services.members.MembersService;
import com.endavourhealth.services.members.models.Code;
import com.endavourhealth.services.perspective.PerspectiveService;
import com.endavourhealth.services.perspective.models.Perspective;
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
	
	@Autowired
	PerspectiveService perspectiveService;
	
	@PostMapping(value = "/concept/search")
	public List<Concept> search(@RequestBody Query query) {
		List<Concept> concepts = null;
		
		Perspective perspective = perspectiveService.getPerspective(query.getPerspectiveId());
		if(perspective != null) {
			concepts = conceptService.search(query.getTerm(), perspective.getRootIri());	
		}
		
		return concepts;
	}
	
	@GetMapping(value = "/concept/{iri}")
	public Concept getConcept(@PathVariable("iri") String iri, @RequestParam(name = "perspective") String perspectiveId) {
		Concept concept = null;
		
		Perspective perspective = perspectiveService.getPerspective(perspectiveId);
		if(perspective != null) {
			concept = conceptService.getConcept(iri, perspective.getInheritancePropertyIris(), perspective.getRootIri());	
		}
		
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
	
	@PostMapping(value = "/concept")
	public Concept createConcept(@RequestBody CreateConcept newConcept) {
		return new Concept(null);
	}

}
