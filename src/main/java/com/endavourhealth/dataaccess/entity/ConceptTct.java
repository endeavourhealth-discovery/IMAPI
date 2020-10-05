package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptTct {

	@Id()
	private int dbid;
	private int source;
	private int property;
	private int level;
	private int target;

	public ConceptTct() {
		super();
	}

	public ConceptTct(int dbid, int source, int property, int level, int target) {
		super();
		this.dbid = dbid;
		this.source = source;
		this.property = property;
		this.level = level;
		this.target = target;
	}

	public int getDbid() {
		return dbid;
	}

	public void setDbid(int dbid) {
		this.dbid = dbid;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getProperty() {
		return property;
	}

	public void setProperty(int property) {
		this.property = property;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

}
