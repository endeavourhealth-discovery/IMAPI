package com.endavourhealth.services.axioms.models;

import com.endavourhealth.dataaccess.entity.ConceptAxiom;

public class Axiom {
	private String axiom;
	private String definition;

	public Axiom() {
		super();
	}
	
	public Axiom(String axiom, String definition) {
		super();
		this.axiom = axiom;
		this.definition = definition;
	}

	public Axiom(ConceptAxiom conceptAxiom) {
		this.axiom = conceptAxiom.getAxiom();
		this.definition = conceptAxiom.getDefinition();
	}

	public String getAxiom() {
		return axiom;
	}

	public void setAxiom(String axiom) {
		this.axiom = axiom;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

}
