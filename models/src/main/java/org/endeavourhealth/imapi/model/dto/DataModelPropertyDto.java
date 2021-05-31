package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.dto.RecordStructureDto.ConceptReference;

public class DataModelPropertyDto {

	private ConceptReference property;
	private ConceptReference range;

	public DataModelPropertyDto() {

	}

	public ConceptReference getProperty() {
		return property;
	}

	public DataModelPropertyDto setProperty(ConceptReference property) {
		this.property = property;
		return this;
	}

	public ConceptReference getRange() {
		return range;
	}

	public DataModelPropertyDto setRange(ConceptReference range) {
		this.range = range;
		return this;
	}

}
