package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class DataModelPropertyDto {

	private TTIriRef property;
	private TTIriRef range;

	public DataModelPropertyDto() {

	}

	public TTIriRef getProperty() {
		return property;
	}

	public DataModelPropertyDto setProperty(TTIriRef property) {
		this.property = property;
		return this;
	}

	public TTIriRef getRange() {
		return range;
	}

	public DataModelPropertyDto setRange(TTIriRef range) {
		this.range = range;
		return this;
	}

}
