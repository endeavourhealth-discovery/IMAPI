package org.endeavourhealth.imapi.model.tripletree;

/**
 * Create update or delete a thing. Updates may be im:PredicateUpdate which overwrites a predicate
 * or im:AddPredicateObjects which add objects to a predicate as an array
 */
public class TTTransaction extends TTConcept{
	private TTIriRef crud;

	public TTIriRef getCrud() {
		return crud;
	}

	public TTTransaction setCrud(TTIriRef crud) {
		this.crud = crud;
		return this;
	}
}
