package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Concept {

	@Id()
	private Integer dbid;
	
	@OneToOne()
	@JoinColumn(name="namespace", referencedColumnName="dbid")
	private Namespace namespace;
	private String id;
	private String iri;
	private String name;
	private String description;
	private String code;
	private Integer scheme;
	@OneToOne()
	@JoinColumn(name="status", referencedColumnName="dbid")
	private ConceptStatus status;
	private Integer weighting;

	public Concept() {
		super();
	}

	public Concept(Integer dbid, Namespace namespace, String id, String iri, String name, String description, String code,
			Integer scheme, ConceptStatus status, Integer weighting) {
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

	public Namespace getNamespace() {
		return namespace;
	}

	public void setNamespace(Namespace namespace) {
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

	public ConceptStatus getStatus() {
		return status;
	}

	public void setStatus(ConceptStatus status) {
		this.status = status;
	}

	public Integer getWeighting() {
		return weighting;
	}

	public void setWeighting(Integer weighting) {
		this.weighting = weighting;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dbid == null) ? 0 : dbid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Concept other = (Concept) obj;
		if (dbid == null) {
			if (other.dbid != null)
				return false;
		} else if (!dbid.equals(other.dbid))
			return false;
		return true;
	}
}