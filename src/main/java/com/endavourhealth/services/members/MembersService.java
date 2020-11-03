package com.endavourhealth.services.members;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.DataAccessService;
import com.endavourhealth.services.members.models.Code;

@Component
public class MembersService {

	@Autowired
	DataAccessService dataAccessService;

	public List<Code> getMembers(String iri) {
		return dataAccessService.getMembers(iri);

	}

}
