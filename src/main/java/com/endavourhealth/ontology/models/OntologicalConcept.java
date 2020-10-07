package com.endavourhealth.ontology.models;

import java.util.List;

public class OntologicalConcept {
	private String iri;
	private String name;
	private String description;
	private List<Axiom> axioms = null;

	public OntologicalConcept() {
		super();
	}

	public OntologicalConcept(String iri, String name, String description, List<Axiom> axioms) {
		super();
		this.iri = iri;
		this.name = name;
		this.description = description;
		this.axioms = axioms;
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

	public List<Axiom> getAxioms() {
		return axioms;
	}

	public void setAxioms(List<Axiom> axioms) {
		this.axioms = axioms;
	}

}
