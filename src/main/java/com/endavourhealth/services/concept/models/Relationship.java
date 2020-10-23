package com.endavourhealth.services.concept.models;

public class Relationship {
	
	private Concept realtion;
	private Concept relationship;
	// TOOD min and max cardinality
	
	public Relationship(Concept realtion, Concept relationship) {
		super();
		this.realtion = realtion;
		this.relationship = relationship;
	}
	
	public Concept getRealtion() {
		return realtion;
	}

	public Concept getRelationship() {
		return relationship;
	}	
}
