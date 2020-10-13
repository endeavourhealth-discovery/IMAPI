package com.endavourhealth.datamodel.models;

import com.endavourhealth.dataaccess.entity.Concept;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {
	@JsonProperty("iri")
	private String iri = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("description")
	private String description = null;

	@JsonProperty("type")
	private String type = null;

	private String owner = null;

	public Value() {
		super();
	}

	public Value(String iri, String name, String description, String type, String owner) {
		super();
		this.iri = iri;
		this.name = name;
		this.description = description;
		this.type = type;
		this.owner = owner;
	}

	public Value(Concept concept, String owner) {
		this.setName(concept.getName());
		this.setIri(concept.getIri());
		this.setDescription(concept.getDescription());
		this.owner = owner;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
