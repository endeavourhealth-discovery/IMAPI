package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConceptTerm {

	@Id()
	private Integer dbid;
	@OneToOne()
	@JoinColumn(name="concept", referencedColumnName="dbid")
	private Concept concept;
	private String term;

	public ConceptTerm() {
		super();
	}

	public ConceptTerm(Integer dbid, Concept concept, String term) {
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

	public Concept getConcept() {
		return concept;
	}

	public void setConcept(Concept concept) {
		this.concept = concept;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

}
