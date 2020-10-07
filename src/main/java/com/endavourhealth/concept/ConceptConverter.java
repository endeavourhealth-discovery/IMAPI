package com.endavourhealth.concept;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import com.endavourhealth.concept.models.Concept;

@Service
class ConceptConverter {

	Concept convert (@NotNull com.endavourhealth.dataaccess.entity.Concept entity) {
		Concept model = new Concept(entity.getIri());
		
		model.setDescription(entity.getDescription());
		model.setName(entity.getName());
		
		return model;
	}
}
