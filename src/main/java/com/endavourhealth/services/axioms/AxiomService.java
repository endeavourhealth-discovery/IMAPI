package com.endavourhealth.services.axioms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.DataAccessService;
import com.endavourhealth.services.axioms.models.Axiom;

@Component
public class AxiomService {

	@Autowired
	DataAccessService dataAccessService;

	public List<Axiom> getAxioms(String iri) {
		return dataAccessService.getAxioms(iri);
	}

}
