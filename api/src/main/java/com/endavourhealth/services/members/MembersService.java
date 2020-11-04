package com.endavourhealth.services.members;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.DataAccessService;
import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.services.members.models.Code;

@Component
public class MembersService {

	@Autowired
	DataAccessService dataAccessService;

	public List<Code> getMembers(String iri) {
		List<Code> codes = new ArrayList<Code>();
		List<Concept> members = dataAccessService.getMembers(iri);
		members.forEach(member -> {
			codes.add(new Code(member.getIri(), member.getName(), member.getDescription(), member.getCode(), null));
		});
		return codes;
	}

}
