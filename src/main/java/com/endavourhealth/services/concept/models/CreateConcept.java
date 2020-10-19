package com.endavourhealth.services.concept.models;

public class CreateConcept {

	private String namespace;
	private String id;
	private String iri;
	private String name;
	private String description;
	private String code;
	private Integer scheme;
	private String status;
	private String parent;

	public CreateConcept() {
		super();
	}

	public CreateConcept(String namespace, String id, String iri, String name, String description, String code,
			Integer scheme, String status, String parent) {
		super();
		this.namespace = namespace;
		this.id = id;
		this.iri = iri;
		this.name = name;
		this.description = description;
		this.code = code;
		this.scheme = scheme;
		this.status = status;
		this.parent = parent;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getScheme() {
		return scheme;
	}

	public void setScheme(Integer scheme) {
		this.scheme = scheme;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

}
