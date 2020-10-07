package com.endavourhealth.ontology;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.endavourhealth.datamodel.models.Ancestory;
import com.endavourhealth.datamodel.models.DataModel;
import com.endavourhealth.ontology.models.OntologicalConcept;

@RestController
@RequestMapping("/ontology")
public class OntologyController {
	
	@Autowired
	OntologyService ontologyService;
	
	@GetMapping(value = "/search")
	public List<OntologicalConcept> search(@RequestParam("term") String term, @RequestParam("root") String root) {
		return ontologyService.search(term, root);
	}

	@GetMapping(value = "/{iri}")
	public OntologicalConcept getOntology(@PathVariable("iri") String iri) {
		return ontologyService.getOntologicalConcept(iri);
	}
	
	@GetMapping(value = "/{iri}/definition")
	public List<String> getDefinitions(@PathVariable("iri") String iri) {
		List<String> definitions = null;
		return definitions;
	}
	
	@GetMapping(value = "/{iri}/parents")
	public List<Ancestory> getParents(@PathVariable("iri") String iri) {
		List<Ancestory> parents = null;
		return parents;
	}
	
	@GetMapping(value = "/{iri}/children")
	public List<DataModel> getChildren(@PathVariable("iri") String iri) {
		List<DataModel> children = null;
		return children;
	}

}
