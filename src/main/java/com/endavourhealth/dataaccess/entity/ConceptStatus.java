package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptStatus {

	@Id()
	private int dbid;
	private String name;

	public ConceptStatus() {
		super();
	}

	public ConceptStatus(int dbid, String name) {
		super();
		this.dbid = dbid;
		this.name = name;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
