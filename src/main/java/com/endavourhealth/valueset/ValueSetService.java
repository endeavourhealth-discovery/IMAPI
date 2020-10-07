package com.endavourhealth.valueset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.valueset.models.ValueSet;
import com.endavourhealth.valueset.models.ValueSetDetail;

@Component
public class ValueSetService {

	@Autowired
	ConceptRepository conceptRepository;

	public List<ValueSet> search(String term) {
		List<ValueSet> valueSets = new ArrayList<ValueSet>();
		List<Concept> concepts = conceptRepository.search(term, ":VSET_ValueSet");

		concepts.forEach(concept -> {
			valueSets.add(new ValueSet(concept.getIri(), concept.getName(), concept.getDescription()));
		});

		return valueSets;

	}

	public ValueSetDetail getValueSet(String iri) {
		Concept concept = conceptRepository.getOne(iri);
		return generateValueSet(concept);
	}

	private ValueSetDetail generateValueSet(Concept concept) {
		return new ValueSetDetail(concept.getIri(), concept.getName(), concept.getDescription(), Collections.emptyList());
	}

}
