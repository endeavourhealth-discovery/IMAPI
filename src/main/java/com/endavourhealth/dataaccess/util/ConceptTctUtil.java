package com.endavourhealth.dataaccess.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;

@Component
public class ConceptTctUtil {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptTctRepository conceptTctRepository;

	public boolean checkIfPropertyIsValidType(ConceptPropertyObject conceptPropertyObject, List<String> types) {
		List<ConceptTct> conceptTcts = conceptTctRepository.findBySourceDbid(conceptPropertyObject.getProperty().getDbid());
		for (ConceptTct conceptTct : conceptTcts) {
			for (String type : types) {
				if (conceptTct.getTarget().getIri().equalsIgnoreCase(type)) {
					return true;
				}
			}
		}
		return false;
	}

}
