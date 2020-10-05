package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptTermMap {

	@Id()
	private Integer dbid;
	private String term;
	private Integer type;
	private Integer target;

	public ConceptTermMap() {
		super();
	}

	public ConceptTermMap(Integer dbid, String term, Integer type, Integer target) {
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

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

}
