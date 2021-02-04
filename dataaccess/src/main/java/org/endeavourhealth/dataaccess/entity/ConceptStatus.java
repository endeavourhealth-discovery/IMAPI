package org.endeavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptStatus {

	@Id()
	private Byte dbid;
	private String name;

	public ConceptStatus() {
		super();
	}

	public ConceptStatus(Byte dbid, String name) {
		super();
		this.dbid = dbid;
		this.name = name;
	}

	public Byte getDbid() {
		return dbid;
	}

	public void setDbid(Byte dbid) {
		this.dbid = dbid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
