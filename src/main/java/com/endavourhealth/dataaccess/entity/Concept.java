package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Concept {
	@Id()
	private String iri;
	private String id;
	private String name;
	private String description;
	private String code;
	private String scheme;

	public Concept() {
		super();
	}

	public Concept(String iri, String id, String name, String description, String code, String scheme) {
		super();
		this.iri = iri;
		this.id = id;
		this.name = name;
		this.description = description;
		this.code = code;
		this.scheme = scheme;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

}