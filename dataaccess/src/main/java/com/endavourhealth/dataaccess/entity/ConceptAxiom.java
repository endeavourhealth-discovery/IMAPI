package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConceptAxiom {

	@Id()
	private Integer dbid;
	private String axiom;
	@OneToOne()
	@JoinColumn(name="concept", referencedColumnName="dbid")
	private Concept concept;
	private String definition;
	private Integer version;

	public ConceptAxiom() {
		super();
	}

	public ConceptAxiom(Integer dbid, String axiom, Concept concept, String definition, Integer version) {
		super();
		this.dbid = dbid;
		this.axiom = axiom;
		this.concept = concept;
		this.definition = definition;
		this.version = version;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getAxiom() {
		return axiom;
	}

	public void setAxiom(String axiom) {
		this.axiom = axiom;
	}

	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
