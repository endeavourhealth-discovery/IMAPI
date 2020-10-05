package com.endavourhealth.valueset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.valueset.models.ValueSet;

@Component
public class ValueSetService {

	@Autowired
	ConceptRepository conceptRepository;

	public ValueSet getValueSet(String iri) {
		Concept concept = conceptRepository.getOne(iri);
		return generateValueSet(concept);
	}

	private ValueSet generateValueSet(Concept concept) {
		ValueSet ValueSet = new ValueSet(concept.getIri(), concept.getName(), concept.getDescription());
		return ValueSet;
	}

}
