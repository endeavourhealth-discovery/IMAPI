package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptStatus {

	@Id()
	private Integer dbid;
	private String name;

	public ConceptStatus() {
		super();
	}

	public ConceptStatus(Integer dbid, String name) {
		super();
		this.dbid = dbid;
		this.name = name;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
