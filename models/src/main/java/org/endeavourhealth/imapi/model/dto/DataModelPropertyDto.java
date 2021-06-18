package org.endeavourhealth.imapi.model.dto;

import org.endeavourhealth.imapi.model.dto.RecordStructureDto.EntityReference;

public class DataModelPropertyDto {

	private EntityReference property;
	private EntityReference range;

	public DataModelPropertyDto() {

	}

	public EntityReference getProperty() {
		return property;
	}

	public DataModelPropertyDto setProperty(EntityReference property) {
		this.property = property;
		return this;
	}

	public EntityReference getRange() {
		return range;
	}

	public DataModelPropertyDto setRange(EntityReference range) {
		this.range = range;
		return this;
	}

}
