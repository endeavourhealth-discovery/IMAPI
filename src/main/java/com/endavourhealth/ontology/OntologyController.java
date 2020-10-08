package com.endavourhealth.ontology;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.datamodel.models.Ancestory;
import com.endavourhealth.datamodel.models.DataModelDetail;
import com.endavourhealth.ontology.models.Axiom;
import com.endavourhealth.ontology.models.OntologicalConcept;
import com.endavourhealth.ontology.models.OntologicalConceptDetail;

@RestController
@RequestMapping("/ontology")
public class OntologyController {
	
	@Autowired
	OntologyService ontologyService;
	
	@GetMapping(value = "/search")
	public List<OntologicalConcept> search(@RequestParam("term") String term) {
		return ontologyService.search(term);
	}

	@GetMapping(value = "/{iri}")
	public OntologicalConceptDetail getOntology(@PathVariable("iri") String iri) {
		return ontologyService.getOntologicalConcept(iri);
	}
	
	@GetMapping(value = "/{iri}/definition")
	public List<Axiom> getDefinitions(@PathVariable("iri") String iri) {
		return ontologyService.getOntologyDefintions(iri);
	}
	
	@GetMapping(value = "/{iri}/parents")
	public List<Ancestory> getParents(@PathVariable("iri") String iri) {
		List<Ancestory> parents = null;
		return parents;
	}
	
	@GetMapping(value = "/{iri}/children")
	public List<DataModelDetail> getChildren(@PathVariable("iri") String iri) {
		List<DataModelDetail> children = null;
		return children;
	}

}
