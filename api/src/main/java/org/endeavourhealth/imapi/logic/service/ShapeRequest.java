package org.endeavourhealth.imapi.logic.service;

import org.endeavourhealth.imapi.model.tripletree.TTBundle;

public interface ShapeRequest {

	/**
	 * Returns a class entity (including its inherited properties)
	 * @param iri the entity iri
	 * @return a bundle with the entity and predicate name map
	 */
	TTBundle getShape(String iri);
}