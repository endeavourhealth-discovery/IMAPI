package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Concept {

	@Id()
	private Integer dbid;
	private Integer namespace;
	private String id;
	private String iri;
	private String name;
	private String description;
	private String code;
	private Integer scheme;
	private Integer status;
	private Integer weighting;

	public Concept() {
		super();
	}

	public Concept(Integer dbid, Integer namespace, String id, String iri, String name, String description, String code,
			Integer scheme, Integer status, Integer weighting) {
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

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public Integer getNamespace() {
		return namespace;
	}

	public void setNamespace(Integer namespace) {
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

	public Integer getScheme() {
		return scheme;
	}

	public void setScheme(Integer scheme) {
		this.scheme = scheme;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getWeighting() {
		return weighting;
	}

	public void setWeighting(Integer weighting) {
		this.weighting = weighting;
	}

}