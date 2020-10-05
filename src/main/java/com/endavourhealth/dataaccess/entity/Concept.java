package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Concept {

	@Id()
	private int dbid;
	private int namespace;
	private String id;
	private String iri;
	private String name;
	private String description;
	private String code;
	private int scheme;
	private int status;
	private int weighting;

	public Concept() {
		super();
	}

	public Concept(int dbid, int namespace, String id, String iri, String name, String description, String code,
			int scheme, int status, int weighting) {
		super();
		this.dbid = dbid;
		this.namespace = namespace;
		this.id = id;
		this.iri = iri;
		this.name = name;
		this.description = description;
		this.code = code;
		this.scheme = scheme;
		this.status = status;
		this.weighting = weighting;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public int getNamespace() {
		return namespace;
	}

	public void setNamespace(int namespace) {
		this.namespace = namespace;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIri() {
		return iri;
	}

	public void setIri(String iri) {
		this.iri = iri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getScheme() {
		return scheme;
	}

	public void setScheme(int scheme) {
		this.scheme = scheme;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getWeighting() {
		return weighting;
	}

	public void setWeighting(int weighting) {
		this.weighting = weighting;
	}

}