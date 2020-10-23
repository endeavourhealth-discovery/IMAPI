package com.endavourhealth.services.concept.models;

public class Parent {
	
	private Parent parent;
	private Relationship relationship;
	
	public Parent(Relationship relationship) {
		super();
		this.relationship = relationship;
	}

	public Parent getParent() {
		return parent;
	}

	public void setParent(Parent parent) {
		this.parent = parent;
	}

	public Relationship getRelationship() {
		return relationship;
	}
	
	
}
