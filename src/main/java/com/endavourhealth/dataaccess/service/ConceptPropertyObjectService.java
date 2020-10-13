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
	
//	public List<ConceptPropertyObject> findAllByConcept(Integer conceptDbid) {
//		return conceptPropertyObjectRepository.findByConceptDbid(conceptDbid);
//	}
	
	public List<ConceptPropertyObject> findAllByPropertyDbid(Integer conceptDbid) {
		return conceptPropertyObjectRepository.findByPropertyDbid(conceptDbid);
	}
	
	public List<ConceptPropertyObject> findAllByObjectDbid(Integer conceptDbid) {
		return conceptPropertyObjectRepository.findByObjectDbid(conceptDbid);
	}
	
	public Concept getPropertyDbid(ConceptPropertyObject conceptPropertyObject) {
		return conceptRepository.findByDbid(conceptPropertyObject.getProperty().getDbid());
	}
	
	public Concept getObjectDbid(ConceptPropertyObject conceptPropertyObject) {
		return conceptRepository.findByDbid(conceptPropertyObject.getObject().getDbid());
	}

}
