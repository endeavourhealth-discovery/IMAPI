package com.endavourhealth.services.properties.models;

public class PropertyNode {
	
	private Property property;
	private PropertyNode parent;
	
	public PropertyNode(Property property) {
		super();
		this.property = property;
	}

	public PropertyNode getParent() {
		return parent;
	}

	public void setParent(PropertyNode parent) {
		this.parent = parent;
	}

	public Property getProperty() {
		return property;
	}

	
}
