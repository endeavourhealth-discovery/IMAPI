package com.endavourhealth.services.axioms;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.DataAccessService;
import com.endavourhealth.dataaccess.entity.ConceptAxiom;
import com.endavourhealth.services.axioms.models.Axiom;

@Component
public class AxiomService {

	@Autowired
	DataAccessService dataAccessService;

	public List<Axiom> getAxioms(String iri) {
		List<Axiom> axioms = new ArrayList<Axiom>();
		List<ConceptAxiom> conceptAxioms = dataAccessService.getAxioms(iri);
		conceptAxioms.forEach(conceptAxiom -> {
			axioms.add(new Axiom(conceptAxiom));
		});
		return axioms;
	}

}
