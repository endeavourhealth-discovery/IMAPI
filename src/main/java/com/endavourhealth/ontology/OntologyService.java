package com.endavourhealth.ontology;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.ontology.models.OntologicalConcept;

@Component
public class OntologyService {
	
	@Autowired
	ConceptRepository conceptRepository;
	
	public OntologicalConcept getOntologicalConcept(String iri) {
		Concept concept = conceptRepository.getOne(iri);
		return generateOntologicalConcept(concept);
	}

	private OntologicalConcept generateOntologicalConcept(Concept concept) {
		OntologicalConcept OntologicalConcept = new OntologicalConcept(concept.getIri(), concept.getName(), concept.getDescription());
		return OntologicalConcept;
	}

}
