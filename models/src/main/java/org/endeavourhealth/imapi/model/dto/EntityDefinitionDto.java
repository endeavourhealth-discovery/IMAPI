package org.endeavourhealth.imapi.model.dto;

import java.util.List;

import org.endeavourhealth.imapi.model.dto.RecordStructureDto.EntityReference;

public class EntityDefinitionDto {

	private String iri;
	private String name;
	private String description;
	private String status;
	private List<EntityReference> types;
	private List<EntityReference> isa;
	private List<EntityReference> subtypes;

	public String getIri() {
		return iri;
	}

	public EntityDefinitionDto setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public String getName() {
		return name;
	}

	public EntityDefinitionDto setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public EntityDefinitionDto setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public EntityDefinitionDto setStatus(String status) {
		this.status = status;
		return this;
	}

	public List<EntityReference> getTypes() {
		return types;
	}

	public EntityDefinitionDto setTypes(List<EntityReference> types) {
		this.types = types;
		return this;
	}

	public List<EntityReference> getIsa() {
		return isa;
	}

	public EntityDefinitionDto setIsa(List<EntityReference> isa) {
		this.isa = isa;
		return this;
	}

	public List<EntityReference> getSubtypes() {
		return subtypes;
	}

	public EntityDefinitionDto setSubtypes(List<EntityReference> subtypes) {
		this.subtypes = subtypes;
		return this;
	}
}
