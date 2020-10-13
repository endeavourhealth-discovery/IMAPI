package com.endavourhealth.dataaccess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.entity.ConceptTct;
import com.endavourhealth.dataaccess.repository.ConceptRepository;
import com.endavourhealth.dataaccess.repository.ConceptTctRepository;

@Component
public class ConceptTctService {

	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptTctRepository conceptTctRepository;

	public boolean checkIfPropertyIsValidType(ConceptPropertyObject conceptPropertyObject) {
		List<ConceptTct> conceptTcts = conceptTctRepository.findBySourceDbid(conceptPropertyObject.getProperty().getDbid());
		for (ConceptTct conceptTct : conceptTcts) {
			if (conceptTct.getTarget().getIri().equalsIgnoreCase("owl:topObjectProperty")
					|| conceptTct.getTarget().getIri().equalsIgnoreCase("owl:topDataProperty")) {
				return true;
			}
		}
		return false;
	}

}
