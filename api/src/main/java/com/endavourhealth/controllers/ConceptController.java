package com.endavourhealth.controllers;

import java.util.Set;

import com.endavourhealth.dataaccess.IConceptService;
import org.endeavourhealth.imapi.model.Concept;
import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptReferenceNode;
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
    @Qualifier("ConceptServiceV3")
    IConceptService conceptService;

	@GetMapping(value = "/")
	public Set<ConceptReference> search(@RequestParam(name = "nameTerm") String nameTerm,
                                        @RequestParam(name = "root", required = false) String root,
                                        @RequestParam(name = "includeLegacy", required = false) Boolean includeLegacy) {
		return conceptService.findByNameLike(nameTerm, root, includeLegacy);
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
	public Set<ConceptReference> getConceptChildren(@PathVariable("iri") String iri,
                                                    @RequestParam(name = "page", required = false) Integer page,
                                                    @RequestParam(name = "size", required = false) Integer size,
                                                    @RequestParam(name = "includeLegacy", required = false) Boolean includeLegacy
    ) {
		return conceptService.getImmediateChildren(iri, page, size, includeLegacy);
	}
	
	@PostMapping(value = "/")
	public ConceptReference createConcept(@RequestBody Concept newConcept) {
		return conceptService.create(newConcept);
	}	
}
