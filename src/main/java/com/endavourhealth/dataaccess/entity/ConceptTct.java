package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConceptTct {

	@Id()
	private Integer dbid;
	private Integer source;
	private Integer property;
	private Integer level;
	private Integer target;

	public ConceptTct() {
		super();
	}

	public ConceptTct(Integer dbid, Integer source, Integer property, Integer level, Integer target) {
		super();
		this.dbid = dbid;
		this.source = source;
		this.property = property;
		this.level = level;
		this.target = target;
	}

	public Integer getDbid() {
		return dbid;
	}

	public void setDbid(Integer dbid) {
		this.dbid = dbid;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getProperty() {
		return property;
	}

	public void setProperty(Integer property) {
		this.property = property;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
		this.target = target;
	}

}
