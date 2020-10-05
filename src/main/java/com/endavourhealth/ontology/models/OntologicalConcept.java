package com.endavourhealth.ontology.models;

public class OntologicalConcept {
	private String iri;
	private String name;
	private String description;

	public OntologicalConcept() {
		super();
	}

	public OntologicalConcept(String iri, String name, String description) {
		super();
		this.iri = iri;
		this.name = name;
		this.description = description;
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

}
