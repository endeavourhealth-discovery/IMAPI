package com.endavourhealth.services.axioms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptAxiom;
import com.endavourhealth.dataaccess.repository.ConceptAxiomRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.services.axioms.models.Axiom;

@Component
public class AxiomService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptAxiomRepository conceptAxiomRepository;

	public List<Axiom> getAxioms(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		List<Axiom> axioms = new ArrayList<Axiom>();
		List<ConceptAxiom> conceptAxioms = conceptAxiomRepository.findByConceptDbid(concept.getDbid());
		conceptAxioms.forEach(conceptAxiom -> {
			axioms.add(new Axiom(conceptAxiom));
		});
		return axioms;
	}

}
