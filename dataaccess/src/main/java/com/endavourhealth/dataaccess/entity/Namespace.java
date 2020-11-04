package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Namespace {

	@Id()
	private Integer dbid;
	private String iri;
	private String prefix;

	public Namespace() {
		super();
	}

	public Namespace(Integer dbid, String iri, String prefix) {
		super();
		this.dbid = dbid;
		this.iri = iri;
		this.prefix = prefix;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
