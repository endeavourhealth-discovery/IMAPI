package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptTerm {

	@Id()
	private Integer dbid;
	private Integer concept;
	private String term;

	public ConceptTerm() {
		super();
	}

	public ConceptTerm(Integer dbid, Integer concept, String term) {
		super();
		this.dbid = dbid;
		this.concept = concept;
		this.term = term;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public Integer getConcept() {
		return concept;
	}

	public void setConcept(Integer concept) {
		this.concept = concept;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

}
