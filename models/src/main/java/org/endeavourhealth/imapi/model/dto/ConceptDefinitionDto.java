package org.endeavourhealth.imapi.model.dto;

import java.util.List;

import org.endeavourhealth.imapi.model.dto.RecordStructureDto.ConceptReference;

public class ConceptDefinitionDto {

	private String iri;
	private String name;
	private String description;
	private String status;
	private List<ConceptReference> types;
	private List<ConceptReference> isa;
	private List<ConceptReference> subtypes;

	public String getIri() {
		return iri;
	}

	public ConceptDefinitionDto setIri(String iri) {
		this.iri = iri;
		return this;
	}

	public String getName() {
		return name;
	}

	public ConceptDefinitionDto setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ConceptDefinitionDto setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public ConceptDefinitionDto setStatus(String status) {
		this.status = status;
		return this;
	}

	public List<ConceptReference> getTypes() {
		return types;
	}

	public ConceptDefinitionDto setTypes(List<ConceptReference> types) {
		this.types = types;
		return this;
	}

	public List<ConceptReference> getIsa() {
		return isa;
	}

	public ConceptDefinitionDto setIsa(List<ConceptReference> isa) {
		this.isa = isa;
		return this;
	}

	public List<ConceptReference> getSubtypes() {
		return subtypes;
	}

	public ConceptDefinitionDto setSubtypes(List<ConceptReference> subtypes) {
		this.subtypes = subtypes;
		return this;
	}
}
