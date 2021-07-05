package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

import java.io.Serializable;

public class SemanticProperty implements Serializable {

	private TTIriRef property;
	private TTIriRef type;

	public TTIriRef getProperty() {
		return property;
	}

	public SemanticProperty setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public TTIriRef getType() {
		return type;
	}

	public SemanticProperty setType(TTIriRef type) {
		this.type = type;
		return this;
	}
}
