package com.endavourhealth.valueset;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptAxiom;
import com.endavourhealth.dataaccess.repository.ConceptAxiomRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.service.ConceptPropertyObjectService;
import com.endavourhealth.valueset.models.Code;
import com.endavourhealth.valueset.models.ValueSet;
import com.endavourhealth.valueset.models.ValueSetDetail;

@Component
public class ValueSetService {

	@Autowired
	ConceptRepository conceptRepository;
	
	@Autowired
	ConceptAxiomRepository conceptAxiomRepository;

	@Autowired
	ConceptPropertyObjectService conceptPropertyObjectService;

	public List<ValueSet> search(String term) {
		List<ValueSet> valueSets = new ArrayList<ValueSet>();
		List<Concept> concepts = conceptRepository.search(term, ":VSET_ValueSet");

		concepts.forEach(concept -> {
			valueSets.add(new ValueSet(concept.getIri(), concept.getName(), concept.getDescription()));
		});

		return valueSets;

	}

	public ValueSetDetail getValueSet(String iri) {
		Concept concept = conceptRepository.findByIri(iri);
		return generateValueSet(concept, getCodes(iri));
	}

	public List<Code> getValuesetCodes(String iri) {
		return getCodes(iri);

	}

	public List<Code> getCodes(String iri) {
		List<Code> codes = new ArrayList<Code>();
		
		List<Concept> members = conceptRepository.getMembers(":3521000252101", iri);

		members.forEach(member -> {
			codes.add(new Code(member.getIri(), member.getName(), member.getDescription(), member.getCode(), null));
		});

		return codes;
	}

	private ValueSetDetail generateValueSet(Concept concept, List<Code> codes) {
		return new ValueSetDetail(concept.getIri(), concept.getName(), concept.getDescription(), codes);
	}

}
