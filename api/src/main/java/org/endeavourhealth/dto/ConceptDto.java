package org.endeavourhealth.dto;

import org.endeavourhealth.imapi.model.ConceptReference;
import org.endeavourhealth.imapi.model.ConceptStatus;
import org.endeavourhealth.imapi.model.ConceptType;

public class ConceptDto {

	private String iri;
	private String name;
	private String description;
	private String code;
	private ConceptReference scheme;
	private ConceptStatus status;
	private Integer version;
	private ConceptType conceptType;
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

	public ConceptReference getScheme() {
		return scheme;
	}

	public void setScheme(ConceptReference scheme) {
		this.scheme = scheme;
	}

	public ConceptStatus getStatus() {
		return status;
	}

	public void setStatus(ConceptStatus status) {
		this.status = status;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public ConceptType getConceptType() {
		return conceptType;
	}

	public void setConceptType(ConceptType conceptType) {
		this.conceptType = conceptType;
	}

	public String getDefinitionText() {
		return definitionText;
	}

	public void setDefinitionText(String definitionText) {
		this.definitionText = definitionText;
	}
}
