package com.endavourhealth.dataaccess.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.dataaccess.entity.ConceptPropertyObject;
import com.endavourhealth.dataaccess.repository.ConceptPropertyObjectRepository;
import com.endavourhealth.dataaccess.repository.ConceptRepository;

@Component
public class ConceptPropertyObjectService {
	
	@Autowired
	ConceptRepository conceptRepository;

	@Autowired
	ConceptPropertyObjectRepository conceptPropertyObjectRepository;
	
	public List<ConceptPropertyObject> findAllByConcept(Integer conceptDbid) {
		return conceptPropertyObjectRepository.findByConcept(conceptDbid);
	}
	
	public Concept getProperty(ConceptPropertyObject conceptPropertyObject) {
		return conceptRepository.findByDbid(conceptPropertyObject.getProperty());
	}
	
	public Concept getObject(ConceptPropertyObject conceptPropertyObject) {
		return conceptRepository.findByDbid(conceptPropertyObject.getObject());
	}

}
