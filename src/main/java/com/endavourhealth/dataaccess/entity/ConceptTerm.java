package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptTerm {

	@Id()
	private int dbid;
	private int concept;
	private String term;

	public ConceptTerm() {
		super();
	}

	public ConceptTerm(int dbid, int concept, String term) {
		super();
		this.dbid = dbid;
		this.concept = concept;
		this.term = term;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public int getConcept() {
		return concept;
	}

	public void setConcept(int concept) {
		this.concept = concept;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

}
