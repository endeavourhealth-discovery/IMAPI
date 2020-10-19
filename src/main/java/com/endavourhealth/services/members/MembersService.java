package com.endavourhealth.services.members;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.repository.ConceptAxiomRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.services.members.models.Code;

@Component
public class MembersService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptAxiomRepository conceptAxiomRepository;

	public List<Code> getMembers(String iri) {
		List<Code> codes = new ArrayList<Code>();

		List<Concept> members = conceptRepository.getMembers(":3521000252101", iri);

		members.forEach(member -> {
			codes.add(new Code(member.getIri(), member.getName(), member.getDescription(), member.getCode(), null));
		});

		return codes;

	}

}
