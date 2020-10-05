package com.endavourhealth.ontology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.ontology.models.OntologicalConcept;

@RestController
@RequestMapping("/ontology")
public class OntologyController {
	
	@Autowired
	OntologyService OntologyService;

	@GetMapping(value = "/{iri}")
	public OntologicalConcept getOntology(@PathVariable("iri") String iri) {
		return OntologyService.getOntologicalConcept(iri);
	}

}
