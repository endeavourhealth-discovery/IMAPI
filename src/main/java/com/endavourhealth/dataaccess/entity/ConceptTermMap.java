package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConceptTermMap {

	@Id()
	private Integer dbid;
	private String term;
	private Integer type;
	@OneToOne()
	@JoinColumn(name="target", referencedColumnName="dbid")
	private Concept target;

	public ConceptTermMap() {
		super();
	}

	public ConceptTermMap(Integer dbid, String term, Integer type, Concept target) {
		super();
		this.dbid = dbid;
		this.term = term;
		this.type = type;
		this.target = target;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Concept getTarget() {
		return target;
	}

	public void setTarget(Concept target) {
		this.target = target;
	}

}
