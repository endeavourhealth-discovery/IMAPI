package com.endavourhealth.ontology;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptAxiom;
import com.endavourhealth.dataaccess.repository.ConceptAxiomRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.ontology.models.Axiom;
import com.endavourhealth.ontology.models.OntologicalConcept;
import com.endavourhealth.ontology.models.OntologicalConceptDetail;

@Component
public class OntologyService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptAxiomRepository conceptAxiomRepository;

	public List<OntologicalConcept> search(String term) {
		List<OntologicalConcept> ontologicalConcept = new ArrayList<OntologicalConcept>();
		List<Concept> concepts = conceptRepository.search(term, ":1301000252100");

		concepts.forEach(concept -> {
			ontologicalConcept.add(new OntologicalConcept(concept.getIri(), concept.getName(), concept.getDescription()));
		});

		return ontologicalConcept;

	}

	public OntologicalConceptDetail getOntologicalConcept(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		return generateOntologicalConcept(concept, getDefintions(concept.getDbid()));
	}
	
	public List<Axiom> getOntologyDefintions(String iri){
		Concept concept = conceptRepository.findByIri(iri);
		return getDefintions(concept.getDbid());
	}

	public List<Axiom> getDefintions(Integer Dbid) {
		List<Axiom> axioms = new ArrayList<Axiom>();
		List<ConceptAxiom> conceptAxioms = conceptAxiomRepository.findByConcept(Dbid);
		conceptAxioms.forEach(conceptAxiom -> {
			axioms.add(new Axiom(conceptAxiom));
		});
		return axioms;
	}

	private OntologicalConceptDetail generateOntologicalConcept(Concept concept, List<Axiom> axioms) {
		return new OntologicalConceptDetail(concept.getIri(), concept.getName(), concept.getDescription(), axioms);
	}

}
