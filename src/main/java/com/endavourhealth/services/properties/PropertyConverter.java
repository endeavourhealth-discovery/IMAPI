package com.endavourhealth.services.properties;

import static com.endavourhealth.services.concept.ConceptConverter.toConcept;

import com.endavourhealth.dataaccess.entity.Concept;
import com.endavourhealth.services.properties.models.Property;

public class PropertyConverter {

	public static Property toProperty(com.endavourhealth.dataaccess.entity.ConceptPropertyObject entity) {
		Property model = null;

		if (entity != null) {
			
			Concept propertyConcept = entity.getProperty();
			
			model = new Property(propertyConcept.getIri(), toConcept(entity.getObject()), toConcept(entity.getConcept()));
			model.setDescription(propertyConcept.getDescription());
			model.setName(propertyConcept.getName());
			model.setMaxCardinality(entity.getMaxCardinality());
			model.setMinCardinality(entity.getMinCardinality());
		}

		return model;
	}

	public static Property flip(Property property) {
		Property flipped = new Property(property.getIri(), property.getOwner(), property.getValue());
		
		flipped.setDescription(property.getDescription());
		flipped.setName(property.getName());
		flipped.setMinCardinality(property.getMinCardinality());
		flipped.setMaxCardinality(property.getMaxCardinality());
		
		return flipped;
	}
}
