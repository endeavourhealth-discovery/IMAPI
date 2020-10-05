package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptAxiom {

	@Id()
	private Integer dbid;
	private String axiom;
	private Integer concept;
	private String defIntegerion;
	private Integer version;

	public ConceptAxiom() {
		super();
	}

	public ConceptAxiom(Integer dbid, String axiom, Integer concept, String defIntegerion, Integer version) {
		super();
		this.dbid = dbid;
		this.axiom = axiom;
		this.concept = concept;
		this.defIntegerion = defIntegerion;
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

	public Integer getConcept() {
		return concept;
	}

	public void setConcept(Integer concept) {
		this.concept = concept;
	}

	public String getDefIntegerion() {
		return defIntegerion;
	}

	public void setDefIntegerion(String defIntegerion) {
		this.defIntegerion = defIntegerion;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
