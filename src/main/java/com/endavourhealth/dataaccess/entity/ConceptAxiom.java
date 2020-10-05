package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptAxiom {

	@Id()
	private int dbid;
	private String axiom;
	private int concept;
	private String defintion;
	private int version;

	public ConceptAxiom() {
		super();
	}

	public ConceptAxiom(int dbid, String axiom, int concept, String defintion, int version) {
		super();
		this.dbid = dbid;
		this.axiom = axiom;
		this.concept = concept;
		this.defintion = defintion;
		this.version = version;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public String getAxiom() {
		return axiom;
	}

	public void setAxiom(String axiom) {
		this.axiom = axiom;
	}

	public int getConcept() {
		return concept;
	}

	public void setConcept(int concept) {
		this.concept = concept;
	}

	public String getDefintion() {
		return defintion;
	}

	public void setDefintion(String defintion) {
		this.defintion = defintion;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
