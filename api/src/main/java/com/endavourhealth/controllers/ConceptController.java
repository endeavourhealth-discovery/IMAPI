package com.endavourhealth.controllers;

import java.util.Set;

import com.endavourhealth.dataaccess.IConceptService;
import org.endeavourhealth.informationmanager.model.Concept;
import org.endeavourhealth.informationmanager.model.ConceptReference;
import org.endeavourhealth.informationmanager.model.ConceptReferenceNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/concept")
@CrossOrigin
public class ConceptController {

    @Autowired
//    @Qualifier("preDOM")
    @Qualifier("postDOM")
    IConceptService conceptService;

	@GetMapping(value = "/")
	public Set<ConceptReference> search(@RequestParam(name = "nameTerm") String nameTerm) {
		return conceptService.findByNameLike(nameTerm, ":DiscoveryCommonDataModel");
	}
		
	@GetMapping(value = "/{iri}")
	public Concept getConcept(@PathVariable("iri") String iri) {
		return conceptService.getConcept(iri);
	}
	
	@GetMapping(value = "/{iri}/parents")
	public Set<ConceptReferenceNode> getConceptParents(@PathVariable("iri") String iri) {
		return conceptService.getParentHierarchy(iri);
	}
	
	@GetMapping(value = "/{iri}/children")
	public Set<ConceptReference> getConceptChildren(@PathVariable("iri") String iri) {
		return conceptService.getImmediateChildren(iri);
	}
	
	@PostMapping(value = "/")
	public ConceptReference createConcept(@RequestBody Concept newConcept) {
		return conceptService.create(newConcept);
	}	
}
