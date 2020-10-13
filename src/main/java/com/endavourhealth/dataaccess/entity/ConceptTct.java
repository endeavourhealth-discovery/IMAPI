package com.endavourhealth.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ConceptTct {

	@Id()
	private Integer dbid;
	@OneToOne()
	@JoinColumn(name="source", referencedColumnName="dbid")
	private Concept source;
	@OneToOne()
	@JoinColumn(name="property", referencedColumnName="dbid")
	private Concept property;
	private Integer level;
	@OneToOne()
	@JoinColumn(name="target", referencedColumnName="dbid")
	private Concept target;

	public ConceptTct() {
		super();
	}

	public ConceptTct(Integer dbid, Concept source, Concept property, Integer level, Concept target) {
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

	public Concept getSource() {
		return source;
	}

	public void setSource(Concept source) {
		this.source = source;
	}

	public Concept getProperty() {
		return property;
	}

	public void setProperty(Concept property) {
		this.property = property;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Concept getTarget() {
		return target;
	}

	public void setTarget(Concept target) {
		this.target = target;
	}

}
