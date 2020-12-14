package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Module {

	@Id()
	private Integer dbid;
	private String iri;

	public Module() {
		super();
	}

	public Module(Integer dbid, String iri) {
		super();
		this.dbid = dbid;
		this.iri = iri;
	}

    public Integer getDbid() {
        return dbid;
    }

    public Module setDbid(Integer dbid) {
        this.dbid = dbid;
        return this;
    }

    public String getIri() {
        return iri;
    }

    public Module setIri(String iri) {
        this.iri = iri;
        return this;
    }
}
