package com.endavourhealth.valueset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.valueset.models.ValueSet;

@Component
public class ValueSetService {

	@Autowired
	ConceptRepository conceptRepository;

	public List<ValueSet> search(String term, String root) {
		List<ValueSet> valueSets = new ArrayList<ValueSet>();
		List<Concept> concepts = conceptRepository.search(term, root);

		concepts.forEach(concept -> {
			valueSets.add(getValueSet(concept.getIri()));
		});

		return valueSets;

	}

	public ValueSet getValueSet(String iri) {
		Concept concept = conceptRepository.getOne(iri);
		return generateValueSet(concept);
	}

	private ValueSet generateValueSet(Concept concept) {
		return new ValueSet(concept.getIri(), concept.getName(), concept.getDescription(), Collections.emptyList());
	}

}
