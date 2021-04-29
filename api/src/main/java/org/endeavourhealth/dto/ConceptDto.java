package org.endeavourhealth.dto;

import org.endeavourhealth.imapi.model.tripletree.TTIriRef;

public class ConceptDto {

	private String iri;
	private String name;
	private String description;
	private String code;
	private TTIriRef scheme;
	private TTIriRef status;
	private Integer version;
	private TTIriRef conceptType;
	private String definitionText;

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public TTIriRef getScheme() {
		return scheme;
	}

	public void setScheme(TTIriRef scheme) {
		this.scheme = scheme;
	}

	public TTIriRef getStatus() {
		return status;
	}

	public void setStatus(TTIriRef status) {
		this.status = status;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TTIriRef getConceptType() {
		return conceptType;
	}

	public void setConceptType(TTIriRef conceptType) {
		this.conceptType = conceptType;
	}

	public String getDefinitionText() {
		return definitionText;
	}

	public void setDefinitionText(String definitionText) {
		this.definitionText = definitionText;
	}
}
