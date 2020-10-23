package com.endavourhealth.services.concept;

import com.endavourhealth.services.concept.models.Concept;

public class ConceptConverter {

	public static Concept toConcept(com.endavourhealth.dataaccess.entity.Concept entity) {
		Concept model = null;

		if (entity != null) {

			model = new Concept(entity.getIri());

			model.setDescription(entity.getDescription());
			model.setName(entity.getName());
			model.setDbId(entity.getDbid());
		}

		return model;
	}
}
