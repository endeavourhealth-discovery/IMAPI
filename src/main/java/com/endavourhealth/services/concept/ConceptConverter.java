package com.endavourhealth.services.concept;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.endavourhealth.services.concept.models.Concept;

@Service
public class ConceptConverter {

	Concept convert (@NotNull com.endavourhealth.dataaccess.entity.Concept entity) {
		Concept model = new Concept(entity.getIri());
		
		model.setDescription(entity.getDescription());
		model.setName(entity.getName());
		
		return model;
	}
}
