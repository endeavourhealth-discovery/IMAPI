package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptTermMap {

	@Id()
	private int dbid;
	private String term;
	private int type;
	private int target;

	public ConceptTermMap() {
		super();
	}

	public ConceptTermMap(int dbid, String term, int type, int target) {
		super();
		this.dbid = dbid;
		this.term = term;
		this.type = type;
		this.target = target;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

}
