package org.endeavourhealth.imapi.logic.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.endeavourhealth.imapi.dataaccess.ConceptRepository;
import org.endeavourhealth.imapi.model.tripletree.TTIriRef;
import org.endeavourhealth.imapi.vocabulary.IM;

import java.util.Map;

public class FunctionService {
	private ConceptRepository conceptRepository = new ConceptRepository();

	public ObjectNode callFunction(String iri, Map<String,String> arguments) throws Exception {
		switch (iri){
			case  (IM.NAMESPACE+"SnomedConceptGenerator") :
				return conceptRepository.createConcept(IM.NAMESPACE);
			default :
				throw new IllegalArgumentException("No such function");
		}

	}


}
